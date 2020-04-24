import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Soldier here.
 * 
 * @author (Prayan Jegathees) 
 * @version (0.1)
 */
public abstract class Soldier extends Actor
{
    /**
     * Private variables used for all soldier subclasses
     *      stats needed to determine the overall stats 
     *      of the character
     *      
     * LIFE is a boolean meant to change & detect the actors "death" 
     * TEAM is a string meant to choose which alliance the actor belongs to 
     */
    private int health;
    private int speed;
    private int atkPower;
    private int atkRate;
    
    private boolean life;
    private String team;
       
    /**
     * Constructor for all Soldiers, 
     */
    
    public Soldier(int hp, int spd, int aPwr, int aRt, String alliance)
    {
        health = hp;
        speed = spd;
        atkPower = aPwr;
        atkRate = aRt;
        
        alliance = team;
        life = true;
    }
    
    /**
     * Attack - An abstract method that is called when the unit subclass 
     *          wants to deal damage to an enemy unit
     */
    public abstract void attack();
    
    /**
     * takeDamage - When soldier subunits are hit by projectiles and lose health
     *              (amount taken varies based on the type of projectile)
     */
    public void takeDamage()
    {
        /*
        if (isTouching(PROJECTILE_ARROW.class))
        {
            health -= 2;
        }
        if (isTouching(PROJECTILE_FIREBL.class))
        {
            health -= 5;
        }
        */
        if (health == 0)
        {
            life = false;
        }
    }
    
    public void die()
    {
        if (!life)
        {
            move(0);
            getImage().setTransparency(50);
            setRotation(90);
        }
    }
}
