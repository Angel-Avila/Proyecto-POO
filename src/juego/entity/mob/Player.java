package juego.entity.mob;

import juego.Game;
import juego.entity.projectile.Projectile;
import juego.entity.projectile.WizardProjectile;
import juego.graphics.Screen;
import juego.graphics.Sprite;
import juego.input.Keyboard;
import juego.input.Mouse;

public class Player extends Mob{
    
    private Keyboard input;
    private Sprite sprite;
    private int anim = 0;
    private boolean walking = false;
    
    private int fireRate = 0;
    
    public Player(Keyboard input){
        this.input = input;
        sprite = Sprite.player_forward;
    }
    
    public Player(int x, int y, Keyboard input){
        this.x = x;
        this.y = y;
        this.input = input;
        dir = 2;
        sprite = Sprite.player_forward;
        fireRate = WizardProjectile.FIRE_RATE;
    }
    
    public void update(){
        int xa = 0, ya = 0;
        if(anim < 7500) anim++;
        else anim = 0;
        if(input.up) ya-=2;
        if(input.down) ya+=2;
        if(input.left) xa-=2;
        if(input.right) xa+=2;
        
        if(xa != 0 || ya != 0) {
            move(xa, ya);
            walking = true;
        }
        else
            walking = false;
        
        // Takes 1 from firerate because the player only shoots in the updateShooting() method if fireRate is <= 0
        // and after shooting it resets the fireRate int value to the declared FIRE_RATE value in WizardProjectile
        if(fireRate > 0) fireRate--;
        
        updateShooting();
    }
    
	private void updateShooting() {	
		// If the player is clicking the left click and the fireRate is <= 0 
    	if(Mouse.getB() == 1 && fireRate <= 0){
    		// Gets the x and y coordinates from where the user wants to shoot relative to the window, not the map
    		double dx = Mouse.getX() - Game.getWindowWidth() / 2;
    		double dy = Mouse.getY() - Game.getWindowHeight() / 2;
    		// Uses the atan function of y / x to get the angle
    		double dir = Math.atan2(dy, dx);
    		// Shoots to the desired direction
    		shoot(x, y, dir);
    		// Resets the fireRate so it can shoot again
    		fireRate = WizardProjectile.FIRE_RATE;
    	}
	}

	// The anim % 20 > 10 is just an animation to alternate between the 2 walking sprites, one with the right leg
	// and one with the left leg
	public void render(Screen screen){
        if(dir == 0) {
            sprite = Sprite.player_backward;
            if(walking){
                if(anim % 20 > 10) sprite = Sprite.player_backward_1;
                else sprite = Sprite.player_backward_2; 
            }
        }
        if(dir == 1) {
            sprite = Sprite.player_right;
            if(walking){
                if(anim % 20 > 10) sprite = Sprite.player_right_1;
                else sprite = Sprite.player_right_2; 
            }
        }
        if(dir == 2) {
            sprite = Sprite.player_forward;
            if(walking){
                if(anim % 20 > 10) sprite = Sprite.player_forward_1;
                else sprite = Sprite.player_forward_2; 
            }
        }
        if(dir == 3) {
            sprite = Sprite.player_left;
            if(walking){
                if(anim % 20 > 10) sprite = Sprite.player_left_1;
                else sprite = Sprite.player_left_2; 
            }
        }
        
        // Renders the player with a slight offset
        screen.renderPlayer(x - 8, y - 16, sprite);        
    }
}