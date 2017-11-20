package com.milkmaid.game;

/**
 * Created by maximus_prime on 3/10/15.
 * This is a custom Queue Class for recycling Vertices efficiently
 * Also handles the positioning and edges of vertices in queue
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

    /**
     * @param x : x position
     * @return max(index).x < x
     */
    public int SearchUpperBound(int x) {

        for(int i = 1; i< size;++i) {
            Vertex prev = getVertex(i-1);
            Vertex current = getVertex(i);
            if( prev.x < x && current.x >= x ) return i-1;
        }
        return -1;
    }

    private void Connect(int index) {

        if(index == 0) return;

        Vertex top_vertex = getVertex(index),second_top = getVertex(index-1);
        Vertex.Connect(top_vertex,second_top);
        if(index > 1) {
            Vertex third_top = getVertex(index-2);
            Vector2 A = new Vector2(top_vertex).sub(second_top), B = new Vector2(third_top).sub(second_top);
            float slopeThreshold = Math.abs(A.angle(B));
            if (slopeThreshold < 145 && Utility.probabilityOf(0.6)) Vertex.Connect(top_vertex,third_top);
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
            top_vertex.x = previous_top.x + (R.nextInt(4)+2)*40;
            top_vertex.y = R.nextInt(7) * (80);
        }
        while(previous_top.dst(top_vertex) < 200 || (index >= 2 && getVertex(index-2).dst(top_vertex) < 200));
    }

    /**TODO:
     * Randomly assigns a type to each vertex
     * @param index
     */
    private void setVertexType(int index) {
        if(index == 0)
            getVertex(0).changeState(Vertex.Status.Reachable);
    }

    private boolean Push(Vertex top_vertex) {

        if(size >= capacity) return false;

        Array[(bottom + size++) % capacity] = top_vertex;
        setRandomPositonOf(size-1);
        Connect(size-1);
        setVertexType(size-1);
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

}
