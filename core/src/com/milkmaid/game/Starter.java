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

        MyGameWorld = new World(Gdx.graphics.getWidth(),Gdx.graphics.getHeight(),VQ);
        
        Gdx.input.setInputProcessor(new InputHandler(MyGameWorld));
        setScreen(MyGameWorld.getPainterScreen());
    }
}
