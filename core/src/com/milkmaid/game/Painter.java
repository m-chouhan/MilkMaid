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
    private Texture MySprites ;
    private TextureRegion Regions[];

    private SpriteBatch batch;

    Painter(World w) {

        myWorld = w;
        VQueue = w.getVQueue();
        Width = myWorld.ScreenWidth;
        Height = myWorld.ScreenHeight;
        camera = myWorld.getCamera();
        node_size = myWorld.getNodeSize();

        MySprites = new Texture(Gdx.files.internal("sprite_compressed.png"));
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

        batch.begin();
        batch.draw(Regions[0],0, 0);
        batch.draw(Regions[1],100,100);
        batch.draw(Regions[2],200,200);
        batch.draw(Regions[3], 300, 300);

        batch.end();

        debugRenderer.begin(ShapeRenderer.ShapeType.Filled);
        debugRenderer.setColor(new Color(1, 0, 0, 1));

        for(int i = 0;i<VQueue.getSize();++i) {

            Vertex vertex = VQueue.getVertex(i);
            if(vertex.x > camera_top ) break;

            if(vertex.getCurrentState() == Vertex.Status.Invisible) vertex.changeState(Vertex.Status.Visible);
            switch (vertex.getCurrentState()) {
                case Visible:
                        debugRenderer.setColor(new Color(1, 0, 0, 1));
                        debugRenderer.circle(vertex.x, vertex.y, node_size);
                        break;
                case Touched:
                        debugRenderer.setColor(new Color(1f, 0.5f, 0.5f, 0.5f));
                        debugRenderer.circle(vertex.x, vertex.y, node_size);
                        break;
                case Dead:
                        break;
            }
        }
        debugRenderer.end();

        debugRenderer.begin(ShapeRenderer.ShapeType.Line);
        debugRenderer.setColor(new Color(1, 0, 0, 1));
        for(int i = 0;i<VQueue.getSize();++i) {
            Vertex vertex = VQueue.getVertex(i);

            if(vertex.x > camera_top) break;//since vertex is not visible

            for(HalfEdge he:vertex.getEdgeList()) {

                if(he.getDst().IsExplored() == false) debugRenderer.line(vertex,he.getDst());
            }
            vertex.MarkExplored();
        }

        debugRenderer.end();
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
