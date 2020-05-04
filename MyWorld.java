import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class MyWorld here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class MyWorld extends World
{  
    // defines objects in the world 
    private ScoreBar scoreBar; 
    private GreenfootImage bg;
   
    // numerical variables for graphics of world
    private static int x = 0, y = 0;
    private static int width = 960;
    private static int height = 400;
    private int maxWidth = 740;
    
    // variables for mechanics of world
    private boolean moveRight = true;
    private boolean begin = false;
    private boolean stop = false;
    
    // Variables for timing of program
    private int count = 0;
    private long endTime;
    private int moveTime = 35 * 1000; // interval of times that world moves
    
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
        
        scoreBar = new ScoreBar (1500);
        addObject(scoreBar, 400, 15);
        
        addObject(castle, 200, 300);
        addObject(catapult, 500, 300);
    }
    
    public void act()
    {
        if(!begin){
            changeEndTime();
            begin = true;
        }
        
        decideWorld();
      
        if (Greenfoot.isKeyDown("a"))
        {
            castle.hit(10);
            catapult.hit(10);
        }    
        
        scoreBar.update(0, 0, 0, 0, 0, 0);
    }
    
    /**
     * Decides if world should move left or right. Will loop
     */
    public void decideWorld()
    {
        if(!stop){
            count++;
         
            // determines which direction the world should move
            if (x == maxWidth) moveRight = false;
            if (x < 0) moveRight = true;
            
            // determines how fast the world will move
            if(count % 10 == 0){
                if (!moveRight) moveWorld(-2, 0);
                else if(moveRight) moveWorld(2, 0);
            }
            
            // Stops program and resets time values
            if(System.currentTimeMillis() > endTime){
                stop = true;
                changeEndTime();
            }
        }   
          
        // Waits for predetermined time, then allows world to move again
        if(stop && System.currentTimeMillis() > endTime){
            stop = false;              
            changeEndTime();  
        } 
    }
    
    public void changeEndTime()
    {
        endTime = System.currentTimeMillis() + moveTime;   
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
