package com.packtpub.canyonbunny.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.packtpub.canyonbunny.game.Assets;
import com.packtpub.canyonbunny.util.Constants;
import com.packtpub.canyonbunny.util.CharacterSkin;
import com.packtpub.canyonbunny.util.GamePreferences;
import com.badlogic.gdx.math.MathUtils;
import com.packtpub.canyonbunny.util.AudioManager;
import com.badlogic.gdx.graphics.g2d.Animation;

public class BunnyHead extends AbstractGameObject {
	public static final String TAG = BunnyHead.class.getName();
	private final float JUMP_TIME_MAX = 0.3f;
	private final float JUMP_TIME_MIN = 0.1f;
	private final float JUMP_TIME_OFFSET_FLYING =
			JUMP_TIME_MAX - 0.018f;

	public enum VIEW_DIRECTION { LEFT, RIGHT }

	public enum JUMP_STATE {
		GROUNDED, FALLING, JUMP_RISING, JUMP_FALLING
	}

	public VIEW_DIRECTION viewDirection;
	public float timeJumping;
	public JUMP_STATE jumpState;
	public boolean hasFeatherPowerup;
	public float timeLeftFeatherPowerup;
	public ParticleEffect dustParticles = new ParticleEffect();
	private Animation animNormal;
	private Animation animCopterTransform;
	private Animation animCopterTransformBack;
	private Animation animCopterRotate;

	public BunnyHead () {
		init();
	}

	public void init () {
		dimension.set(1, 1);
		animNormal = Assets.instance.bunny.animNormal;
		animCopterTransform = Assets.instance.bunny.animCopterTransform;
		animCopterTransformBack =
				Assets.instance.bunny.animCopterTransformBack;
		animCopterRotate = Assets.instance.bunny.animCopterRotate;
		setAnimation(animNormal);		
		//regHead = Assets.instance.bunny.head;
		origin.set(dimension.x / 2, dimension.y / 2);
		bounds.set(0, 0, dimension.x, dimension.y);
		terminalVelocity.set(3.0f, 4.0f);
		friction.set(12.0f, 0.0f);
		acceleration.set(0.0f, -25.0f);
		viewDirection = VIEW_DIRECTION.RIGHT;
		jumpState = JUMP_STATE.FALLING;
		timeJumping = 0;
		dustParticles.load(Gdx.files.internal("particles/dust.pfx"), Gdx.files.internal("particles"));
	}

	public void setFeatherPowerup (boolean pickedUp) {
		hasFeatherPowerup = pickedUp;
		if (pickedUp) {
			timeLeftFeatherPowerup =
					Constants.ITEM_FEATHER_POWERUP_DURATION;
		}
	}

	public boolean hasFeatherPowerup() {
		return hasFeatherPowerup && timeLeftFeatherPowerup > 0;
	}

	@Override
	public void render (SpriteBatch batch) {
		TextureRegion reg = null;
		// Draw Particles
		dustParticles.draw(batch);
		// Apply Skin Color
		batch.setColor(
				CharacterSkin.values()[GamePreferences.instance.charSkin]
						.getColor());
		float dimCorrectionX = 0;
		float dimCorrectionY = 0;
		if (animation != animNormal) {
			dimCorrectionX = 0.05f;
			dimCorrectionY = 0.2f;
		}
		// Draw image
		reg = animation.getKeyFrame(stateTime, true);
		batch.draw(reg.getTexture(),
				position.x, position.y,
				origin.x, origin.y,
				dimension.x + dimCorrectionX,
				dimension.y + dimCorrectionY,
				scale.x, scale.y,
				rotation,
				reg.getRegionX(), reg.getRegionY(),
				reg.getRegionWidth(), reg.getRegionHeight(),
				viewDirection == VIEW_DIRECTION.LEFT, false);
		// Reset color to white
		batch.setColor(1, 1, 1, 1);
	}

	@Override
	public void update (float deltaTime) {
		super.update(deltaTime);
		if (velocity.x != 0) {
			viewDirection = velocity.x < 0 ? VIEW_DIRECTION.LEFT :
				VIEW_DIRECTION.RIGHT;
		}

		if (timeLeftFeatherPowerup > 0) {
			if (animation == animCopterTransformBack) {
				// Restart "Transform" animation if another feather power-up
				// was picked up during "TransformBack" animation. Otherwise,
				// the "TransformBack" animation would be stuck while the
				// power-up is still active.
				setAnimation(animCopterTransform);
			}
			timeLeftFeatherPowerup -= deltaTime;
			if (timeLeftFeatherPowerup < 0) {
				timeLeftFeatherPowerup = 0;
				setFeatherPowerup(false);
				setAnimation(animCopterTransformBack);
			}
		}
		dustParticles.update(deltaTime);
		// Change animation state according to feather power-up
		if (hasFeatherPowerup) {
			if (animation == animNormal) {
				setAnimation(animCopterTransform);
			} else if (animation == animCopterTransform) {
				if (animation.isAnimationFinished(stateTime))
					setAnimation(animCopterRotate);
			}
		} else {
			if (animation == animCopterRotate) {
				if (animation.isAnimationFinished(stateTime))
					setAnimation(animCopterTransformBack);
			} else if (animation == animCopterTransformBack) {
				if (animation.isAnimationFinished(stateTime))
					setAnimation(animNormal);
			}
		}
	}

	@Override
	protected void updateMotionY (float deltaTime) {
		switch (jumpState) {
		case GROUNDED:
			jumpState = JUMP_STATE.FALLING;
			if (velocity.x != 0) {
				dustParticles.setPosition(position.x + dimension.x / 2, position.y);
				dustParticles.start();
			}			
			break;
		case JUMP_RISING:
			timeJumping += deltaTime;
			if (timeJumping <= JUMP_TIME_MAX) {
				velocity.y = terminalVelocity.y;
			}
			break;
		case FALLING:
			break;
		case JUMP_FALLING:
			timeJumping += deltaTime;
			if (timeJumping > 0 && timeJumping <= JUMP_TIME_MIN) {
				velocity.y = terminalVelocity.y;
			}
		}

		if (jumpState != JUMP_STATE.GROUNDED) {
			dustParticles.allowCompletion();
			super.updateMotionY(deltaTime);
		}
	}	

	public void setJumping (boolean jumpKeyPressed) {
		switch (jumpState) {
		case GROUNDED:
			if (jumpKeyPressed) {
				timeJumping = 0;
				jumpState = JUMP_STATE.JUMP_RISING;
				AudioManager.instance.play(Assets.instance.sounds.jump);
			}
			break;
		case JUMP_RISING:
			if (!jumpKeyPressed)
				jumpState = JUMP_STATE.JUMP_FALLING;
			break;
		case FALLING:
		case JUMP_FALLING:		
			if (jumpKeyPressed && hasFeatherPowerup) {
				timeJumping = JUMP_TIME_OFFSET_FLYING;
				jumpState = JUMP_STATE.JUMP_RISING;
				AudioManager.instance.play(
						Assets.instance.sounds.jumpWithFeather, 1,
						MathUtils.random(1.0f, 1.1f));				
			}
			break;
		}	
	}	
}