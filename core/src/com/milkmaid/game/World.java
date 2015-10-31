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
    protected OrthographicCamera camera;

    protected final GameSuperviser Superviser;

    public final int Height ; //Height of our world
    public final int ScreenWidth,ScreenHeight;
    public final int NODE_SIZE = 120;

    public World(VertexQueue vqueue,GameSuperviser superviser) {

        Superviser = superviser;
        ScreenWidth = Superviser.getWidth(); ScreenHeight = Superviser.getHeight();
        Height = superviser.getWorldHeight();
        VQueue = vqueue;

        camera = superviser.getDisplayCamera();

        InflateWorld();
    }

    private void InflateWorld() {

        int x = 100,y = Height/2;
        VQueue.Push(new Vertex(x, y));

        for(int i = 1 ;i<VQueue.getMax_size();++i) {
            VQueue.Push(new Vertex(0,0));
        }
    }

    private int generateProbability(double val) {
        double prob = Math.random()/val;
        if( prob <= 1) return 1;
        else return 0;
    }

    public VertexQueue getVQueue() {return VQueue; }

    //public Screen getPainterScreen() { return myPainter; }
    public OrthographicCamera getCamera() { return camera; }
    public int getNodeSize() { return NODE_SIZE/5;}
    public Vertex getLastTouched() { return LastTouched; }

    void setLastTouched(Vertex last) { LastTouched = last;}
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
                    case Normal:
                    case Bonus:
                }
                for (HalfEdge h : LastTouched.getEdgeList())
                    h.getDst().changeState(Vertex.Status.Reachable);
                Superviser.updateScore(LastTouched.getWeight());
                return;
            }
        }
    }

    public void update() {

        if(Game_Started) {
            camera.translate(Speed, 0);
            camera.update();
            Speed += 0.005f;
        }

        int camera_bottom = (int) (camera.position.x - camera.viewportWidth/2);
        if(VQueue.getVertex(0).x <camera_bottom) {

            VQueue.Push(VQueue.Pop());
        }

    }

}
