package com.milkmaid.game;

/**
 * Created by maximus_prime on 27/9/15.
 */

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;

import java.util.Random;


public class World {

    private final String TAG = "WORLD";


    private Vector2 VertexArray[];
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

        VertexArray = new Vector2[MAX_ELEMENTS];
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
        VertexArray[0] = new Vector2(x,Height/2);
        VQueue.Push(new Vertex(x,Height/2));

        for(int i = 1 ;i<MAX_ELEMENTS;++i) {

            x += (r.nextInt(window/NODE_SIZE)+1)*(NODE_SIZE+10);
            VertexArray[i] = new Vector2(x,r.nextInt(Height/NODE_SIZE)*(NODE_SIZE));
            VQueue.Push(new Vertex((int)VertexArray[i].x,(int)VertexArray[i].y));

            if( i < 7) {
                int rnum = r.nextInt(i);
                Graph[rnum][i] = Graph[i][rnum] = 1;
                rnum = r.nextInt(i);
                Graph[rnum][i] = Graph[i][rnum] = 1;
            }
            else {
                int a = i - (r.nextInt(6) + 1),
                        b = i - (r.nextInt(3));
                Graph[i][a] = Graph[a][i] =
                        Graph[i][b] = Graph[b][i] = 1;
            }
        }
        /*
        for( int i = 0;i<MAX_ELEMENTS;++i) {
            for(int j = 0;j<i;++j) {
                double prob = 50f/VertexArray[i].dst(VertexArray[j]);
                Graph[i][j] = generateProbability(prob);
            }
        }*/
    }

    private int generateProbability(double val) {
        double prob = Math.random()/val;
        if( prob <= 1) return 1;
        else return 0;
    }

    public VertexQueue getVQueue() {return VQueue; }
    public Vector2[] getVertexArray() { return VertexArray; }
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
        /*
        if( VertexArray[BottomIndex].x < camera_bottom) {

            Random r = new Random();
            int window = Max_Width/MAX_ELEMENTS+50;
            int topmostIndex = BottomIndex - 1;

            if( BottomIndex == 0 ) topmostIndex = MAX_ELEMENTS -1;

            VertexArray[BottomIndex].x = VertexArray[topmostIndex].x + (r.nextInt(window/NODE_SIZE)+1)*(NODE_SIZE+10);
            VertexArray[BottomIndex].y = r.nextInt(Height/NODE_SIZE)*(NODE_SIZE);

            for(int i = 0;i<MAX_ELEMENTS;++i)
                Graph[BottomIndex][i] = Graph[i][BottomIndex] = 0;

            //Now bottom most element is at top so generate new edges in invisible top area

            int a = BottomIndex -(r.nextInt(6)+1),
                    b = BottomIndex - (r.nextInt(3));
            if( a < 0) a += MAX_ELEMENTS;
            if( b < 0) b += MAX_ELEMENTS;
            Graph[BottomIndex][a] = Graph[a][BottomIndex] =
                    Graph[BottomIndex][b] = Graph[b][BottomIndex] = 1;

            /*while( VertexArray[topmostIndex].x > camera_top) {

                double prob = 20f/VertexArray[BottomIndex].dst(VertexArray[topmostIndex]);
                Graph[BottomIndex][topmostIndex] = Graph[topmostIndex][BottomIndex]
                        = generateProbability(prob);

                if(topmostIndex == 0 ) topmostIndex = MAX_ELEMENTS -1;
                else topmostIndex--;
            }*/
            //BottomIndex = (++BottomIndex) % MAX_ELEMENTS;
        //}
        //Gdx.app.log(TAG, "LowerBound[" + (camera.position.x - camera.viewportWidth / 2));
        //Gdx.app.log(TAG, "UpperBound[" + (camera.position.x + camera.viewportWidth / 2));

    }

}
