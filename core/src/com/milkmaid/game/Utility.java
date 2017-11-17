package com.milkmaid.game;

import com.badlogic.gdx.graphics.OrthographicCamera;

/**
 * Created by mahendras on 14/11/17.
 */

public class Utility {

    /**
     *
     * @param probability
     * @return true with given probability
     */
    public static boolean generateProbability(double probability) {
        return (Math.random() <= probability);
    }

    public static OrthographicCamera setupCamera(int Width,int Height) {
        OrthographicCamera DisplayCamera = new OrthographicCamera(Width,Height); //viewport dimensions
        DisplayCamera.position.set(DisplayCamera.viewportWidth / 2f,
                Model.WorldHeight / 2, 0);
        DisplayCamera.rotate(180);
        DisplayCamera.update();
        return DisplayCamera;
    }
}
