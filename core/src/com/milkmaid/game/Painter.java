package com.milkmaid.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/**
 * Created by maximus_prime on 27/9/15.
 */

public class Painter implements Screen {

    private final String TAG = "PAINTER";
    private final VertexQueue VQueue;

    private World myWorld;
    private OrthographicCamera camera;
    private ShapeRenderer debugRenderer = new ShapeRenderer();
    private int Width,Height,node_size;
    private Texture MySprites,background ;
    private TextureRegion Regions[],BackgroundRegion;

    private SpriteBatch batch;

    Painter(World w) {

        myWorld = w;
        VQueue = w.getVQueue();
        Width = myWorld.ScreenWidth;
        Height = myWorld.ScreenHeight;
        camera = myWorld.getCamera();
        node_size = myWorld.getNodeSize();

        MySprites = new Texture(Gdx.files.internal("short_sprites.png"));
        background = new Texture(Gdx.files.internal("backgroundtexture.png"));
        BackgroundRegion = new TextureRegion(background,0,0,background.getWidth(),background.getHeight());
        Regions = new TextureRegion[4];

        int width = MySprites.getWidth()/2,height = MySprites.getHeight()/2;


        Regions[0] = new TextureRegion(MySprites,0,0,width,height);
        Regions[1] = new TextureRegion(MySprites,width,0,width,height);
        Regions[2] = new TextureRegion(MySprites,0,height,width,height);
        Regions[3] = new TextureRegion(MySprites,width,height,width,height);

        batch = new SpriteBatch();

    }
    @Override
    public void show() {

    }

    @Override
    public void render(float v) {

        myWorld.update();
        Vertex.Reset();

        int camera_top = (int) (camera.position.x + camera.viewportWidth/2);

        debugRenderer.setProjectionMatrix(camera.combined);//for working in camera coordinates
        batch.setProjectionMatrix(camera.combined);

        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glLineWidth(7);

        batch.begin();

        batch.draw(BackgroundRegion, 600, 0, BackgroundRegion.getRegionWidth()/2,
                BackgroundRegion.getRegionHeight()/2,BackgroundRegion.getRegionWidth(),
                BackgroundRegion.getRegionHeight(),2,5,90);

        for(int i = 0;i<VQueue.getSize();++i) {

            Vertex vertex = VQueue.getVertex(i);
            if(vertex.x > camera_top ) break;

            if(vertex.getCurrentState() == Vertex.Status.Invisible) vertex.changeState(Vertex.Status.Visible);
            switch (vertex.getCurrentState()) {

                case UnReachable:
                case Visible:
                        batch.draw(Regions[2], vertex.x-Regions[2].getRegionWidth()/2,
                                vertex.y-Regions[2].getRegionHeight()/2);
                        break;
                case Touched:
                        batch.draw(Regions[0], vertex.x - Regions[0].getRegionWidth()/2,
                            vertex.y-Regions[0].getRegionHeight()/2);
                        break;
                case Reachable:
                        batch.draw(Regions[1], vertex.x - Regions[1].getRegionWidth()/2,
                            vertex.y-Regions[1].getRegionHeight()/2);
                    break;
                case Dead:
                        break;

            }
        }

        batch.end();


        debugRenderer.begin(ShapeRenderer.ShapeType.Line);
        debugRenderer.setColor(new Color(1, 1, 1, 1));
        for(int i = 0;i<VQueue.getSize();++i) {
            Vertex vertex = VQueue.getVertex(i);

            if(vertex.x > camera_top) break;//since vertex is not visible

            for(HalfEdge he:vertex.getEdgeList()) {

                if(he.getDst().IsExplored() == false || he.getDst().getCurrentState() == Vertex.Status.Invisible)
                    debugRenderer.line(vertex,he.getDst());
            }
            vertex.MarkExplored();
        }

        debugRenderer.end();
        /**/
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
