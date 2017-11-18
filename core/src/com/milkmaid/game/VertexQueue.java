package com.milkmaid.game;

/**
 * Created by maximus_prime on 3/10/15.
 * This is a custom Queue Class for recycling Vertices efficiently
 * Also handles the pushing and popping of elements and graph generation logic
 */

import com.badlogic.gdx.math.Vector2;

import java.util.EmptyStackException;
import java.util.Random;

public class VertexQueue {

    private Vertex Array[];
    private int bottom,size;
    private Random R = new Random();
    private final int max_size;

    /**
     * @param size = num of vertices you want in your queue
     * @param WorldHeight = 'width' of game
     */
    public VertexQueue(int size,int WorldHeight) {

        bottom = 0;this.size = 0;
        max_size = size;
        Array = new Vertex[size];

        int x = 100,y = WorldHeight/2;
        Vertex start = new Vertex(x,y);
        Push(start);

        for(int i = 1 ;i < max_size;++i) {
            Push(new Vertex());
        }
    }

    public int getSize() { return size; }

    public Vertex getVertex(int i) {
        if( i < size && i>= 0) return Array[ (bottom + i) % max_size ];
        throw new ArrayIndexOutOfBoundsException();
    }

    //search for max value < x and return the index for iterating over it
    public int SearchUpperBound(int x) {

        int step_size = (int)Math.ceil(size/2f);
        int i = step_size;

        while (i >= 0 && i < size && step_size > 0) {

            Vertex v = getVertex(i);
            step_size = (int) Math.ceil(step_size /2f);

            if (v.x < x) {
                Vertex v2 = getVertex(i + 1);
                if (v2.x >= x) return i;
                else i += step_size;

            }
            else i -= step_size;
        }
        return -1;
    }

    private void ConnectRandomly(int size) {

        Vertex v = getVertex(size-1);
//
//        int a = 0,b = 0;
//        a = size - 1;b = size - 2;
//        if (size < 3) {
//            a = R.nextInt(size);
//            b = R.nextInt(size);
//            b = a;
//        } else {
//            a = size - 1;//(R.nextInt(3) + 1);
//            b = size - 2;//(R.nextInt(3) + 1);
//        }
//
//        v.Connect(getVertex(a));
//        if( a != b && !Overlap(b,v) ) v.Connect(getVertex(b));

        v.setVertexType(Vertex.Type.Normal);
    }

    private boolean Push(Vertex v) {

        if(size >= max_size) return false;

        Array[(bottom + size) % max_size] = v;
        size++;
        if(size == 1) return true;
        if(size < 4) {
            // Randomly select a position from 5 X 6 grid
            v.x = (getVertex(size - 1).x + (R.nextInt(5)+5)*40);
            v.y = R.nextInt(6) * (80) + 20;
            v.Connect(getVertex(size-2));
            return true;
        }

        // set the location of next node at some minimum distance from other nodes
        Vector2 v1 = getVertex(size - 2) ,v2 = getVertex(size - 3) ,v3 = getVertex(size - 4);
        do {
            v.x = (v1.x + (R.nextInt(5)+2)*40);
            v.y = R.nextInt(6) * (80);
        }
        while(v1.dst(v) < 200 || v2.dst(v) < 200 || v3.dst(v) < 200);

        Vertex vertexA = getVertex(size - 2),vertexB = getVertex(size - 3);
        v.Connect(vertexA);

        Vector2 A = new Vector2(v).sub(vertexA), B = new Vector2(vertexB).sub(vertexA);
        float slopeThreshold = Math.abs(A.angle(B));
        if( slopeThreshold < 120) v.Connect(vertexB);
        return true;
    }

    public void RecycleStartVertex() {
        Vertex v = Pop();
        v.changeState(Vertex.Status.Alive);
        Push(v);
    }

    private Vertex Pop() {

        if( size > 0) {
            Vertex v = Array[bottom];
            v.changeState(Vertex.Status.Dead);
            bottom = (bottom + 1) % max_size;
            size--;
            return v;
        }
        throw new EmptyStackException();
    }

    public int getLargestIndex() { return (bottom+size-1)%max_size; }
    public int getSmallestIndex(){ return bottom; }

}
