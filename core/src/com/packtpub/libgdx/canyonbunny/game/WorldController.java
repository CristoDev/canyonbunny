package com.packtpub.libgdx.canyonbunny.game;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.packtpub.libgdx.canyonbunny.CanyonBunnyMain;
import com.packtpub.libgdx.canyonbunny.util.CameraHelper;
import com.packtpub.libgdx.canyonbunny.util.Constants;

public class WorldController extends InputAdapter {
	private static final String TAG =CanyonBunnyMain.class.getName();
	public Sprite[] testSprites;
	public int selectedSprite;
	public CameraHelper cameraHelper;
	public Level level;
	public int lives;
	public int score;
	
	public WorldController () {
		init();
	}

	private void init () {
		Gdx.input.setInputProcessor(this);
		cameraHelper = new CameraHelper();
		lives = Constants.LIVES_START-1; // -1 pour les tests de GUI
		initLevel();	
	}

	private void initLevel () {
		score = 0;
		level = new Level(Constants.LEVEL_01);
	}

	public void update (float deltaTime) {
		handleDebugInput(deltaTime);		
		cameraHelper.update(deltaTime);
	}

	private void handleDebugInput (float deltaTime) {
		if (Gdx.app.getType() != ApplicationType.Desktop) { 
			return;
		}


		// Camera Controls (move)
		float camMoveSpeed = 5 * deltaTime;
		float camMoveSpeedAccelerationFactor = 5;
		if (Gdx.input.isKeyPressed(Keys.I)) camMoveSpeed *=
				camMoveSpeedAccelerationFactor;
		if (Gdx.input.isKeyPressed(Keys.LEFT)) moveCamera(-camMoveSpeed,
				0);
		if (Gdx.input.isKeyPressed(Keys.RIGHT)) moveCamera(camMoveSpeed,
				0);
		if (Gdx.input.isKeyPressed(Keys.UP)) moveCamera(0, camMoveSpeed);
		if (Gdx.input.isKeyPressed(Keys.DOWN)) moveCamera(0,
				-camMoveSpeed);
		if (Gdx.input.isKeyPressed(Keys.O))
			cameraHelper.setPosition(0, 0);

		// Camera Controls (zoom)
		float camZoomSpeed = 1 * deltaTime;
		float camZoomSpeedAccelerationFactor = 5;
		if (Gdx.input.isKeyPressed(Keys.H)) camZoomSpeed *=
				camZoomSpeedAccelerationFactor;
		if (Gdx.input.isKeyPressed(Keys.J))
			cameraHelper.addZoom(camZoomSpeed);
		if (Gdx.input.isKeyPressed(Keys.K)) 
			cameraHelper.addZoom(-camZoomSpeed);
		if (Gdx.input.isKeyPressed(Keys.L)) 
			cameraHelper.setZoom(1);
	}

	private void moveCamera (float x, float y) {
		x += cameraHelper.getPosition().x;
		y += cameraHelper.getPosition().y;
		cameraHelper.setPosition(x, y);
	}


	@Override
	public boolean keyUp(int keycode) {
		// Reset game world
		if (keycode == Keys.R) {
			init();
			Gdx.app.debug(TAG, "Game world resetted");
		}

		return false;
	}	
}