package com.mygdx.bisimulationgame.misc;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.bisimulationgame.BisimulationGame;

public class ControlScreen implements Screen {

    final BisimulationGame game;

    private ControlScreenHUD hud;
    private OrthographicCamera camera;
    private FitViewport fitViewport;

    private Color c1 = new Color(1f, 0.627f, 0.082f, 1);
    private Color c2 = new Color(0.6f, 0.054f, 0.262f, 1);

    public ControlScreen(BisimulationGame game){
        this.game = game;
        game.setScreenToDispose(this);

        camera = new OrthographicCamera();
        fitViewport = new FitViewport(game.worldWidth, game.worldHeight, camera);

        hud = new ControlScreenHUD(game, this);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(game.bgColor, game.bgColor, game.bgColor, 1);

        camera.update();

        //Create Two State Squares
        game.shapeRenderer.setProjectionMatrix(camera.combined);
        game.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        game.shapeRenderer.setColor(c1);
        game.shapeRenderer.rect(-100, -200, 64, 64);
        game.shapeRenderer.setColor(c2);
        game.shapeRenderer.rect(50, -200, 64, 64);
        game.shapeRenderer.end();

        //Create Text for States
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        game.font.setColor(Color.BLACK);
        game.font.draw(game.batch, "State 1", -95, -158);
        game.font.draw(game.batch, "Label", -95, -176);
        game.font.draw(game.batch, "State 2", 55, -158);
        game.font.draw(game.batch, "Label", 55, -176);
        game.batch.end();

        //Controls
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            camera.position.y += 350 * delta;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            camera.position.y -= 350 * delta;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            camera.position.x -= 350 * delta;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            camera.position.x += 350 * delta;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.E)) {
            camera.zoom += 0.4 * delta;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.Q)) {
            camera.zoom -= 0.4 * delta;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.R)){
            camera.position.x = -100;
            camera.position.y = -100;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.T)){
            camera.position.x = 50;
            camera.position.y = -100;
        }

        //Render HUD
        game.batch.setProjectionMatrix(hud.getStage().getCamera().combined);
        hud.getStage().act(delta);
        hud.getStage().draw();
    }

    @Override
    public void resize(int width, int height) {
        fitViewport.update(width, height);
        hud.getStage().getViewport().update(width, height);
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
        hud.dispose();
    }
}
