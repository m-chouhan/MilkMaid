package com.milkmaid.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;

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

    private Painter Renderer;
    private World NormalWorld ,  StrongerWorld , CurrentWorld;
    private SuperSharperWorld SharperWorld;

    private InputHandler InputProcessor;

    public GameSuperviser(int width,int height) {

        Width = width;
        Height = height;
        VertexQueue VQ = new VertexQueue(25);
        DisplayCamera = new OrthographicCamera(Width,Height); //viewport dimensions
        DisplayCamera.position.set(DisplayCamera.viewportWidth / 2f,
                WorldHeight/2, 0);
        DisplayCamera.rotate(180);
        DisplayCamera.update();

        NormalWorld = new World(VQ,this);
        SharperWorld = new SuperSharperWorld(VQ,this);
        CurrentWorld = NormalWorld;

        Renderer = new Painter(VQ,this);
        InputProcessor = new InputHandler(NormalWorld);
        Gdx.input.setInputProcessor(InputProcessor);
    }

    public int getWidth() { return Width; }
    public int getHeight() { return Height; }
    public int getWorldHeight() { return WorldHeight; }

    public OrthographicCamera getDisplayCamera() { return DisplayCamera; }

    public void updateScore(int weight) {
        Score += weight;
    }

    public int getScore() { return Score; }

    public void SwitchState(GameState g) {
        CurrentGameState = g;
        Renderer.setPaintingMode(g);

        switch (CurrentGameState) {
            case NORMAL:
                NormalWorld.setLastTouched(CurrentWorld.getLastTouched());
                CurrentWorld = NormalWorld;
                InputProcessor.setMyWorld(NormalWorld);
                break;
            case SHARPER:
                InputProcessor.setMyWorld(SharperWorld);
                SharperWorld.searchPath(NormalWorld.getLastTouched());
                CurrentWorld = SharperWorld;
                break;
            case STRONGER:
                CurrentWorld = StrongerWorld;
                InputProcessor.setMyWorld(StrongerWorld);
                break;

        }
    }
    @Override
    public void show() {

    }

    /*This is my Game loop
    * Magic Happnes Here :P
    * */
    @Override
    public void render(float v) {

        CurrentWorld.update();
        Renderer.updateBackground(CurrentWorld.getSpeed() / 2);
        Renderer.render(v);
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
