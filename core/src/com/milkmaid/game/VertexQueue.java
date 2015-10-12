package com.milkmaid.game;

/**
 * Created by maximus_prime on 3/10/15.
 * This is a custom Queue Class for recycling Vertices efficiently
 */

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

        if( size == 0 ) {
            Array[bottom] = v;
            size++;
        }
        else if( size < max_size ) {

            int a = 0,b = 0;

            if( size < 7) {
                a = R.nextInt(size);
                b = R.nextInt(size);b = a;
            }
            else {
                a = size - (R.nextInt(3) + 1);
                b = size - (R.nextInt(3) + 1);

            }
            v.Connect(getVertex(a),1);
            if( a != b ) v.Connect(getVertex(b),1);

            Array[(bottom + size) % max_size] = v;
            size++;
        }
        v.changeState(Vertex.Status.Invisible);
        //TODO: Add case for superpower Taller :/
        switch ((int)(Math.random()*30) ) {

            case 9:
            case 8:
                v.setVertexType(Vertex.Type.Sharper);
                break;
            case 7:
                v.setVertexType(Vertex.Type.Stronger);
                break;
            default:
                v.setVertexType(Vertex.Type.Normal);
                break;
        }
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
