package md.mclama.GameCo;

import java.io.IOException;

import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.openal.AudioLoader;
import org.newdawn.slick.openal.SoundStore;
import org.newdawn.slick.util.ResourceLoader;

public class Sounds {

	public Audio wavHitTree;
	public Audio oggHitTree;
	
	public void Sounds(){
		try {
//			oggEffect = AudioLoader.getAudio("OGG", ResourceLoader.getResourceAsStream("testdata/restart.ogg"));
//			oggStream = AudioLoader.getStreamingAudio("OGG", ResourceLoader.getResource("testdata/bongos.ogg"));
//			102.modStream = AudioLoader.getStreamingAudio("MOD", ResourceLoader.getResource("testdata/SMB-X.XM"));
			//modStream.playAsMusic(1.0f, 1.0f, true);
//			aifEffect = AudioLoader.getAudio("AIF", ResourceLoader.getResourceAsStream("testdata/burp.aif"));
//			wavEffect = AudioLoader.getAudio("WAV", ResourceLoader.getResourceAsStream("testdata/cbrown01.wav"));
			
			wavHitTree = AudioLoader.getAudio("WAV", ResourceLoader.getResourceAsStream("/res/sounds/hittree.wav"));
			oggHitTree = AudioLoader.getAudio("OGG", ResourceLoader.getResourceAsStream("res/sounds/hittree.ogg"));
			
			} catch (IOException e) {
				e.printStackTrace();
			
		}
	}
	
}
