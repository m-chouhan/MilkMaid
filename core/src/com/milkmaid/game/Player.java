package com.milkmaid.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import java.util.LinkedList;

/**
 * Created by maximus_prime on 23/11/15.
 */

public class Player {

    private static final int FRAME_COLS = 7;
    private static final int FRAME_ROWS = 1;
    private final int FRAME_WIDTH,FRAME_HEIGHT;

    Animation walkAnimation;
    Texture walkSheet;
    TextureRegion[] walkFrames;
    TextureRegion currentFrame;
    LinkedList<Vector3> EventQueue;

    float stateTime;//maintains game time

    final Vector3 position,velocity = new Vector3();
    GameSuperviser.GameState CurrentGameState = GameSuperviser.GameState.NORMAL;
    boolean running = false;

    /* You should provide starting position to the player*/

    public Player(Vector3 pos) {

        position = pos;
        EventQueue = new LinkedList<Vector3>();
        walkSheet = new Texture(Gdx.files.internal("frog.png"));
        FRAME_WIDTH = walkSheet.getWidth()/FRAME_COLS;
        FRAME_HEIGHT = walkSheet.getHeight()/FRAME_ROWS;

        TextureRegion[][] tmp = TextureRegion.split(walkSheet,FRAME_WIDTH,FRAME_HEIGHT);
        walkFrames = new TextureRegion[FRAME_COLS * FRAME_ROWS];
        int index = 0;
        for (int i = 0; i < FRAME_ROWS; i++) {
            for (int j = 0; j < FRAME_COLS; j++) {
                walkFrames[index++] = tmp[i][j];
            }
        }
        walkAnimation = new Animation(0.12f, walkFrames);
        stateTime = 0f;

        currentFrame = walkAnimation.getKeyFrame(stateTime, true );
    }

    public void render( SpriteBatch spriteBatch) {

        stateTime += Gdx.graphics.getDeltaTime();
        spriteBatch.begin();
        spriteBatch.draw(currentFrame,position.x-FRAME_WIDTH/2, position.y-FRAME_HEIGHT/2);
        spriteBatch.end();
//        if( walkAnimation.isAnimationFinished(stateTime)) {
//
//            if(EventQueue.size() > 0 ) {
//                Move_TO(EventQueue.removeFirst());
//                return;
//            }
//            running = false;
//            velocity.set(0,0,0);
//        }
    }

    public void Move_TO(Vector3 pos) {

        Move_TO(pos.x,pos.y);
    }

    public void Move_TO(float x,float y) {

        //if the animation is already running, the event should be processed later
        if( EventQueue.size() > 0 ) {
            EventQueue.addLast(new Vector3(x, y, 0));
            return;
        }

        EventQueue.addLast(new Vector3(x, y, 0));

        Vector2 delta = new Vector2(x- position.x,y - position.y);
        delta.scl(0.2f);
        velocity.set(delta.x, delta.y, 0);
        running = true;
    }

    public void update() {

        if( EventQueue.size() == 0 ) return;

        if( EventQueue.getFirst().dst(position) < 10 ) {
            velocity.set(0, 0, 0);
            EventQueue.removeFirst();
            if(EventQueue.size() > 0) {
                Vector3 v = EventQueue.getFirst();
                Vector2 delta = new Vector2(v.x- position.x,v.y - position.y);
                delta.scl(0.2f);
                velocity.set(delta.x, delta.y, 0);
                running = true;
            }
        }

        position.add(velocity);
    }

    public void ChangeMode(GameSuperviser.GameState state) {

        CurrentGameState = state;
        //TODO: State transitions
    }

}