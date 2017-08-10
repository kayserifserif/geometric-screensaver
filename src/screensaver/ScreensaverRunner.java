package screensaver;

/**
 * A class that creates the frame that holds the application.
 *
 */
public class ScreensaverRunner {
	/**
	 * Create ScreensaverFrame and make it visible.
	 * @param args	arguments
	 * @see ScreensaverFrame
	 */
	public static void main(String[] args) {
		ScreensaverFrame frame = new ScreensaverFrame();	// create new frame
		frame.setVisible(true);	// show frame
	}
}