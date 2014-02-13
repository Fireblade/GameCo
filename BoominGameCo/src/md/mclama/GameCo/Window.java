package md.mclama.GameCo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

public class Window {
	
	private GameCo game;

	public int x;
	public int y;
	
	public int width;
	public int height;
	
	public Texture corner, corner2, corner3, corner4;
	public Texture side1, side2, side3, side4, wcenter;
	
	public Window(int x, int y, int width, int height, GameCo Game){
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		
		this.game = game;
		
		corner = loadTexture("corner1");
		corner2 = loadTexture("corner2");
		corner3 = loadTexture("corner3");
		corner4 = loadTexture("corner4");
		
		side1 = loadTexture("wside1");
		side2 = loadTexture("wside2");
		side3 = loadTexture("wside3");
		side4 = loadTexture("wside4");
		
		wcenter = loadTexture("wcenter");
	}

	
	public void render(){
		Color.white.bind();
        corner.bind(); // or GL11.glBind(texture.getTextureID());
//        GL11.glPushMatrix();
//        //GL11.glTranslatef(translate_x, translate_y, 0);
//        //GL11.glTranslatef(0, 0, 0);
//        //GL11.glTranslatef(x, y, 0);
//        GL11.glTranslatef((corner.getImageWidth()/2), (corner.getImageHeight()/2), 0);
//        GL11.glRotatef(45f, 0.0f, 0.0f, 1.0f);
//        //GL11.glTranslatef(x, y, 0);
//        GL11.glTranslatef(-(corner.getImageWidth()/2), -(corner.getImageHeight()/2), 0);
        //GL11.glTranslatef(translate_x, translate_y, 0);
        
        //This rotation shit is fucking annoying and

        //Top left corner
        GL11.glBegin(GL11.GL_QUADS);
            GL11.glTexCoord2f(0,0);
            GL11.glVertex2f(x,y);
            GL11.glTexCoord2f(1,0);
            GL11.glVertex2f(x+corner.getTextureWidth(),y);
            GL11.glTexCoord2f(1,1);
            GL11.glVertex2f(x+corner.getTextureWidth(),y+corner.getTextureHeight());
            GL11.glTexCoord2f(0,1);
            GL11.glVertex2f(x,y+corner.getTextureHeight());
        GL11.glEnd();
        //Top Right corner
        corner2.bind();
        GL11.glBegin(GL11.GL_QUADS);
	        GL11.glTexCoord2f(0,0);
	        GL11.glVertex2f(x+width,y);
	        GL11.glTexCoord2f(1,0);
	        GL11.glVertex2f(x+width+corner2.getTextureWidth(),y);
	        GL11.glTexCoord2f(1,1);
	        GL11.glVertex2f(x+width+corner2.getTextureWidth(),y+corner2.getTextureHeight());
	        GL11.glTexCoord2f(0,1);
	        GL11.glVertex2f(x+width,y+corner2.getTextureHeight());
        GL11.glEnd();
        //Bottom Right corner
        corner3.bind();
        GL11.glBegin(GL11.GL_QUADS);
	        GL11.glTexCoord2f(0,0);
	        GL11.glVertex2f(x+width,y+height);
	        GL11.glTexCoord2f(1,0);
	        GL11.glVertex2f(x+width+corner3.getTextureWidth(),y+height);
	        GL11.glTexCoord2f(1,1);
	        GL11.glVertex2f(x+width+corner3.getTextureWidth(),y+height+corner3.getTextureHeight());
	        GL11.glTexCoord2f(0,1);
	        GL11.glVertex2f(x+width,y+height+corner3.getTextureHeight());
        GL11.glEnd();
      //Bottom Right corner
        corner4.bind();
        GL11.glBegin(GL11.GL_QUADS);
	        GL11.glTexCoord2f(0,0);
	        GL11.glVertex2f(x,y+height);
	        GL11.glTexCoord2f(1,0);
	        GL11.glVertex2f(x+corner4.getTextureWidth(),y+height);
	        GL11.glTexCoord2f(1,1);
	        GL11.glVertex2f(x+corner4.getTextureWidth(),y+height+corner4.getTextureHeight());
	        GL11.glTexCoord2f(0,1);
	        GL11.glVertex2f(x,y+height+corner4.getTextureHeight());
        GL11.glEnd();
        
        //Top side
        side1.bind();
        GL11.glBegin(GL11.GL_QUADS);
	        GL11.glTexCoord2f(0,0);
	        GL11.glVertex2f(x+32,y);
	        GL11.glTexCoord2f(1,0);
	        GL11.glVertex2f(x+width+side1.getTextureWidth()-2,y);
	        GL11.glTexCoord2f(1,1);
	        GL11.glVertex2f(x+width+side1.getTextureWidth()-2,y+side1.getTextureHeight());
	        GL11.glTexCoord2f(0,1);
	        GL11.glVertex2f(x+32,y+side1.getTextureHeight());
        GL11.glEnd();
        
      //Right side
        side2.bind();
        GL11.glBegin(GL11.GL_QUADS);
	        GL11.glTexCoord2f(0,0);
	        GL11.glVertex2f(x+width,y+32);
	        GL11.glTexCoord2f(1,0);
	        GL11.glVertex2f(x+width+side2.getTextureWidth(),y+32);
	        GL11.glTexCoord2f(1,1);
	        GL11.glVertex2f(x+width+side2.getTextureWidth(),y+height+side2.getTextureHeight()-2);
	        GL11.glTexCoord2f(0,1);
	        GL11.glVertex2f(x+width,y+height+side2.getTextureHeight()-2);
        GL11.glEnd();
        
      //Bottom side
        side3.bind();
        GL11.glBegin(GL11.GL_QUADS);
	        GL11.glTexCoord2f(0,0);
	        GL11.glVertex2f(x+32,y+height);
	        GL11.glTexCoord2f(1,0);
	        GL11.glVertex2f(x+width+side3.getTextureWidth()-2,y+height);
	        GL11.glTexCoord2f(1,1);
	        GL11.glVertex2f(x+width+side3.getTextureWidth()-2,y+height+side3.getTextureHeight());
	        GL11.glTexCoord2f(0,1);
	        GL11.glVertex2f(x+32,y+height+side3.getTextureHeight());
        GL11.glEnd();
        
        //Left side
        side4.bind();
        GL11.glBegin(GL11.GL_QUADS);
	        GL11.glTexCoord2f(0,0);
	        GL11.glVertex2f(x,y+32);
	        GL11.glTexCoord2f(1,0);
	        GL11.glVertex2f(x+side4.getTextureWidth(),y+32);
	        GL11.glTexCoord2f(1,1);
	        GL11.glVertex2f(x+side4.getTextureWidth(),y+height+side4.getTextureHeight()-2);
	        GL11.glTexCoord2f(0,1);
	        GL11.glVertex2f(x,y+height+side4.getTextureHeight()-2);
        GL11.glEnd();
        
      //center
        wcenter.bind();
        GL11.glBegin(GL11.GL_QUADS);
	        GL11.glTexCoord2f(0,0);
	        GL11.glVertex2f(x+32,y+32);
	        GL11.glTexCoord2f(1,0);
	        GL11.glVertex2f(x+width+wcenter.getTextureWidth()-2,y+32);
	        GL11.glTexCoord2f(1,1);
	        GL11.glVertex2f(x+width+wcenter.getTextureWidth()-2,y+height+wcenter.getTextureHeight()-2);
	        GL11.glTexCoord2f(0,1);
	        GL11.glVertex2f(x+32,y+height+wcenter.getTextureHeight()-2);
        GL11.glEnd();
        
        //glPopMatrix();
        }

	
	private Texture loadTexture(String key){
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
	
}
