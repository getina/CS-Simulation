
import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.HashMap;
/**
 * The Animated Character superclass is designed to help manage animations for 2d sprites
 * in Greenfoot. It implements TIME-BASED (rather than Greenfoot act-based) 
 * frame changing and moving to create both smoothness and preciseness. 
 * <p>
 * If you have any questions, please email me:
 * <p>
 * <a href="mailto:jordan.cohen@yrdsb.ca">jordan.cohen@yrdsb.ca</a>
 * <p>
 * Inherit from this class to use it. See Player as an example of how to use it with
 * player controls. Enemy demonstrates how this can be applied to a computer controlled
 * character.
 * </p>
 * <p>Please know that I am a teacher, not a developer. I'm creating this in my spare time,
 * and as it grows in complexity, bugs are sure to arise. If you find any, please help me by 
 * sending me detailed reports! If you have ideas, you are welcome to send me constructive
 * feedback and suggestions.</p>
 * <p>My code is yours to use, for free, for any purpose. I only ask that you credit me
 * in your credits.</p>
 * Features:<ul>
 * <li>Directional Movement</li>
 * <li>Click-to-move (move toward point, then stop)</li>
 * <li>Animation and movement based on time, not acts, so you should see consistent
 *     rates of movement between Actors even if your game and your CPU cannot maintain
 *     a smooth framerate.</li>
 *     </li></ul>
 *     <p>
 *     OPTIONAL include this method if you want to be notified when an animation is complete: </p>
 *       <pre>   protected void terminalAnimationComplete(){
 *              attacking = false;
 *          }</pre>
 * 
 * <h2>Version Notes</h2>
 * 
 * <p><b>v0.6.0:</b> (January 2020)</p>
 * <ul>
 *  <li>Now uses a Hashmap to organize named animations</li>
 *  <li> Now divides animations into two types:<ol>
 *  <li>Primary Animation: Usually movement - this is the base animation.</li>
 *  <li>Terminal Animations - animations that run, then end, and return to the primary animation,
 *                  or remove itself from the World as specified</li>
 * </ul>
 * 
 * <p><b>v0.8.0:</b> (April 2020)</p>
 * <ul>
 *  <li> Import code is completely revamped, now imports spritesheets direction, not 
 *  individual images.</li>
 *  <li> Adding an optional layering system.</li>
 *  <li> Adding the result of many lessons learned from Greentree Chronicles code</li>
 *  <li> Trying to move as much functionality as possible out of the subclasses and into here</li>
 * </ul>
 * 
 * <p><b>v0.9.0:</b> (April 2020)</p>
 * <ul>
 *  <li> Collision system implemented. SimplePlayer still works without it, ComplexPlayer now uses it.</li>
 *  <li> Continuing small improvements</li>
 * </ul> 
 * 
 * <p><b>v1.0.1 (alpha) </b></p>
 * <ul>
 *  <li> Cleaned up, big time!</li>
 *  <li> FIXED layers can now be removed / reset.
 *  <li> Basic bug testing - please give feedback if you encounter bugs!</li>
 * </ul> 
 * 
 * <h2> To-Do: </h2>
 * <ul>
 *  <li> Remove xx, yy, Dot class, and replace with Coordinate and internal rotation variable</li>
 *  <li> Implement built-in collisions within the movement methods, on or off based on collisionEnabled </li>
 *  <li> Stepback function to be improved along with the removal of the Dot</li>
 *  <li> Consider combining stopMoving and stepBack(Actor)</li>
 *  
 *  <ul> Improve instructions</li>
 * </ul>
 *  
 * 
 * @author Mr. Jordan Cohen, PETHS
 * @version 1.0.1a - April 2020
 */

public abstract class AnimatedCharacter extends Actor
{
    // Change this if you need more layers for your sprite sheet
    public final static int MAX_LAYERS = 12;

    // ========== Protected Variables,  for sublcasses to share ===
    // Many of these will be abstracted away from the subclass in later versions
    protected HashMap<String, Animation> animations;

    // The primary animation is generally movement - it's what gets
    // defaulted to when no terminal animation is playing
    protected Animation primaryAnimation;
    protected Animation currentAnimation;

    protected Direction direction; // Direction is 0-Right, 1-Left, 2-Up, 3-Down
    // And is based on a public Enum below

    // ======== Private variables ========
    private double framesPerSecond;   // animation speed
    private double secondsPerFrame;   // calculated fraction of second per frame

    private double tolerance;         // how close to target to stop
    private double maxFrameLength;    // Used to avoid "jumping" if lag or GF pause.
    // What fraction of a second is max dead time?

    private boolean autoMove;  // determine if currently moving 

    private boolean collisionEnabled; // collision system is optional

    private int frame;          // current frame counter
    private double xx, yy;      // internal, double representation of coordinates
    private int dirX, dirY;     // variables used to control direction
    private int prevX, prevY;   // previous rounded X and Y values
    private boolean idle;       // used to specify idle frame
    private boolean stopAtEnd;  // is this a TERMINAL animation?
    private boolean removeAfterAnimation; // After stopAtEnd animation completes, remove myself?

    private double moveSpeed;   // how many pixels per SECOND (not per act)

    private int repeats;        // Determines how many times a terminal animation repeates before
    // defaulting back to the primary animation

    // Current set of images, loaded from one of the above arrays.
    // This is one dimension of an Animation, and will be cycled
    // through in the animation code.
    private GreenfootImage[] currentImages; 
    private GreenfootImage[] spriteSheetLayers;
    private GreenfootImage spriteSheet;
    private Collider myCollider;

