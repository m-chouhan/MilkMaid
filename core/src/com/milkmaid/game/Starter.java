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
        setScreen(Superviser);
    }
}
