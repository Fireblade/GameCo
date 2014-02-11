package md.mclama.GameCo;

import java.awt.Font;

import org.newdawn.slick.Color;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;

public class Fonts {
	
	public UnicodeFont vani12;
	
	public Fonts() {
		//
		vani12 = new UnicodeFont(new java.awt.Font ("Vani", Font.BOLD, 12));
		vani12.getEffects().add(new ColorEffect(java.awt.Color.white));
		vani12.addNeheGlyphs();
//		try {
//			vani12.loadGlyphs();
//		} catch (SlickException e) {
//			e.printStackTrace();
//		}
	}
	
	
}
