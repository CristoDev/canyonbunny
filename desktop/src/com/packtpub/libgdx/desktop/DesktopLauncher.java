package com.packtpub.libgdx.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.badlogic.gdx.tools.texturepacker.TexturePacker.Settings;

import com.packtpub.canyonbunny.CanyonBunnyMain;

public class DesktopLauncher {
	private static boolean rebuildAtlas = false;
	private static boolean drawDebugOutline = false;

	public static void main (String[] arg) {
		// pour creer le pack il faut supprimer d'abord les fichiers .pack et
		// .png s'ils existent sinon il y a une erreur
		if (rebuildAtlas) {
			Settings settings = new Settings();
			settings.maxWidth = 1024;
			settings.maxHeight = 1024;
			settings.duplicatePadding = false;
			settings.debug = drawDebugOutline;
			try {
				TexturePacker.process(settings, "assets-raw/images", "../android/assets/images", "canyonbunny.pack");
				TexturePacker.process(settings, "assets-raw/images-ui",	"../android/assets/images", "canyonbunny-ui.pack");
			}
			catch (RuntimeException e) {
				System.out.println("##### RuntimeException - START #####");
				System.out.println("ON: DesktopLauncher > main > MESSAGE:");
				System.out.println(e.getMessage());
				System.out.println("##### RuntimeException -  END  #####\n");
			}

			System.out.println("Création terminée");
		}

		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		config.width=800;
		config.height=480;

		new LwjglApplication(new CanyonBunnyMain(), config);
	}

}
