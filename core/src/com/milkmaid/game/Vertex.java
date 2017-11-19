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

    public void changeState(Status nextState) {

        if(nextState == Status.Dead) {
            for(Iterator<Vertex> it = EdgeList.iterator(); it.hasNext(); ) {
                Vertex vertex = it.next();
                it.remove();
                for(Iterator<Vertex> it2 = vertex.EdgeList.iterator(); it2.hasNext(); ) {
                    Vertex vertex2 = it2.next();
                    if (vertex2 == this) it2.remove();
                }
            }
        }
        currentState = nextState;
    }

    public ArrayList<Vertex> getEdgeList() { return EdgeList; }

    public static void Disconnect(Vertex a,Vertex b) {

        for(Iterator<Vertex> it = a.EdgeList.iterator(); it.hasNext(); ) {
            Vertex vertex = it.next();
            if (vertex == b) it.remove();
        }

        for(Iterator<Vertex> it = b.EdgeList.iterator(); it.hasNext(); ) {
            Vertex vertex = it.next();
            if (vertex == a) it.remove();
        }

        if(a.EdgeList.size() == 0 ) a.changeState(Status.Dead);
        if(b.EdgeList.size() == 0 ) b.changeState(Status.Dead);
    }

    public static void Connect(Vertex a,Vertex b) {
        boolean connected = false;
        for(Vertex vertex: a.EdgeList) {
            if(vertex == b) connected = true;
        }
        if(!connected) a.EdgeList.add(b);
        connected = false;
        for(Vertex vertex: b.EdgeList) {
            if(vertex == a) connected = true;
        }
        if(!connected) b.EdgeList.add(a);
    }

    public int getSize() { return SIZE; }
    public Type getVertexType() { return VertexType;}
    public void setVertexType(Type t) { VertexType = t; }
    public int getWeight() { return WEIGHT; }
    public Status getCurrentState() { return currentState; }

}
