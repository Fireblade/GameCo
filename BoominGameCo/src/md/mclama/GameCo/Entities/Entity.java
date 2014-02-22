package md.mclama.GameCo.Entities;

import java.awt.Rectangle;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import md.mclama.GameCo.GameCo;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

import static org.lwjgl.opengl.GL11.*;

public abstract class Entity {
	
	public float x;
	public float y;
	public float hspeed;
	public float vspeed;
	public float speed=0.12f;
	public int imgw;
	public int imgh;
	//public int displayx, displayy;
	public int hitx, hity;
	//private float direction=0; //Don't need rotation i hope
	
	public int maxHp = 0;
	public int health=0;
	
	public float size;
	
	protected Texture texture;
	public long lifetime=1;
	
	public Rectangle hitbox = new Rectangle();
	
	public Entity(float x, float y, String tex, int displayx, int displayy, int hitx, int hity, int health, float size, float speed){
		this.x=x;
		this.y=y;
		this.hitx=hitx;
		this.hity=hity;
		texture = loadTexture(tex);
		imgw = displayx;
		imgh = displayy;
		this.health = health;
		maxHp = health;
		this.size = size;
		this.speed = speed;
		//System.out.println(imgw + "," + imgh);
		
		hitbox.x = (int) x;
		hitbox.y = (int) y;
		hitbox.width = hitx;
		hitbox.height = hity;
		
		
	}
	
	public void tick(){
		//stuff
	}
	
	public boolean inBounds(int x, int y, int ex, int ey, int picw, int pich,int hx, int hy){
		int exl = ex + (picw/2) - (hx/2); //against this
		int exr = ex + (picw/2) + (hx/2);
		int eyu = ey + (pich/2) - (hy/2);
		int eyd = ey + (pich/2) + (hy/2);
		
		int xl = (int) (x + (imgw/2) - (hitx/2)); //we are testing this one
		int xr = (int) (x + (imgw/2) + (hitx/2));
		int yu = (int) (y + (imgh/2) - (hity/2));
		int yd = (int) (y + (imgh/2) + (hity/2));
		//sides 
		int xc = (int) (x + (imgw/2)); //we are testing this one
		int yc = (int) (y + (imgh/2));
		
   //      V               V           V              V
//		if(xl >= exl+1 && xl <= exr-1 && yu >= eyu+1 && yu <= eyd-1) return true;//topleft
//		if(xr >= exl+1 && xr <= exr-1 && yu >= eyu+1 && yu <= eyd-1) return true;//topright
//		if(xl >= exl+1 && xl <= exr-1 && yd >= eyu+1 && yd <= eyd-1) return true;//botleft
//		if(xr >= exl+1 && xr <= exr-1 && yd >= eyu+1 && yd <= eyd-1) return true;//botright
		
		if(xl+1 >= exl && xl+1 <= exr && yu+1 >= eyu && yu+1 <= eyd) return true;//topleft
		if(xr-1 >= exl && xr-1 <= exr && yu+1 >= eyu && yu+1 <= eyd) return true;//topright
		if(xl+1 >= exl && xl+1 <= exr && yd-1 >= eyu && yd-1 <= eyd) return true;//botleft
		if(xr-1 >= exl && xr-1 <= exr && yd-1 >= eyu && yd-1 <= eyd) return true;//botright
		//sides
		if(xc >= exl+1 && xc <= exr-1 && yu >= eyu+1 && yu <= eyd-1) return true;//TOP
		if(xc >= exl+1 && xc <= exr-1 && yd >= eyu+1 && yd <= eyd-1) return true;//BOT
		if(xl >= exl+1 && xl <= exr-1 && yc >= eyu+1 && yc <= eyd-1) return true;//left
		if(xr >= exl+1 && xr <= exr-1 && yc >= eyu+1 && yc <= eyd-1) return true;//right
		
		if(xc >= exl && xc <= exr-1 && yc >= eyu+1 && yc <= eyd-1) return true;//CENTER
		
		return false;
	}
	
