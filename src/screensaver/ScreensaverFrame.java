package screensaver;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

import javax.swing.JFrame;

/**
 * A JFrame that contains the main application.
 * 
 */
@SuppressWarnings("serial")
public class ScreensaverFrame extends JFrame {
	
	/**
	 * Get screen size and pass the width and height to a new Screensaver, then add the Screensaver to the frame.
	 * @see Screensaver
	 */
	public ScreensaverFrame() {
		GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[0];	// get device
		int frameWidth = device.getDisplayMode().getWidth();	// get screen width
		int frameHeight = device.getDisplayMode().getHeight();	// get screen height
//		int frameWidth = 1280;
//		int frameHeight = 800;
		setSize(frameWidth, frameHeight);	// set size of frame
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setUndecorated(true);	// remove title bar
		device.setFullScreenWindow(this);	// set full screen
		
		// add panel to frame
		Screensaver screensaver = new Screensaver(frameWidth, frameHeight);
		this.add(screensaver);
	}
}
