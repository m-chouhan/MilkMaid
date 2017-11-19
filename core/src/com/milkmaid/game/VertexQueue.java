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
    private final int capacity;

    /**
     * @param capacity = num of vertices you want in your queue
     */
    public VertexQueue(int capacity) {

        bottom = 0;
        size = 0;
        this.capacity = capacity;
        Array = new Vertex[capacity];
        for(int i = 0; i < capacity; ++i) {
            Push(new Vertex());
        }
    }

    public int getSize() { return size; }

    public Vertex getVertex(int i) {
        if( i < size && i>= 0) return Array[ (bottom + i) % capacity];
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

    private void Connect(int index) {

        if(index == 0) return;

        Vertex top_vertex = getVertex(index),second_top = getVertex(index-1);
        top_vertex.Connect(second_top);
        if(index > 1) {
            Vertex third_top = getVertex(index-2);
            Vector2 A = new Vector2(top_vertex).sub(second_top), B = new Vector2(third_top).sub(second_top);
            float slopeThreshold = Math.abs(A.angle(B));
            if (slopeThreshold < 120) top_vertex.Connect(third_top);
        }
    }

    private void setRandomPositonOf(int index) {

        Vertex top_vertex = getVertex(index);
        if(index == 0) {
            top_vertex.set(100,Model.WorldHeight/2);
            return;
        }

        Vertex previous_top = getVertex(index-1);
        // set the location of next node at some minimum distance from previous 2 other nodes
        do {
            top_vertex.x = previous_top.x + (R.nextInt(5)+2)*40;
            top_vertex.y = R.nextInt(6) * (80);
        }
        while(previous_top.dst(top_vertex) < 200 || (index >= 2 && getVertex(index-2).dst(top_vertex) < 200));
    }

    private boolean Push(Vertex top_vertex) {

        if(size >= capacity) return false;

        Array[(bottom + size++) % capacity] = top_vertex;
        setRandomPositonOf(size-1);
        Connect(size-1);
        return true;
    }

    public void RecycleBottomVertex() {
        Vertex v = Pop();
        v.changeState(Vertex.Status.Alive);
        Push(v);
    }

    private Vertex Pop() {

        if( size > 0) {
            Vertex v = Array[bottom];
            v.changeState(Vertex.Status.Dead);
            bottom = (bottom + 1) % capacity;
            size--;
            return v;
        }
        throw new EmptyStackException();
    }

    public int getLargestIndex() { return (bottom+size-1)% capacity; }
    public int getSmallestIndex(){ return bottom; }

}