    private Dot dot;                // dot used for direction / turnTowards method
    private Dot targetDot;          // extra Dot used as target for the dot

    // for time-keeping to keep animation going at consistent speed
    private long lastFrame;         // Keep track of when the last animation was updated
    private long current;           // Keep track of time between frames for movement
    private long elapsed;           // how long a frame was

    /**
     * Default constructor - must be called via super() in your object's Constructor.
     */
    public AnimatedCharacter (){
        animations = new HashMap<String, Animation>();

        // Set initial direction and speed. Override this in your constructor.
        direction = Direction.RIGHT;
        changeSpeed (15, 20);

        // Start values for private variables
        frame = 0;
        dirX = 0;
        dirY = 0;
        idle = false;
        autoMove = false;
        stopAtEnd = false;

        collisionEnabled = false;

        // VALUES FOR ANIMATION 
        tolerance = 0.5; // See superclass for description
        maxFrameLength = 0.10;

        // How close, in pixels, should character have to get to it's intended
        // target in clickToMove mode before movement is automatically ended
        tolerance = 2.0;

        // In case program is too laggy, this determines the max time per frame.
        // In other words, if a frame takes longer than maxFrameLength to render,
        // or the program is paused for more time, the animation will place the
        // object at the point at the distance that would have been covered in
        // maxFrameLength time.
        maxFrameLength = 1/30.0; // aboutn 1/60th of a second, 60 FPS  

        // Spawn my hidden friend dot, which I can follow:
        dot = new Dot ();

        spriteSheetLayers = new GreenfootImage[MAX_LAYERS];

        // Set the initial timestamp for animation timer
        lastFrame = System.nanoTime();

    }

    public void addedToWorld (World w){
        // When I get added to world, set my internal double variables 
        xx = getX();
        yy = getY();
        prevX = getX();
        prevY = getY();
        setLocation ((int)Math.round(xx), (int)Math.round(yy));
        w.addObject(dot, getX(), getY());

        currentAnimation = primaryAnimation;

        setCurrentImages (currentAnimation.getDirectionalImages()[direction.getDirection()]);
        if (collisionEnabled){
            positionCollider();
        }
    }

    /**
     * This turns on the Collision system. If you have not already called setCollider to create a 
     * custom Collider, this method will create one for you that is the same width as this Actor's
     * current image, so set an image before calling this. 
     * 
     * Note that this Collision system adds extra Actors to the World and may have a performance cost.
     * This is not always the best way to do collision detection, and you should consider your own
     * implementation of the Greenfoot collision detection methods in your subclass if they work and are simple.
     * 
     * The benefit of this system is that it has a method that does collision detection based on the direction
     * that the object is facing (based on it's Direction direction object). 
     */
    public void enableCollision (){
        if (myCollider == null){
            myCollider = new Collider (getImage().getWidth(), getImage().getHeight(), 0, 0, this);
        }
        collisionEnabled = true;
        if (getWorld() != null){
            positionCollider();
        }
    }

    public void enableCollision (boolean troubleshooting){

        enableCollision();
        if (troubleshooting){
            myCollider.showHighlight();
        } else {
            myCollider.hideHighlight();
        }

    }

    public void setCollider (int width, int height, int xOffset, int yOffset){
        myCollider = new Collider (width, height, xOffset, yOffset,  this);
    }

    public void disableCollision () {
        if (myCollider != null && myCollider.getWorld() != null)
            getWorld().removeObject(myCollider);
        collisionEnabled = false;
    }

    private void positionCollider () {
        if (myCollider.getWorld()== null){
            getWorld().addObject(myCollider, 0, 0);
        } 
        // Until Coordinate is fully implemented, set the Greenfoot location AND the internal coordinate
        // (which is currently used only for collision detection, but will later be improved to work for this
        // whole class).
        myCollider.setPosition (getX() + myCollider.getXOffset(), getY() + myCollider.getYOffset());
        myCollider.setLocation (getX() + myCollider.getXOffset(), getY() + myCollider.getYOffset());
    }

    protected boolean isColliding (){
        if (myCollider.checkCollision(direction) == null){
            return false;
        }
        return true;
    }

    protected Actor checkCollision (){
        return myCollider.checkCollision (direction);
    }

    public Collider getCollider () {
        return myCollider;
    }

    /**
     *  Simplified way to set your image.
     *  
     *  This avoid needing to specify a layer and is ideal for beginners.
     *  
     *  This actually sets the 0th (bottom) layer, in case you want to add to 
     *  it later. This also clears any existing layers.
     *  
     *  @param image    The sprite sheet that you want to set as the image for this character
     * 
     */
    public void setSpriteSheet (GreenfootImage image) {
        // Reset the array and place the image in the 0th layer.
        // This will reset any previous layers, but you can add
        // layers on top of this one. 
        spriteSheetLayers = new GreenfootImage[MAX_LAYERS];
        spriteSheetLayers[0] = image;
        spriteSheet = Animation.updateSpriteSheet (spriteSheetLayers);
    }

    public GreenfootImage getSpriteSheet () {
        return spriteSheet;
    }

    public GreenfootImage getStartingImage () {
        return primaryAnimation.getOneImage(direction, 0);
    }

