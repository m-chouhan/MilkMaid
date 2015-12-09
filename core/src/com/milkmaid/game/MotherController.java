package com.milkmaid.game;

import com.badlogic.gdx.graphics.OrthographicCamera;

/**
 * Created by maximus_prime on 9/12/15.
 */
public abstract class MotherController {

    private final String TAG = "MOTHER WORLD CLASS";

    protected float Speed = 0.0f;
    protected Vertex LastTouched = null;

    protected VertexQueue VQueue;
    protected boolean Game_Started = false;

    protected final OrthographicCamera camera;
    protected final GameSuperviser Superviser;
    protected final Player player;

    public final int WorldHeight ; //Height of our world
    public final int ScreenWidth,ScreenHeight;

    public MotherController(VertexQueue vqueue,GameSuperviser superviser,Player p) {

        Superviser = superviser;
        ScreenWidth = Superviser.getWidth(); ScreenHeight = Superviser.getHeight();
        WorldHeight = superviser.getWorldHeight();
        VQueue = vqueue;
        camera = superviser.getDisplayCamera();
        player = p;
    }

    private int generateProbability(double val) {
        double prob = Math.random()/val;
        if( prob <= 1) return 1;
        else return 0;
    }


    public abstract void setLastTouched(Vertex last);
    //NOT IN USE FOR NOW :)
    public abstract void VertexUnTouched(Vertex vertex);



    public abstract void VertexTouched(Vertex vertex);
    public abstract void update();

    public void startGame() { Game_Started = true; }
    public void setSpeed(float f) { Speed = f; }

    public float getSpeed() { return Speed;}
    public VertexQueue getVQueue() {return VQueue; }
    public OrthographicCamera getCamera() { return camera; }
    public Vertex getLastTouched() { return LastTouched; }

}
