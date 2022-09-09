package com.mygdx.bisimulationgame.desktop;


import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.mygdx.bisimulationgame.BisimulationGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setTitle("Bisimulation Game");
		config.setWindowedMode(1600, 900);
		new Lwjgl3Application(new BisimulationGame(), config);
	}
}