    /**
     * Set layers for your image.
     * 
     * The 0th layer is on the bottom and will be drawn first. The highest 
     * layer is on top and will be drawn last. By default there are 12 layers,
     * but you can add more by changing the MAX_LAYERS constant.
     * 
     * The supplied graphics should all be Spritesheets, and you should consider
     * which layers should be drawn on top of which for the best visual effect.
     * 
     * @param layer The layer number you want to place this spritesheet at
     * @param image The sprite sheet that you want to place at this layer
     */   
    public void setLayer (int layer, GreenfootImage image){
        spriteSheetLayers[layer] = image;
        spriteSheet = Animation.updateSpriteSheet (spriteSheetLayers);
        
    }

    /**
     * Set layers for your image all at once with an array of GreenfootImage..
     * 
     * The 0th layer is on the bottom and will be drawn first. The highest 
     * layer is on top and will be drawn last. By default there are 12 layers,
     * but you can add more by changing the MAX_LAYERS constant. Your input array
     * should not have more than MAX_LAYERS elements. Extra elements will be ignored.
     * You're free to experiment with more layers, just change the constant above.
     * 
     * Note that this will remove any existing Layers before setting.
     * 
     * The supplied graphics should all be Spritesheets, and you should consider
     * which layers should be drawn on top of which for the best visual effect.
     * 
     * @param layers    The array of images to set as drawing layers.
     * 
     */   
    public void setLayers (GreenfootImage[] layers){
        spriteSheetLayers = new GreenfootImage[MAX_LAYERS];
        int elements = Math.min (layers.length, MAX_LAYERS);
        for (int i = 0; i < elements; i++){

            spriteSheetLayers[i].drawImage(layers[i], 0, 0);
        }
        spriteSheet = Animation.updateSpriteSheet (spriteSheetLayers);
    }

    /**
     * Custom refresh - specify your own spriteSheet, and refresh this animation to reflect it.
     * Remember to apply trim() after, if required,  as this does not trim the images.
     * 
     * @param   anim    The Animation object that you wish to refresh.
     * @param   GreenfootImage  The image object to use to update this Animation
     */
    public void refresh (Animation anim, GreenfootImage spriteSheet){
        anim.refresh(spriteSheet);
        setCurrentImages (currentAnimation.getDirectionalImages()[direction.getDirection()]);
    }
    
    /**
     * Standard refresh - uses the spritesheet built into this AnimatedCharacter. This should
     * be called after making changes to the SpriteSheet, so that they will change in the 
     * active Animation. Remember to apply trim() after, if required,  as this does not trim the images.
     * 
     * @param   anim    The Animation object that you wish to refresh. This will use
     *                  the default / built-in spriteSheet, which you may have just
     *                  changed using setLayer, or otherwise.
     */
    public void refresh (Animation anim){
        anim.refresh(this.spriteSheet);
        setCurrentImages (currentAnimation.getDirectionalImages()[direction.getDirection()]);
    }
    
    public void setPrimaryAnimation (String anim){
        setPrimaryAnimation (animations.get(anim));
    }

    public void setPrimaryAnimation (Animation anim){
        primaryAnimation = anim;
    }

    /**
     * Set the movement speed (in pixels per second) and the animation rate
     * (in frames per second)
     */
    protected void changeSpeed (int moveSpeed, int framesPerSecond)
    {
        // Only run code if something has changed
        if (this.moveSpeed != moveSpeed || this.framesPerSecond != framesPerSecond){
            this.framesPerSecond = framesPerSecond;
            this.moveSpeed = moveSpeed;

            // Figure out how many seconds per frame
            secondsPerFrame = 1.0 / this.framesPerSecond;
            // Reset animation timer
            lastFrame = System.nanoTime();
        }
    }

    /**
     * Move towards a specified actor. 
     * 
     * @param Actor the Actor to move toward at the current speed and framerate
     */
    public void moveTowards (Actor a)
    {
        dot.setTarget(a, this);
        dot.setLocation (getX(), getY());

        // Reset animation timer
        lastFrame = System.nanoTime();
        autoMove = true;
        double dx = getX() -  a.getX();
        double dy = getY() -  a.getY();
        if (Math.abs (dx) > Math.abs (dy)){ // more horizontal
            if (dx > 0){
                direction = Direction.LEFT;
            } else {
                direction = Direction.RIGHT;
            }
        } else {// more vertical
            if (dy > 0){
                direction = Direction.UP;
            } else {
                direction = Direction.DOWN;
            }
        }
        setCurrentImages (currentAnimation.getDirectionalImages()[direction.getDirection()]);
    }

    public void moveTowards (int targetX, int targetY)
    {
        getWorld().removeObject(targetDot);
        targetDot = null;

        // create a temporary object at the target lotcation
        targetDot = new Dot();
        getWorld().addObject (targetDot, targetX, targetY);

        // Now that I've made an object, I can use the other clickToMove method
        // to set my Dot to follow it
        moveTowards (targetDot);
    }

