import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Projectile here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public abstract class Projectile extends Actor
{
    protected int gravity, speed, angle, direction, height;
    protected boolean hit;
    protected double vX, vY;
    /**
     * Act - do whatever the Projectile wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    //check if it hit the ground
    public abstract void checkGround();
    public abstract void update();
    
    public void remove(){
        getWorld().removeObject(this);
    }
    
    
    
}
