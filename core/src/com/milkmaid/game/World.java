package com.milkmaid.game;

/**
 * Created by maximus_prime on 27/9/15.
 */

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;

import java.util.Random;


public class World {

    private final String TAG = "WORLD";

    private float Speed = 1.0f;
    private VertexQueue VQueue;
    private final int MAX_ELEMENTS = 25;
    private Painter myPainter;
    private boolean Game_Started = false;
    private Random RandomGenerator = new Random();
    private OrthographicCamera camera;
    private Vertex LastTouched = null;

    public final int Height = 480,Max_Width = 1600; //Height of our world
    public final int ScreenWidth,ScreenHeight;
    public final int NODE_SIZE = 120;

    public World(int width,int height) {

        ScreenWidth = width; ScreenHeight = height;

        VQueue = new VertexQueue(MAX_ELEMENTS);

        camera = new OrthographicCamera(ScreenWidth,ScreenHeight); //viewport dimensions
        camera.position.set(camera.viewportWidth / 2f, Height / 2f, 0);
        camera.rotate(180);
        camera.update();

        myPainter = new Painter(this);
        InflateWorld();
    }

    private void InflateWorld() {

        int window = 150;//Max_Width/MAX_ELEMENTS;
        int x = 100,y = Height/2;
        VQueue.Push(new Vertex(x, y));

        for(int i = 1 ;i<MAX_ELEMENTS;++i) {

            VQueue.Push(new Vertex(0,0 ));
        }
    }

    private int generateProbability(double val) {
        double prob = Math.random()/val;
        if( prob <= 1) return 1;
        else return 0;
    }

    public VertexQueue getVQueue() {return VQueue; }

    public Screen getPainterScreen() { return myPainter; }
    public OrthographicCamera getCamera() { return camera; }
    public int getNodeSize() { return NODE_SIZE/5;}
    public Vertex getLastTouched() { return LastTouched; }

    public void VertexUnTouched(Vertex vertex) {

    }

    public void VertexTouched(Vertex vertex) {

        //TODO: Check for vertext type and set their control
        if( LastTouched == null) {
          if( vertex == VQueue.getVertex(0)) {
              LastTouched = vertex;
              LastTouched.changeState(Vertex.Status.Touched);
              for(HalfEdge h:LastTouched.getEdgeList())
                  h.getDst().changeState(Vertex.Status.Reachable);
              Game_Started = true;

          }
          return;
        }

        for( HalfEdge he:LastTouched.getEdgeList() ) {

            if(he.getDst() == vertex) {

                for (HalfEdge h : LastTouched.getEdgeList())
                    h.getDst().changeState(Vertex.Status.UnReachable);

                LastTouched = vertex;
                LastTouched.changeState(Vertex.Status.Touched);

                for (HalfEdge h : LastTouched.getEdgeList())
                    h.getDst().changeState(Vertex.Status.Reachable);
            }
        }
    }

    public void update() {

        if(Game_Started) {
            camera.translate(Speed, 0);
            camera.update();
            myPainter.updateBackground(Speed/2);
            Speed += 0.002f;
        }

        int camera_bottom = (int) (camera.position.x - camera.viewportWidth/2);
        int camera_top = (int) (camera.position.x + camera.viewportWidth/2);

        if(VQueue.getVertex(0).x <camera_bottom) {

            VQueue.Push(VQueue.Pop());
        }

    }

}
