package com.packtpub.canyonbunny.game;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.packtpub.canyonbunny.CanyonBunnyMain;
import com.packtpub.canyonbunny.game.objects.BunnyHead;
import com.packtpub.canyonbunny.game.objects.Feather;
import com.packtpub.canyonbunny.game.objects.GoldCoin;
import com.packtpub.canyonbunny.game.objects.Rock;
import com.packtpub.canyonbunny.util.CameraHelper;
import com.packtpub.canyonbunny.util.Constants;
import com.badlogic.gdx.math.Rectangle;

public class WorldController extends InputAdapter {
	private static final String TAG = CanyonBunnyMain.class.getName();
	public Sprite[] testSprites;
	public int selectedSprite;
	public CameraHelper cameraHelper;
	public Level level;
	public int lives;
	public int score;
	
	// Rectangles for collision detection
	private Rectangle collisionRectangle1 = new Rectangle();
	private Rectangle collisionRectangle2 = new Rectangle();
	private float timeLeftGameOverDelay;

	public WorldController () {
		init();
	}

	private void init () {
		Gdx.input.setInputProcessor(this);
		cameraHelper = new CameraHelper();
		lives = Constants.LIVES_START;
		timeLeftGameOverDelay = 0;
		initLevel();	
	}

	private void initLevel () {
		score = 0;
		level = new Level(Constants.LEVEL_01);
		cameraHelper.setTarget(level.bunnyHead);
	}

	public void update (float deltaTime) {
		handleDebugInput(deltaTime);

		if (isGameOver()) {
			timeLeftGameOverDelay -= deltaTime;
			if (timeLeftGameOverDelay < 0) {
				init();
			}				
		} else {
			handleInputGame(deltaTime);
		}

		level.update(deltaTime);
		testCollisions();
		cameraHelper.update(deltaTime);
		if (!isGameOver() && isPlayerInWater()) {
			lives--;
			if (isGameOver())
				timeLeftGameOverDelay = Constants.TIME_DELAY_GAME_OVER;
			else
				initLevel();
		}			
	}

	private void handleDebugInput (float deltaTime) {
		if (Gdx.app.getType() != ApplicationType.Desktop) { 
			return;
		}

		if (!cameraHelper.hasTarget(level.bunnyHead)) {
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
	}

	private void moveCamera (float x, float y) {
		x += cameraHelper.getPosition().x;
		y += cameraHelper.getPosition().y;
		cameraHelper.setPosition(x, y);
	}


	@Override
	public boolean keyUp(int keycode) {
		if (keycode == Keys.R) {
			init();
			Gdx.app.debug(TAG, "Game world resetted");
		}
		// Toggle camera follow
		else if (keycode == Keys.ENTER) {
			cameraHelper.setTarget(cameraHelper.hasTarget()	? null: level.bunnyHead);
			Gdx.app.debug(TAG, "Camera follow enabled: " + cameraHelper.hasTarget());
		}
		return false;
	}

	private void handleInputGame (float deltaTime) {
		if (cameraHelper.hasTarget(level.bunnyHead)) {
			// Player Movement
			if (Gdx.input.isKeyPressed(Keys.LEFT)) {
				level.bunnyHead.velocity.x =
						-level.bunnyHead.terminalVelocity.x;
			} else if (Gdx.input.isKeyPressed(Keys.RIGHT)) {
				level.bunnyHead.velocity.x =
						level.bunnyHead.terminalVelocity.x;
			} else {
				// Execute auto-forward movement on non-desktop platform
				if (Gdx.app.getType() != ApplicationType.Desktop) {
					level.bunnyHead.velocity.x =
							level.bunnyHead.terminalVelocity.x;
				}
			}
			// Bunny Jump
			if (Gdx.input.isTouched() ||
					Gdx.input.isKeyPressed(Keys.SPACE)) {
				level.bunnyHead.setJumping(true);
			} else {
				level.bunnyHead.setJumping(false);
			}
		}
	}	

	private void testCollisions () {
		collisionRectangle1.set(level.bunnyHead.position.x, level.bunnyHead.position.y,
				level.bunnyHead.bounds.width, level.bunnyHead.bounds.height);
		// Test collision: Bunny Head <-> Rocks
		for (Rock rock : level.rocks) {
			collisionRectangle2.set(rock.position.x, rock.position.y, rock.bounds.width,
					rock.bounds.height);
			if (!collisionRectangle1.overlaps(collisionRectangle2)) continue;
			onCollisionBunnyHeadWithRock(rock);
			// IMPORTANT: must do all collisions for valid
			// edge testing on rocks.
		}
		// Test collision: Bunny Head <-> Gold Coins
		for (GoldCoin goldcoin : level.goldcoins) {
			if (goldcoin.collected) continue;
			collisionRectangle2.set(goldcoin.position.x, goldcoin.position.y,
					goldcoin.bounds.width, goldcoin.bounds.height);
			if (!collisionRectangle1.overlaps(collisionRectangle2)) continue;
			onCollisionBunnyWithGoldCoin(goldcoin);
			break;
		}
		// Test collision: Bunny Head <-> Feathers
		for (Feather feather : level.feathers) {
			if (feather.collected) continue;
			collisionRectangle2.set(feather.position.x, feather.position.y,
					feather.bounds.width, feather.bounds.height);
			if (!collisionRectangle1.overlaps(collisionRectangle2)) continue;
			onCollisionBunnyWithFeather(feather);
			break;
		}
	}

	private void onCollisionBunnyHeadWithRock (Rock rock) {
		BunnyHead bunnyHead = level.bunnyHead;
		float heightDifference = Math.abs(bunnyHead.position.y
				- ( rock.position.y + rock.bounds.height));
		if (heightDifference > 0.25f) {
			boolean hitRightEdge = bunnyHead.position.x > (
					rock.position.x + rock.bounds.width / 2.0f);
			if (hitRightEdge) {
				bunnyHead.position.x = rock.position.x + rock.bounds.width;
			} else {
				bunnyHead.position.x = rock.position.x -
						bunnyHead.bounds.width;
			}
			return;
		}
		switch (bunnyHead.jumpState) {
		case GROUNDED:
			break;
		case FALLING:
		case JUMP_FALLING:
			bunnyHead.position.y = rock.position.y +
			bunnyHead.bounds.height + bunnyHead.origin.y;
			bunnyHead.jumpState = BunnyHead.JUMP_STATE.GROUNDED;
			break;
		case JUMP_RISING:
			bunnyHead.position.y = rock.position.y +
			bunnyHead.bounds.height + bunnyHead.origin.y;
			break;
		}
	}

	private void onCollisionBunnyWithGoldCoin (GoldCoin goldcoin) {
		goldcoin.collected = true;
		score += goldcoin.getScore();
		Gdx.app.log(TAG, "Gold coin collected");
	}

	private void onCollisionBunnyWithFeather (Feather feather) {
		feather.collected = true;
		score += feather.getScore();
		level.bunnyHead.setFeatherPowerup(true);
		Gdx.app.log(TAG, "Feather collected");
	}

	public boolean isGameOver () {
		return lives < 0;
	}

	public boolean isPlayerInWater () {
		return level.bunnyHead.position.y < -5;
	}	

}