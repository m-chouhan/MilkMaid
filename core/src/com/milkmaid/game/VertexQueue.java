package com.milkmaid.game;

/**
 * Created by maximus_prime on 3/10/15.
 * This is a custom Queue Class for recycling Vertices efficiently
 */

import com.badlogic.gdx.math.Vector2;

import java.util.EmptyStackException;
import java.util.Random;

public class VertexQueue {

    private Vertex Array[];
    private int bottom,size;
    private Random R = new Random();
    private final int max_size;
    public VertexQueue(int size) {

        bottom = 0;this.size = 0;

        max_size = size;
        Array = new Vertex[size];
    }

    public int getSize() { return size; }
    public int getMax_size() { return max_size; }

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
    public void Push(Vertex v) {

        if(size >= max_size) return;

        if( size == 0 ) {
            Array[bottom] = v;
            size++;
            return;
        }

        v.x = (getVertex(size - 1).x + (R.nextInt(5)+1)*40);
        v.y = R.nextInt(6) * (80);

        if( size > 3) {
            Vector2 v1 = getVertex(size - 1) ,v2 = getVertex(size - 2) ,v3 = getVertex(size - 3);
            do {
                v.x = (v1.x + (R.nextInt(5)+1)*40);
                v.y = R.nextInt(6) * (80);
            }
            while( v1.dst(v) < 100 || v2.dst(v) < 150 || v3.dst(v) < 200);
        }

        int a = 0,b = 0;

        if (size < 7) {
            a = R.nextInt(size);
            b = R.nextInt(size);
            b = a;
        } else {
            a = size - 1;//(R.nextInt(3) + 1);
            b = size - 2;//(R.nextInt(3) + 1);

        }

        if( !Overlap(a,v) )v.Connect(getVertex(a),1);
        if( a != b && !Overlap(b,v) ) v.Connect(getVertex(b),1);

        Array[(bottom + size) % max_size] = v;
        size++;

        v.changeState(Vertex.Status.Invisible);
        //TODO: Add case for superpower Taller :/
        switch ((int)(Math.random()*30) ) {

            case 9:
            case 8:
            case 7:
                v.setVertexType(Vertex.Type.Stronger);
                break;

            default:
                v.setVertexType(Vertex.Type.Normal);
                break;
        }
    }

    /*Checks if two line overlap or not  */
    private boolean Overlap(int a,Vertex v) {

        Vertex ver = getVertex(a);
        float slope = (v.y - ver.y) /(v.x-ver.x) ;
        while(++a < size) {

            Vertex v2 = getVertex(a);
            float y = v2.y - ver.y;
            float x = slope*(v2.x-ver.x);
            float res = y - x;
            if( Math.abs(res) < 60 ) return true;
        }
        return false;
    }

    public Vertex Pop() {

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
