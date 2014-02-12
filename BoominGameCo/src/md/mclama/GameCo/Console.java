package md.mclama.GameCo;

import org.newdawn.slick.Color;

public class Console {

	public static final int CSIZE = 40;
	
	public String prelog[] = new String[CSIZE];
	public Color[] precolor = new Color[CSIZE];
	
	public String postlog[] = new String[CSIZE];
	public Color postcolor[] = new Color[CSIZE];
	
	public boolean show=false;
	
	
	public Console(){
		for(int i=0; i<CSIZE; i++){
			prelog[i] = "[]";
			postlog[i] = "-"+i;
			precolor[i] = Color.green;
			postcolor[i] = Color.cyan;
		}
	}
	
	public void scroll(){
		for(int i=CSIZE-1; i>0; i--){
			prelog[i] = prelog[i-1];
			postlog[i] = postlog[i-1];
			precolor[i] = precolor[i-1];
			postcolor[i] = postcolor[i-1];
		}
	}
	
	public void addLog(String text){
		scroll();
		prelog[0] = "[log] ";
		postlog[0] = text;
		precolor[0] = Color.green;
		postcolor[0] = Color.cyan;
		
		System.out.println("[log] " + text);
	}
	
	public void addWarn(String text){
		scroll();
		prelog[0] = "[WARNING] ";
		postlog[0] = text;
		precolor[0] = Color.red;
		postcolor[0] = Color.cyan;
		
		System.out.println("[WARNING] " + text);
	}
	
}
