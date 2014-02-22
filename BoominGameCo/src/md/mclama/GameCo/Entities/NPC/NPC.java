package md.mclama.GameCo.Entities.NPC;

import static org.lwjgl.opengl.GL11.*;

import java.util.Random;

import md.mclama.GameCo.GameCo;
import md.mclama.GameCo.Entities.Entity;

public class NPC extends Entity {

	public float spawnx;

	public float spawny;
	
	private float angle;
	public boolean hostile=false;
	
	private boolean isRunning = false;
	public boolean attacking = false;
	private boolean isMoving = false;
	private boolean canMove = true;
	private int moveTick = 0;
	private int moveWait = 0;
	private float moveDir=0;
	
	
	public NPC(float x, float y, NPClist npcl) {
		super(x, y, npcl.getTex(), npcl.getImgx(), npcl.getImgy(), npcl.getHitx(), npcl.getHity(), npcl.getHealth(), npcl.getSize(), npcl.getSpeed());
		spawnx = x;
		spawny = y;
		hostile = npcl.isHostile();
	}
	
	public void tick(int delta){
		//x += (hspeed*delta);
		//y += (vspeed*delta);
		//System.out.println(""+ hspeed + "," + vspeed + ",,,, " + delta);
		//hspeed=0;
		//vspeed=0;
		//angle +=  1f;
		if(moveWait>0){
			moveWait--;
		}
		else if (moveWait==0 && !canMove){
			canMove=true;
		}
		if(!hostile || !attacking){
			if(!isMoving){
				if(canMove){
					Random gen = new Random(System.currentTimeMillis());
					moveDir = gen.nextInt(360);
					float scaleX = (float)Math.cos((moveDir/180)*3.14f);
					float scaleY = (float)Math.sin((moveDir/180)*3.14f);
					hspeed = (scaleX);            //speed is already defined
					vspeed = (scaleY);
					
					canMove=false;
					isMoving=true;
					moveTick = gen.nextInt(4*60)+30;
					System.out.println("Moving... " + moveTick);
				}
			}
			else if(isMoving){ //we are moving
				moveTick--;
				if(moveTick==0){
					Random gen = new Random(System.currentTimeMillis());
					isMoving=false;
					hspeed=0;
					vspeed=0;
					moveWait = gen.nextInt(7*60)+10;
					System.out.println("Waiting... " + moveWait);
				}
			}
		}
	}
	
	public void moveDir(float x2, float y2){
		float angle = (float) (Math.atan2 (y2 - y, x2 - x ) * 57.2957795f); //57... is 180/pi
		
		float scaleX = (float)Math.cos((angle/180)*3.14f);
		float scaleY = (float)Math.sin((angle/180)*3.14f);
		System.out.println(scaleX + "," + scaleY);
		
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
		//glTranslatef(width/2 ,height/2 , 0); //Translate to the center
		//glRotatef( angle, 0, 0, 1.0f ); // now rotate
		//glTranslatef(-(width/2) ,-(height/2) , 0); //Translate back to position
		
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
