package md.mclama.GameCo.Entities.Buildings;

import md.mclama.GameCo.Entities.Entity;

public class WoodWall extends Entity {

	public WoodWall(float x, float y, String tex, int rotate) {
		super(x, y, tex, 8+(rotate*8), 16-(rotate*8), 8+(rotate*8), 16-(rotate*8), 80, 0);
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
