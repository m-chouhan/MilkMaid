package com.milkmaid.game;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by maximus_prime on 1/11/15.
 */

public class SuperStrongerWorld extends World implements InputProcessor {

    private static final float MAX_VELOCITY = 20;
    private final String TAG = "STRONGERWORLD";
    private float AFFINITY_RANGE = 200;
    private int Bottom = 0;

    enum State{SHOOTING,SHOT,ENDING};
    private State currentState = State.SHOOTING;

    private Vector2 InitialPos = new Vector2(),LaunchVelocity = new Vector2();
    private ArrayList<Vertex> Affinity = new ArrayList<Vertex>();

    private boolean IsPaused = true;
    private boolean player_selected = false;

    public SuperStrongerWorld(VertexQueue vertexQueue, GameSuperviser superviser,Player p) {
        super(vertexQueue, superviser,p);
    }

    public void startGame(Vertex last_touched) {

        AFFINITY_RANGE = 200;
        currentState = State.SHOOTING;
        last_touched.changeState(Vertex.Status.Dead);

        crazyFrog.position.set(last_touched.x,last_touched.y,0);
        crazyFrog.velocity.set(0,0,0);
        InitialPos.set(last_touched);
        Bottom = (int) (camera.position.x - camera.viewportWidth/2) - 32;
    }

    public boolean  IsPaused() { return IsPaused; }

    public Vector2 getInitialPos() { return InitialPos; }

    //TODO: update player position
    @Override
    public void update() {

        switch (currentState){
            case SHOOTING: return;

            case SHOT:

                if( crazyFrog.position.y < 32 ) crazyFrog.position.y = 32;

                else if( crazyFrog.position.y > 450 ) crazyFrog.position.y = 450;

                if( crazyFrog.position.x < Bottom ) {

                    Player.Velocity.x = Math.abs(Player.Velocity.x);
                    Player.Position.x = Bottom;
                }
                Player.update();
                int index = VQueue.SearchUpperBound((int)Player.Position.x);

                while(index < VQueue.getSize() && index >= 0 ) {

                    Vertex v = VQueue.getVertex(index);
                    Vector2 dist = new Vector2(Player.Position.x,Player.Position.y);
                    dist.sub(v);
                    if(dist.len() < AFFINITY_RANGE && v.getCurrentState() != Vertex.Status.Reachable ) {

                        v.changeState(Vertex.Status.Reachable);
                        Affinity.add(v);
                        //v.changeState(Vertex.Status.Dead);
                    }
                    index++;
                }

                for(Iterator<Vertex> iterator = Affinity.iterator();iterator.hasNext();) {

                    Vertex v= iterator.next();
                    Vector2 dir = new Vector2(Player.Position.x,Player.Position.y);
                    dir.sub(v);
                    v.add(dir.x / 2, dir.y / 2);

                    dir.set(Player.Position.x-v.x,Player.Position.y-v.y);

                    if(dir.len() < 30 ) {
                        v.changeState(Vertex.Status.Dead);
                        Superviser.updateScore(v.getWeight());
                        iterator.remove();
                    }
                }

                if(camera.position.x +300 < Player.Position.x)
                    camera.translate(Player.Velocity.x, 0);
                else camera.translate(Player.Velocity.x/2,0);

                camera.update();

                int camera_bottom = (int) (camera.position.x - camera.viewportWidth/2);

                if(VQueue.getVertex(0).x <camera_bottom)
                    VQueue.Push(VQueue.Pop());

                /*Update Player */
                Player.Velocity.x *= 0.99f;
                Player.Velocity.y *= 0.99f;
                Player.Velocity.z *= 0.95f;
                if(Player.Velocity.len() < 6 ) {

                    currentState = State.ENDING;
                    MoveTo(getNearestVertex());
                }

                AFFINITY_RANGE++;
                break;

            case ENDING:
                Player.update();
                camera.translate(Player.Velocity.x, 0);
                camera.update();
                if(LastTouched.dst(Player.Position.x,Player.Position.y) < 5 ) {
                    Superviser.SwitchState(GameSuperviser.GameState.NORMAL);
                    LastTouched.changeState(Vertex.Status.Touched);
                    LastTouched.setVertexType(Vertex.Type.Stronger);
                }
        }

    }

    private void MoveTo(Vertex v) {

        Vector3 velo = new Vector3(v.x - Player.Position.x,v.y - Player.Position.y,
                                    1 - Player.Position.z);
        velo.scl(0.05f);
        Player.Velocity.set(velo);
        LastTouched = v;
    }

    private Vertex getNearestVertex() {
        int i = VQueue.SearchUpperBound((int)Player.Position.x);
        Vertex v = VQueue.getVertex(i);

        while(++i < VQueue.getSize()) {

            v = VQueue.getVertex(i);
            if(v.getCurrentState() != Vertex.Status.Dead) break;
        }
        return v;
    }

    @Override
    public void VertexTouched(Vertex vertex) {

    }

    @Override
    public boolean touchDown(int x, int y, int pointer, int button) {

        if(player_selected || currentState == State.SHOT ) return true;

        Vector3 touch3D = camera.unproject(new Vector3(x, y, 0));
        Vector2 touchPos = new Vector2(touch3D.x,touch3D.y);
        if (touchPos.dst(Player.Position.x,Player.Position.y) < 50 )
            player_selected = true;

        return true;
    }

    @Override
    public boolean touchUp(int x, int y, int pointer, int button) {

        if(!player_selected || currentState == State.SHOT ) return true;

        Vector3 touch3D = camera.unproject(new Vector3(x,y,0));
        Vector2 touchPos = new Vector2(touch3D.x,touch3D.y);
        Vector2 InitialVelo = new Vector2(InitialPos.x - touchPos.x,InitialPos.y - touchPos.y);

        //Gdx.app.log(TAG, "TouchPos " + touchPos.x + "|" + touchPos.y);
        //Gdx.app.log(TAG, "InitialPos " + InitialPos.x + "|" + InitialPos.y);
        currentState = State.SHOT;

        player_selected = false;
        Player.Velocity.set(InitialVelo.x,InitialVelo.y,1f);
        LaunchVelocity.set(InitialVelo);

        if(Player.Velocity.len() > MAX_VELOCITY) {

            float X = Player.Velocity.len()/MAX_VELOCITY;
            Player.Velocity.scl(1 / X);
            LaunchVelocity.set(Player.Velocity.x,Player.Velocity.y);
        }

        return true;
    }

    @Override
    public boolean touchDragged(int x, int y, int pointer) {

        Vector3 touch3D = camera.unproject(new Vector3(x,y,0));
        Vector2 touchPos = new Vector2(touch3D.x,touch3D.y);
        if (touchPos.dst(Player.Position.x,Player.Position.y) < 50 )
            player_selected = true;

        if(!player_selected || currentState == State.SHOT ) return true;

        if(touchPos.y < 32  ) touchPos.y = 32;
        if(touchPos.y > 450 ) touchPos.y = 450;
        touchPos.sub(InitialPos);
        if(touchPos.len() > 200 ) {
            float X = 200/touchPos.len();
            touchPos.scl(X);
        }
        touchPos.add(InitialPos);
        Player.Position.set(touchPos.x,touchPos.y,1);
        return true;
    }

    @Override
    public boolean keyDown(int i) {
        return false;
    }

    @Override
    public boolean keyUp(int i) {
        return false;
    }

    @Override
    public boolean keyTyped(char c) {
        return false;
    }

    @Override
    public boolean mouseMoved(int i, int i1) {
        return false;
    }

    @Override
    public boolean scrolled(int i) {
        return false;
    }
}
