package md.mclama.GameCo;

import java.awt.Font;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import md.mclama.GameCo.Entities.Entity;
import md.mclama.GameCo.Entities.Player;
import md.mclama.GameCo.Entities.Buildings.Buildings;
import md.mclama.GameCo.Entities.Buildings.SolidWall;
import md.mclama.GameCo.Entities.NPC.NPC;
import md.mclama.GameCo.Entities.NPC.NPClist;
import md.mclama.GameCo.Entities.Resources.Boulder;
import md.mclama.GameCo.Entities.Resources.Stones;
import md.mclama.GameCo.Entities.Resources.Tree;
import md.mclama.GameCo.Entities.Resources.WoodPile;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.openal.AL;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.input.Mouse;
import org.lwjgl.input.Keyboard;
import org.newdawn.slick.Color;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.openal.SoundStore;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

import static org.lwjgl.opengl.GL11.*;

public class GameCo {
	
	public static GameCo Game;
	
	public UnicodeFont font;
	
	//public static final int WIDTH=880, HEIGHT=512; //MC2 res
	public static final int WIDTH=1280, HEIGHT=720;  //720p
	//public static final int WIDTH=1920, HEIGHT=1080;  //1080p
	public static final int GRIDSIZE = 8;
	//public static final int XOFFSET=384;
	
	//protected Fonts font = new Fonts();
	private boolean draw_grid=true;
	
	protected int fps;
	protected int FPS = 60;
	protected int ticks=0;
	protected int gridx, gridy, tilex, tiley;
	protected int xg, yg;
	protected float mx, my;
	protected float timescale=1;
	
	private Player player;
	public float translate_x=-50000;
	public float translate_y=-50000;
	
	NPClist npcl = NPClist.ANIMAL;
	Buildings ding = Buildings.WOODWALLH;
	private boolean placing = false;
	private int placew, placeh, placehx, placehy;
	
	public int invWood=0, woodInWorld=0;
	public int invStones=0, stonesInWorld=0;
	public int treesInWorld=0, bouldersInWorld=0;
	public int invIron=0, ironInWorld=0;
	
	private Texture texture;
	private Texture BackgroundTex;
	private Texture placeTex;
	
	protected List<Entity> Entitys = new ArrayList<Entity>(1024);
	protected Sounds sound = new Sounds();
	
	DecimalFormat df = new DecimalFormat("#.##");
	Random gen = new Random(System.currentTimeMillis());
	
	//Buildings ding = Buildings.WOODWALLH;
	
	private long lastFPS;
	private long lastFrame;
	private int delta;
	
	private long seconds=0;
	
	public Window buyWind;
	public boolean buymenu = false;
	
	public Window conWind;
	protected Console console = new Console();
	
