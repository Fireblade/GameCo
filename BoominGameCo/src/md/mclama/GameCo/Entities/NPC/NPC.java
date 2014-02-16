package md.mclama.GameCo.Entities.NPC;

import static org.lwjgl.opengl.GL11.*;
import md.mclama.GameCo.GameCo;
import md.mclama.GameCo.Entities.Entity;

public class NPC extends Entity {

	private float angle;
	
	public NPC(float x, float y, NPClist npcl) {
		super(x, y, npcl.getTex(), npcl.getImgx(), npcl.getImgy(), npcl.getHitx(), npcl.getHity(), npcl.getHealth(), npcl.getSize(), npcl.getSpeed());
		
	}
	
	public void tick(int delta){
		x += (hspeed*delta);
		y += (vspeed*delta);
		//System.out.println(""+ hspeed + "," + vspeed + ",,,, " + delta);
		hspeed=0;
		vspeed=0;
		angle +=  1f;
	}
	
	public void moveDir(float x2, float y2){
		float angle = (float) (Math.atan2 (y2 - y, x2 - x ) * 57.2957795f); //57... is 180/pi
		
		float scaleX = (float)Math.cos((angle/180)*3.14f);
		float scaleY = (float)Math.sin((angle/180)*3.14f);
		
		hspeed = (scaleX);            //speed is already defined
		vspeed = (scaleY);
		
	}

	public void render(float translate_x, float translate_y){
		float width = texture.getTextureWidth()*size; 
		float height = texture.getTextureHeight()*size;
		glPushMatrix();
		//glTranslatef(0,0,0);
		//glTranslatef(x,y,0);
		//glTranslatef((translate_x-x),(translate_y-y),0);
		glTranslatef(width/2 ,height/2 , 0); //Translate to the center
		glRotatef( angle, 0, 0, 1.0f ); // now rotate
		glTranslatef(-(width/2) ,-(height/2) , 0); //Translate back to position
		
		texture.bind();
		glColor3f(1,1,1); //White
		glBegin(GL_QUADS);
			glTexCoord2f(0,0);
			glVertex2f(x,y);
			glTexCoord2f(1f,0);
			glVertex2f(x+width,y);
			glTexCoord2f(1,1);
			glVertex2f(x+width,y+height);
			glTexCoord2f(0,1);
			glVertex2f(x,y+height);
		glEnd();
		
		glPopMatrix();
	}
	
	
	
}
