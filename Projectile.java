import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Projectile here.
 * 
 * @author Kevin Lin
 * @version 1.0
 * 
 */
public abstract class Projectile extends Actor
{
    /**
    * Variables stored for calculation purposes
    */
    protected int gravity, speed, angle, direction, height;
    /**
     * Variables stored for calculation purposes
     */
    protected double vX, vY;
    
    
    
    /**
     * Makes sure every subclass has a method called checkGround().
     */
    //check if it hit the ground
    public abstract void checkGround();
    /**
     * Makes sure every subclass has a update() method.
     */
    public abstract void update();
    /**
     * removes the object from the world.
     */
    public void remove(){
        getWorld().removeObject(this);
    }
}