	public GameCo(){ //My initialization code
		initGL();
		initFont();
		lastFPS = getTime();
		
		//if(gamesave = found) loadgame; else newGame();
		newGame();
		
		BackgroundTex = loadTexture("grass");
				
		conWind = new Window(100, 100, 800, 600, this);
		buyWind = new Window(8, 180, 128, 256, this);
		
		//gameLoop();
		while (!Display.isCloseRequested()){
			gameLoop();
		}
		Display.destroy();
        AL.destroy();
        System.exit(0);
	}
	private void gameLoop() {
            glClear(GL_COLOR_BUFFER_BIT);
            //Clear the buffer before we start
            
            delta = getDelta(); //set our delta
            
            
            mx = Mouse.getX()+1 - translate_x;
            my = HEIGHT - Mouse.getY()-1 - translate_y;
            xg = (int) (Math.floor(mx/8)*8); 
			yg = (int) (Math.floor(my/8)*8);
            
           
            input();
            
            //player.tick();
            
            for(int i=Entitys.size()-1; i>=0; i--) {
            	Entity e = Entitys.get(i);
            	e.tick(); // effectively, due to tick
            	if(e instanceof Tree){
            		if(ticks%FPS==0) e.lifetime++;
		        	//if(e.lifetime%90==0){ //Old way
            		if(gen.nextInt((int) (5000*timescale))==0){ //will grow more randomly and not all at the same time
		        		growTree(e);
		        	}
            	}
            	if(e instanceof NPC){
            		if(((NPC) e).hostile){
	            		if(distance(player.x, player.y, e.x, e.y) > 30){
	            			((NPC) e).moveDir(player.x, player.y);
	            			((NPC) e).attacking = true;
	            		}
            		}else { //passive NPC.
	            		boolean canmove=true; //allow moving until we find a reason not to.
	            		for(int i1=Entitys.size()-1; i1>=0; i1--) {
	                    	Entity e1 = Entitys.get(i1);
	                    	//if(e.inBounds((int) e.x+(int) (e.hspeed*(delta*e.speed)),(int) e.y+(int) (e.vspeed*(delta*e.speed)), (int) e1.x, (int) e1.y, e1.imgw, e1.imgh, e1.hitx, e1.hity)){
//	                    	if(e.inBounds(e.x+(e.hspeed*(delta*e.speed)),e.y+(e.vspeed*(delta*e.speed)), e1)){
//	                    		canmove=false;
//	                    		i=0;
//	                    	} //bounds disabled because it causes teleporting instead of moving
	            		}
	            		if(canmove){ //if we can move
	            			int range = 700; //Range of our leash for the animal
	            			if(distance(((NPC) e).spawnx, ((NPC) e).spawny, e.x + (e.hspeed*(delta*e.speed)), e.y + (e.vspeed*(delta*e.speed))) <= range){
		            			e.x += (e.hspeed*(delta*e.speed));
		            			e.y += (e.vspeed*(delta*e.speed));
		            			//e.setX(e.getX() + (e.hspeed*(e.speed*delta)));
		            			//e.y += (e.vspeed*(e.speed*delta));
		            			//e.setY(e.getY() + (e.vspeed*(e.speed*delta)));
	            			}
	            			//else {System.out.println("hit leash limit");}
	            		}
            		} //end of passive NPC
            		((NPC) e).tick(1);
            	}
            }
            
            
            ticks++;
            if(ticks%FPS==0) { //every second do this.
            	//System.out.println(ticks/FPS);
            	seconds++;
            	
            	if(seconds % 30 == 0) {
            		if(woodInWorld <= 2000){ //Don't spawn in any more wood once we have hit this number
	            		for(int i=0; i<40; i++){
	            			int sx, sy;
	            			int attempts = 5; //attempt 5 times to create the resource
	            			int range = 4000; //range of our creation
	            			while(attempts != 0){
			        			sx = gen.nextInt(range);//box around the player that spawns
			        			sy = gen.nextInt(range);
			        			sx += (int) player.getX() - (range/2);
			        			sy += (int) player.getY() - (range/2);
			        			attempts--;
			        			if(!onScreen(sx,sy)){ //Only create if it is off-screen
				        			Entitys.add(new WoodPile(sx,sy, "WoodPile"));
				        			woodInWorld++;
				        			attempts=0;//we successfully created, so stop attempting.
			        			}
	            			}
	            		}
            		}
            		if(stonesInWorld <= 2000){ //Dont spawn in any more until we have reached xxx
	            		for(int i=0; i<15; i++){
	            			int sx, sy;
	            			int attempts = 5; //max attempts at spawning
	            			int range = 4000; //range 
	            			while(attempts != 0){
			        			sx = gen.nextInt(range); //create our x and y
			        			sy = gen.nextInt(range);
			        			sx += (int) player.getX() - (range/2); //around player
			        			sy += (int) player.getY() - (range/2);
			        			attempts--;
			        			if(!onScreen(sx,sy)){
				        			Entitys.add(new Stones(sx,sy, "Stones"));
				        			stonesInWorld++;
				        			attempts=0;
			        			}
	            			}
	            		}
            		}
            		if(bouldersInWorld <= 50){ //Dont spawn in any more until we have reached xxx
	            		for(int i=0; i<4; i++){
	            			int sx, sy;
	            			int attempts = 5; //max attempts at spawning
	            			int range = 5000; //range 
	            			while(attempts != 0){
			        			sx = gen.nextInt(range); //create our x and y
			        			sy = gen.nextInt(range);
			        			sx += (int) player.getX() - (range/2); //around player
			        			sy += (int) player.getY() - (range/2);
			        			attempts--;
			        			if(!onScreen(sx,sy)){
				        			Entitys.add(new Boulder(sx,sy, "Boulder"));
				        			bouldersInWorld++;
				        			attempts=0;
			        			}
	            			}
	            		}
            		}
            	}
//            	if(ticks % (int) (FPS*(90*timescale)) == 0) { //5 minutes
//            		growTree();
//            	}
            }//end of every second
            
            updateFPS();
            render();
            
            Display.update();
            Display.sync(FPS);
	}

