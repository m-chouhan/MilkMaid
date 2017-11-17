package com.milkmaid.game;

import com.badlogic.gdx.math.Vector2;
import com.sun.org.apache.xml.internal.serializer.ElemDesc;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by maximus_prime on 3/10/15.
 * MODEL CLASS
 */
public class Vertex extends Vector2 {

    enum Status { Visible,Touched,Dead,Reachable,UnReachable };
    enum Type {Normal,Taller,Stronger,Sharper,Bonus};

    private static boolean RESET = false;

    private Status currentState = Status.Visible;
    private ArrayList<Vertex> EdgeList = new ArrayList<Vertex>();
    private boolean explored = false;
    private Type VertexType = Type.Normal;
    private final int WEIGHT = 5; //weight of the vertex;
    private final int SIZE = 50; //DIMENSIONS of the vertex

    public Vertex() {
        super();
        x = 0;y = 0;
    }

    public Vertex(int x,int y) {

        super();
        this.x = x;this.y = y;
    }

    public static void Reset() { RESET = !RESET; } //optimized reset function for painting


    public void changeState(Status s) {

        //TODO: add state transitions
        switch (s) {

            case Touched:
                    break;
            case Dead:
                    for(Vertex v:EdgeList) {
                        v.changeState(Status.Visible);
                        v.Disconnect(this);
                    }
                    EdgeList.clear();
                    break;
            case Visible:
                    break;

            case Reachable:
            case UnReachable:
                    if( currentState == Status.Touched) return;
                    break;
        }
        currentState = s;
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
