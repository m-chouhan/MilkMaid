package com.milkmaid.game;

/**
 * Created by maximus_prime on 27/9/15.
 */

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;

import java.util.Random;


public class World {

    private final String TAG = "WORLD";

    private VertexQueue VQueue;

    private int BottomIndex = 0; //For implementing circular x- sorted array

    private final int MAX_ELEMENTS = 25;
    private int Graph[][];
    private Painter myPainter;

    public final int Height = 480,Max_Width = 1600; //Height of our world
    public final int ScreenWidth,ScreenHeight;
    public final int NODE_SIZE = 100;

    private OrthographicCamera camera;

    public World(int width,int height) {

        ScreenWidth = width; ScreenHeight = height;

        VQueue = new VertexQueue(MAX_ELEMENTS);

        Graph = new int[MAX_ELEMENTS][MAX_ELEMENTS];

        camera = new OrthographicCamera(ScreenWidth,ScreenHeight); //viewport dimensions
        camera.position.set(camera.viewportWidth / 2f, Height / 2f, 0);
        camera.rotate(180);
        camera.update();

        myPainter = new Painter(this);
        InflateWorld();

    }

    private void InflateWorld() {

        Random r = new Random();
        int window = Max_Width/MAX_ELEMENTS+100;
        int x = 100;
        VQueue.Push(new Vertex(x,Height/2));

        for(int i = 1 ;i<MAX_ELEMENTS;++i) {

            x += (r.nextInt(window / NODE_SIZE) + 1) * (NODE_SIZE + 10);

            VQueue.Push(new Vertex(x, r.nextInt(Height / NODE_SIZE) * (NODE_SIZE)));
        }
    }

    private int generateProbability(double val) {
        double prob = Math.random()/val;
        if( prob <= 1) return 1;
        else return 0;
    }

    public VertexQueue getVQueue() {return VQueue; }

    public int[][] getGraph() { return Graph; }
    public Screen getPainterScreen() { return myPainter; }
    public OrthographicCamera getCamera() { return camera; }
    public int getBottom() { return BottomIndex; }

    public void update() {

        camera.translate(2, 0);
        camera.update();

        int camera_bottom = (int) (camera.position.x - camera.viewportWidth/2);
        int camera_top = (int) (camera.position.x + camera.viewportWidth/2);

        if(VQueue.getVertex(0).x <camera_bottom) {

            Vertex vbottom = VQueue.Pop();
            Vertex vtop = VQueue.getVertex(VQueue.getSize()-1);

            Random r = new Random();
            int window = Max_Width/MAX_ELEMENTS+50;

            vbottom.x = vtop.x + (r.nextInt(window/NODE_SIZE)+1)*(NODE_SIZE+10);
            vbottom.y = r.nextInt(Height/NODE_SIZE)*(NODE_SIZE);
            VQueue.Push(vbottom);
        }

    }

}
