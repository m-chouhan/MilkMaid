package com.milkmaid.game;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by maximus_prime on 3/10/15.
 */

public class InputHandler implements InputProcessor {

    private final String TAG = "INPUTPROCESSOR";
    private OrthographicCamera camera;
    private World myWorld;
    private VertexQueue VQueue;
    private final int Node_Size,TOUCH_DIM = 10;

    public InputHandler(World world) {
        myWorld = world;
        camera = world.getCamera();
        VQueue = world.getVQueue();
        Node_Size = world.getNodeSize();
    }

    public void setMyWorld(World w) { myWorld = w; }

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

        Vector3 touch3D = camera.unproject(new Vector3(x,y,0));
        Vector2 touchPos = new Vector2(touch3D.x,touch3D.y);

        //Gdx.app.log(TAG, "TouchDown " + touchPos.x + "|" + touchPos.y);
        int index = VQueue.SearchUpperBound((int) touchPos.x - TOUCH_DIM - Node_Size);
            Vertex v = VQueue.getVertex(++index);
            do {

                if ( v.dst(touchPos) < Node_Size + TOUCH_DIM ) {
                    //Gdx.app.log(TAG, "Collision at" + index + "[" + v.x + "|" + v.y + "]");
                    myWorld.VertexTouched(v);
                    return true;
                }
                v = VQueue.getVertex(++index);
            }while (v.x < touchPos.x + 10);
        return false;
    }

    @Override
    public boolean touchUp(int x, int y, int pointer, int button) {

        Vector3 touch3D = camera.unproject(new Vector3(x,y,0));
        Vector2 touchPos = new Vector2(touch3D.x,touch3D.y);

        //Gdx.app.log(TAG, "TouchDown " + touchPos.x + "|" + touchPos.y);
        int index = VQueue.SearchUpperBound((int) touchPos.x - TOUCH_DIM - Node_Size);
        Vertex v = VQueue.getVertex(++index);
        do {

            if ( v.dst(touchPos) < Node_Size + TOUCH_DIM ) {
                //Gdx.app.log(TAG, "Collision at" + index + "[" + v.x + "|" + v.y + "]");
                myWorld.VertexUnTouched(v);
                return true;
            }
            v = VQueue.getVertex(++index);
        }while (v.x < touchPos.x + 10);
        return false;
    }

    @Override
    public boolean touchDragged(int x, int y, int pointer) {

        touchDown(x,y,pointer,pointer);
        //Vector3 v = camera.unproject(new Vector3(x,y,0));
        //Gdx.app.log(TAG,"TouchDragged");

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