	public void input(){
		float speed=player.getSpeed();
		if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
			speed*=2.5;
		}
		speed*=delta;
		if (Keyboard.isKeyDown(Keyboard.KEY_LEFT) || Keyboard.isKeyDown(Keyboard.KEY_A)) {
			if(canMove(player.getX()+(-speed), player.getY())
					|| !canMove(player.getX(), player.getY())){ //If we're already stuck, allow movement
				player.setHspeed(-speed);
				translate_x += speed;
			}
		}
		else if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT) || Keyboard.isKeyDown(Keyboard.KEY_D)) {
			if(canMove(player.getX()+speed, player.getY())
				|| !canMove(player.getX(), player.getY())){ //If we're already stuck, allow movement
				player.setHspeed(speed);
				translate_x += -speed;
			}
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_UP) || Keyboard.isKeyDown(Keyboard.KEY_W)) {
			if(canMove(player.getX(), player.getY()+(-speed))
					|| !canMove(player.getX(), player.getY())){ //If we're already stuck, allow movement
				player.setVspeed(-speed);
				translate_y += speed;
			}
		}
		else if (Keyboard.isKeyDown(Keyboard.KEY_DOWN) || Keyboard.isKeyDown(Keyboard.KEY_S)) {
			if(canMove(player.getX(), player.getY()+speed)
					|| !canMove(player.getX(), player.getY())){ //If we're already stuck, allow movement
				player.setVspeed(speed);
				translate_y += -speed;
			}
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_END)) {
			console.show=true;
		}
		else console.show=false;
		if (Keyboard.isKeyDown(Keyboard.KEY_B)) {
			buymenu=true;
		}
		else buymenu=false;
		
		
		if (Keyboard.isKeyDown(Keyboard.KEY_COMMA)) {
			timescale-=0.005;
			if(timescale <= 0.006f) timescale = 0.006f;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_PERIOD)) {
			timescale+=0.05;
		}
		
		while (Keyboard.next()) {
			
			
			
			
			if(Keyboard.getEventKeyState()){ //PRESSED
				if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
					fixScreen();
				}
			}
		}

		
