package com.milkmaid.game;

import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

/**
 * Created by maximus_prime on 3/10/15.
 */
public class Vertex extends Vector2 {

    enum Status { Visible,Touched,Dead,Invisible,Reachable,UnReachable };
    private static boolean RESET = false;

    private Status currentState = Status.Visible;
    private ArrayList<HalfEdge> EdgeList = new ArrayList<HalfEdge>();
    private boolean explored = false;


    public Vertex(int x,int y) {

        super();
        this.x = x;this.y = y;
    }

    public static void Reset() { RESET = !RESET; }
    public Status getCurrentState() { return currentState; }
    public void changeState(Status s) {

        //TODO: add state transitions
        switch (s) {

            case Touched:
                    break;
            case Dead:
                    for(HalfEdge he:EdgeList) {
                        he.getDst().Disconnect(this);
                    }
                    EdgeList.clear();
                    break;

            case Invisible:
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
    public ArrayList<HalfEdge> getEdgeList() { return EdgeList; }

    public boolean IsExplored() { return explored != RESET;}

    public void MarkExplored() { explored = !RESET; }

    public void Disconnect(Vertex v) {
        int i = 0;
        for(;i<EdgeList.size();++i) {
            if (EdgeList.get(i).getDst() == v) break;
        }
        if( i < EdgeList.size() ) EdgeList.remove(i);

    }
    public void Connect(Vertex v,int weight) {

        HalfEdge H1 = new HalfEdge(this,v,weight);
        HalfEdge H2 = new HalfEdge(v,this,weight);

        addEdge(H1);
        v.addEdge(H2);
    }

    private void addEdge(HalfEdge h) {
        //TODO: check for redundancy in edgelist
        EdgeList.add(h);
    }

}
