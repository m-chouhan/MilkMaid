package com.milkmaid.game;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by mahendras on 13/11/17.
 */

public class Model {

    static final int WorldHeight= 480;
    static VertexQueue VQueue = new VertexQueue(25);
    public enum GameState {NORMAL,STRONGER,SHARPER}
    public interface MyComparator {
        public boolean compare(int a1, int a2);
    }

//    MyComparator myComparator = (a1, a2) -> {return a1 > a2;};
//    boolean result = myComparator.compare(2, 5);
}
