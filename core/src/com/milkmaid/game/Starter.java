package com.milkmaid.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;

import rx.Observer;

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

    Observer<Integer> observer = new Observer<Integer>() {
        @Override
        public void onCompleted() {
            System.out.println("All data emitted.");
        }

        @Override
        public void onError(Throwable e) {
            System.out.println("Error received: " + e.getMessage());
        }

        @Override
        public void onNext(Integer integer) {
            System.out.println("New data received: " + integer);
        }
    };
}
