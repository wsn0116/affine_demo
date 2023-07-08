import java.awt.*;
import java.applet.Applet;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.geom.AffineTransform;
import java.lang.StrictMath;

public class demo extends java.applet.Applet implements Runnable
{
	private int width = 512, height = 512;
	private BufferedImage offscreen = null;
	private Thread t = null;

	public void init()
	{
		resize(width, height);
	}

	public void start()
	{
		if ( offscreen == null ){
			offscreen = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
			Graphics2D g = offscreen.createGraphics(); 
			g.setBackground(Color.black);
		}
		if ( t == null ){
			t = new Thread (this);
			t.setPriority( Thread.MIN_PRIORITY );
			t.start();
		}
	}

	public void stop()
	{
		if ( offscreen != null ){
			offscreen = null;
		}
		if ( t != null ){
			t = null;
		}
		System.gc();
	}

	public void destroy()
	{
		stop();
	}

	public void run()
	{
		Graphics2D g = (Graphics2D)offscreen.getGraphics();
		Image image = getImage(getDocumentBase(), "demo.jpg");

		MediaTracker mt = new MediaTracker(this);
		mt.addImage(image, 0);
		try {
			mt.waitForAll();
		} catch (Exception e) {
			System.out.println("ERROR!!");
		}

		AffineTransform a = new AffineTransform();

		int x = (width / 2), y = (height / 2);

		g.setColor(g.getBackground());
		g.fillRect(0, 0, width, height);

		while (t != null) {
			for (double i = 0; i <= 30; i++ ) {
				a.rotate((StrictMath.toRadians(i)), x, y);
				g.drawImage(image, a, this);

				repaint();
				pauseproc(100);
				g.setColor(g.getBackground());
				g.fillRect(0, 0, width, height);
			}
		}

		try {
			Thread.sleep(1000);
		} catch(Exception e) {}
	}

	public void update(Graphics g)
	{
		paint(g);
	}

	public void paint(Graphics g)
	{
		super.paint(g);

		Graphics2D g2d = (Graphics2D)g;
		g2d.drawImage(offscreen, 0, 0, this);
	}

	private void pauseproc(int pause)
	{
		try {
			Thread.sleep(pause);
		} catch(Exception e) {}
	}
}