    /**
     * <p>Make this AnimatedCharacter move in a specified direction.</p>
     * 
     * <p><b>Instructions:</b></p>
     * <ol>
     * <li>Subclasses can just set direction once and the animated character
     * will keep moving until stopped or direction changes.</li>
     * <li>Subclasses can call this repeatedly - if direction doesn't change,
     * these method calls will be ignored</li>
     * <li> This method does not actually perform movement - only set direction
     * variables. This is intended - the super.act() call in the subclass will run
     * last in the subclass' act() method and perform the actual movement.</li>
     *
     * <li>Intended to receive a 1 or -1 for for ONE of the parameters, and a 
     * zero (0) for the other. This method does not allow diagonal movement.</li>
     * </ol>
     * @param dirX  The direction for x movement. Should be -1, 0 or 1.
     * @param dirY  The direction for y movement. Should be -1, 0 or 1. 
     */
    protected void moveInDirection (int dirX, int dirY)
    {
        // stop automoving
        autoMove = false;
        // If there has been a change in direction
        if (this.dirX != dirX || this.dirY != dirY){

            if (dirX == 0 && dirY == 0){
                idle = true; 
                lastFrame = System.nanoTime(); // reset animation timer to start fresh
                frame = 0; // 0 is the idle frame
                setImage (currentAnimation.getDirectionalImages()[direction.getDirection()][frame]);
            } else {
                idle = false; 
                //frame = 1; // set to first frame if dir has changed
                // set the facing direction if direction has changed
                if (dirX == 1){
                    direction = Direction.RIGHT;
                } else if (dirX == -1){
                    direction = Direction.LEFT;
                } else if (dirY == 1){
                    direction = Direction.DOWN;
                } else if (dirY == -1){
                    direction = Direction.UP;
                }
            }
            setCurrentImages (currentAnimation.getDirectionalImages()[direction.getDirection()]);
            // set these variables so that I can check for changes next time
            this.dirX = dirX;
            this.dirY = dirY;
        } 
    }

    /**
     * 
     */
    public void moveInDirection (Direction direction){

        dirX = 0;
        dirY = 0;
        idle = false;
        autoMove = false;
        this.direction = direction;
        if (direction == direction.RIGHT){
            dirX = 1;
        } else if (direction == direction.LEFT){
            dirX = -1;
        } else if (direction == direction.DOWN){
            dirY = 1;
        } else if (direction == direction. UP){
            dirY = -1;
        }
        setCurrentImages (currentAnimation.getDirectionalImages()[direction.getDirection()]);
    }

    public boolean isMoving () {
        return autoMove;
    }

    // This method if you want to set an idle facing direction
    public void stopMoving (Direction direction){
        this.direction = direction;
        idle=true;
        stopMoving();
    }

    // This method if you want to just stop and use current facing direction.
    public void stopMoving ()
    {

        autoMove = false;
        dirX = 0;
        dirY = 0;
        if (!stopAtEnd){
            lastFrame = System.nanoTime();
        }
    }

    // TODO: Diagonal Movement
    protected void moveBySetRotation (int rotation){

    }

    /**
     * Override the default setLocation method because the backing
     * variables xx and yy need to be updated, otherwise they will
     * immediately move the player back every time it tries to move.
     */
    @Override
    public void setLocation (int x, int y){
        xx = (int)x;
        yy = (int)y;   
        // once variable have been updated, call the normal method:
        super.setLocation ((int)Math.round(xx), (int)Math.round(yy));
    }

    /**
     * Runs an animation that will come to an end when it's finished doing it's thing.
     * 
     * @param animationName The name of the Animation (it must have been added to animations already)
     * @param removeAfterAnimation  Should this AnimatedCharacter delete itself when this Animation is complete?
     * @param stopMoving    Should this AnimatedCharacter stop moving BEFORE performing this operation?
     * @param d     What direction is this Animation currently facing?
     * @param repeats   How many times should this animation play? Ususally 1.
     */  
    public void runTerminalAnimation (String animationName, boolean removeAfterAnimation, boolean stopMoving, Direction d, int repeats){

        this.removeAfterAnimation = removeAfterAnimation;
        stopAtEnd = true;
        idle = false;
        frame = 0;
        this.repeats = repeats;

        currentAnimation = animations.get(animationName);
        direction = d;

        if (currentAnimation.isDirectional()){
            //setCurrentImages (anim.getDirectionalImages()[d.getDirection()]);

            setCurrentImages (currentAnimation.getDirectionalImages()[direction.getDirection()]);
        } else {
            //setCurrentImages (anim.getNonDirectionalImages());            
            setCurrentImages (currentAnimation.getNonDirectionalImages());
        }

        if (stopMoving){
            stopMoving (d);
        }
    }

    /**
     * Called to avoid getting stuck on an object - when you detect that you
     * have hit an object, call this method to go back to the previous position,
     * so your Actor won't get stuck in a loop where it's touching the object so
     * it cannot move. See Player class for example.
     */
    public void stepBack (){
        xx = (double)prevX;
        yy = (double)prevY;
    }

    /**
     * Step back from the specified Actor
     */
    public void stepBack (Actor a){

        dot.setLocation (getX(), getY());
        if (collisionEnabled){
            if (a instanceof AnimatedCharacter){
                AnimatedCharacter ac = (AnimatedCharacter)a;
                while (Collider.checkTouching(this.getCollider(), ac.getCollider())){
                    dot.turnTowards (a.getX(), a.getY());
                    dot.turn(180);
                    dot.move (1);
                    setLocation (dot.getX(), dot.getY());
                }
            }

        } else {
            while (this.intersects(a)){
                dot.turnTowards (a.getX(), a.getY());
                dot.turn(180);
                dot.move (1);
                setLocation (dot.getX(), dot.getY());
            }
        }
    }

    private void setCurrentImages (GreenfootImage[] images){
        currentImages = new GreenfootImage[images.length];
        for (int i = 0; i < images.length; i++){
            currentImages[i] = images[i];
        }
    }

