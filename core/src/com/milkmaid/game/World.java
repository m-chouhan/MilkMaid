package com.milkmaid.game;


/**
 * Created by maximus_prime on 27/9/15.
 */

import com.badlogic.gdx.graphics.OrthographicCamera;


public class World extends MotherController {

    private final String TAG = "WORLD CLASS";

    public World(GameSuperviser superviser,Player p) {
        super(superviser,p);
    }

    @Override
    public void VertexTouched(Vertex vertex) {

        if(LastTouched == vertex ) return;//eliminate repeated events

        //TODO: Check for vertext type and set their control
        if( LastTouched == null) {
          if(vertex == VQueue.getVertex(0)) {
              LastTouched = vertex;
              LastTouched.changeState(Vertex.Status.Touched);
              for(Vertex ver:LastTouched.getEdgeList())
                  ver.changeState(Vertex.Status.Reachable);
              startGame(LastTouched);
              setSpeed(1.0f);
          }
          return;
        }

        for( Vertex nextVertex:LastTouched.getEdgeList() ) {

            if(nextVertex == vertex) {
                updateLastTouched(vertex);
                return;
            }
        }
    }

    private void updateLastTouched(Vertex newLastTouched) {

        for (Vertex h : LastTouched.getEdgeList())
            h.changeState(Vertex.Status.Alive);
        LastTouched.changeState(Vertex.Status.Dead);
        newLastTouched.changeState(Vertex.Status.Touched);
        for (Vertex vertex : newLastTouched.getEdgeList())
            vertex.changeState(Vertex.Status.Reachable);

        switch (LastTouched.getVertexType()){
            case Sharper:
                Game_Started = false;
                Superviser.SwitchState(Model.GameState.SHARPER);
                break;
            case Stronger:
                Game_Started = false;
                Superviser.SwitchState(Model.GameState.STRONGER);
                break;
            case Normal:
            case Bonus:
        }

        Superviser.updateScore(newLastTouched.getWeight());
        LastTouched = newLastTouched;
    }

    @Override
    public void update() {

        if(Game_Started) {
            camera.translate(Speed, 0);
            camera.update();
            Speed += 0.005f;
            // player.update();
        }

        int camera_bottom = (int) (camera.position.x - camera.viewportWidth/2);
        if(VQueue.getVertex(0).x < camera_bottom)
            VQueue.RecycleStartVertex();
    }

    @Override
    public void startGame(Vertex v) {
        Game_Started = true;
        setLastTouched(v);
    }

    public VertexQueue getVQueue() {return VQueue; }
    public OrthographicCamera getCamera() { return camera; }
    public Vertex getLastTouched() { return LastTouched; }

    @Override
    public void setLastTouched(Vertex last) {

        LastTouched = last;
        for (Vertex ver : LastTouched.getEdgeList())
            ver.changeState(Vertex.Status.Reachable);
    }
    //NOT IN USE FOR NOW :)
    public void VertexUnTouched(Vertex vertex) {

    }

}
