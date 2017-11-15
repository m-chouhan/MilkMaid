package com.milkmaid.game;

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
}
