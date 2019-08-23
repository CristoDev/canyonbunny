package com.packtpub.canyonbunny.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.utils.Disposable;
import com.packtpub.canyonbunny.util.Constants;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.utils.Array;

public class Assets implements Disposable, AssetErrorListener {
	public static final String TAG = Assets.class.getName();
	public static final Assets instance = new Assets();
	public AssetFonts fonts;
	private AssetManager assetManager;

	private Assets () {}

	public AssetBunny bunny;
	public AssetRock rock;
	public AssetGoldCoin goldCoin;
	public AssetFeather feather;
	public AssetLevelDecoration levelDecoration;
	public AssetSounds sounds;
	public AssetMusic music;

	public void init (AssetManager assetManager) {
		this.assetManager = assetManager;
		assetManager.setErrorListener(this);
		assetManager.load(Constants.TEXTURE_ATLAS_OBJECTS, TextureAtlas.class);
		assetManager.load("sounds/jump.wav", Sound.class);
		assetManager.load("sounds/jump_with_feather.wav", Sound.class);
		assetManager.load("sounds/pickup_coin.wav", Sound.class);
		assetManager.load("sounds/pickup_feather.wav", Sound.class);
		assetManager.load("sounds/live_lost.wav", Sound.class);
		assetManager.load("music/keith303_-_brand_new_highscore.mp3",
				Music.class);
		assetManager.finishLoading();
		Gdx.app.debug(TAG, "# of assets loaded: " + assetManager.getAssetNames().size);

		for (String a : assetManager.getAssetNames()) {
			Gdx.app.debug(TAG, "asset: " + a);
		}

		TextureAtlas atlas = assetManager.get(Constants.TEXTURE_ATLAS_OBJECTS);
		for (Texture t : atlas.getTextures()) {
			t.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		}

		fonts = new AssetFonts();
		bunny = new AssetBunny(atlas);
		rock = new AssetRock(atlas);
		goldCoin = new AssetGoldCoin(atlas);
		feather = new AssetFeather(atlas);
		levelDecoration = new AssetLevelDecoration(atlas);
		sounds = new AssetSounds(assetManager);
		music = new AssetMusic(assetManager);
	}

	@Override
	public void dispose() {
		assetManager.dispose();
		fonts.defaultSmall.dispose();
		fonts.defaultNormal.dispose();
		fonts.defaultBig.dispose();
	}

	public void error (String filename, Class type,	Throwable throwable) {
		Gdx.app.error(TAG, "Couldn't load asset '" + filename + "'", (Exception)throwable);
	}

	@Override
	public void error(AssetDescriptor asset, Throwable throwable) {
		Gdx.app.error(TAG, "Couldn't load asset '" + asset.fileName + "'", (Exception)throwable);
	}

	public class AssetBunny {
		public final Animation animNormal;
		public final Animation animCopterTransform;
		public final Animation animCopterTransformBack;
		public final Animation animCopterRotate;		
		public final AtlasRegion head;

		public AssetBunny (TextureAtlas atlas) {
			head = atlas.findRegion("bunny_head");
			Array<AtlasRegion> regions = null;
			// Animation: Bunny Normal
			regions = atlas.findRegions("anim_bunny_normal");
			animNormal = new Animation(1.0f / 10.0f, regions,
					Animation.PlayMode.LOOP_PINGPONG);
			// Animation: Bunny Copter - knot ears
			regions = atlas.findRegions("anim_bunny_copter");
			animCopterTransform = new Animation(1.0f / 10.0f, regions);
			// Animation: Bunny Copter - unknot ears
			regions = atlas.findRegions("anim_bunny_copter");
			animCopterTransformBack = new Animation(1.0f / 10.0f, regions,
					Animation.PlayMode.REVERSED);
			// Animation: Bunny Copter - rotate ears
			regions = new Array<AtlasRegion>();
			regions.add(atlas.findRegion("anim_bunny_copter", 4));
			regions.add(atlas.findRegion("anim_bunny_copter", 5));
			animCopterRotate = new Animation(1.0f / 15.0f, regions);
		}
	}

