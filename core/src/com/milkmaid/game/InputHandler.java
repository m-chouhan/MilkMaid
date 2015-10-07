package com.milkmaid.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by maximus_prime on 3/10/15.
 */

public class InputHandler implements InputProcessor {

    private final String TAG = "INPUTPROCESSOR";
    private OrthographicCamera camera;
    private World myWorld;

    public InputHandler(World world) {
        myWorld = world;
        camera = world.getCamera();
    }

    @Override
    public boolean keyDown(int i) {
        return false;
    }

    @Override
    public boolean keyUp(int i) {
        return false;
    }

    @Override
    public boolean keyTyped(char c) {
        return false;
    }

    @Override
    public boolean touchDown(int x, int y, int pointer, int button) {

        Vector3 v = camera.unproject(new Vector3(x,y,0));

        Gdx.app.log(TAG,"TouchDown "+v.x+"|"+v.y);
        return false;
    }

    @Override
    public boolean touchUp(int x, int y, int pointer, int button) {

        Vector3 v = camera.unproject(new Vector3(x,y,0));
        Gdx.app.log(TAG,"TouchUP");

        return false;
    }

    @Override
    public boolean touchDragged(int x, int y, int pointer) {

        Vector3 v = camera.unproject(new Vector3(x,y,0));
        Gdx.app.log(TAG,"TouchDragged");

        return false;
    }

    @Override
    public boolean mouseMoved(int i, int i1) {
        return false;
    }

    @Override
    public boolean scrolled(int i) {
        return false;
    }
}
