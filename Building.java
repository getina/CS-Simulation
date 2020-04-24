import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Buildings are objects that are stationary but have healt
 * once health reaches 0, buildings are destroyed
 * 
 * @author Ryan
 * @version April 2020
 */
public abstract class Building extends Actor
{

    protected int health; //how much health a building has
    protected String team; //team "red" or "blue"
    protected boolean alive = false;

    /**
     * Act - do whatever the Building wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
        if (alive)
        {
            checkHitProjectile();
        }
    }

    /**
     * checks if hit by projectile and takes damage accordingly
     */
    protected void checkHitProjectile()
    {
        if (this.isTouching(Fireball.class))
        {
            this.hit(4);
        }

        if (this.isTouching(Arrow.class))
        {
            this.hit(1);
        }

    }

    /**
     * makes the building take damage from an attack
     * @param damage how much damage an attack deals
     */
    public void hit(int damage)
    {
        if (alive)
        {
            health -= damage;
            if (health < 0) //if health less than 0, building dies
            {
                die();
            }
        }
    }

    /**
     * if building has no health left, remove
     */
    protected abstract void die();

    public String getTeam()
    {
        if (alive)
        {
            return team;
        }
        else
        {
            return null;
        }
    }
}