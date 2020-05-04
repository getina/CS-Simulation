import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * ScoreBar is a Greenfoot Actor that displays text. 
 * The text is white on a reddish brown
 * background and designed to work in a scenario that is
 * 800x600 pixels. 
 * <p>
 * Display any String that fits, or send
 * game info straight to customized method.
 * @author Jordan Cohen
 * @version April 2013
 */
public class ScoreBar extends Actor
{
    // Declare Objects
    private GreenfootImage scoreBoard;
    private Color background;
    private Color foreground;
    private Font textFont;
    private String textBlue;
    private String textRed;

    // Declare Variables:
    private int width;
    
    /**
     * Construct a ScoreBar of the appropriate size.
     * 
     * @param width     width of the World where the
     *                  ScoreBar will be placed
     */
    public ScoreBar (int width)
    {
        scoreBoard = new GreenfootImage (width, 30);
        textFont = new Font ("Comic Sans", 24);
        scoreBoard.setColor(Color.WHITE);
        scoreBoard.fill();
        scoreBoard.setTransparency(200);
        
        this.setImage (scoreBoard);
        scoreBoard.setFont(textFont);

        this.width = width;
    } 

    /**
     * Updates this ScoreBar with game stats. This method should be
     * re-written to work with your specific labels/values
     * 
     * @param alive     current number of living Bugs
     * @param dead      number of dead bugs
     * @param averageLifespan   
     */
    public void update (int redAlive, int redDead, int redDamage, int blueAlive, int blueDead, int blueDamage)
    {
        // In order to make uniform sizes and preceding zeros:
        String redAliveString, blueAliveString, redDeadString, blueDeadString, redDamageString, blueDamageString;
        
        // If there is only one digit
        redAliveString = zeroAdder (redAlive, 3);
        blueAliveString = zeroAdder (blueAlive, 3);
        redDeadString = zeroAdder (redDead, 3);
        blueDeadString = zeroAdder (blueDead, 3);
        redDamageString = zeroAdder(redDamage, 3);
        blueDamageString = zeroAdder(blueDamage, 3);
                
        textRed = "N: " + redAliveString + "  DEAD: " + redDeadString + "  DAMAGE: " + redDamageString + "%";
        textBlue = "N: " + blueAliveString + "  DEAD: " + blueDeadString + "  DAMAGE: " + blueDamageString + "%";
        
        // Now that we have built the text to output...
        // this.update (String) calls the other version of update(), in this case
        // update(String) - see below
        this.update (textRed, textBlue);
    }

    /**
     * Takes a String and displays it centered to the screen.
     * 
     * @param output    Text for displaying. 
     */
    public void update (String outputRed, String outputBlue)
    {
        // Refill the background with background color
        scoreBoard.setColor(Color.WHITE);
        scoreBoard.fill();
        scoreBoard.setTransparency(200);

        // Smart piece of code that centers text
        // int centeredY = (width/2) - ((output.length() * 14)/2);

        scoreBoard.setColor(Color.RED);  
        scoreBoard.drawString(outputRed, 375, 22);
        
        scoreBoard.setColor(Color.BLUE);  
        scoreBoard.drawString(outputBlue, 900, 22);
    }

    /**
     * Method that aids in the appearance of the scoreboard by generating
     * Strings that fill in zeros before the score. For example:
     * 
     * 27 ===> to 5 digits ===> 00027
     * 
     * @param   value   integer value to use for score output
     * @param   digits   number of zeros desired in the return String
     * @return  String  built score, ready for display
     */ 
    public static String zeroAdder (int value, int digits)
    {
        // Figure out how many digits the number is
        int numDigits = digitCounter(value);

        // If not extra digits are needed
        if (numDigits >= digits)
            return Integer.toString(value);

        else // Build the number with zeroes for extra place values:
        {
            String zeroes = "";
            for (int i = 0; i < (digits - numDigits); i++)
            {
                zeroes += "0";
            }
            return (zeroes + value);
        }
    }
    
    /**
     * Useful private method that counts the digit in any integer.
     * 
     * @param number    The number whose digits you want to count
     * @return  int     The number of digits in the given number
     */
    private static int digitCounter (int number)
    {
        if (number < 10) {
            return 1;
        }
        int count = 0;
        while (number > 0) {
            number = 10;
            count++;
        }
        return count;
    }
}

