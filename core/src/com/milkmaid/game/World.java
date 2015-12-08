package com.milkmaid.game;


/**
 * Created by maximus_prime on 27/9/15.
 */

import com.badlogic.gdx.graphics.OrthographicCamera;


public class World {

    private final String TAG = "WORLD CLASS";

    protected float Speed = 0.0f;
    protected Vertex LastTouched = null;

    protected VertexQueue VQueue;
    protected boolean Game_Started = false;

    protected final OrthographicCamera camera;
    protected final GameSuperviser Superviser;
    protected final Player player;

    public final int Height ; //Height of our world
    public final int ScreenWidth,ScreenHeight;

    public World(VertexQueue vqueue,GameSuperviser superviser,Player p) {

        Superviser = superviser;
        ScreenWidth = Superviser.getWidth(); ScreenHeight = Superviser.getHeight();
        Height = superviser.getWorldHeight();
        VQueue = vqueue;
        camera = superviser.getDisplayCamera();
        player = p;
    }

    private int generateProbability(double val) {
        double prob = Math.random()/val;
        if( prob <= 1) return 1;
        else return 0;
    }

    public VertexQueue getVQueue() {return VQueue; }

    //public Screen getPainterScreen() { return myPainter; }
    public OrthographicCamera getCamera() { return camera; }
    public Vertex getLastTouched() { return LastTouched; }

    void setLastTouched(Vertex last) {

        LastTouched = last;
        for (HalfEdge h : LastTouched.getEdgeList())
            h.getDst().changeState(Vertex.Status.Reachable);
    }
    //NOT IN USE FOR NOW :)
    public void VertexUnTouched(Vertex vertex) {

    }

    public void startGame() {
        Game_Started = true;
    }

    public void setSpeed(float f) {
        Speed = f;
    }

    public float getSpeed() { return Speed;}

    public void VertexTouched(Vertex vertex) {

        if(LastTouched == vertex ) return;//eliminate repeated events

        //TODO: Check for vertext type and set their control
        if( LastTouched == null) {
          if( vertex == VQueue.getVertex(0)) {
              LastTouched = vertex;
              LastTouched.changeState(Vertex.Status.Touched);
              for(HalfEdge h:LastTouched.getEdgeList())
                  h.getDst().changeState(Vertex.Status.Reachable);

              startGame();
              setSpeed(1.0f);
          }
          return;
        }

        for( HalfEdge he:LastTouched.getEdgeList() ) {

            if(he.getDst() == vertex) {

                for (HalfEdge h : LastTouched.getEdgeList())
                    h.getDst().changeState(Vertex.Status.UnReachable);

                LastTouched.changeState(Vertex.Status.Dead);

                LastTouched = vertex;
                LastTouched.changeState(Vertex.Status.Touched);
                switch (LastTouched.getVertexType()){

                    case Sharper:
                            Superviser.SwitchState(GameSuperviser.GameState.SHARPER);
                            break;
                    case Stronger:
                            Superviser.SwitchState(GameSuperviser.GameState.STRONGER);
                            break;
                    case Normal:
                    case Bonus:
                }
                for (HalfEdge h : LastTouched.getEdgeList())
                    h.getDst().changeState(Vertex.Status.Reachable);
                Superviser.updateScore(LastTouched.getWeight());
                player.Move_TO(LastTouched.x,LastTouched.y);
                return;
            }
        }
    }

    public void update() {

        if(Game_Started) {
            camera.translate(Speed, 0);
            camera.update();
            Speed += 0.005f;
            player.update();
        }

        int camera_bottom = (int) (camera.position.x - camera.viewportWidth/2);
        if(VQueue.getVertex(0).x <camera_bottom) {

            VQueue.Push(VQueue.Pop());
        }

    }

}
