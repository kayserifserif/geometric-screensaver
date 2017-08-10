package screensaver;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Polygon;
import java.awt.RadialGradientPaint;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.TexturePaint;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 * The main application containing the graphics.
 *
 */
@SuppressWarnings("serial")
public class Screensaver extends JPanel implements ActionListener {
	// VARIABLES
	/**
	 * A Swing Timer for animation.
	 */
	private Timer timer;
	
	private final int frameWidth;
	private final int frameHeight;
	
	// DESIGN
	private Color shapeColor;
	private Color textColor;
	/**
	 * The image used as the background texture.
	 */
	private BufferedImage background = new BufferedImage(500, 500, BufferedImage.TYPE_INT_RGB);	// texture
	private TexturePaint texture;
	private RadialGradientPaint gradient;														// gradient
	/**
	 * The colors for the radial gradient.
	 */
	private Color[] gradientColors = new Color[2];
	/**
	 * The distribution of colors for the radial gradient.
	 */
	private final float[] gradientDistro = {0.0f, 1.0f};
	
	// SHAPE CONSTRUCTION
	/**
	 * The number used to determine the core shape.
	 */
	private int coreShape;
	/**
	 * The x positions of the shapes, relative to the width of the frame.
	 */
	private double[][] xPos = {{50/100.0, 60/100.0, 40/100.0},
								   {52/100.0, 52/100.0, 46/100.0},
								   {52/100.0, 50/100.0, 48/100.0}};
	/**
	 * The y positions of the shapes, relative to the height of the frame.
	 */
	private double[][] yPos = {{30/100.0, 60/100.0, 60/100.0},
								   {45/100.0, 55/100.0, 50/100.0},
								   {48/100.0, 53/100.0, 48/100.0}};
	private double[][] rotations = {new double[3], new double[3], new double[3]};
	/**
	 * The diameters of the shapes.
	 */
	private final double[] diameter = {800.0, 180.0, 30.0};
	/**
	 * The line widths of the shapes.
	 */
	private final int[] lineWidth = {40, 13, 3};
	/**
	 * The shape objects.
	 */
	private Shape[][] shapes = {new Shape[3], new Shape[3], new Shape[3]};
	
	// ANIMATION
	private final double[][] thetas = {{0, 400, 800}, {0, 400, 800}, {0, 400, 800}};
	/**
	 * The diameters of the circular paths used to guide each group of shapes.
	 */
	private final double[] guides = {350.0, 70.0, 20.0};
	
	// TEXT
	private String time = "";
	private Font font;
	private final int fontSize = 400;
	/**
	 * The metrics of the font.
	 */
	private FontRenderContext frc;
	private int textWidth;
	private int textHeight;
	
	
	/**
	 * Creates a new Screensaver with a specified size, then creates the resources needed for the graphics.
	 * @param frameWidth The width of the frame.
	 * @param frameHeight The height of the frame.
	 */
	public Screensaver(int frameWidth, int frameHeight) {
		this.frameWidth = frameWidth;
		this.frameHeight = frameHeight;
		
		timer = new Timer(1, this);
		timer.start();
		coreShape = (int)(Math.random() * 3 + 1);	// pick a random number from 1 to 3, inclusive
		
		createBackground();
		createAndSetFont();
	}
	
	/**
	 * Creates the background by setting the texture and radial gradient.
	 */
	public void createBackground() {
		
		// use the image file as the texture
		try {
			background = ImageIO.read(getClass().getResourceAsStream("resources/linen.png"));
		} catch(IOException e) {
			e.printStackTrace();
		}
		texture = new TexturePaint(background, new Rectangle(0, 0, 500, 500));
		
		//  gradient
		float[] center = {(float)Math.random(), 0.1f, 0.8f};
		Color gradientLight = Color.getHSBColor(center[0], center[1], center[2]);
		Color gradientDark = Color.getHSBColor(center[0], center[1] + 0.1f, center[2] - 0.03f);
		gradientColors = new Color[]{gradientLight, gradientDark};
		gradient = new RadialGradientPaint(frameWidth/2, frameHeight/2, frameWidth/2, gradientDistro, gradientColors);
		
		// text
		textColor = Color.getHSBColor(center[0], 0.02f, 0.95f);
		
		// shape
		float[] shape = {center[0] - 0.3f, center[1] - 0.05f, center[2] + 0.1f};
		for(int i = 1; i < 3; i++) {
			if(shape[i] > 1f) shape[i] = 1f;
			if(shape[i] < 0f) shape[i] = 0f;
		}
		shapeColor = Color.getHSBColor(shape[0], shape[1], shape[2]);
	}
	