    // Act method - manages animation. MUST be called by subclass at end of subclass' own act() method with super.act().
    public void act() 
    {

        long lastAct = current;
        // determine how much time has passed since the last act
        current = System.nanoTime();
        // Find elapsed time since last frame switch in milliseconds (ms) for animation
        elapsed = (long)((current - lastFrame) / 1000000.0); 
        // Find elapsed time since last act, for movement
        long deltaTime = (current - lastAct) / 1000000;

        boolean removeMe = false; // flag that can be set to true for object self-removal

        //System.out.println("current: " + current + " last: " + lastFrame + " elapsed: " + elapsed + " actual: " + (current - lastFrame));

        // === ANIMATION ===

        if ((dirX == 0 && dirY == 0) && !autoMove && !stopAtEnd){ // if not moving, and not playing a terminal animation, switch to idle

            idle = true;
            lastFrame = current;
            // reset animation timer so it always starts fresh in next frame if
            // next frame is animated
            frame = 0;
            //System.out.println("Detected not moving - switching to idle");
            setImage (currentImages[frame]);
        }        
        else{

            // Troubleshooting code:
            // System.out.println("System Nano: " + System.nanoTime());
            // System.out.println("Last: " + lastFrame);
            //System.out.println("Elapsed: " + elapsed);

            // Check if ready to show next frame, and if so, advance frame
            if (elapsed > secondsPerFrame * 1000 || idle){
                // note - the use of the idle variable here is to avoid restarting the animation
                // timer after idle. This way, the first frame after idle starts instantly

                lastFrame = current;
                frame++;

                idle = false;
            }

            if (frame > currentImages.length - 1){ // IF THIS IS THE LAST FRAME:
                if(stopAtEnd){ // if this is a terminal animation:

                    if(removeAfterAnimation){ // if this is supposed to be removed from world after it finishes
                        frame = currentImages.length - 1; // keep it on the last image to avoid out of bounds
                        removeMe = true;
                    } else {
                        if (repeats > 0){ // if a terminal animation needs to be repeated
                            repeats--;                          
                        } else { // if a terminal animation is finished, return to primary
                            currentAnimation = primaryAnimation;
                            stopAtEnd = false;
                            terminalAnimationComplete();
                            setCurrentImages (currentAnimation.getDirectionalImages()[direction.getDirection()]);
                        }
                        frame = 1;
                    }
                } else { // if this is just a regular animation, just go back to frame 1
                    frame = 1; // 0th frame is idle frame only, so count 1..last, not 0..last
                }

            }
            // now that the calculations are done, set the correct image
            setImage (currentImages[frame]);
        }
        // Troubleshooting player frame:

        // === MOVEMENT ===

        // calculate delta time - how many seconds have passed since the last act (I.e. 30 fps, dT = 0.0333)
        double dT = (current-lastAct) / 1000000000.0;

        // store previous coordinates, in case I have to bounce back to them due to hitting a wall etc.
        prevX = (int)Math.round(xx);
        prevY = (int)Math.round(yy);

        if (dT > maxFrameLength){
            dT = maxFrameLength;
        }

        if (autoMove){// Click to move mode
            dot.turnTowards();
            //System.out.println("Move: " + (double) moveSpeed* ((current-lastAct) / 1000000000.0));
            dot.move((double) moveSpeed* dT);

            xx = (double)dot.getX();
            yy = (double)dot.getY();

            //Stop moving if I'm close to my target
            if (Math.sqrt(Math.pow(targetDot.getX() - getX(), 2) + Math.pow(targetDot.getY() - getY(), 2)) < tolerance){
                idle = true;
                getWorld().removeObject(targetDot);
                targetDot = null;
                stopMoving();
                movementComplete();
            }

        } else { // Regular move mode
            // calculate exact new location. Decimal values will be rounded, but stored accurately, for
            // smooth animation over time
            xx += ((double)(dirX) * moveSpeed) * dT;
            yy += ((double)(dirY) * moveSpeed) * dT;

        }

        // Normalize position - makes sure it can't go outside of world      
        if (xx > getWorld().getBackground().getWidth() || xx < 0){
            xx = (double)getX();
        }
        if (yy > getWorld().getBackground().getHeight() || yy < 0){
            yy = (double)getY();
        }

        // update my location
        super.setLocation ((int)Math.round(xx), (int)Math.round(yy));

        if (collisionEnabled){
            positionCollider();
        }

        if (removeMe){
            disableCollision();
            getWorld().removeObject(this);
        }
    }    

    protected void terminalAnimationComplete(){
        // override this method in your subclasses if you want a signal that an
        // animation is complete

    }

    protected void movementComplete () {
        // override this method in your subclasses if you want a signal that an
        // automated movement is complete
    }

    public boolean isTerminal(){
        return stopAtEnd;
    }

    // ENUM to keep direction related code clean.
    public enum Direction {
        RIGHT(0), 
        LEFT(1), 
        UP(2), 
        DOWN(3);

        private final int dirCode;
        private Direction (int dirCode){
            this.dirCode = dirCode;
        }

        public int getDirection (){
            return this.dirCode;
        }

        public static Direction fromInteger(int x) {
            switch(x) {
                case 0:
                return RIGHT;
                case 1:
                return LEFT;
                case 2:
                return UP;
                case 3: 
                return DOWN;
            }
            return null;
        }
        public final static int size = Direction.values().length;
    }    

}

/**
 * Dot is a simple helper class that has it's own built in
 * precise X and Y variables (like SmoothMover) and can be moved smoothly
 */
class Dot extends Actor
{
    private GreenfootImage myImage;
    private double xx, yy;
    private int targetX, targetY;
    private Actor target;

    public Dot ()
    {
        myImage = new GreenfootImage(1,1);
        // Troubleshooting Code
        //  myImage.setColor(Color.YELLOW);
        //  myImage.fill();
        setImage(myImage);
        target = null;
    }

    public double getPreciseX(){
        return xx;
    }

    public double getPreciseY(){
        return yy;
    }

