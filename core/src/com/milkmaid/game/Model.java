package com.milkmaid.game;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by mahendras on 13/11/17.
 */

public class Model {

    static final int WorldHeight= 480;
    static VertexQueue VQueue = new VertexQueue(25,WorldHeight);
    public enum GameState {NORMAL,STRONGER,SHARPER}

}
