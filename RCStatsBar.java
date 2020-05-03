import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.ArrayList;
/**
 * Statsbar is a Greenfoot Actor that displays either 1 - 3 statistical variables through text. 
 * Variables have a name and a value (integer) ex. (Health, 12), (Score, 1231), etc..
 * <P>
 * Displays as: VARIABLE1 VALUE1 | VARIABLE2 VALUE2 | VARIABLE3 VALUE3
 * <ul>
 * <p>
 * - Lets user choose variables to display, display them, and update them
 * <ul>
 * variables are integers with a max absolute value of 99999
 * </ul>
 * <p>
 * - Lets user change colour of the text's outline and filling
 * <p>
 * - Lets user animate the colors of the text
 * </u>
 * @author Ryan Chi
 * @version March, 2020
 */
public class RCStatsBar extends Actor
{
    private ArrayList<String> nameList = new ArrayList<String>(); //array list for holding the names of variables
    private ArrayList<Integer> valueList = new ArrayList<Integer>(); //array list for holding the values of variables
    private Label label1 = new Label("", 35); //label that displays the variable's name and value

    //Variables used to animate the colors
    private ArrayList<Color> colors= new ArrayList<Color>(); //List with all colors used for animation
    private Boolean animate = false; //Boolean to see if animation is turned on
    private int timer = 4; //delay before color changes
    private int colorCounter = 0; //counter for the ArrayList for color
    private Color fillColor = Color.WHITE; //color that User chooses to set while not animating, set to white by default
    /**
     * Creates new Statsbar with 1 variable
     * 
     * @param name1 name of variable
     * @param val1 value of variable
     */
    public RCStatsBar(String name1, int val1)
    { 
        nameList.add(name1);
        valueList.add(val1);
        updateLabel();
        setUpAnimate();
    }

    /**
     * Creates new Statsbar with 2 variable (integers)
     * 
     * @param name1 name of the first variable
     * @param val1 value of the first variable
     * @param name2 name of the second variable
     * @param val2 value of the second variable
     */
    public RCStatsBar(String name1, int val1, String name2, int val2)
    {
        nameList.add(name1);
        valueList.add(val1);
        nameList.add(name2);
        valueList.add(val2);
        updateLabel();
        setUpAnimate();
    }

    /**
     * Creates new Stats bar with 3 variable
     * 
     * @param name1 name of the first variable
     * @param val1 value of the first variable
     * @param name2 name of the second variable
     * @param val2 value of the second variable
     * @param name3 name of the third variable
     * @param val3 value of the third variable
     */
    public RCStatsBar(String name1, int val1, String name2, int val2, String name3, int val3)
    {
        nameList.add(name1);
        valueList.add(val1);
        nameList.add(name2);
        valueList.add(val2);
        nameList.add(name3);
        valueList.add(val3);
        updateLabel();
        setUpAnimate();
    }

    /**
     * Greenfoot method called when actor enters the world. 
     * 
     * Creates a label that displays the variable names and values.
     */
    public void addedToWorld(World world)
    {
        getWorld().addObject(label1, getX(), getY());

    }

    /**
     * Loads all the colors into color array used for animating
     * 
     */
    private void setUpAnimate()
    {
        colors.add(Color.RED);
        colors.add(Color.ORANGE);
        colors.add(Color.YELLOW);
        colors.add(Color.GREEN);
        colors.add(Color.BLUE);
        colors.add(Color.MAGENTA);
    }

    /**
     * Called every time the world runs through a frame.
     * Checks if animate is true and if so, animates the label.
     * If animate is turned off, label color is set to default or user color.
     * 
     */
    public void act()
    {
        if (animate)
        {
            if (timer==0)
            {
                label1.setFillColor(colors.get(colorCounter));
                colorCounter++;
                if (colorCounter == colors.size())
                {
                    colorCounter = 0;
                }
                timer = 4;
            }
            else
            {
                timer--;
            }
        }
        else
        {
            label1.setFillColor(fillColor);
        }
            
    }

    /**
     * Animates the text by changing colors automatically.
     *
     */
    public void enableAnimate()
    {
        animate = true;
    }

    /**
     * Disables the animate function. Text color is static
     * 
     */
    public void disableAnimate()
    {
        animate = false;
    }

    /**
     * Changes the color of the text's outlines
     * 
     * @param colour Greenfoot colour of the outlines
     */
    public void changeLineColour(Color colour)
    {
        label1.setLineColor(colour);
    }

    /**
     * Changes the color of the text's filling
     * 
     * @param colour Greenfoot colour of the Text's filling
     */
    public void changeFillColour(Color colour)
    {
        fillColor = colour;
        label1.setFillColor(colour);
    }

    /**
     * Lets user change 1 variable (integers) and update it
     * 
     * @param name1 name of the variable that will change
     * @param val1 new value of the variable
     */
    public void update(String name1, int val1)
    {
        //checks to see if any new value for all variable and updates
        for (int i = 0; i < nameList.size(); i++)
        {
            if (name1.equals(nameList.get(i)))
            {
                valueList.set(i, val1);
            }
        }
        updateLabel();
    }

