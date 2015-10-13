package com.milkmaid.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;

/**
 * Created by maximus_prime on 13/10/15.
 */
public class GameSuperviser implements Screen {

    //only three for now
    public enum GameState {NORMAL,STRONGER,SHARPER}
    private GameState CurrentGameState = GameState.NORMAL;

    private final int Width,Height;
    private Painter Renderer;
    private World NormalWorld , SharperWorld , StrongerWorld , CurrentWorld;

    public GameSuperviser(int width,int height) {

        Width = width;
        Height = height;
        VertexQueue VQ = new VertexQueue(25);
        NormalWorld = new World(VQ,this);
        CurrentWorld = NormalWorld;

        Painter P = new Painter(width,height,NormalWorld);
        Gdx.input.setInputProcessor(new InputHandler(NormalWorld));
    }

    public int getWidth() { return Width; }
    public int getHeight() { return Height; }

    public void SwitchState(GameState g) {
        CurrentGameState = g;
        Renderer.setPaintingMode(g);

        switch (g) {
            case NORMAL:
                CurrentWorld = NormalWorld;
                break;
            case SHARPER:
                CurrentWorld = SharperWorld;
                break;
            case STRONGER:
                CurrentWorld = StrongerWorld;
                break;

        }
    }
    @Override
    public void show() {

    }

    @Override
    public void render(float v) {

        CurrentWorld.update();
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
