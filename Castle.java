import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Castle are homebases for each team
 * Once health reaches 0, team loses
 * 
 * @author Ryan 
 * @version April 2020
 */
public class Castle extends Building
{

    /**
     * Creates a new Castle
     * 
     * @param health how much health castle has
     * @param team which team its on
     */
    public Castle (int health, String team)
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
        if (team.equals("red"))
        {
            setImage(new GreenfootImage("castleRed.png"));
        }
        else
        {
            setImage(new GreenfootImage("castleBlue.png"));
        }
        alive = true;
    }
    /**
     * Act - do whatever the Castle wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
        super.act();
    }

    /**
     * if building has no health left, other team wins
     */
    protected void die()
    {
        if (this.team.equals("red"))
        {
            //((MyWorld) getWorld()).teamBlueWins();
        }
        else
        {
            ((MyWorld) getWorld()).teamRedWins();
        }
        alive = false;
    }
}