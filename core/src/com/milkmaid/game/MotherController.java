package com.milkmaid.game;

import com.badlogic.gdx.graphics.OrthographicCamera;

/**
 * Created by maximus_prime on 9/12/15.
 * MOTHER CLASS for all game controllers
 * Provides all basic methods and variables that should be supported by any controller class
 */

public abstract class MotherController {

    private final String TAG = "MOTHER WORLD CLASS";

    protected float Speed = 0.0f;
    protected Vertex LastTouched = null; //Lastouched Vertex, required during switching game states

    protected final VertexQueue VQueue; //Initialized only once cannot change value
    protected boolean Game_Started = false;

    protected final OrthographicCamera camera; //Represents everything that is being displayed on screen
    protected final GameSuperviser Superviser;
    protected final Player player;

    public final int WorldHeight ; //Height of our world
    public final int ScreenWidth,ScreenHeight;

    public MotherController(VertexQueue vqueue,GameSuperviser superviser,Player p) {

        Superviser = superviser;
        ScreenWidth = Superviser.getWidth();
        ScreenHeight = Superviser.getHeight();
        WorldHeight = superviser.getWorldHeight();
        VQueue = vqueue;
        camera = superviser.getDisplayCamera();
        player = p;
    }

    //returns 0/1 according to some probability of an event given as  val
    private int generateProbability(double val) {
        double prob = Math.random()/val;
        if( prob <= 1) return 1;
        else return 0;
    }


    //used during switching game states for concurrency
    public abstract void setLastTouched(Vertex last);

    public abstract void VertexUnTouched(Vertex vertex);
    //Action to be taken when a vertex is touched
    public abstract void VertexTouched(Vertex vertex);
    public abstract void update();

    public void startGame(Vertex v) { Game_Started = true;LastTouched = v; }
    public void setSpeed(float f) { Speed = f; }

    public float getSpeed() { return Speed;}
    public VertexQueue getVQueue() {return VQueue; }
    public OrthographicCamera getCamera() { return camera; }
    public Vertex getLastTouched() { return LastTouched; }

}
