package com.milkmaid.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by maximus_prime on 13/10/15.
 */
public class GameSuperviser implements Screen {

    //only three for now
    public enum GameState {NORMAL,STRONGER,SHARPER}
    private GameState CurrentGameState = GameState.NORMAL;

    private OrthographicCamera DisplayCamera;
    private final int Width,Height,WorldHeight= 480;
    private int Score = 0;

    private Painter NormalRenderer,CurrentRenderer,StrongerRenderer;
    private World NormalWorld , CurrentWorld;
    private SuperStrongerWorld StrongerWorld;
    private SuperSharperWorld SharperWorld;
    private InputHandler InputProcessor;
    private Player crazyFrog;

    public GameSuperviser(int width,int height) {

        Width = width;
        Height = height;
        VertexQueue VQ = new VertexQueue(25);
        InflateVertices(VQ);
        Vector2 v = VQ.getVertex(0);

        crazyFrog = new Player(new Vector3(v.x,v.y,0));
        DisplayCamera = new OrthographicCamera(Width,Height); //viewport dimensions
        DisplayCamera.position.set(DisplayCamera.viewportWidth / 2f,
                WorldHeight / 2, 0);
        DisplayCamera.rotate(180);
        DisplayCamera.update();

        NormalWorld = new World(VQ,this);
        SharperWorld = new SuperSharperWorld(VQ,this);
        SuperStrongerWorld s = new SuperStrongerWorld(VQ,this);
        StrongerWorld = s;
        CurrentWorld = NormalWorld;

        NormalRenderer = new Painter(VQ,this);
        StrongerRenderer = new StrongerPainter(VQ,s.getPlayerPosition(),s.getInitialPos(),this);
        CurrentRenderer = NormalRenderer;
    }

    public int getWidth() { return Width; }
    public int getHeight() { return Height; }
    public int getWorldHeight() { return WorldHeight; }

    public OrthographicCamera getDisplayCamera() { return DisplayCamera; }

    private void InflateVertices(VertexQueue VQueue) {

        int x = 100,y = WorldHeight/2;
        VQueue.Push(new Vertex(x, y));

        for(int i = 1 ;i<VQueue.getMax_size();++i) {
            VQueue.Push(new Vertex(0,0));
        }
    }

    public void updateScore(int weight) {
        Score += weight;
    }

    public int getScore() { return Score; }

    public void SwitchState(GameState g) {
        CurrentGameState = g;
        CurrentRenderer.setPaintingMode(g);

        switch (CurrentGameState) {
            case NORMAL:
                NormalWorld.setLastTouched(CurrentWorld.getLastTouched());
                CurrentWorld = NormalWorld;
                CurrentRenderer = NormalRenderer;
                InputProcessor.Enable();
                InputProcessor.setMyWorld(NormalWorld);
                break;
            case SHARPER:
                InputProcessor.Enable();
                InputProcessor.setMyWorld(SharperWorld);
                SharperWorld.searchPath(NormalWorld.getLastTouched());
                CurrentWorld = SharperWorld;
                CurrentRenderer = NormalRenderer;
                break;
            case STRONGER:

                StrongerWorld.startGame(CurrentWorld.getLastTouched());
                CurrentWorld = StrongerWorld;
                CurrentRenderer = StrongerRenderer;
                InputProcessor.Disable();
                //InputProcessor.setMyWorld(StrongerWorld);

                break;

        }
    }
    @Override
    public void show() {

        InputProcessor = new InputHandler(NormalWorld);
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(InputProcessor);
        multiplexer.addProcessor(StrongerWorld);
        Gdx.input.setInputProcessor(multiplexer);
    }

    /*This is my Game loop
    * Magic Happnes Here :P
    * */
    @Override
    public void render(float v) {

        CurrentWorld.update();
        CurrentRenderer.updateBackground(CurrentWorld.getSpeed() / 2);
        CurrentRenderer.render(v);
    }

    @Override
    public void resize(int i, int i1) {

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