    public void addedToWorld(World w){
        xx = getX();
        yy = getY();   
    }

    public void setTarget (Actor a, AnimatedCharacter owner){
        target = a;
        xx = owner.getX();
        yy = owner.getY();
    }

    /**
     * Move forward by the specified exact distance in the current direction.
     */
    public void move(double distance)
    {
        double radians = Math.toRadians(getRotation());
        double dx = Math.cos(radians) * distance;
        double dy = Math.sin(radians) * distance;
        // Use my precise location variables to handle the movement
        double tempx = xx;
        double tempy = yy;
        xx += dx;
        yy += dy;

        setLocation ((int)Math.round(xx), (int)Math.round(yy));
    }

    /**
     * Turn towards my current target.
     */
    public void turnTowards (){
        if (target != null){
            setRotation ((int)Math.round(Math.toDegrees((Math.atan2(target.getY() - yy, target.getX() - xx)))));
        }
    }
}

/**
 *  Animation Class
 *  
 *  This is a 1d or 2d array of images. 
 *  
 *  For Animations that are directional (can face in more than one direction), 
 *  this class will store as 2d array. For animations with more than one direction,
 *  this class will store as 1d array. If you need to determine what sort of animation
 *  you are working with, you can call isDirectional() to see which one this is.
 *  
 *  Includes a number of static helper methods to assist in creating Animations.
 */
class Animation {
    private GreenfootImage[][] directionalImages;
    private GreenfootImage[] nonDirectionalImages;

    private int startRow;
    private int numRows;
    private int width;
    private int height;
    private int numFrames;

    private boolean directional;

    private int directions;

    /**
     * Constructor for directional animations - should be a 2d array of GreenfootImage with 
     * 4 directions (dimension 1) and at least one image per direction (dimension 2)
     * 
     * @param images    2d array of images as described above
     * @param terminal  true if this animation is not intended to repeat
     */ 
    public Animation (GreenfootImage[][] images, int startRow, int numRows, int numFrames, int width, int height){
        this.startRow = startRow;
        this.numRows = numRows;
        this.width = width;
        this.height = height;
        this.directional = true;
        this.numFrames = numFrames;
        directionalImages = images;

        directions = images.length;
    }

    /**
     * Constructor for directional animations - should be a 2d array of GreenfootImage with 
     * 4 directions (dimension 1) and at least one image per direction (dimension 2)
     * 
     * @param images    2d array of images as described above
     * @param terminal  true if this animation is not intended to repeat
     */ 
    public Animation (GreenfootImage[] images, int startRow, int numRows, int numFrames, int width, int height){
        this.startRow = startRow;
        this.numRows = numRows;
        this.width = width;
        this.height = height;
        this.directional = false;
        this.numFrames = numFrames;
        nonDirectionalImages = images;

        directions = images.length;
    }

    public void refresh (GreenfootImage spriteSheet) {
        if (directional){
            GreenfootImage[][] temp = processImages (spriteSheet, startRow, numRows, numFrames, width, height);
            setImages (temp);
        } else {
            GreenfootImage[]temp = processImages(spriteSheet, startRow, numRows, numFrames, width, height)[0];
            setImages (temp);
        }
    }

    /**
     * Use this to change the images in an existing Animation. You will have to provide
     * a new array.
     * 
     * @param   images  2d Array of GreenfootImages.
     */
    public void setImages (GreenfootImage[][] images){
        directionalImages = images;
    }

    /**
     * Use this to change the images in an existing Animation. You will have to provide
     * a new array.
     * 
     * @param   images  2d Array of GreenfootImages.
     */
    public void setImages (GreenfootImage[] images){
        nonDirectionalImages = images;
    }    

    public int getDirectionCount () {
        return directions;
    }

    public boolean isDirectional (){
        return this.directional;
    }

    public GreenfootImage getOneImage (AnimatedCharacter.Direction d, int frame){
        return directionalImages[d.getDirection()][frame];
    }

    public GreenfootImage[][] getDirectionalImages (){
        return directionalImages;
    }

    public GreenfootImage[] getNonDirectionalImages (){
        return nonDirectionalImages;
    }

    /**
     *  A method designed specifically to get walking frames from Liquid Pixel Cup sprite 
     *  sheets.
     */
    protected static Animation createWalkingAnimation (GreenfootImage LPCSpriteSheet){
        return createAnimation (LPCSpriteSheet, 9, 4, 9, 64, 64);
    }

    /**  This will allow for importing armor etc to make the character dynamic! Without spritefoot work!
     *  Rows must be 4 (directional) or 1 (non-directional). This is designed to work with spritesheets from
     *  LPC but could be tailored to work with other source material.
     *  
     *  @param spriteSheet  the Spritesheet to pull frames from
     *  @param startRow     the row on which the desired sprites are located (not x,y coordinate)
     *  @param numRows      the number of rows to import, which will correspond with the number of directions (currently,
     *                      only supports 1 or 4 directions, TODO: add support for 2 and/or 6, 8 directions)
     *  @param numFrames    the number of frames in the animation
     *  @param width        the width of each frame
     *  @param height       the height of each frame
     *  @return Animation   an appropriate Animation object that is either 1 direction or 4 direction.
     *  
     */
    protected static Animation createAnimation(GreenfootImage spriteSheet, int startRow, int numRows, int numFrames, int width, int height){
        GreenfootImage[][] images = processImages(spriteSheet, startRow, numRows, numFrames, width, height);

        if (numRows == 1){
            // If this only has one dimension, create a 1 dimension Animation
            GreenfootImage[] img1d = new GreenfootImage[images[0].length];
            for (int i = 0; i < img1d.length; i++){
                img1d[i] = images[0][i];
            }
            Animation anim = new Animation(img1d, startRow, numRows, numFrames, width, height);
            return anim;

            // If this is directional
        } else {
            //System.out.println("Creating directional animation");
            Animation anim = new Animation (images, startRow, numRows, numFrames, width, height);
            return anim;
        }

    }