	public class AssetRock {
		public final AtlasRegion edge;
		public final AtlasRegion middle;

		public AssetRock (TextureAtlas atlas) {
			edge = atlas.findRegion("rock_edge");
			middle = atlas.findRegion("rock_middle");
		}
	}

	public class AssetGoldCoin {
		public final Animation animGoldCoin;
		public final AtlasRegion goldCoin;

		public AssetGoldCoin (TextureAtlas atlas) {
			goldCoin = atlas.findRegion("item_gold_coin");
			// Animation: Gold Coin
			Array<AtlasRegion> regions =
					atlas.findRegions("anim_gold_coin");
			AtlasRegion region = regions.first();
			for (int i = 0; i < 10; i++)
				regions.insert(0, region);
			animGoldCoin = new Animation(1.0f / 20.0f, regions,	Animation.PlayMode.LOOP_PINGPONG);
		}
	}

	public class AssetFeather {
		public final AtlasRegion feather;

		public AssetFeather (TextureAtlas atlas) {
			feather = atlas.findRegion("item_feather");
		}
	}

	public class AssetLevelDecoration {
		public final AtlasRegion cloud01;
		public final AtlasRegion cloud02;
		public final AtlasRegion cloud03;
		public final AtlasRegion mountainLeft;
		public final AtlasRegion mountainRight;
		public final AtlasRegion waterOverlay;
		public final AtlasRegion carrot;
		public final AtlasRegion goal;

		public AssetLevelDecoration (TextureAtlas atlas) {
			cloud01 = atlas.findRegion("cloud01");
			cloud02 = atlas.findRegion("cloud02");
			cloud03 = atlas.findRegion("cloud03");
			mountainLeft = atlas.findRegion("mountain_left");
			mountainRight = atlas.findRegion("mountain_right");
			waterOverlay = atlas.findRegion("water_overlay");
			carrot = atlas.findRegion("carrot");
			goal = atlas.findRegion("goal");
		}
	}

	public class AssetFonts {
		public final BitmapFont defaultSmall;
		public final BitmapFont defaultNormal;
		public final BitmapFont defaultBig;
		public AssetFonts () {
			defaultSmall = new BitmapFont(
					Gdx.files.internal("images/arial-15.fnt"), true);
			defaultNormal = new BitmapFont(
					Gdx.files.internal("images/arial-15.fnt"), true);
			defaultBig = new BitmapFont(
					Gdx.files.internal("images/arial-15.fnt"), true);
			defaultSmall.setScale(0.75f);
			defaultNormal.setScale(1.0f);
			defaultBig.setScale(2.0f);
			defaultSmall.getRegion().getTexture().setFilter(
					TextureFilter.Linear, TextureFilter.Linear);
			defaultNormal.getRegion().getTexture().setFilter(
					TextureFilter.Linear, TextureFilter.Linear);
			defaultBig.getRegion().getTexture().setFilter(
					TextureFilter.Linear, TextureFilter.Linear);
		}
	}

	public class AssetSounds {
		public final Sound jump;
		public final Sound jumpWithFeather;
		public final Sound pickupCoin;
		public final Sound pickupFeather;
		public final Sound liveLost;

		public AssetSounds (AssetManager am) {
			jump = am.get("sounds/jump.wav", Sound.class);
			jumpWithFeather = am.get("sounds/jump_with_feather.wav",
					Sound.class);
			pickupCoin = am.get("sounds/pickup_coin.wav", Sound.class);
			pickupFeather = am.get("sounds/pickup_feather.wav",
					Sound.class);
			liveLost = am.get("sounds/live_lost.wav", Sound.class);
		}
	}

	public class AssetMusic {
		public final Music song01;

		public AssetMusic (AssetManager am) {
			song01 = am.get("music/keith303_-_brand_new_highscore.mp3",
					Music.class);
		}
	}
}