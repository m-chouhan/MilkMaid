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

    Animation walkAnimation;
    Texture walkSheet;
    TextureRegion[] walkFrames;
    TextureRegion currentFrame;
    LinkedList<Vector3> TargetQueue;

    float stateTime;//maintains game time

    Vector3 position = new Vector3(),velocity = new Vector3();
    GameSuperviser.GameState CurrentGameState = GameSuperviser.GameState.NORMAL;
    boolean running = false;

    /* You should provide starting position to the player*/

    public Player(Vector3 pos) {

        TargetQueue = new LinkedList<Vector3>();
        walkSheet = new Texture(Gdx.files.internal("frog.png"));
        TextureRegion[][] tmp = TextureRegion.split(walkSheet, walkSheet.getWidth()/FRAME_COLS,
                                walkSheet.getHeight()/FRAME_ROWS);
        walkFrames = new TextureRegion[FRAME_COLS * FRAME_ROWS];
        int index = 0;
        for (int i = 0; i < FRAME_ROWS; i++) {
            for (int j = 0; j < FRAME_COLS; j++) {
                walkFrames[index++] = tmp[i][j];
            }
        }
        walkAnimation = new Animation(0.125f, walkFrames);
        stateTime = 0f;
    }

    public void render( SpriteBatch spriteBatch) {

        stateTime += Gdx.graphics.getDeltaTime();
        currentFrame = walkAnimation.getKeyFrame(stateTime, running );
        spriteBatch.begin();
        spriteBatch.draw(currentFrame, position.x, position.y);
        spriteBatch.end();

        if( walkAnimation.isAnimationFinished(stateTime)) {

            if(TargetQueue.size() > 0 ) {
                Move_TO(TargetQueue.removeFirst());
                return;
            }
            running = false;
            velocity.set(0,0,0);
        }
    }

    public void Move_TO(Vector3 pos) {

        Move_TO(pos.x,pos.y);
    }

    public void Move_TO(float x,float y) {

        //if the animation is already running, the event should be processed later
        if( running ) {
            TargetQueue.addLast(new Vector3(x, y, 0));
            return;
        }

        Vector2 delta = new Vector2(x- position.x,y - position.y);
        delta.scl(0.1f);
        velocity.set(delta.x, delta.y, 0);
        running = true;
    }

    public void update() {
        position.add(velocity);
    }

    public void ChangeMode(GameSuperviser.GameState state) {

        CurrentGameState = state;
        //TODO: State transitions
    }

}