	/**
	 * Creates and sets the font used for the text.
	 */
	public void createAndSetFont() {
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		try {
			ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream("resources/Oswald-Regular.ttf")));
		} catch(IOException|FontFormatException e) {
			e.printStackTrace();
		}
		font = new Font("Oswald", Font.PLAIN, fontSize);
	}
	
	/**
	 * Repaints the background, shapes, and text.
	 */
	@Override
	public void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		
		g2d.setPaint(gradient);
		g2d.fillRect(0, 0, frameWidth, frameHeight);
		

		g2d.setPaint(texture);
		g2d.fillRect(0, 0, frameWidth, frameHeight);
		
		g2d.setColor(shapeColor);
		
		for(int n = 0; n < 3; n ++) {
			g2d.setStroke(new BasicStroke(lineWidth[n]));						// use one line width for each group of shapes
			for(int i = 0; i < 3; i++) {
				Path2D.Double path = new Path2D.Double();						// create the object to be drawn
				switch(coreShape) {													// execute according to the randomly generated number
					case 1: path = new Path2D.Double(createCircle(n, i));
							break;
					case 2: path = new Path2D.Double(createSquare(n, i));
							break;
					case 3: path = new Path2D.Double(createTriangle(n, i));
							break;
					default: break;
				}
				setRotation(n, i, path);
				g2d.draw(shapes[n][i]);
			}
		}
		
		g2d.setColor(textColor);
		g2d.setFont(font);
		frc = g2d.getFontRenderContext();
		setFontMetrics(frc);
		g2d.drawString(time, frameWidth/2 - textWidth/2, frameHeight/2 + textHeight/2);
	}
	
	/**
	 * Returns a new ellipse based on the parameters.
	 * @param n the current group
	 * @param i the current shape in the group
	 * @return the new ellipse
	 */
	public Ellipse2D.Double createCircle(int n, int i) {
		Ellipse2D.Double circle = new Ellipse2D.Double(xPos[n][i] - diameter[n]/2,
													   yPos[n][i] - diameter[n]/2,
													   diameter[n],
													   diameter[n]);
		return circle;
	}
	
	/**
	 * Returns a new square based on the parameters.
	 * @param n the current group
	 * @param i the current shape in the group
	 * @return the new square
	 */
	public Rectangle2D.Double createSquare(int n, int i) {
		Rectangle2D.Double square = new Rectangle2D.Double(xPos[n][i] - diameter[n]/2, 
														   yPos[n][i] - diameter[n]/2, 
														   diameter[n], 
														   diameter[n]);
		return square;
	}
	
	/**
	 * Returns a new triangle based on the parameters. 
	 * Code from <a href="http://www.experts-exchange.com/questions/23232637/Draw-Equilateral-triangle-at-x-y.html">"
	 * 				Draw Equilateral triangle at x,y"</a>.
	 * @param n the current group
	 * @param i the current shape in the group
	 * @return the new triangle
	 */
	public Polygon createTriangle(int n, int i) {
		double angle = 60;
		double r = (diameter[n]) / Math.sin(Math.PI * 60.0 / 180.0);
		int[] pointX = new int[3];
		int[] pointY = new int[3];																
		for(int a = 0; a < 3; a++) {																	// for each point
			pointX[a] = (int)(xPos[n][i] + r*Math.cos(angle + a * 2. * Math.PI / 3.));
			pointY[a] = (int)(yPos[n][i] + r*Math.sin(angle + a * 2. * Math.PI / 3.));
		}
		Polygon triangle = new Polygon(pointX, pointY, 3);
		return triangle;
	}
	
	/**
	 * Sets the rotation for the current shape and adds the new shape to the array of shape objects.
	 * @param n The current group.
	 * @param i The current shape in the group.
	 * @param path the path to be rotated
	 */
	public void setRotation(int n, int i, Path2D path) {
		AffineTransform at = AffineTransform.getRotateInstance(rotations[n][i],
															   xPos[n][i],
															   yPos[n][i]);
		Shape rotated = at.createTransformedShape(path);
		shapes[n][i] = rotated;
	}
	
	/**
	 * Sets the textWidth and textHeight variables according to the font and the new text.
	 * @param frc the FontRenderContext
	 * @see	textWidth
	 * @see	textHeight
	 */
	public void setFontMetrics(FontRenderContext frc) {
		GlyphVector gv = font.createGlyphVector(frc, time);
		textWidth = (int)gv.getVisualBounds().getWidth();
		textHeight = (int)gv.getVisualBounds().getHeight();
	}
	
	/**
	 * Updates the shape position, shape rotation, and time values.
	 * @see	#updatePosition(int n, int i)
	 * @see	#updateRotation(int n, int i)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		for(int n = 0; n < 3; n++) {
			for(int i = 0; i < 3; i++) {
				updatePosition(n, i);
				updateRotation(n, i);
			}
		}
		
		// set String to new current time
		SimpleDateFormat formatted = new SimpleDateFormat("HH:mm");
		time = formatted.format(new Date());
		
		repaint();
	}
	
	/**
	 * Updates the x and y positions of the current shape.
	 * @param n the current group
	 * @param i the current shape
	 */
	public void updatePosition(int n, int i) {
		thetas[n][i] += Math.toRadians((n + 0.5)*2.5)
						* Math.pow(-1, n + 1);														// alternate direction of movement
		xPos[n][i] = frameWidth/2 + guides[n]/2 * Math.cos(thetas[n][i]);
		yPos[n][i] = frameHeight/2 + guides[n]/2 * Math.sin(thetas[n][i]);
	}
	
	/**
	 * Updates the rotations of the current shape.
	 * @param n the current group
	 * @param i the current shape
	 */
	public void updateRotation(int n, int i) {
		rotations[n][i] = (Math.toDegrees(thetas[n][0]) / 100) * -1;								// set direction of rotation to inverse of direction of movement
	}
}