    private static GreenfootImage[][] processImages (GreenfootImage spriteSheet, int startRow, int numRows, int numFrames, int width, int height){
        GreenfootImage[][] images = new GreenfootImage[numRows][numFrames];
        for (int row = 0; row < numRows; row++){
            int dir = -1;
            if (numRows == 1){ // for single direction animationsAnimation.createAnimation(spriteSheet, 9, 4, 9, 64, 64)
                dir = 0;
            } else { 
                switch (row) { // translate between Direction values and the order in which the frames are organized in LPC sheets
                    case 0: dir = 2;  break;
                    case 1: dir = 1;  break;
                    case 2: dir = 3;  break;
                    case 3: dir = 0;  break; 
                }
            }
            if (dir == -1) return null;
            
            for (int frame = 0; frame < numFrames; frame++){
                //System.out.println(spriteSheet + " Row: " + row + " dir: " + dir + " frame: " + frame);
                images[dir][frame] = new GreenfootImage (getSlice(spriteSheet, frame * width, (startRow + row - 1) * height, width, height));
            }
        }
        return images;
    }

    /**
     * Grabs a part of a sprite sheet (or any other GreenfootImage) and returns it as a new
     * GreenfootImage. The sprite sheet must be larger than the resulting image.
     * 
     * @param spriteSheet   the larger spritesheet to pull images from
     * @param xPos  the x position (of the left) of the desired spot to draw from
     * @param yPos  the y position (of the top) of the desired spot to draw from
     * @param frameWidth     the horizontal tile size
     * @param frameHeight    the vertical tile size
     * @return GreenfootImage   the resulting image
     */
    private static GreenfootImage getSlice (GreenfootImage spriteSheet, int xPos, int yPos, int frameWidth, int frameHeight)
    {
        if (frameWidth > spriteSheet.getWidth() || frameHeight > spriteSheet.getHeight()){

            System.out.println("Error in AnimationManager: GetSlice: You specified a SpriteSheet that was smaller than your desired output");
            return null;
        }
        GreenfootImage small = new GreenfootImage (frameWidth, frameHeight);
        // negatively offset the larger sprite sheet image so that a correct, small portion
        // of it is drawn onto the smaller, resulting image.
        small.drawImage (spriteSheet, -xPos, -yPos);
        return small;
    }

    // should be called by AnimatedCharacter when Layers change
    public static GreenfootImage updateSpriteSheet (GreenfootImage[] layers){
        GreenfootImage spriteSheet = new GreenfootImage (layers[0].getWidth(), layers[0].getHeight());
        for (int i = 0; i < layers.length; i++){
            if (layers[i] != null){
                spriteSheet.drawImage(layers[i], 0, 0);
            }
        }
        return spriteSheet;
    }

    /**
     *  Crop a whole animation stripping a specified number of pixels from each of the sides. This can be called manually
     *  on an Animation, or used by a procedural crop method.
     *  
     *  @param leftTrim     number of pixels to trim from the left
     *  @param rightTrim    number of pixels to trim from the right
     *  @param topTrim      number of pixels to trim from the top
     *  @param bottomTrim   number of pixels to trim from the bottom
     *  @return Animation   a new Animation consisting of the same, but nownewly trimmed, images.
     */
    public static void trim (Animation anim, int leftTrim, int rightTrim, int topTrim, int bottomTrim){

        if (anim.isDirectional()){
            GreenfootImage[][] images = anim.getDirectionalImages();
            for (int direction = 0; direction < images.length; direction++){
                for (int frame = 0; frame < images[direction].length; frame++){
                    images[direction][frame] = getSlice(images[direction][frame], leftTrim, topTrim, images[direction][frame].getWidth() - rightTrim - leftTrim, images[direction][frame].getHeight() - bottomTrim - topTrim);
                }
            }

            anim.setImages(images);
        } else {
            GreenfootImage[] images = anim.getNonDirectionalImages();
            for (int frame = 0; frame < images.length; frame++){
                images[frame] = getSlice(images[frame], leftTrim, topTrim, rightTrim - leftTrim, bottomTrim - topTrim);
            }
            anim.setImages(images);
        }
    }
}

/**
 * Colliders are used for collision detection. 
 * 
 * A Collider may or may not be attached to an Actor.
 * 
 * Colliders can be different sizes than the Actor they are attached to, allowing for
 * customization of hit boxes. 
 * 
 * Highlights can be shown for troubleshooting (or visual effect). Otherwise, Colliders
 * have a blank image (necessary for Greenfoot collision detection).
 * 
 * Includes an easy collision check method that checks in the currently facing direction
 * for a collision. Also includes a simple checkTouching static method to see if any two
 * specific Colliders are touching.
 * 
 * @author Jordan Cohen 
 * @version GTC 0.231
 */
class Collider extends Actor implements Comparable<Collider>
{
    // instance variables - replace the example below with your own
    private int offsetX;
    private int offsetY;
    private int xSize;
    private int ySize;
    private int halfX, halfY;
    private Coordinate coordinate;

    // the GameObject that owns this Collider. GameObject is an interface, so this could be a player, a tree, etc.
    private Actor owner; 

