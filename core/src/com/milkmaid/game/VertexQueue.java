package com.milkmaid.game;

/**
 * Created by maximus_prime on 3/10/15.
 */
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
        if( i < size) return Array[ (bottom + i) % max_size ];
        return null;
    }

    //TODO: Implement binary Search
    public int Search(int x) {
        return 0;
    }
    public void Push(Vertex v) {

        if( size == 0 ) {
            Array[bottom] = v;
            size++;
        }
        else if( size < max_size ) {

            //TODO: generate random edges here
            //TODO: Testing code
            int a = 0,b = 0;

            if( size < 7) {
                a = R.nextInt(size);
                b = R.nextInt(size);
            }
            else {
                a = size - (R.nextInt(6) + 1);
                b = size - (R.nextInt(3) + 1);

            }
            v.Connect(getVertex(a),1);
            if( a != b ) v.Connect(getVertex(b),1);

            Array[(bottom + size) % max_size] = v;
            size++;
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
        return null;
    }

    public int getLargestIndex() { return (bottom+size-1)%max_size; }
    public int getSmallestIndex(){ return bottom; }

}
