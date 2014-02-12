package md.mclama.GameCo;

import java.awt.Rectangle;
import java.io.IOException;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

//Class thanks to griffy100 from stackoverflow
public class Button {

    public int X;
    public int Y;
    public Texture buttonTexture;
    public boolean isClicked=false;
    Rectangle bounds = new Rectangle();


    public void addButton(int x, int y , String TEXPATH){
        X=x;
        Y=y;
        try {
            buttonTexture = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream(TEXPATH));
            System.out.println(buttonTexture.getTextureID());
        } catch (IOException e) {
            e.printStackTrace();
        }
        bounds.x=X;
        bounds.y=Y;
        bounds.height=buttonTexture.getImageHeight();
        bounds.width=buttonTexture.getImageWidth();
        System.out.println("[BUTTON CREATE]"+bounds.x+" "+bounds.y+" "+bounds.width+" "+bounds.height);
    }

    public void Draw(){
        if(bounds.contains(Mouse.getX(),(600 - Mouse.getY()))&&Mouse.isButtonDown(0)){
            isClicked=true;
        }else{
            isClicked=false;
        }
        Color.white.bind();
        buttonTexture.bind(); // or GL11.glBind(texture.getTextureID());

        GL11.glBegin(GL11.GL_QUADS);
            GL11.glTexCoord2f(0,0);
            GL11.glVertex2f(X,Y);
            GL11.glTexCoord2f(1,0);
            GL11.glVertex2f(X+buttonTexture.getTextureWidth(),Y);
            GL11.glTexCoord2f(1,1);
            GL11.glVertex2f(X+buttonTexture.getTextureWidth(),Y+buttonTexture.getTextureHeight());
            GL11.glTexCoord2f(0,1);
            GL11.glVertex2f(X,Y+buttonTexture.getTextureHeight());
        GL11.glEnd();
        }

    }
