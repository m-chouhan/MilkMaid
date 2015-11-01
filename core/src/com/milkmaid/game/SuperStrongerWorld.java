package com.milkmaid.game;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by maximus_prime on 1/11/15.
 */

public class SuperStrongerWorld extends World implements InputProcessor {

    enum State{SHOOTING,};
    private PlayerClass Player = new PlayerClass();
    private Vector2 InitialPos = new Vector2();

    private boolean IsPaused = true;
    private boolean player_selected = false,touch_disabled = false;
    private class PlayerClass {

        Vector2 Position = new Vector2();
        Vector2 Velocity = new Vector2();
        void update() {
            Position.add(Velocity);
        }
    };

    public SuperStrongerWorld(VertexQueue vertexQueue, GameSuperviser superviser) {
        super(vertexQueue,superviser);

    }

    public void startGame(Vertex last_touched) {

        //TODO: reset player position
        last_touched.changeState(Vertex.Status.Dead);

        Player.Position.set(last_touched);
        InitialPos.set(last_touched);
    }

    public boolean  IsPaused() { return IsPaused; }

    public Vector2 getPlayerPosition() { return Player.Position; }

    //TODO: update player position
    @Override
    public void update() {

        if( !touch_disabled ) return;

        if( Player.Position.x < 32 || Player.Position.x > 450 )
            Player.Velocity.x = -Player.Velocity.x;

        Player.update();
        Player.Velocity.x *= 0.9f;
        Player.Velocity.y *= 0.9f;

    }

    @Override
    public void VertexTouched(Vertex vertex) {

    }

    @Override
    public boolean touchDown(int x, int y, int pointer, int button) {

        if(player_selected || touch_disabled ) return true;

        Vector3 touch3D = camera.unproject(new Vector3(x, y, 0));
        Vector2 touchPos = new Vector2(touch3D.x,touch3D.y);
        if (touchPos.dst(Player.Position) < 50 )
            player_selected = true;

        return true;
    }

    @Override
    public boolean touchUp(int x, int y, int pointer, int button) {

        if(!player_selected || touch_disabled) return true;

        Vector3 touch3D = camera.unproject(new Vector3(x,y,0));
        Vector2 touchPos = new Vector2(touch3D.x,touch3D.y);

        //touchPos.sub(InitialPos);
        InitialPos.sub(touchPos);
        touch_disabled = true;
        player_selected = false;
        Player.Velocity.set(InitialPos.x/2,InitialPos.y/2);
        //TODO: SHOOT Player

        return true;
    }

    @Override
    public boolean touchDragged(int x, int y, int pointer) {

        if(!player_selected || touch_disabled) return true;

        Vector3 touch3D = camera.unproject(new Vector3(x,y,0));
        Vector2 touchPos = new Vector2(touch3D.x,touch3D.y);
        Player.Position.set(touchPos);

        return false;
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
