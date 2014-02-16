package md.mclama.GameCo.Entities.Buildings;

import md.mclama.GameCo.Entities.Entity;

public class SolidWall extends Entity {

	//public SolidWall(float x, float y, String tex, int imgx, int imgy, int hitx, int hity, int health) {
	public SolidWall(float x, float y, Buildings ding) {	
		//ding.getImgx(), ding.getImgx(), ding.getHitx(), ding.getHity(), ding.getHealth()
		super(x, y, ding.getTex(), ding.getImgx(), ding.getImgy(), ding.getHitx(), ding.getHity(), ding.getHealth(), 0, 0);
	}

	
//	public void render(){
//		texture.bind();
//		glColor3f(1,1,1); //White
//		glBegin(GL_QUADS);
//			glTexCoord2f(0,0);
//			glVertex2f(x,y);
//			glTexCoord2f(1,0);
//			glVertex2f(x+texture.getImageWidth(),y);
//			glTexCoord2f(1,1);
//			glVertex2f(x+texture.getImageWidth(),y+texture.getImageHeight());
//			glTexCoord2f(0,1);
//			glVertex2f(x,y+texture.getImageHeight());
//		glEnd();
//	}

}
