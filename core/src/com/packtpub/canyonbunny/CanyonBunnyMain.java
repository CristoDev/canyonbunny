package com.packtpub.canyonbunny;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;

import com.packtpub.canyonbunny.game.Assets;
import com.packtpub.canyonbunny.screens.MenuScreen;

public class CanyonBunnyMain extends Game {
	@Override
	public void create () {
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
		Assets.instance.init(new AssetManager());
		setScreen(new MenuScreen(this));
	}
}