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

    public SuperSharperWorld(GameSuperviser superviser,Player p) {
        super(superviser,p);
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
                Superviser.SwitchState(Model.GameState.NORMAL);
                return;
            }

            for (Vertex vertex : LastTouched.getEdgeList())
                vertex.changeState(Vertex.Status.UnReachable);

            LastTouched.changeState(Vertex.Status.Dead);

            LastTouched = Stack.removeFirst();

            LastTouched.changeState(Vertex.Status.Touched);
            for (Vertex vertex: LastTouched.getEdgeList())
                vertex.changeState(Vertex.Status.Reachable);

            MoveTo(LastTouched.x);
            Superviser.updateScore(LastTouched.getWeight());
            counter = 0;
        }

        camera.translate(Speed, 0);
        camera.update();

        int camera_bottom = (int) (camera.position.x - camera.viewportWidth/2);
        if(VQueue.getVertex(0).x <camera_bottom)
        {
            VQueue.RecycleStartVertex();
            //Do this only limited times to ensure correctness
            /*
            if(correction_counter > 0) {
                v.Connect(Top);
                correction_counter--;
            }
            */
        }

        counter++;
    }

    //Searches path from Vertext v to topmost vertex
    public void searchPath(Vertex v) {

        correction_counter = 2;
        Top = VQueue.getVertex(VQueue.getSize() -1);
        LastTouched = v;
        for(Vertex e: v.getEdgeList()) {
            if (DFS(e, Top, Stack)) {
                Gdx.app.log(TAG, "Reachable :)");
                return;
            }
        }
        Gdx.app.log(TAG, "Not Reachable :(");
    }

    /**
     *
     * @param start : start vertex
     * @param end : end vertex
     * @param stack : list of vertices
     * @return
     */
    private boolean DFS(Vertex start, Vertex end, LinkedList<Vertex> stack) {

        if(start == end) {
            stack.addLast(start);
            return true;
        }
        if(stack.indexOf(start) != -1 || start.getCurrentState() == Vertex.Status.Touched ) return false;

        stack.addLast(start);
        for( Vertex e: start.getEdgeList()) {
            if(DFS(e,end,stack)) return true;
        }

        stack.removeLast();
        return false;
    }

    @Override
    public void startGame(Vertex v) {
        Game_Started = true;
        searchPath(v);
    }
    @Override
    public void VertexTouched(Vertex v) {

    }
}
