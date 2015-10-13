package com.milkmaid.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;

/**
 * Created by maximus_prime on 27/9/15.
 */
public class Starter extends Game {

    GameSuperviser Superviser;
    private final String TAG = "STARTER";
    @Override
    public void create() {
        Gdx.app.log(TAG, "wid--"+ Gdx.graphics.getWidth()+"Hei--"+Gdx.graphics.getHeight());
        int width = Gdx.graphics.getWidth(),height = Gdx.graphics.getHeight();
        Superviser = new GameSuperviser(width,height);
        /*
        VertexQueue VQ = new VertexQueue(25);

        MyGameWorld = new World(width,height,VQ);
        Painter P = new Painter(width,height,MyGameWorld);
        Gdx.input.setInputProcessor(new InputHandler(MyGameWorld));*/
        setScreen(Superviser);
    }
}
