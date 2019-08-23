package com.packtpub.canyonbunny.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.packtpub.canyonbunny.game.WorldController;
import com.packtpub.canyonbunny.game.WorldRenderer;
import com.packtpub.canyonbunny.util.GamePreferences;

public class GameScreen extends AbstractGameScreen {
	private WorldController worldController;
	private WorldRenderer worldRenderer;
	private boolean paused;

	public GameScreen (DirectedGame game) {
		super(game);
	}

	@Override
	public void render (float deltaTime) {
		if (!paused) { // Do not update game world when paused.
			// Update game world by the time that has passed
			// since last rendered frame.
			worldController.update(deltaTime);
		}
		// Sets the clear screen color to: Cornflower Blue
		Gdx.gl.glClearColor(0x64 / 255.0f, 0x95 / 255.0f,0xed /
				255.0f, 0xff / 255.0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		worldRenderer.render();
	}

	@Override
	public void resize (int width, int height) {
		worldRenderer.resize(width, height);
	}

	@Override
	public void show () {
		GamePreferences.instance.load();
		worldController = new WorldController(game);
		worldRenderer = new WorldRenderer(worldController);
		Gdx.input.setCatchBackKey(true);
	}

	@Override
	public void hide () {
		worldRenderer.dispose();
		Gdx.input.setCatchBackKey(false);
	}

	@Override
	public void pause () {
		paused = true;
	}

	@Override
	public void resume () {
		super.resume();
		paused = false; // Only called on Android!
	}

	@Override
	public InputProcessor getInputProcessor () {
		return worldController;
	}
}