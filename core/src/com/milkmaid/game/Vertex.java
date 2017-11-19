package com.milkmaid.game;

import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Represents a node in the Graph
 */
public class Vertex extends Vector2 {

    enum Status {Alive,Touched,Dead,Reachable};
    enum Type {Normal,Stronger,Sharper};
    private final ArrayList<Vertex> EdgeList = new ArrayList<Vertex>();
    private final int WEIGHT = 5; //weight of the vertex;
    private final int SIZE = 50; //DIMENSIONS of the vertex

    private Status currentState = Status.Alive;
    private Type VertexType = Type.Normal;

    public Vertex() {
        super(0,0);
    }

    public Vertex(int x,int y) {
        super(x,y);
    }

    public void changeState(Status nextState) {

        if(nextState == Status.Dead) {
            for (Vertex v : EdgeList) {
                v.changeState(Status.Alive);
                v.Disconnect(this);
            }
            EdgeList.clear();
        }
        currentState = nextState;
    }

    public ArrayList<Vertex> getEdgeList() { return EdgeList; }

    public void Disconnect(Vertex v) {

        for(Iterator<Vertex> it = EdgeList.iterator(); it.hasNext(); ) {
            Vertex vertex = it.next();
            if (vertex == v) it.remove();
        }

        if(EdgeList.size() == 0 ) currentState = Status.Dead;
    }

    public void Connect(Vertex v) {
        //ignores duplicate edges
        for(Vertex vertex: EdgeList) {
            if(vertex == v) return;
        }

        EdgeList.add(v);
        v.EdgeList.add(this);
    }

    public int getSize() { return SIZE; }
    public Type getVertexType() { return VertexType;}
    public void setVertexType(Type t) { VertexType = t; }
    public int getWeight() { return WEIGHT; }
    public Status getCurrentState() { return currentState; }

}
