package md.mclama.GameCo.Entities.Resources;

import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glClearDepth;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glVertex2f;

import org.newdawn.slick.opengl.Texture;

import md.mclama.GameCo.Entities.Entity;

public class Tree extends Entity {
	
	private Texture tree;

	public Tree(float x, float y, String tex, int size) {
		super(x, y, tex, 8, 16, 8, 16, 0, size);
		tree = loadTexture("tree");
		//System.out.println("Size of tree: " + size);
	}
	
	public void tick(){
		//lifetime++;
		//if(lifetime%90==0){
		//	
		//}
	}
	

	public void render(){
		texture.bind();
		glColor3f(1,1,1); //White
		glBegin(GL_QUADS);	
			glTexCoord2f(0,0); //draw the tree trunk
			glVertex2f(x,y);
			glTexCoord2f(1,0);
			glVertex2f(x+texture.getImageWidth(),y);
			glTexCoord2f(1,1);
			glVertex2f(x+texture.getImageWidth(),y+texture.getImageHeight());
			glTexCoord2f(0,1);
			glVertex2f(x,y+texture.getImageHeight());						
		glEnd();
		
		tree.bind();
		glBegin(GL_QUADS);	
		for(int i=1; i<size+1; i++){ //Draw the tree
			glTexCoord2f(0,0);
			glVertex2f(x,y-(i*16));
			glTexCoord2f(1,0);
			glVertex2f(x+tree.getImageWidth(),y-(i*16));
			glTexCoord2f(1,1);
			glVertex2f(x+tree.getImageWidth(),y+tree.getImageHeight()-(i*16));
			glTexCoord2f(0,1);
			glVertex2f(x,y+tree.getImageHeight()-(i*16));
		}
		glEnd();
	}
}
