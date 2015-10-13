package com.milkmaid.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;

/**
 * Created by maximus_prime on 27/9/15.
 */
public class Starter extends Game {

    World MyGameWorld;
    private final String TAG = "STARTER";
    @Override
    public void create() {
        Gdx.app.log(TAG, "wid--"+ Gdx.graphics.getWidth()+"Hei--"+Gdx.graphics.getHeight());

        VertexQueue VQ = new VertexQueue(25);
        int width = Gdx.graphics.getWidth(),height = Gdx.graphics.getHeight();
        MyGameWorld = new World(width,height,VQ);
        Painter P = new Painter(width,height,MyGameWorld);
        Gdx.input.setInputProcessor(new InputHandler(MyGameWorld));
        setScreen(MyGameWorld.getPainterScreen());
    }
}
