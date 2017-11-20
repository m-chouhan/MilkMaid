package com.milkmaid.game;

import org.reactivestreams.Subscriber;

import io.reactivex.Flowable;

import static com.milkmaid.game.Model.VQueue;

/**
 * Created by mahendras on 20/11/17.
 */

public class GameEvent {

    /**
     * Transforms an Input event to a vertex touched game event
     * @param event Input Event
     * @return a vertex where touch event happened
     */
    public static Flowable<Vertex> createVertexEvent(InputObservable.InputEvent event) {

        for(int index = 0;index < VQueue.getSize();++index) {
            Vertex vertex = VQueue.getVertex(index);
            if( vertex.dst(event.x,event.y) < vertex.getSize() + 20) {
                return Flowable.just(vertex);
            }
            else if(vertex.x > (event.x+20)) break;
        }
        return Flowable.empty();
    }
}
