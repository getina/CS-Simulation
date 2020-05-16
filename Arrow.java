import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Arrow here.
 * 
 * @author Kevin Lin 
 * @version 1.0
 * <p>
 * png from https://www.pngfind.com/mpng/iTRJm_arrow-png-bow-archery-arrow-clip
 * -art-transparent/
 * </p>
 */
public class Arrow extends Projectile
{
    
    
    final private int timer = 0; 
    /**
     * @param power The amount of power that the arrow is launched with
     * @param theta The angle at which the arrow is launched
     * @param dir The direction of the arrow launched, left (negative int value) or right (positive int value)
     * @param grav The gravitational constant
     */
    public Arrow(int power, int theta, int dir, int grav){
        gravity = grav;
        speed = power;
        angle = theta;
        direction = dir;
        defineV(speed,angle);
        setImg();
    }
    
    /**
     * @param power The amount of power that the arrow is launched with
     * @param theta The angle at which the arrow is launched
     * @param dir The direction of the arrow launched, left (negative int value) or right (positive int value)
     * 
     */
    
    public Arrow(int power, int theta, int dir){
        gravity = 10;
        speed = power;
        angle = theta;
        direction = dir;
        defineV(speed,angle);
        setImg();
    }
    /**
     * @param theta The angle at which the arrow is launched
     * @param dir The direction of the arrow launched, left (negative int value) or right (positive int value)
     */
    public Arrow(int theta, int dir){
        gravity = 10;
        angle = theta;
        speed = 120;
        direction = dir;
        defineV(speed,angle);
        setImg();
    }
    //define x and y axis velocity
    /**
     * @param speed Inherited from the superclass, the initial power of the arrow
     * @param angle Inherited from the superclass, angle at which the arrow is launched
     * 
     * This method sets the x and y axis speed for arrow
     */
    private void defineV(int speed, int angle){
        vX = (double)speed * Math.cos((double)angle*Math.PI/180.0) ;
        vY = -(double)speed * Math.sin((double)angle*Math.PI/180.0);
    }
    public void act(){
        update();
    }
    /**
     * Setting image for the arrow, if the direction is negative set flipped image
     */
    private void setImg(){
        if(direction < 0){
            setImage("arrowFlipped.png");
        }else{
            setImage("arrow.png");
        }
        
        getImage().scale(30,30);
        getImage().rotate((int)(Math.atan(vY/vX) * 180/Math.PI));
    }
    /**
     * This method updates the location and rotation of the arrow
     */
    public void update(){
        double theta = 0;
        theta = Math.atan((vY/2)/vX) * 180/Math.PI;
        if(direction >= 0){
            setLocation(getX() + (int)vX, getY() + (int)vY);
        }else{
            setLocation(getX() - (int)vX, getY() + (int)vY);
        }
        if(getY() > getWorld().getHeight() - 10){
            checkGround();
        }else{
            vY+= gravity;
            if(theta <= 0){
                getImage().rotate(-(int)(theta));
            }else{
                getImage().rotate((int)(theta/1.9));
            }
        }
        
        
    }
    /**
     * If the arrow touches the gound, or the edge of the world, remove the arrow
     */
    public void checkGround(){
        if(this.getY() >= getWorld().getHeight()-10 || this.getX() <= 10 || this.getX() >= getWorld().getWidth()-10){
            remove();
        }
    }
    
    
}