//		if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)
//                && Mouse.getX() > 0 && Mouse.getX() < (WIDTH-1)) {
//			        translate_x += Mouse.getDX();
//			        translate_y -= Mouse.getDY();    
//		}
//		mx = Mouse.getX()+1 - translate_x;
//        my = HEIGHT - Mouse.getY()-1 - translate_y;
		
		if(Keyboard.isKeyDown(Keyboard.KEY_1)){
			boolean canplace=true; //We can spawn it before we try
			for(int i=Entitys.size()-1; i>=0; i--) { //go through every entity
	        	Entity e = Entitys.get(i);
				if(inBounds(xg, yg, 8, 16, 8, 16, e)){  //if it is in bounds
					i=0; 								//then end the for loop
					canplace=false;					    //and dont allow placement
					//System.out.println("Hit object");
				}
			}
			ding = Buildings.TWIGWALLV;
			placeTex = loadTexture(ding.getTex());
			if (canplace) Entitys.add(new SolidWall(xg, yg, ding));
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_2)){
			boolean canplace=true;
			for(int i=Entitys.size()-1; i>=0; i--) {
	        	Entity e = Entitys.get(i);
				if(inBounds(xg, yg, 16, 8, 16, 8, e)){
					i=0;
					canplace=false;
				}
			}
			ding = Buildings.TWIGWALLH;
			placeTex = loadTexture(ding.getTex());
			if (canplace) Entitys.add(new SolidWall(xg, yg, ding));
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_3)){
			ding = Buildings.WOODWALLH;
			placeTex = loadTexture(ding.getTex());
			placing = true;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_4)){
			ding = Buildings.WOODWALLV;
			placeTex = loadTexture(ding.getTex());
			placing = true;
		}
		
		while (Keyboard.next()) {
			
			if(Keyboard.getEventKeyState()){
				
			}
		}
		
		while (Mouse.next()){
			if(Mouse.isButtonDown(2)){ //middle click
				Entitys.add(new Tree(mx,my, "Tree",gen.nextInt(16)+2));
				woodInWorld++;
				//for(int i=0; i<2000; i++){
				//	Entitys.add(new WoodPile(mx,my, "WoodPile"));
				//}
				console.addLog("Spawned tree at " + mx + "," + my + ".");
			}
			if(Mouse.isButtonDown(0)){ //left click
				translate_x += Mouse.getDX();
		        translate_y -= Mouse.getDY(); 
		        if(placing){
		        	boolean canplace=true; //We can spawn it before we try
					for(int i=Entitys.size()-1; i>=0; i--) { //go through every entity
			        	Entity e = Entitys.get(i);
						if(inBounds(xg, yg, ding.getImgx(), ding.getImgy(), ding.getHitx(), ding.getHity(), e)){  //if it is in bounds
							i=0; 								//then end the for loop
							canplace=false;					    //and dont allow placement
						}
					}
					if (canplace){
						placeDing();
					}
		        }
			}
			if(Mouse.isButtonDown(1)){ //right click
				for(int i=Entitys.size()-1; i>=0; i--) {
		        	Entity e = Entitys.get(i);
					if(inBounds(xg, yg, 8, 16, 8, 16, e)){
						if(e instanceof Player){} //dont delete our precious player!
						else{
							i=0;
							Entitys.remove(e);
						}
					}
				}
				placing=false;
			}
		}
	}
	

	private void placeDing() {
		switch(ding){
			case WOODWALLH:
			case WOODWALLV:
			case TWIGWALLV:
			case TWIGWALLH:
				Entitys.add(new SolidWall(xg, yg, ding));
				break;
		}
	}

	private void fixScreen() {
		translate_x = -(player.x - (WIDTH/2));
		translate_y = -(player.y - (HEIGHT/2));
	}

	private boolean canMove(float x, float y) {
        for(int i=Entitys.size()-1; i>=0; i--) {
        	Entity e = Entitys.get(i);
        	
			if (e instanceof Player){
			}
			else if (distance(x,y, e.x, e.y) <= max(e.hitx, e.hity)*2){ //Only check collision if we are within 2x of its hitbox area
				if(player.inBounds((int) x,(int) y, (int) e.x, (int) e.y, e.imgw, e.imgh, e.hitx, e.hity)){
				//if(player.hitbox.intersects(e.hitbox)){
					if (e instanceof WoodPile){
						invWood++;
						Entitys.remove(e);
						woodInWorld--;
					}
					else if (e instanceof Stones){
						invStones++;
						Entitys.remove(e);
						stonesInWorld--;
					}
					else if (e instanceof Boulder){
						invStones++;
						e.health--;
						knockback(12);
						if(e.health <1){
							Entitys.remove(e);
							bouldersInWorld--;
						}
						return false;
					}
					else if (e instanceof Tree){
						invWood+=2;
						e.size--;
						knockback(12);
						if(e.size <0){
							Entitys.remove(e);
							treesInWorld--;
						}
						//sound.oggHitTree.playAsSoundEffect(1.0f, 1.0f, false);
						return false;
					}
					else return false;
					i=0; //If we hit something, lets stop searching.
				}
        	}
		}
		
		return true;
	}
	
	

	private void knockback(float power) {
		float speed=player.getSpeed();
		if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
			speed*=2.5;
		}
		speed*=delta;
		speed*=power;
		if (Keyboard.isKeyDown(Keyboard.KEY_LEFT) || Keyboard.isKeyDown(Keyboard.KEY_A)) {
			player.setHspeed(speed);
			translate_x += -speed;
		}
		else if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT) || Keyboard.isKeyDown(Keyboard.KEY_D)) {
			player.setHspeed(-speed);
			translate_x += speed;
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_UP) || Keyboard.isKeyDown(Keyboard.KEY_W)) {
			player.setVspeed(speed);
			translate_y += -speed;
		}
		else if (Keyboard.isKeyDown(Keyboard.KEY_DOWN) || Keyboard.isKeyDown(Keyboard.KEY_S)) {
			player.setVspeed(-speed);
			translate_y += speed;
		}
		fixScreen();
	}

	public void render(){
		
		
		glPushMatrix();
		Color.white.bind();
		glTranslatef(translate_x, translate_y, 0);
		glDisable(GL_TEXTURE_2D);
		
		glColor4f(0.1f, 1f, 0.1f, 1f);
		glRecti(0,0, WIDTH, HEIGHT); //Whole background
		
//		glColor4f(0.5f, 0.5f, 0.5f, 1f);
//		glRecti(0,0, XOFFSET, HEIGHT);
//		glRecti(864,0, 880, HEIGHT);
//		glRecti(XOFFSET,0, 880, 16);
//		glRecti(XOFFSET, 496, 880, 512);
		
		
	      	
		for(int xx = -1; xx<(WIDTH/16)+2; xx++){//57,34
			for(int yy = -1; yy<(HEIGHT/16)+2; yy++){
				int xg = (int) Math.floor(((xx*16)-translate_x)/16); 
				int yg = (int) Math.floor(((yy*16)-translate_y)/16);
				drawGrass(xg*16,yg*16);
			}
		}
		int ents_on_screen=0;
		int woodcount=0;
		int stonecount=0;
		int bouldercount=0;
		int treecount=0;
		int NPCcount=0;
		for(Entity e : Entitys){
			if (e instanceof Player){
				//do nothing. render it ontop of everything.
				e.render();
			}
			else if (e instanceof NPC){
				NPCcount++;
				((NPC) e).render(translate_x, translate_y);
				if(onScreen(e)){
					ents_on_screen++;
				}
			}
			else if(onScreen(e)){
				e.render();
				ents_on_screen++;
			}
			if (e instanceof WoodPile){
				woodcount++;
			}
			else if (e instanceof Stones){
				stonecount++;
			}
			else if (e instanceof Boulder){
				bouldercount++;
			}
			else if (e instanceof Tree){
				treecount++;
			}
			
		}
		//player.render();
		
		if(draw_grid){
			//glEnable(GL_TEXTURE_2D);
			//Color.white.bind();
			glColor4f(1f, 0.5f, 0.5f, 1f);
			for(int i = 0; i<(WIDTH/GRIDSIZE); i++){        //WIDTH
				glBegin(GL_LINES);
					glVertex2i((int) translate_x+(i*GRIDSIZE),(int) translate_y); //2d, integer
					glVertex2i((int) translate_x+(i*GRIDSIZE),(int) translate_y+HEIGHT);
				glEnd();
			}
			glBegin(GL_LINES);
			glVertex2i((int) translate_x+100,(int) translate_y+100); //2d, integer
			glVertex2i((int) translate_x+WIDTH,(int) translate_y+100);
		glEnd();
			for(int i = 0; i<(HEIGHT/GRIDSIZE); i++){       //HEIGHT
				glBegin(GL_LINES);
					glVertex2i((int) translate_x,(int) translate_y  + (i*32)); //2d, integer
					glVertex2i((int) translate_x+WIDTH,(int) translate_y  + (i*32));
				glEnd();
			}
			glColor4f(1f, 1f, 1f, 1f);
		}
		
		glColor3f(.75f, 0.5f, 0f);
		glBegin(GL_LINES);
			glVertex2i((int) translate_x,(int) translate_y);
			glVertex2i((int) translate_x,(int) translate_y+32);
			glVertex2i((int) translate_x,(int) translate_y+32);
			glVertex2i((int) translate_x+32,(int) translate_y+32);
			glVertex2i((int) translate_x+32,(int) translate_y+32);
			glVertex2i((int) translate_x+32,(int) translate_y);
			glVertex2i((int) translate_x+32,(int) translate_y);
			glVertex2i((int) translate_x,(int) translate_y);
		glEnd();
		
		if(placing){
			placeTex.bind();
			glColor4f(1,1,1,0.5f); //Transparent
			glBegin(GL_QUADS);
				glTexCoord2f(0,0);
				glVertex2f(xg,yg);
				glTexCoord2f(1,0);
				glVertex2f(xg+placeTex.getImageWidth(),yg);
				glTexCoord2f(1,1);
				glVertex2f(xg+placeTex.getImageWidth(),yg+placeTex.getImageHeight());
				glTexCoord2f(0,1);
				glVertex2f(xg,yg+placeTex.getImageHeight());
			glEnd();
			glColor4f(1,1,1,1); //Visible
		}
		
		
		
		glPopMatrix(); 
		
		if(console.show) conWind.render();
		if(buymenu) buyWind.render();
		
		glEnable(GL_TEXTURE_2D);
		//on-screen text 
		
		font.drawString(0, 52, "Mouse: " + mx + ", " + my, Color.yellow);
		font.drawString(0, 64, "Player: " + (int) player.getX() + ", " + (int) player.getY(), Color.yellow);
		//font.drawString(0, 82, "Wood in hand: " + invWood, Color.yellow);
		font.drawString(0, 82, "Wood: " + woodcount + ", in Hand: " + invWood, Color.yellow);
		font.drawString(0, 94, "Stones: " + stonecount + ", in Hand: " + invStones, Color.yellow);
		font.drawString(0, 106, "Boulders: " + bouldercount, Color.yellow);
		font.drawString(0, 118, "Trees: " + treecount, Color.yellow);
		int xoff = WIDTH-112;
		int yoff = HEIGHT-112;
		font.drawString(0, yoff, "Entities: " + Entitys.size() + " On Screen: " + ents_on_screen, Color.cyan);
		font.drawString(0, yoff+12, "NPC's: " + NPCcount, Color.cyan);
		font.drawString(0, yoff+24, "Timescale: " + timescale, Color.cyan);
		//font.drawString(0, 118, "info: " + Math.abs(translate_x-20) +","+ Math.abs(translate_x + WIDTH + 20), Color.yellow);
		if(console.show){
			for(int i=0; i<console.CSIZE; i++){
				font.drawString(conWind.x+32, conWind.y+conWind.height-(i*14)-14, console.prelog[i], console.precolor[i]);
				font.drawString(conWind.x+96, conWind.y+conWind.height-(i*14)-14, console.postlog[i], console.postcolor[i]);
			}
		}
	}
	
	protected float distance(float x1, float y1, float x2, float y2){
		return (float) Math.sqrt((x1-x2)*(x1-x2) + (y1-y2)*(y1-y2));
	}
	
	private boolean onScreen(Entity e) {
		if(e.x >= Math.abs(translate_x)-20 && e.x <= Math.abs(translate_x) + WIDTH + 20
				&& e.y >= Math.abs(translate_y)-20 && e.y <= Math.abs(translate_y) + HEIGHT + 20 + (e.size*16)){
			return true;
		}
		return false;
	}

	private boolean onScreen(float x, float y) {
		if(x >= Math.abs(translate_x)-20 && x <= Math.abs(translate_x) + WIDTH + 20
				&& y >= Math.abs(translate_y)-20 && y <= Math.abs(translate_y) + HEIGHT + 20){
			return true;
		}
		return false;
	}

	private void drawGrass(int x, int y) {
		BackgroundTex.bind();
		glColor3f(1,1,1); //White
		glBegin(GL_QUADS);
			glTexCoord2f(0,0);
			glVertex2i(x,y);
			glTexCoord2f(1,0);
			glVertex2i(x+16,y);
			glTexCoord2f(1,1);
			glVertex2i(x+16,y+16);
			glTexCoord2f(0,1);
			glVertex2i(x,y+16);
		glEnd();
	}

	private void initGL() {
		try {
			Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
			Display.setTitle("GameCo");
			Display.create();
			Display.setVSyncEnabled(true);
		} catch (LWJGLException e) {
			e.printStackTrace();
			Display.destroy();
			System.exit(0);
		}
		
		
		
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(0,WIDTH,HEIGHT, 0, 1, -1);
		glMatrixMode(GL_MODELVIEW);
		
		glEnable(GL_TEXTURE_2D);
		glShadeModel(GL_SMOOTH);
		glDisable(GL_DEPTH_TEST);
		glDisable(GL_LIGHTING);
		
		glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		glClearDepth(1);
		
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		
		glViewport(0,0,WIDTH,HEIGHT);
		glMatrixMode(GL_MODELVIEW);
	}
	
	public int min(int n1, int n2){
		if ( n1>n2) return n2;
		else return n1;
	}
	public int max(int n1, int n2){
		if ( n1>n2) return n1;
		else return n2;
	}

	public static void main(String[] args) {
		Game = new GameCo();
	}
	
	private void initFont() {
		font = new UnicodeFont(new java.awt.Font ("Vani", Font.BOLD, 12));
		font.getEffects().add(new ColorEffect(java.awt.Color.white));
		font.addNeheGlyphs();
		try {
			font.loadGlyphs();
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
	
	public long getTime() {
	    return (Sys.getTime() * 1000) / Sys.getTimerResolution();
	}

	public int getDelta() {
	    long time = getTime();
	    int delta = (int) (time - lastFrame);
	    lastFrame = time;
	 
	    return delta;
	}
	
	public void updateFPS() {
	    if (getTime() - lastFPS > 1000) {
	        Display.setTitle("GameCo .... FPS: " + fps);
		fps = 0;
		lastFPS += 1000;
	    }
	    fps++;
	}
	//int ex, int ey, int picw, int pich,int hx, int hy
	public boolean inBounds(int x, int y, int imgw, int imgh, int hitx, int hity, Entity e){
		int ex = (int) e.x;
		int ey = (int) e.y;
		int picw = e.imgw;
		int pich = e.imgh;
		int hx = e.hitx;
		int hy = e.hity;
		
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
		if(xl+1 >= exl && xl+1 <= exr && yu+1 >= eyu && yu+1 <= eyd) return true;//topleft
		if(xr-1 >= exl && xr-1 <= exr && yu+1 >= eyu && yu+1 <= eyd) return true;//topright
		if(xl+1 >= exl && xl+1 <= exr && yd-1 >= eyu && yd-1 <= eyd) return true;//botleft
		if(xr-1 >= exl && xr-1 <= exr && yd-1 >= eyu && yd-1 <= eyd) return true;//botright
		//sides
		if(xc >= exl && xc <= exr-1 && yc >= eyu+1 && yc <= eyd-1) return true;//CENTER
		//if(xc >= exl && xc <= exr-1 && yd >= eyu+1 && yd <= eyd-1) return true;//BOT
		//if(xl >= exl && xl <= exr-1 && yc >= eyu+1 && yc <= eyd-1) return true;//left
		//if(xr >= exl && xr <= exr-1 && yc >= eyu+1 && yc <= eyd-1) return true;//right\
		return false;
	}
	
	private void newGame(){
		Entitys.add(player = new Player(50000+(WIDTH/2),50000+(HEIGHT/2)));
		Entitys.add(new NPC(50500, 50500, npcl));
		
		for(int i=0; i<gen.nextInt(10)+15; i++){
			Entitys.add(new WoodPile(gen.nextInt(WIDTH-16)+50008,gen.nextInt(HEIGHT-16)+50008, "WoodPile"));
			woodInWorld++;
		}
		for(int i=0; i<gen.nextInt(5)+5; i++){
			Entitys.add(new Stones(gen.nextInt(WIDTH-16)+50008,gen.nextInt(HEIGHT-16)+50008, "Stones"));
			woodInWorld++;
		}
		for(int i=0; i<gen.nextInt(5)+5; i++){
			Entitys.add(new Boulder(gen.nextInt(WIDTH-16)+50008,gen.nextInt(HEIGHT-16)+50008, "Boulder"));
			bouldersInWorld++;
		}
		for(int i=0; i<gen.nextInt(30)+2; i++){
			Entitys.add(new Tree(gen.nextInt(6000)+47000,gen.nextInt(6000)+47000, "treeTrunk", gen.nextInt(8)+2));
			treesInWorld++;
		}
		console.addInfo("New game loaded");
	}
	
	private void growTree(Entity e) {
		e.size++; //The tree grew!! yay!
		if(e.size > 10){
			if(gen.nextInt(max((int) (10-(e.size-10)),1))==0 || e.size>=20){ //after the tree is size 10, chance to break
				for(int j=0; j<gen.nextInt(2)+1; j++){
					int attempts = 5; //atempts at spawning tree
					while (attempts!=0){
						boolean createTree = true;
						int range=250; //range of spawning tree
						float xx=e.x-(range/2) + gen.nextInt(range); //set area around previous tree
						float yy=e.y-(range/2) + gen.nextInt(range);
						for(int k=Entitys.size()-1; k>=0; k--) { //check every other tree
				        	Entity e1 = Entitys.get(k);
				        	if (e1 instanceof Tree){ //check every other tree
        						if(distance(xx, yy, e1.x, e1.y) <= 35){ 
        							createTree = false; //if too close, dont create tree.
        						}
    						}
			        	}//Once we're done checking all the other tree's
						if(createTree){
							Entitys.add(new Tree(xx,yy, "treeTrunk", 0));
							treesInWorld++;
							attempts=0;
							console.addLog("Created tree at: " + xx + "," + yy);
			        	} 
						else { //If we failed to create the tree
							attempts--;
							console.addWarn("Failed attempt at spawning tree..." + attempts);
						}
					}
				}//Failed or created, delete the tree that died.
				Entitys.remove(e);
			}
		}
		e.lifetime++;
	}
	
	public Texture loadTexture(String key){
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
