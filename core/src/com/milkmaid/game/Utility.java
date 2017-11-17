package com.milkmaid.game;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by mahendras on 14/11/17.
 */

public class Utility {

    /**
     *
     * @param probability
     * @return true with given probability
     */
    public static boolean probabilityOf(double probability) {
        return (Math.random() <= probability);
    }

    public static float slope(Vector2 a,Vector2 b) { return (a.y - b.y)/(a.x - b.x);}
    public static OrthographicCamera setupCamera(int Width,int Height) {
        OrthographicCamera DisplayCamera = new OrthographicCamera(Width,Height); //viewport dimensions
        DisplayCamera.position.set(DisplayCamera.viewportWidth / 2f,
                Model.WorldHeight / 2, 0);
        DisplayCamera.rotate(180);
        DisplayCamera.update();
        return DisplayCamera;
    }
}
