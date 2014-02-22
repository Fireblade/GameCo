package md.mclama.GameCo.Entities.NPC;

public enum NPClist {
	ANIMAL("wall_h", 16, 16, 16, 16, 40, 1, 0.07f, false);


	private String tex;
	private final int hitx;
	private final int hity;
	private final int imgx;
	private final int imgy;
	private final int health;
	private float size;
	private float speed;
	private boolean hostile;
	
	private NPClist(String texture, int hitx, int hity, int imgx, int imgy, int health, float size, float speed, boolean hostile){
		tex = texture;
		this.hitx = hitx;
		this.hity = hity;
		this.imgx = imgx;
		this.imgy = imgy;
		this.health = health;
		this.size = size;
		this.speed = speed;
		this.hostile = hostile;
	}	
	
	
	public String getTex() {
		return tex;
	}

	public int getHitx() {
		return hitx;
	}

	public int getHity() {
		return hity;
	}

	public int getImgx() {
		return imgx;
	}

	public int getImgy() {
		return imgy;
	}

	public int getHealth() {
		return health;
	}

	public float getSpeed() {
		return speed;
	}

	public boolean isHostile() {
		return hostile;
	}
	
	public float getSize() {
		return size;
	}
}