    private GreenfootImage blank;
    private GreenfootImage highlight;

    /**
     * Constructor for objects of class Collider, for use when Collider is being
     * used as an object on it's own
     */
    public Collider(int xSize, int ySize, int offsetX, int offsetY,  Actor owner)
    {
        init (xSize, ySize, offsetX, offsetY,  owner);

    }

    protected void init (int xSize, int ySize, int offsetX, int offsetY,  Actor owner) {
        this.owner = owner;
        this.xSize = xSize;
        this.ySize = ySize;
        this.halfX = xSize / 2;
        this.halfY = ySize / 2;
        this.offsetX = offsetX;
        this.offsetY = offsetY;

        blank = new GreenfootImage (xSize, ySize);
        highlight = new GreenfootImage (xSize, ySize);
        highlight.setColor (Color.RED);
        highlight.fill();
        highlight.setTransparency (75);

        if (owner != null){
            if (owner.getWorld() != null){
                coordinate = new Coordinate (owner.getX() + offsetX, owner.getY() + offsetY);
            }
            else{
                coordinate = new Coordinate (0, 0);
            }
        }

        setImage(blank);
        //setImage(highlight);
    }

    public void showHighlight(){
        setImage(highlight);
    }

    public void hideHighlight () {
        setImage (blank);
    }

    public int getWidth() {
        return getImage().getWidth();
    }

    public int getHeight () {
        return getImage().getWidth();
    }

    public int getXOffset() {
        return offsetX;
    }

    public int getYOffset() {
        return offsetY;
    }

    public String toString () {
        return ("X size: " + xSize + " Y size: " + ySize);
    }

    public Coordinate getCoordinate (){
        return coordinate;   
    }

    public void setPosition (int x, int y){
        coordinate.setLocation (x, y);

    }

    public void setCoordinate (Coordinate c){
        this.coordinate = c;
    }

    public Collider getCollider () {
        return this;
    }

    public GreenfootImage getSliceImage ()
    {
        return null;
    }

    public Collider checkCollision (AnimatedCharacter.Direction direction){

        Collider c;

        if (direction == AnimatedCharacter.Direction.DOWN){
            for (int i = 0; i < xSize; i+= xSize/4 ){
                c = (Collider)getOneObjectAtOffset(i - halfX, halfY, Collider.class);
                if (c != null){
                    return c;
                }
            }
        }
        else if (direction == AnimatedCharacter.Direction.UP){
            for (int i = 0; i < xSize; i+= xSize/4){
                c = (Collider)getOneObjectAtOffset(i - halfX, -halfY, Collider.class);
                if (c != null){
                    return c;
                }
            }           
        }
        else if (direction == AnimatedCharacter.Direction.LEFT){
            for (int i = 0; i < ySize; i += ySize / 4){
                c = (Collider)getOneObjectAtOffset(-halfX, i - halfY, Collider.class);
                if (c != null){
                    return c;
                }
            }
        }
        else if (direction == AnimatedCharacter.Direction.RIGHT){
            for (int i = 0; i < ySize; i += ySize / 4){
                c = (Collider)getOneObjectAtOffset(halfX, i - halfY, Collider.class);
                if (c != null){
                    return c;
                }
            }

        }

        return null;
    }

    public static boolean checkTouching (Collider a, Collider b){
        if (a.getX() < b.getX() + b.getWidth() && a.getX() + a.getWidth() > b.getX() && a.getY() < b.getY() + b.getHeight() && a.getY() + a.getHeight()  > b.getY()){
            return true;
        }   
        return false;
    }

    /**
     * Static utility method:
     * 
     * Determine if two Collider objects are the same.
     */
    public static boolean checkSame (Collider a, Collider b){
        // This is probably more than necessary - but I'm not confident in the Collider algorithm yet ... ... ... 
        if (a.getX() == b.getX())
            if (a.getY() == b.getY())
                if (a.getWidth() == b.getWidth())
                    if (a.getHeight() == b.getHeight())
                        return true;
        return false;
    }

    /**
     * This allows the implementation of Comparable, so Collections.sort()
     * can sort an ArrayList of Colliders. This method should not be called
     * directly. Orders Colliders first horizontally then vertically.
     */
    @Override
    public int compareTo(Collider t) {
        // comparison weighting I made up that I THINK will sort left to right then top to bottom:
        int stackedValueOther = (t.getY() * xSize) + t.getX();
        int stackedValueThis = (this.getY() * xSize) + this.getX();
        return stackedValueThis - stackedValueOther;

    }
}

class Coordinate {
    private double x;
    private double y;

    public Coordinate (int x, int y){
        this.x = (double)x;
        this.y = (double)y;
    }

    public Coordinate (double x, double y){
        this.x = x;
        this.y = y;
    }

    public void setPreciseLocation (double x, double y){
        this.x = x;
        this.y = y;
    }

    public void setLocation (int x, int y)
    {
        this.x = x;
        this.y = y;
    }

    public void setX(int x){
        this.x = (double)x;
    }

    public void setY (int y ){
        this.y = (double)y;
    }

    public int getX(){
        //return (int)Math.round(x);
        return (int)x;
    }

    public int getY(){
        //return (int)Math.round(y);
        return (int)y;
    }

    public double getPreciseX(){
        return x;
    }

    public double getPreciseY(){
        return y;
    }

    public void dX (double value){
        this.x += value;
    }

    public void dY (double value){
        this.y += value;
    }

    public String toString () {
        return "x: " + x + ", y: " + y;
    }
}
