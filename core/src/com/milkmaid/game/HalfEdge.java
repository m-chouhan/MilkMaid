package com.milkmaid.game;

/**
 * Created by maximus_prime on 3/10/15.
 */

public class HalfEdge {

    private Vertex origin,dst;
    private int weight;

    public int getWeight() { return  weight; }
    public HalfEdge(Vertex ori,Vertex dst, int w) {

        origin = ori;
        this.dst = dst;
        weight = w;
    }

    public Vertex getDst() {
        return dst;
    }
}