    /**
     * Lets user change 2 variable (integers) and update it
     * 
     * @param name1 name of the first variable that changes
     * @param val1 new value of the first variable
     * @param name2 name of the second variable that changes
     * @param val2 new value of the second variable
     */
    public void update(String name1, int val1, String name2, int val2)
    {
        //checks to see if any new value for all variable and updates
        for (int i = 0; i < nameList.size(); i++)
        {
            if (name1.equals(nameList.get(i)))
            {
                valueList.set(i, val1);
            }
            else if (name2.equals(nameList.get(i)))
            {
                valueList.set(i, val2);
            }
        }
        updateLabel();
    }

    /**
     * Lets user change 3 variable (integers) and update it
     * 
     * @param name1 name of the first variable that changes
     * @param val1 new value of the first variable
     * @param name2 name of the second variable that changes
     * @param val2 new value of the second variable
     * @param name3 name of the third variable that changes
     * @param val3 new value of the third variable
     */
    public void update(String name1, int val1, String name2, int val2, String name3, int val3)
    {
        //checks to see if any new value for all variable and updates
        for (int i = 0; i < nameList.size(); i++)
        {
            if (name1.equals(nameList.get(i)))
            {
                valueList.set(i, val1);
            }
            else if (name2.equals(nameList.get(i)))
            {
                valueList.set(i, val2);
            }
            else if (name3.equals(nameList.get(i)))
            {
                valueList.set(i, val3);
            }
        }
        updateLabel();
    }
    
    /**
     * updates the text on the label based on changes to variables and number of variables
     */
    private void updateLabel()
    {
        if (nameList.size() == 1)
        {
            //updates a label for 1 var
            label1.setValue(nameList.get(0) + " " + rollZero(valueList.get(0)));
        }
        else if (nameList.size() == 2)
        {
            //updates a label for 2 var
            label1.setValue(nameList.get(0) + " " + rollZero(valueList.get(0)) + " | " + nameList.get(1) + " " + rollZero(valueList.get(1)));
        }

        else if (nameList.size() == 3)
        {
            //updates a label for 3 var
            label1.setValue(nameList.get(0) + " " + rollZero(valueList.get(0)) + " | " + nameList.get(1) + " " + rollZero(valueList.get(1)) + " | " + nameList.get(2) + " " + rollZero(valueList.get(2)));
        }
    }

    /**
     * rolls the zeroes so that numbers are more uniformed.
     *  Ex. turns 12 into 00012
     * <p>
     * Range of -99999 to 99999
     * <ul>
     * If absolute value larger, only takes last 5 digits
     * </ul>
     */
    private String rollZero(int value)
    {
        String str = "";
        if (value < 100000 && value > -1)
        {
            //value is postitive and within range
            for (int i = 0; i < (5-Integer.toString(value).length()); i++)
            {
                str+= "0";
            }
            str+= Integer.toString(value);
            return str;
        }

        else if (value > -100000 && value < 0)
        {
            //value is negative and within range
            str+= "-";
            for (int i = 0; i < (5-Integer.toString(Math.abs(value)).length()); i++)
            {
                str+= "0";
            }
            str+= Integer.toString(Math.abs(value));
            return str;
        }
        else if (value > 99999)
        {
            //value is positive and out of range
            return Integer.toString(value).substring(Integer.toString(value).length()-5);
        }
        else
        {
            //value is negative and out of range
            return "-" + Integer.toString(value).substring(Integer.toString(value).length()-5);
        }
    }

    
}

class Label extends Actor
{
    private String value;
    private int fontSize;
    private Color lineColor = Color.BLACK;
    private Color fillColor = Color.WHITE;

    private static final Color transparent = new Color(0,0,0,0);

    /**
     * Create a new label, initialise it with the int value to be shown and the font size 
     */
    public Label(int value, int fontSize)
    {
        this(Integer.toString(value), fontSize);
    }

    /**
     * Create a new label, initialise it with the needed text and the font size 
     */
    public Label(String value, int fontSize)
    {
        this.value = value;
        this.fontSize = fontSize;
        updateImage();
    }

    /**
     * Sets the value  as text
     * 
     * @param value the text to be show
     */
    public void setValue(String value)
    {
        this.value = value;
        updateImage();
    }

    /**
     * Sets the value as integer
     * 
     * @param value the value to be show
     */
    public void setValue(int value)
    {
        this.value = Integer.toString(value);
        updateImage();
    }

    /**
     * Sets the line color of the text
     * 
     * @param lineColor the line color of the text
     */
    public void setLineColor(Color lineColor)
    {
        this.lineColor = lineColor;
        updateImage();
    }

    /**
     * Sets the fill color of the text
     * 
     * @param fillColor the fill color of the text
     */
    public void setFillColor(Color fillColor)
    {
        this.fillColor = fillColor;
        updateImage();
    }

    /**
     * Update the image on screen to show the current value.
     */
    private void updateImage()
    {
        setImage(new GreenfootImage(value, fontSize, fillColor, transparent, lineColor));
    }
}

