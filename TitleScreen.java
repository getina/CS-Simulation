import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class TitleScreen here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class TitleScreen extends World
{
    /**
     * Constructor for objects of class TitleScreen.
     * 
     */
    public TitleScreen()
    {   
        // Create a new world with 600x400 cells with a cell size of 1x1 pixels.
        super(960, 400, 1);
        getBackground().scale(960, 400);
    }

    public void act()
    {
        // Create the game world when user clicks on the title screen
        if(Greenfoot.mouseClicked(null)) {
            Greenfoot.setWorld(new MyWorld());
        }
    }
}
