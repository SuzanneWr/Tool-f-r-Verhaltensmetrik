package com.mygdx.bisimulationgame.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.bisimulationgame.BisimulationGame;
import com.mygdx.bisimulationgame.interfaces.HUD;
import com.mygdx.bisimulationgame.datatypes.LabelledMarkovChain;
import com.mygdx.bisimulationgame.datatypes.State;
import com.mygdx.bisimulationgame.game.setup.SetupHUD;

public class MarkovChainScreen implements Screen{

    final BisimulationGame game;

    private HUD hud;
    private OrthographicCamera camera;
    private FitViewport setupViewport;

    private LabelledMarkovChain markovChainA;
    private LabelledMarkovChain markovChainB;

    public MarkovChainScreen(BisimulationGame game){
        this.game = game;
        game.setScreenToDispose(this);

        if(game.isTutorialMode()){
            hud = new DupliProbabilityHUD(game, this);
        }else{
            hud = new SetupHUD(game, this);
        }

        camera = new OrthographicCamera();
        setupViewport = new FitViewport(game.worldWidth, game.worldHeight, camera);

        game.controller.setupMarkovChainForRender();
        markovChainA = game.getMarkovChainA();
        markovChainB = game.getMarkovChainB();

        camera.position.add(markovChainA.getStates().get(1).getX(), markovChainA.getStates().get(1).getY(), 0);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(game.bgColor, game.bgColor, game.bgColor, 1);

        camera.update();


        game.shapeRenderer.setProjectionMatrix(camera.combined);
        game.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        //Render States of Markov Chain A
        for(State state : markovChainA.getStates()){
            game.shapeRenderer.setColor(state.getLabel().getColor());
            game.shapeRenderer.rect(state.getX(), state.getY(), 64, 64);
            for(int j = 0; j < state.getTransition().getStateNames().size; j++){
                float xOrigin = state.getX();
                float yOrigin = state.getY();
                float xTarget = game.getMarkovChainA().getStateByName(state.getTransition().getStateNames().get(j)).getX();
                float yTarget = game.getMarkovChainA().getStateByName(state.getTransition().getStateNames().get(j)).getY();
                game.shapeRenderer.rectLine(xOrigin, yOrigin, xTarget, yTarget, 2f);
            }
        }
        //Render States of Markov Chain B
        for(State state : markovChainB.getStates()){
            game.shapeRenderer.setColor(state.getLabel().getColor());
            game.shapeRenderer.rect(state.getX(), state.getY(), 64, 64);
            for(int j = 0; j < state.getTransition().getStateNames().size; j++){
                float xOrigin = state.getX();
                float yOrigin = state.getY();
                float xTarget = game.getMarkovChainB().getStateByName(state.getTransition().getStateNames().get(j)).getX();
                float yTarget = game.getMarkovChainB().getStateByName(state.getTransition().getStateNames().get(j)).getY();
                game.shapeRenderer.rectLine(xOrigin, yOrigin, xTarget, yTarget, 2f);
            }
        }
        game.shapeRenderer.end();


        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        game.font.setColor(Color.BLACK);
        //Render Text of Markov Chain A
        for(State state : markovChainA.getStates()){
            game.font.draw(game.batch, state.getName(), state.getX() + 5, state.getY() + 42);
            game.font.draw(game.batch, "Label:" + state.getLabel().getName(), state.getX() + 5, state.getY() + 28);
            for(int j = 0; j < state.getTransition().getStateNames().size; j++){
                float xOrigin = state.getX();
                float yOrigin = state.getY();
                float xTarget = game.getMarkovChainA().getStateByName(state.getTransition().getStateNames().get(j)).getX();
                float yTarget = game.getMarkovChainA().getStateByName(state.getTransition().getStateNames().get(j)).getY();
                game.font.draw(game.batch, state.getTransition().getProbabilitys().get(j) + "", (xOrigin + xTarget) / 2, (yOrigin + yTarget) / 2);
            }
        }
        //Render Text of Markov Chain B
        for(State state : markovChainB.getStates()){
            game.font.draw(game.batch, state.getName(), state.getX() + 5, state.getY() + 42);
            game.font.draw(game.batch, "Label:" + state.getLabel().getName(), state.getX() + 5, state.getY() + 28);
            for(int j = 0; j < state.getTransition().getStateNames().size; j++){
                float xOrigin = state.getX();
                float yOrigin = state.getY();
                float xTarget = game.getMarkovChainB().getStateByName(state.getTransition().getStateNames().get(j)).getX();
                float yTarget = game.getMarkovChainB().getStateByName(state.getTransition().getStateNames().get(j)).getY();
                game.font.draw(game.batch, state.getTransition().getProbabilitys().get(j) + "", (xOrigin + xTarget) / 2, (yOrigin + yTarget) / 2);
            }
        }
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
            if(game.getConfiguration() != null && game.getConfiguration().getStateA() != null){
                camera.position.x = game.getConfiguration().getStateA().getX();
                camera.position.y = game.getConfiguration().getStateA().getY();
            }
        }
        if(Gdx.input.isKeyPressed(Input.Keys.T)){
            if(game.getConfiguration() != null && game.getConfiguration().getStateB() != null){
                camera.position.x = game.getConfiguration().getStateB().getX();
                camera.position.y = game.getConfiguration().getStateB().getY();
            }
        }
        if(Gdx.input.isKeyPressed(Input.Keys.I)){
            hud.getStage().getCamera().position.y += 350 * delta;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.K)){
            hud.getStage().getCamera().position.y -= 350 * delta;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.J)){
            hud.getStage().getCamera().position.x -= 350 * delta;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.L)){
            hud.getStage().getCamera().position.x += 350 * delta;
        }

        //Render HUD
        game.batch.setProjectionMatrix(hud.getStage().getCamera().combined);
        hud.getStage().act(delta);
        hud.getStage().draw();
    }

    @Override
    public void resize(int width, int height) {
        setupViewport.update(width, height);
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

    public void setHud(HUD hud) {
        this.hud = hud;
    }

}
