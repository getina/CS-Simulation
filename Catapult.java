import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Stationary building that throws projectiles
 * 
 * 
 * @author Ryan Chi
 * @version April 2020
 */
public class Catapult extends Building
{

    int reloadTime = 80; //speed at which it fires at, lower is faster
    int timer = 0; //counter used to fire

    /**
     * Creates a new catapult
     * 
     * @param health how much health catapult has
     * @param team which team its on
     */
    public Catapult (int health, String team)
    {
        this.health = health;
        this.team = team;
    }

    /**
     * sets colour based on team
     * alive when added to world
     */
    public void addedToWorld(World world)
    {
        //loads image based on team
        if (team.equals("red"))
        {
            setImage(new GreenfootImage("catapultRed0.png"));
        }
        else
        {
            setImage(new GreenfootImage("catapultBlue0.png"));
        }
        timer++;
        alive = true;
    }

    /**
     * Act - do whatever the Catapult wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
        if (alive)
        {
            super.act();
            //shoots projectiles constantly after delayed time
            if (timer == reloadTime)
            {
                timer = 0;
                fire();
            }
            else
            {
                //reload animation
                if (team.equals("red"))
                {
                    setImage(new GreenfootImage("catapultRed" + (int) (timer/20) + ".png"));
                }
                else
                {
                    setImage(new GreenfootImage("catapultBlue" + (int) (timer/20) + ".png"));
                }
                timer++;
            }
        }
    }

    /**
     * shoots a projectile
     */
    private void fire()
    {
        ((MyWorld) getWorld()).addObject(new Fireball(5), this.getX(), this.getY());
    }

    /**
     * if building has no health left, remove
     */
    protected void die()
    {
        alive = false;
        ((MyWorld) getWorld()).removeObject(this);

    }
}