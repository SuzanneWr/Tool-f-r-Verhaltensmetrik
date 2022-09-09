package com.mygdx.bisimulationgame;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import com.mygdx.bisimulationgame.AI.SpoilerAI;
import com.mygdx.bisimulationgame.Util.FormatUtil;
import com.mygdx.bisimulationgame.Util.SaveUtil;
import com.mygdx.bisimulationgame.datatypes.*;

public class BisimulationGame extends Game{

	private LabelledMarkovChain markovChainA;
	private LabelledMarkovChain markovChainB;
	private Configuration configuration;
	private DuplicatorTable duplicatorTable;
	private boolean unBounded;
	private int nBounded;
	private boolean duplicatorWin;
	private boolean spoilerWin;
	private SpoilerAI spoilerAI;
	private boolean vsSpoilerAI;

	private String infoMessage;
	private LabelledMarkovChain toEdit;
	private boolean editMode;
	private boolean tutorialMode;
	private String editFileName;

	public SpriteBatch batch;
	public ShapeRenderer shapeRenderer;
	public Skin skin;
	public BitmapFont font;

	public Controller controller;
	public FormatUtil formatUtil;

	private Screen screen;
	private SaveUtil saveUtil;

	public float bgColor = 1f;
	public int worldHeight;
	public int worldWidth;



	public void create () {
		batch = new SpriteBatch();
		shapeRenderer = new ShapeRenderer();
		skin = new Skin(Gdx.files.internal("orange/skin/uiskin.json"));
		font = new BitmapFont(Gdx.files.internal("orange/raw/font-export.fnt"));

		saveUtil = new SaveUtil();

		duplicatorWin = false;
		spoilerWin = false;
		vsSpoilerAI = false;
		tutorialMode = false;

		controller = new Controller(this);
		formatUtil = new FormatUtil(this);
		infoMessage = "test";
		worldHeight = Gdx.graphics.getHeight();
		worldWidth = Gdx.graphics.getWidth();

		this.setScreen(new MainMenuScreen(this));
	}


	public void render(){
		super.render();
	}

	public void dispose () {
		if(screen != null){
			screen.dispose();
		}
		batch.dispose();
		shapeRenderer.dispose();
		skin.dispose();
	}

	public void setScreenToDispose(Screen screen){
		this.screen = screen;
	}

	public Array<String> getLoadFiles(){
		return saveUtil.loadFileNames();
	}

	public LabelledMarkovChain getMarkovChainA(){
		return markovChainA;
	}

	public LabelledMarkovChain getMarkovChainB(){
		return markovChainB;
	}

	public Configuration getConfiguration() {
		return configuration;
	}

	public void setConfiguration(Configuration configuration) {
		this.configuration = configuration;
	}

	public boolean isUnBounded() {
		return unBounded;
	}

	public void setUnBounded(boolean unBounded) {
		this.unBounded = unBounded;
	}

	public int getnBounded() {
		return nBounded;
	}

	public void setnBounded(int nBounded) {
		this.nBounded = nBounded;
	}

	public boolean isDuplicatorWin() {
		return duplicatorWin;
	}

	public void setDuplicatorWin(boolean duplicatorWin) {
		this.duplicatorWin = duplicatorWin;
	}

	public boolean isSpoilerWin() {
		return spoilerWin;
	}

	public void setSpoilerWin(boolean spoilerWin) {
		this.spoilerWin = spoilerWin;
	}

	public LabelledMarkovChain getToEdit() {
		return toEdit;
	}

	public void setToEdit(LabelledMarkovChain toEdit) {
		this.toEdit = toEdit;
	}

	public SpoilerAI getSpoilerAI() {
		return spoilerAI;
	}

	public void setSpoilerAI(SpoilerAI spoilerAI) {
		this.spoilerAI = spoilerAI;
	}

	public boolean isVsSpoilerAI() {
		return vsSpoilerAI;
	}

	public void setVsSpoilerAI(boolean vsSpoilerAI) {
		this.vsSpoilerAI = vsSpoilerAI;
	}

	public void setMarkovChainA(LabelledMarkovChain markovChainA) {
		this.markovChainA = markovChainA;
	}

	public void setMarkovChainB(LabelledMarkovChain markovChainB) {
		this.markovChainB = markovChainB;
	}

	public DuplicatorTable getDuplicatorTable() {
		return duplicatorTable;
	}

	public void setDuplicatorTable(DuplicatorTable duplicatorTable) {
		this.duplicatorTable = duplicatorTable;
	}

	public String getInfoMessage() {
		return infoMessage;
	}

	public void setInfoMessage(String infoMessage) {
		this.infoMessage = infoMessage;
	}

	public boolean isEditMode() {
		return editMode;
	}

	public void setEditMode(boolean editMode) {
		this.editMode = editMode;
	}

	public boolean isTutorialMode() {
		return tutorialMode;
	}

	public void setTutorialMode(boolean tutorialMode) {
		this.tutorialMode = tutorialMode;
	}

	public String getEditFileName() {
		return editFileName;
	}

	public void setEditFileName(String editFileName) {
		this.editFileName = editFileName;
	}

	public SaveUtil getSaveUtil() {
		return saveUtil;
	}
}
