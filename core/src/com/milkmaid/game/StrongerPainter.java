package com.milkmaid.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by maximus_prime on 1/11/15.
 */

 public class StrongerPainter extends Painter {

    private final Vector3 PlayerNode;
    private final Vector2 InitialPos;
    private Sprite PlayerSprite;

    public StrongerPainter(VertexQueue VQ,Vector3 node,Vector2 initialpos,GameSuperviser superviser) {

        super(VQ,superviser);
        PlayerNode = node;
        InitialPos = initialpos;
        PlayerSprite = new Sprite(Regions[0][0]);
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

        PlayerSprite.setScale(PlayerNode.z);
        PlayerSprite.setPosition(PlayerNode.x - Regions[0][0].getRegionWidth() / 2,
                PlayerNode.y - Regions[0][0].getRegionHeight() / 2);
        PlayerSprite.draw(batch);
        batch.end();

        debugRenderer.begin(ShapeRenderer.ShapeType.Line);
        debugRenderer.circle(InitialPos.x,InitialPos.y,50);
        debugRenderer.circle(InitialPos.x,InitialPos.y,200);
        debugRenderer.end();

        camera.rotate(-90);
        camera.update();
        camera_top = (int) (camera.position.y + camera.viewportWidth/2);

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        Score.draw(batch, "" + Superviser.getScore(), camera.position.x - 300, camera_top - 10);
        batch.end();

        camera.rotate(90);
        camera.update();


    }

}