	public boolean inBounds(float x, float y, Entity e){
		float exl = e.x + (e.imgw/2) - (e.hitx/2); //against this
		float exr = e.x + (e.imgw/2) + (e.hitx/2);
		float eyu = e.y + (e.imgh/2) - (e.hity/2);
		float eyd = e.y + (e.imgh/2) + (e.hity/2);
		
		float xl = (x + (imgw/2) - (hitx/2)); //we are testing this one
		float xr = (x + (imgw/2) + (hitx/2));
		float yu = (y + (imgh/2) - (hity/2));
		float yd = (y + (imgh/2) + (hity/2));
		//sides 
		float xc = (x + (imgw/2)); //we are testing this one
		float yc = (y + (imgh/2));
		
		if(xl+1 >= exl && xl+1 <= exr && yu+1 >= eyu && yu+1 <= eyd) return true;//topleft
		if(xr-1 >= exl && xr-1 <= exr && yu+1 >= eyu && yu+1 <= eyd) return true;//topright
		if(xl+1 >= exl && xl+1 <= exr && yd-1 >= eyu && yd-1 <= eyd) return true;//botleft
		if(xr-1 >= exl && xr-1 <= exr && yd-1 >= eyu && yd-1 <= eyd) return true;//botright
		//sides
		if(xc >= exl+1 && xc <= exr-1 && yu >= eyu+1 && yu <= eyd-1) return true;//TOP
		if(xc >= exl+1 && xc <= exr-1 && yd >= eyu+1 && yd <= eyd-1) return true;//BOT
		if(xl >= exl+1 && xl <= exr-1 && yc >= eyu+1 && yc <= eyd-1) return true;//left
		if(xr >= exl+1 && xr <= exr-1 && yc >= eyu+1 && yc <= eyd-1) return true;//right
		
		if(xc >= exl && xc <= exr-1 && yc >= eyu+1 && yc <= eyd-1) return true;//CENTER
		
		return false;
	}


	public void render(){
		
		//glPushMatrix();
		//glTranslatef(texture.getTextureWidth()/2 ,texture.getTextureHeight()/2 , 0); // move to the proper position
		//glRotatef( 0.25f, 0, 0, 1 ); // now rotate
		//glTranslatef(-(texture.getTextureWidth()/2) ,-(texture.getTextureHeight()/2) , 0);
		//glBindTexture(GL_TEXTURE_2D,texture.getTextureID()); //doesnt work?! use texture.bind();
		
		texture.bind();
		glColor3f(1,1,1); //White
		glBegin(GL_QUADS);
			glTexCoord2f(0,0);
			glVertex2f(x,y);
			glTexCoord2f(1f,0);
			glVertex2f(x+texture.getTextureWidth(),y);
			glTexCoord2f(1,1);
			glVertex2f(x+texture.getTextureWidth(),y+texture.getTextureHeight());
			glTexCoord2f(0,1);
			glVertex2f(x,y+texture.getTextureHeight());
		glEnd();
		
//		glColor3f(1,0,0);       //test the center
//		glBegin(GL_QUADS);
//			glTexCoord2f(0,0);
//			glVertex2f(x,y);
//			glTexCoord2f(1,0);
//			glVertex2f(x+2,y);
//			glTexCoord2f(1,1);
//			glVertex2f(x+2,y+2);
//			glTexCoord2f(0,1);
//			glVertex2f(x,y+2);
//		glEnd();
//		glColor3f(1,1,1);
		
//		glBegin(GL_QUADS);
//			glTexCoord2f(0,0);
//			glVertex2f(x,y);
//			glTexCoord2f(1,0);
//			glVertex2f(x+imgw,y);
//			glTexCoord2f(1,1);
//			glVertex2f(x+imgw,y+imgh);
//			glTexCoord2f(0,1);
//			glVertex2f(x,y+imgh);
//		glEnd();
		
//		glBegin(GL_QUADS);
//			glTexCoord2f(0,0);
//			glVertex2f(x,y);
//			glTexCoord2f(1,0);
//			glVertex2f(x+displayx,y);
//			glTexCoord2f(1,1);
//			glVertex2f(x+displayx,y+displayy);
//			glTexCoord2f(0,1);
//			glVertex2f(x,y+displayy);
//		glEnd();
		
		//glPopMatrix();
	}
	

	protected Texture loadTexture(String key){
		try {
			return TextureLoader.getTexture("PNG", new FileInputStream(new File("res/" + key + ".png")));
			//return TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("/res/" + key + ".png"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public float getHspeed() {
		return hspeed;
	}

	public void setHspeed(float hspeed) {
		this.hspeed = hspeed;
	}

	public float getVspeed() {
		return vspeed;
	}

	public void setVspeed(float vspeed) {
		this.vspeed = vspeed;
	}

	public float getSpeed() {
		return speed;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}

}
