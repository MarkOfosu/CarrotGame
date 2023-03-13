
import java.util.*; 
import java.awt.*; 
import java.awt.event.*; 
import javax.swing.*;
import java.io.*;
import javax.imageio.*;

/* Implements the main component for the carrot game. */
/*
 * Implements the Frame for the carrot game.
 * Manages the components for creating carrots and Rabbits 
 * Checks for collision of rabbit and carrot and core points for the two rabbits if they collide with a carrot  
 *   @autor: Mark Ofosu
 *   @date: 10/27/2021
 */
public class CarrotComponent extends JComponent {
public static final int SIZE = 500;                 // initial size
public static final int PIXELS = 50;                // square size per image
public static final int MOVE = 20;                  // keyboard move
public static final int GRAVITY = 2;                // gravity move
public static final int CARROTS = 20;               // number of carrots

private ArrayList myPoints;                         // x,y upper left of each carrot 
                                   // change in y for gravity
private Image carrotImage; 

//int scoreValue;

// creates rabbits class to be able to create multiple rabbits.
public class Rabbit {
	
	private Image headImage1;
	private Image headImage2;
	private int myX;                                   // upper left of head x
	private int myY;                                     // upper left of head y
	private int myDy;
	public int scoreValue;
	
	// Rabbit constructor has the blue prints to create rabbits.
	public Rabbit () {
		
		//set first bunny image
		headImage1 = readImage("bugs-bunny2.jpg");
		headImage1 = headImage1.getScaledInstance(PIXELS, PIXELS,
		Image.SCALE_SMOOTH); 
		
		//set second bunny image
		headImage2 = readImage("bunny1.jpg");
		headImage2 = headImage2.getScaledInstance(PIXELS, PIXELS,
										Image.SCALE_SMOOTH); 
		
	}
	
	//Advance things by one tick -- do gravity, check collisions
	public void tick( ) {
		
		myDy = myDy + GRAVITY; // increase dy
		myY += myDy; // figure new y
		
	

		//check if hit bottom
		if (myY + PIXELS >= getHeight( )) {
		//back y up
		myY -= myDy;


		//reverse direction of dy (i.e. bounce), but with 98% efficiency
		myDy = (int) (0.98 * -myDy);
		
		

		}
	}
	
