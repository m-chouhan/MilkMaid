package com.milkmaid.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;


/**
 * Created by maximus_prime on 27/9/15.
 */
public class Painter implements Screen {

    private final String TAG = "PAINTER";
    private final VertexQueue VQueue;

    private int Graph[][];
    private World myWorld;
    private OrthographicCamera camera;
    private ShapeRenderer debugRenderer = new ShapeRenderer();
    private int Width,Height,node_size;

    Painter(World w) {

        myWorld = w;
        VQueue = w.getVQueue();
        Graph = w.getGraph();
        Width = myWorld.ScreenWidth;
        Height = myWorld.ScreenHeight;
        camera = myWorld.getCamera();
        node_size = myWorld.NODE_SIZE/5;

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

        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        debugRenderer.begin(ShapeRenderer.ShapeType.Filled);
        debugRenderer.setColor(new Color(1, 0, 0, 1));

        for(int i = 0;i<VQueue.getSize();++i) {

            debugRenderer.circle(VQueue.getVertex(i).x,VQueue.getVertex(i).y,node_size);
        }
        debugRenderer.end();

        debugRenderer.begin(ShapeRenderer.ShapeType.Line);

        for(int i = 0;i<VQueue.getSize();++i) {
            Vertex vertex = VQueue.getVertex(i);

            if(vertex.x > camera_top) break;

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
