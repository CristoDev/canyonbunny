package com.packtpub.canyonbunny.util;

public class Constants {
	public static final float VIEWPORT_WIDTH = 5.0f;
	public static final float VIEWPORT_HEIGHT = 5.0f;
	public static final String TEXTURE_ATLAS_OBJECTS = "images/canyonbunny.pack";
	public static final String LEVEL_01 = "levels/level-01.png";
	public static final int LIVES_START = 3;
	public static final float VIEWPORT_GUI_WIDTH = 800.0f;
	public static final float VIEWPORT_GUI_HEIGHT = 480.0f;
	public static final float ITEM_FEATHER_POWERUP_DURATION = 9;
	public static final float TIME_DELAY_GAME_OVER = 3;
	public static final String TEXTURE_ATLAS_UI = "images/canyonbunny-ui.pack";	
	public static final String TEXTURE_ATLAS_LIBGDX_UI = "images/uiskin.atlas";
	public static final String SKIN_LIBGDX_UI =	"images/uiskin.json";
	public static final String SKIN_CANYONBUNNY_UI = "images/canyonbunny-ui.json";
	public static final String PREFERENCES = "canyonbunny.prefs";
	// Number of carrots to spawn
	public static final int CARROTS_SPAWN_MAX = 100;
	// Spawn radius for carrots
	public static final float CARROTS_SPAWN_RADIUS = 3.5f;
	// Delay after game finished
	public static final float TIME_DELAY_GAME_FINISHED = 6;
	// Shader
	public static final String shaderMonochromeVertex =	"shaders/monochrome.vs";
	public static final String shaderMonochromeFragment = "shaders/monochrome.fs";
	// Angle of rotation for dead zone (no movement)
	public static final float ACCEL_ANGLE_DEAD_ZONE = 5.0f;
	// Max angle of rotation needed to gain max movement velocity
	public static final float ACCEL_MAX_ANGLE_MAX_MOVEMENT = 20.0f;
}

