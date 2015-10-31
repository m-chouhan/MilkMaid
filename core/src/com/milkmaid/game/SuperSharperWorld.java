package com.milkmaid.game;

import com.badlogic.gdx.Gdx;

import java.util.LinkedList;

/**
 * Created by maximus_prime on 13/10/15.
 */

public class SuperSharperWorld extends World {

    private LinkedList<Vertex> Stack = new LinkedList<Vertex>();
    private final String TAG = "SHARPERWORLD";

    private final float timeStep = 20;
    private float counter = timeStep;
    private Vertex Top;

    public SuperSharperWorld(VertexQueue vqueue,GameSuperviser superviser) {
        super(vqueue,superviser);
    }

    private void MoveTo(float xpos ) {

        Speed = (xpos-camera.position.x)/timeStep;
        //Speed *= 0.4f;
    }

    private int correction_counter = 2;

    @Override
    public void update() {

        if(counter == timeStep) {

            if(Stack.size() == 0) {
                Superviser.SwitchState(GameSuperviser.GameState.NORMAL);
                return;
            }

            for (HalfEdge h : LastTouched.getEdgeList())
                h.getDst().changeState(Vertex.Status.UnReachable);

            LastTouched.changeState(Vertex.Status.Dead);

            LastTouched = Stack.removeFirst();

            LastTouched.changeState(Vertex.Status.Touched);
            for (HalfEdge h : LastTouched.getEdgeList())
                h.getDst().changeState(Vertex.Status.Reachable);

            MoveTo(LastTouched.x);
            Superviser.updateScore(LastTouched.getWeight());
            counter = 0;
        }

        camera.translate(Speed, 0);
        camera.update();

        int camera_bottom = (int) (camera.position.x - camera.viewportWidth/2);
        if(VQueue.getVertex(0).x <camera_bottom)
        {
            Vertex v = VQueue.Pop();
            VQueue.Push(v);
            //Do this only limited times to ensure correctness
            if(correction_counter > 0) {
                v.Connect(Top, v.getWeight());
                correction_counter--;
            }
        }

        counter++;
    }

    //Searches path from lastTouched Vertext to topmost vertex
    public void searchPath(Vertex v) {

        correction_counter = 2;
        Top = VQueue.getVertex(VQueue.getSize() -1);
        LastTouched = v;
        for(HalfEdge e: v.getEdgeList()) {
            if (Greedy_DFS(e.getDst(), Top, Stack)) {
                Gdx.app.log(TAG, "Reachable :)");
                return;
            }
        }
        Gdx.app.log(TAG, "Not Reachable :(");
    }

    //TODO: Implement Greedy Part :P
    private boolean Greedy_DFS(Vertex v, Vertex top, LinkedList<Vertex> stack) {
        if(v == top) {
            stack.addLast(v);
            return true;
        }
        if( stack.indexOf(v) != -1 || v.getCurrentState() == Vertex.Status.Touched ) return false;

        stack.addLast(v);
        for( HalfEdge e: v.getEdgeList()) {
            if(Greedy_DFS(e.getDst(),top,stack)) return true;
        }

        stack.removeLast();
        return false;
    }

    @Override
    public void VertexTouched(Vertex v) {

    }
}
