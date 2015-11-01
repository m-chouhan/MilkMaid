package com.milkmaid.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by maximus_prime on 1/11/15.
 */

 public class StrongerPainter extends Painter {

    private final Vector2 PlayerNode;
    private Sprite PlayerSprite;

    public StrongerPainter(VertexQueue VQ,Vector2 node,GameSuperviser superviser) {

        super(VQ,superviser);
        //VQueue = VQ;
        PlayerNode = node;
        PlayerSprite = new Sprite(Regions[0][0]);
        //Superviser = superviser;
    }

    @Override
    public void render(float v) {

        int camera_top = (int) (camera.position.x + camera.viewportWidth/2); //for optimization
        debugRenderer.setProjectionMatrix(camera.combined);//for working in camera coordinates
        batch.setProjectionMatrix(camera.combined);

        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glLineWidth(5);

        batch.begin();

        for(Sprite s: BackgroundSprites) s.draw(batch); //render background

        for(int i = 0;i<VQueue.getSize();++i) {

            Vertex vertex = VQueue.getVertex(i);
            if(vertex.x > camera_top ) break; //optimizaton
            if( vertex.getCurrentState() == Vertex.Status.Dead ) continue;

            batch.draw(Regions[1][2], vertex.x - Regions[1][2].getRegionWidth() / 2,
                    vertex.y-Regions[1][2].getRegionHeight()/2);

        }

        PlayerSprite.setPosition(PlayerNode.x - Regions[0][0].getRegionWidth()/2,
                PlayerNode.y-Regions[0][0].getRegionHeight()/2);
        PlayerSprite.draw(batch);

        Score.draw(batch,""+Superviser.getScore(),camera_top-100,520);
        batch.end();


    }

}
