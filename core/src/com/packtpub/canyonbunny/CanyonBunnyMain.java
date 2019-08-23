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

public class CanyonBunnyMain extends DirectedGame {
	@Override
	public void create () {
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
		Assets.instance.init(new AssetManager());
		ScreenTransition transition = ScreenTransitionSlice.init(2,
				ScreenTransitionSlice.UP_DOWN, 10, Interpolation.pow5Out);
				setScreen(new MenuScreen(this), transition);
	}
}