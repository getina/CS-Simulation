import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Arrow here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 * 
 * png from https://www.pngfind.com/mpng/iTRJm_arrow-png-bow-archery-arrow-clip
 * -art-transparent/
 */
public class Arrow extends Projectile
{
    /**
     * Act - do whatever the Arrow wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    
    final private int timer = 0; 
    public Arrow(int power, int theta, int dir, int grav){
        gravity = grav;
        speed = power;
        angle = theta;
        direction = dir;
        defineV(speed,angle);
        setImg();
    }
    
    public Arrow(int power, int theta, int dir){
        gravity = 10;
        speed = power;
        angle = theta;
        direction = dir;
        defineV(speed,angle);
        setImg();
    }
    
    public Arrow(int theta, int dir){
        gravity = 10;
        angle = theta;
        speed = 120;
        direction = dir;
        defineV(speed,angle);
        setImg();
    }
    //define x and y axis velocity
    private void defineV(int speed, int angle){
        vX = (double)speed * Math.cos((double)angle*Math.PI/180.0) ;
        vY = -(double)speed * Math.sin((double)angle*Math.PI/180.0);
    }
    
    public void act() 
    {
        update();
    }    
    
    private void setImg(){
        setImage("arrow.png");
        getImage().scale(30,30);
        getImage().rotate((int)(Math.atan(vY/vX) * 180/Math.PI));
    }
    //update method with rotating arrow
    public void update(){
        double theta = 0;
        theta = Math.atan((vY/2)/vX) * 180/Math.PI;
        if(direction >= 0){
            setLocation(getX() + (int)vX, getY() + (int)vY);
        }else{
            setLocation(getX() - (int)vX, getY() + (int)vY);
        }
        if(getY() > getWorld().getHeight() - 10){
            vX = 0;
            vY = 0;
            remove();
        }else{
            vY+= gravity;
            if(theta <= 0){
                getImage().rotate(-(int)(theta));
            }else{
                getImage().rotate((int)(theta/1.9));
            }
        }
    }
    
    public void checkGround(){
        if(this.getY() >= getWorld().getHeight()-10){
            remove();
        }
    }
    
    
}
