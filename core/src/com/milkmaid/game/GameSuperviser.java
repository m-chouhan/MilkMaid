package com.milkmaid.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by maximus_prime on 13/10/15.
 * Maintains and supervises game states transition
 * Also dispatches all the events based on GameState
 */
public class GameSuperviser implements Screen {

    private Model.GameState CurrentGameState = Model.GameState.NORMAL;

    private OrthographicCamera DisplayCamera;
    private final int Width,Height;
    private int Score = 0;

    private Painter NormalRenderer,CurrentRenderer,StrongerRenderer;
    private World NormalWorld ;
    private MotherController CurrentWorld;
    private SuperStrongerWorld StrongerWorld;
    private SuperSharperWorld SharperWorld;
    private InputHandler InputProcessor;
    private Player crazyFrog;

    public GameSuperviser(int width,int height) {

        Width = width;
        Height = height;
        Vector2 v = Model.VQueue.getVertex(0);

        crazyFrog = new Player(new Vector3(v.x,v.y,0));
        DisplayCamera = Utility.setupCamera(width, height);
        NormalWorld = new World(this,crazyFrog);
        SharperWorld = new SuperSharperWorld(this,crazyFrog);
        StrongerWorld = new SuperStrongerWorld(this,crazyFrog);

        NormalRenderer = new Painter(this,crazyFrog);
        StrongerRenderer = new StrongerPainter(crazyFrog.position,
                    StrongerWorld.getInitialPos(),this,crazyFrog);

        CurrentWorld = NormalWorld;
        CurrentRenderer = NormalRenderer;
    }

    public void SwitchState(Model.GameState g) {

        CurrentGameState = g;
        Vertex last_touched = CurrentWorld.getLastTouched();

        switch (CurrentGameState) {
            case NORMAL:
                CurrentWorld = NormalWorld;
                //Start game from the last touched vertex
//                CurrentWorld.startGame(CurrentWorld.getLastTouched());
                CurrentRenderer = NormalRenderer;
                InputProcessor.Enable();
                InputProcessor.setMyWorld(NormalWorld);
                break;
            case SHARPER:
//                SharperWorld.startGame(CurrentWorld.getLastTouched());
                CurrentWorld = SharperWorld;
                CurrentRenderer = NormalRenderer;
                InputProcessor.Enable();
                InputProcessor.setMyWorld(SharperWorld);
                break;
            case STRONGER:
//                StrongerWorld.startGame(CurrentWorld.getLastTouched());
                CurrentWorld = StrongerWorld;
                CurrentRenderer = StrongerRenderer;
                InputProcessor.Disable();
                break;

        }

        CurrentWorld.startGame(last_touched);
        CurrentRenderer.setPaintingMode(g);
    }
    @Override
    public void show() {

        InputProcessor = new InputHandler(NormalWorld);
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(InputProcessor);
        multiplexer.addProcessor(StrongerWorld); // called when previous processor returns true
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

    public int getWidth() { return Width; }
    public int getHeight() { return Height; }

    public OrthographicCamera getDisplayCamera() { return DisplayCamera; }
    public int getScore() { return Score; }

    public void updateScore(int weight) {
        Score += weight;
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
