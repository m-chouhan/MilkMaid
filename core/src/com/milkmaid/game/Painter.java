package com.milkmaid.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

/**
 * Created by maximus_prime on 27/9/15.
 * Painter class handles all the rendering
 */

public class Painter implements Screen {

    enum PainterMode{NORMAL,TALLER,STRONGER,SHARPER};

    private PainterMode PaintingMode = PainterMode.NORMAL;

    private final String TAG = "PAINTER";
    private final VertexQueue VQueue;

    private World myWorld;
    private OrthographicCamera camera;
    private ShapeRenderer debugRenderer = new ShapeRenderer();
    private final int Width,Height;
    private TextureRegion Regions[][];
    private Sprite BackgroundSprites[] = new Sprite[4];

    private SpriteBatch batch;

    Painter(int ScreenWidth,int ScreenHeight,World w) {

        myWorld = w;
        VQueue = w.getVQueue();
        Width = myWorld.ScreenWidth;
        Height = myWorld.ScreenHeight;
        camera = myWorld.getCamera();

        Texture SpriteSheet = new Texture(Gdx.files.internal("short_sprites.png"));
        Texture background = new Texture(Gdx.files.internal("backgroundtexture2.png"));
        TextureRegion Backgrounds[][] = TextureRegion.split(background,301,200);

        int width = SpriteSheet.getWidth()/2,height = SpriteSheet.getHeight()/2;
        Regions = TextureRegion.split(SpriteSheet,width,height);

        BackgroundSprites[0] = new Sprite(Backgrounds[0][0]);
        BackgroundSprites[1] = new Sprite(Backgrounds[0][1]);
        BackgroundSprites[2] = new Sprite(Backgrounds[1][0]);
        BackgroundSprites[3] = new Sprite(Backgrounds[1][1]);

        int x = 0;
        for(Sprite s:BackgroundSprites) {
            s.setScale(2);
            s.setPosition(x, 250);
            Rectangle r = s.getBoundingRectangle();
            x += r.getWidth();
//            Gdx.app.log(TAG, "RegionInfo"+ r.getX() +"|" + r.getY() +"|" +
//                        r.getWidth() +"|" + r.getHeight());
//            Gdx.app.log(TAG, "SpriteInfo"+ s.getX() +"|" + s.getY() +"|" +
//                    s.getWidth() +"|" + s.getHeight());
        }

        batch = new SpriteBatch();

    }
    @Override
    public void show() {

    }

    public void updateBackground(float Velocity) {

        int camera_bottom = (int) (camera.position.x - camera.viewportWidth/2);

        for(Sprite s : BackgroundSprites) {

            s.setPosition(s.getX()+Velocity,s.getY());
            Rectangle r = s.getBoundingRectangle();
            int rtop = (int) (r.getX()+r.getWidth());
            if(rtop < camera_bottom) {
                s.setPosition(s.getX()+r.getWidth()*4,250);
                //break;
            }
        }
    }

    public void setPaintingMode(PainterMode P,World w) {
        PaintingMode = P;
        myWorld = w;
    }

    /*This is my Game loop
    * Magic Happnes Here :P
    * */
    @Override
    public void render(float v) {

        myWorld.update();
        switch(PaintingMode){

            case NORMAL:
            case SHARPER:
                NormalPaint();
                break;
            case STRONGER:
                StrongerPaint();
                break;
            case TALLER:
                TallerPaint();
                break;
        }
    }

    private void NormalPaint() {

        Vertex.Reset();//reset the Isexplored boolean value

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
            if(vertex.x > camera_top ) break;

            if(vertex.getCurrentState() == Vertex.Status.Invisible) vertex.changeState(Vertex.Status.Visible);
            switch (vertex.getCurrentState()) {

                case UnReachable:
                case Visible:
                    batch.draw(Regions[1][0], vertex.x-Regions[1][0].getRegionWidth()/2,
                            vertex.y-Regions[1][0].getRegionHeight()/2);
                    break;
                case Touched:
                    batch.draw(Regions[0][0], vertex.x - Regions[0][0].getRegionWidth()/2,
                            vertex.y-Regions[0][0].getRegionHeight()/2);
                    break;
                case Reachable:
                    batch.draw(Regions[0][1], vertex.x - Regions[0][1].getRegionWidth()/2,
                            vertex.y-Regions[0][1].getRegionHeight()/2);
                    break;
                case Dead:
                    break;

            }
            switch (vertex.getVertexType()) {

                case Normal: break;
                case Sharper:
                case Stronger:
                    batch.draw(Regions[1][1], vertex.x - Regions[1][1].getRegionWidth()/2,
                            vertex.y-Regions[1][1].getRegionHeight()/2);
                    break;
                case Taller:break;

            }
        }

        batch.end();


        debugRenderer.begin(ShapeRenderer.ShapeType.Line);
        debugRenderer.setColor(new Color(1, 1, 1, 1));
        Rectangle r = BackgroundSprites[0].getBoundingRectangle();
        for(int i = 0;i<VQueue.getSize();++i) {
            Vertex vertex = VQueue.getVertex(i);

            if(vertex.x > camera_top) break;//since vertex is not visible

            for(HalfEdge he:vertex.getEdgeList()) {

                if( !he.getDst().IsExplored() || he.getDst().getCurrentState() == Vertex.Status.Invisible)
                    debugRenderer.line(vertex,he.getDst());
            }
            vertex.MarkExplored();
        }

        debugRenderer.end();
        /**/
    }

    private void StrongerPaint() {

    }

    private void TallerPaint() {

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
