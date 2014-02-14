package md.mclama.GameCo.Entities;

public class Player extends Entity {

	public Player(int x, int y) {
		super(x,y,"water", 16, 16, 16, 16, 100, 0);
	}
	
	public void tick(){
		x += hspeed;
		y += vspeed;
		hspeed=0;
		vspeed=0;
		hitbox.x = (int) x;
		hitbox.y = (int) y;
	}

}
