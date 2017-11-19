package com.milkmaid.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector3;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;

/**
 * Created by mahendras on 19/11/17.
 */

public class InputObservable extends InputAdapter {

    private final FlowableEmitter<InputEvent> emitter;
    private Camera camera;
    private InputObservable(Camera camera, FlowableEmitter<InputEvent> emitter) {
        this.camera = camera;
        this.emitter = emitter;
    }

    enum EventType{UP,DOWN,DRAGGED};
    static class InputEvent{
        public final int x,y;
        public final EventType type;
        public InputEvent(int x,int y,EventType type){
            this.x = x;
            this.y = y;
            this.type = type;
        }
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Vector3 touch3D = camera.unproject(new Vector3(screenX,screenY,0));
        emitter.onNext(new InputEvent((int)touch3D.x,(int)touch3D.y, EventType.DOWN));
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        Vector3 touch3D = camera.unproject(new Vector3(screenX,screenY,0));
        emitter.onNext(new InputEvent((int)touch3D.x,(int)touch3D.y, EventType.UP));
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        Vector3 touch3D = camera.unproject(new Vector3(screenX,screenY,0));
        emitter.onNext(new InputEvent((int)touch3D.x,(int)touch3D.y, EventType.DRAGGED));
        return false;
    }

    /**
     * @param camera : camera object
     * @return an Observable which emits value based on camera world coordinates
     */
    static Flowable<InputEvent> create(Camera camera){
        return Flowable.create(emitter-> {
            Gdx.input.setInputProcessor(new InputObservable(camera,emitter));
        }, BackpressureStrategy.MISSING);
    }
}
