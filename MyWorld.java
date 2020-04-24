import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class MyWorld here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class MyWorld extends World
{  
    // sets values of world
    private GreenfootImage bg;
    private int count = 0;
    private static int x = 0, y = 0;
    private static int width = 960;
    private static int height = 400;
    private int maxWidth = 740;
    private boolean moveRight = true;
    
    Castle castle = new Castle(50, "blue");
    Catapult catapult = new Catapult(50, "blue");
    
    /**
     * Constructor for objects of class MyWorld.
     */
    public MyWorld()
    {    
        // Create a new world with 600x400 cells with a cell size of 1x1 pixels.
        super(960, 400, 1); 
        
        bg = getBackground();
        moveWorld(0, -140);
        
        prepare();   
    }
    
    /**
     * Prepare the world for the start of the program.
     * That is: create the initial objects and add them to the world.
     */
    private void prepare()
    {
        Fireball fireball = new Fireball(60, 30, 0);
        addObject(fireball,100,314);
        Arrow arrow = new Arrow(60, 30, 0);
        addObject(arrow,167,216);
        
        addObject(castle, 200, 300);
        addObject(catapult, 500, 300);
    }
    
    public void act()
    {
        count++;
        decideWorld();
      
        if (Greenfoot.isKeyDown("a"))
        {
            castle.hit(10);
            catapult.hit(10);
        }    
    }
    
    /**
     * Decides if world should move left or right
     */
    public void decideWorld()
    {
        if (x == maxWidth) moveRight = false;
        if (x < 0) moveRight = true;
        
        if(count % 10 == 0){
            if (!moveRight){
                moveWorld(-2, 0);
            }else if(moveRight){
                moveWorld(2, 0);
            }
        }
    }
    
    /**
     * Moves the world left/right.
     */
    public void moveWorld(int dx, int dy)
    {
        x += dx;
        y += dy;

        GreenfootImage mask = new GreenfootImage(width, height);
        mask.drawImage(bg, -x, y);
        setBackground(mask); 
    }  
    
    /**
     * Moves all objects in the screen left/right with the world
     */
    public void moveXValue(int dx)
    {
        
    }
    
    public void teamRedWins()
    {
        
    }   
}
