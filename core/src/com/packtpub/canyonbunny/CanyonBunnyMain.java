package com.packtpub.canyonbunny;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;

import com.packtpub.canyonbunny.game.Assets;
import com.packtpub.canyonbunny.screens.MenuScreen;
import com.packtpub.canyonbunny.screens.DirectedGame;
import com.badlogic.gdx.math.Interpolation;
import com.packtpub.canyonbunny.screens.transitions.ScreenTransition;
import com.packtpub.canyonbunny.screens.transitions.ScreenTransitionSlice;
import com.packtpub.canyonbunny.util.AudioManager;
import com.packtpub.canyonbunny.util.GamePreferences;

public class CanyonBunnyMain extends DirectedGame {
	@Override
	public void create () {
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
		Assets.instance.init(new AssetManager());
		GamePreferences.instance.load();
		AudioManager.instance.play(Assets.instance.music.song01);
		ScreenTransition transition = ScreenTransitionSlice.init(2,
				ScreenTransitionSlice.UP_DOWN, 10, Interpolation.pow5Out);
				setScreen(new MenuScreen(this), transition);
	}
}