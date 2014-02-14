package md.mclama.GameCo.Entities.Buildings;

public enum Buildings {
	TWIGWALLH("wood_pile_wall_h", 16, 8, 16, 8, 40),
	TWIGWALLV("wood_pile_wall_v", 8, 16, 8, 16, 40),
	WOODWALLH("wall_h", 16, 8, 16, 8, 80),
	WOODWALLV("wall_v", 8, 16, 8, 16, 80);
	
	private final String tex;
	private final int hitx;
	private final int hity;
	private final int imgx;
	private final int imgy;
	private final int health;
	
	private Buildings(String texture, int hitx, int hity, int imgx, int imgy, int health){
		tex = texture;
		this.hitx = hitx;
		this.hity = hity;
		this.imgx = imgx;
		this.imgy = imgy;
		this.health = health;
	}
	
	public int getHealth() {
		return health;
	}

	public String getTex(){
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
}