	public void checkCollisions( ) {
		for (int i=0; i < myPoints.size( ); i++) {
			Point point = (Point) myPoints.get(i);
			//if we overlap a carrot, remove it
			if (Math.abs(point.getX( ) - myX) <= PIXELS
					   && Math.abs(point.getY( ) - myY) <= PIXELS) {
				
				myPoints.remove(i);    // removes the ith elem from an ArrayList 
				i--;             // tricky:
				                  // back i up, since we removed the ith element                 
				scoreValue += 1;
				repaint( );
			} 
		}
			if (myPoints.size( ) == 0) {
				reset( );                        //new game
				
				
			}
			
		}
	
	
}


Rabbit bunnyOne;
Rabbit bunnyTwo;

public CarrotComponent( ) {
	setPreferredSize(new Dimension(SIZE, SIZE));
	// getScaledInstance( ) gives us re-sized version of the image --
	// speeds up the drawImage( ) if the image is already the right size 
	// See paintComponent( )
//	
//	headImage = readImage("bugs-bunny2.jpg");
//	headImage = headImage.getScaledInstance(PIXELS, PIXELS,
//									Image.SCALE_SMOOTH); 
//	
	 bunnyOne = new Rabbit();
	 bunnyTwo = new Rabbit();
	
	carrotImage = readImage("carrot.gif");
	carrotImage = carrotImage.getScaledInstance(PIXELS, PIXELS,
										Image.SCALE_SMOOTH);
	myPoints = new ArrayList( );
}

//Utility -- create a random point within the window 
// leaving PIXELS space at the right and bottom
private Point randomPoint( ) {
Point p = new Point( (int) (Math.random( ) * (getWidth () - PIXELS)),
		(int) (Math.random( ) * (getHeight() - PIXELS))); 

return(p); 
}


//Reset things for the start of a game
public void reset( ) {
	myPoints.clear( ); // removes all the points 
	for (int i=0; i < CARROTS; i++) {
		myPoints.add(randomPoint( ) );
	}
	
	bunnyOne.myX = getWidth( ) / 2; 
	bunnyOne.myY = 0;
	bunnyOne.myDy = 0;
	bunnyTwo.myX = getWidth( ) / 2; 
	bunnyTwo.myY = 0;
	bunnyTwo.myDy = 0;
	repaint( );
}

//Advance bunnyOne and bunnyTwo by one tick -- do gravity, check collisions
public void tick2( ) {
	
bunnyOne.tick();
bunnyTwo.tick();
checkCollisions2( );
repaint( );
}


//Check the current x,y position of rabbit 1 & 2  vs. the carrots position
public void checkCollisions2( ) {
	bunnyOne.checkCollisions();
	bunnyTwo.checkCollisions();
	}

//Process one key click -- up, down, left, right
public void key(int code) {
	
	//control keys for bunnyTwo
	if (code == KeyEvent.VK_UP) { 
		bunnyOne.myY += -MOVE;
	}
	else if (code == KeyEvent.VK_DOWN) { 
		
		bunnyOne.myY += MOVE;
	}
	else if (code == KeyEvent.VK_LEFT) { 
		
		bunnyOne.myX += -MOVE;
	}
	else if (code == KeyEvent.VK_RIGHT) {
		bunnyOne.myX += MOVE;

	}
	//control keys for bunnyTwo
	if (code == KeyEvent.VK_W) { 
		bunnyTwo.myY += -MOVE;
	}
	else if (code == KeyEvent.VK_S) { 
		
		bunnyTwo.myY += MOVE;
	}
	else if (code == KeyEvent.VK_A) { 
		
		bunnyTwo.myX += -MOVE;
	}
	else if (code == KeyEvent.VK_D) {
		bunnyTwo.myX += MOVE;
		
	}
	
	checkCollisions2( ); 
	repaint( );
	
}

//Utility to read in an Image object
//If image cannot load, prints error output and returns null. 
private Image readImage(String filename) {
	Image image = null;
	try {
		image = ImageIO.read(new File(filename));
	}
	catch (IOException e) {
		System.out.println("Failed to load image '" + filename + "'");
		e.printStackTrace( );
	}
return(image); 
}

//Draws the head and carrots
public void paintComponent(Graphics g) {
	
	g.drawImage(bunnyOne.headImage1,bunnyOne.myX, bunnyOne.myY, PIXELS, PIXELS, null);
	g.drawImage(bunnyTwo.headImage2,bunnyTwo.myX, bunnyTwo.myY, PIXELS, PIXELS, null);
	//Draw all the carrots
	for (int i=0; i < myPoints.size( ); i++) { 
		Point p = (Point) myPoints.get(i);
	
		//point.getX( ) returns a double, so we must cast to int
	g.drawImage(carrotImage,(int) (p.getX( )), (int) (p.getY( )),
				PIXELS, PIXELS, null);
	// Draw score on frame scores for bunnyOne and bunnyTwo	,set color to Black
	g.setColor(Color.BLACK);
	g.drawString("Bunny1Score: " + bunnyOne.scoreValue, (int) CarrotFrame.WIDTH , (int) (CarrotFrame.HEIGHT + 20)); //Keep bunnyOne score at top left coor
	g.drawString("Bunny2Score: " + bunnyTwo.scoreValue, (int) (CarrotFrame.WIDTH + 380), (int) (CarrotFrame.HEIGHT + 20)); //Keep bunnyTwo score at top left conor

	}
}

public interface KeyListener extends EventListener {
	// Invoked when a key has been typed
	public void keyTyped(KeyEvent e);
	
	// invoked when a key has been pressed
	public void keyPressed(KeyEvent e);
	
	// invoked when a key has been released
	public void keyReleased(KeyEvent e);

}


public class keyAdaptor implements KeyListener {
	// Invoked when a key has been typed
	public void keyTyped(KeyEvent e) { } 
	
	// invoked when a key has been pressed
	public void keyPressed(KeyEvent e) { }
	
	// invoked when a key has been released
	public void keyReleased(KeyEvent e) { } 
}

	
	
	public interface ActionListener extends EventListener {
		
	// Invoked when an action is performed
	public void actionPerformed(KeyListener e); 
	}
	
	
}




