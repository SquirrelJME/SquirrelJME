// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.lcduidemo;

import java.util.Random;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Graphics;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

/**
 * This is a MIDlet which simulates the old-style Mystify Your Mind
 * screensaver from Windows 3.1.
 *
 * @since 2018/11/22
 */
public class Mystify
	extends MIDlet
{
	/** The number of polygon points. */
	public static final int NUM_POINTS =
		4;
	
	/** The number of shadows to draw. */
	public static final int NUM_SHADOWS =
		5;
	
	/** The maximum line speed. */
	public static final int MAX_SPEED =
		5;
	
	/** Use to detect way off coordinates. */
	public static final int WAY_OFF =
		50;
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/22
	 */
	@Override
	protected void destroyApp(boolean __uc)
		throws MIDletStateChangeException
	{
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/22
	 */
	@Override
	protected void startApp()
		throws MIDletStateChangeException
	{
		// Setup canvas
		DemoCanvas cv = new DemoCanvas();
		
		// Set display to the canvas
		Display.getDisplay(this).setCurrent(cv);
		
		// Setup thread to force repaints on canvas
		new RepaintTimer(cv, 100).start();
	}
	
	/**
	 * The demo canvas which does the animation.
	 *
	 * @since 2018/11/22
	 */
	static public final class DemoCanvas
		extends Canvas
	{
		/** Random number generator for bounces and such. */
		protected final Random random =
			new Random();
		
		/** Points and their shadows. */
		protected final Point[][] points =
			new Point[NUM_SHADOWS][NUM_POINTS];
		
		/** Colors. */
		protected final int[] colors =
			new int[NUM_SHADOWS];
		
		/** The direction of the points. */
		protected final Point[] direction =
			new Point[NUM_POINTS];
		
		/**
		 * Initializes the canvas state.
		 *
		 * @since 2018/11/22
		 */
		{
			// Setup title
			this.setTitle("Mystify Your Squirrels");
			
			// Draw as opaque
			this.setPaintMode(false);
			
			// Setup points
			Point[][] points = this.points;
			
			// Generate random start points
			Random random = this.random;
			Point[] start = points[0];
			for (int i = 0; i < NUM_POINTS; i++)
				start[i] = new Point(random.nextInt(), random.nextInt());
			
			// Copy all the points to the shadow
			for (int i = 1; i < NUM_SHADOWS; i++)
				for (int j = 0; j < NUM_POINTS; j++)
					points[i][j] = new Point(start[j]);
			
			// Initialize the color cycle
			int[] colors = this.colors;
			for (int i = 0; i < NUM_SHADOWS; i++)
				colors[i] = random.nextInt();
			
			// Determine new directions for all the points
			Point[] direction = this.direction;
			for (int i = 0; i < NUM_POINTS; i++)
				direction[i] = this.__newDirection(new Point(),
					random.nextBoolean(), random.nextBoolean());
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2018/11/22
		 */
		@Override
		public void paint(Graphics __g)
		{
			// Get widget bounds
			int w = this.getWidth(),
				h = this.getHeight();
			
			// Needed for cycling	
			Random random = this.random;
			
			// Needed for drawing
			Point[][] points = this.points;
			Point[] direction = this.direction;
			int[] colors = this.colors;
			
			// Draw every point, from older shadows to the newer shape
			for (int i = NUM_SHADOWS - 1; i >= 0; i--)
			{
				Point[] draw = points[i];
				
				// Jump the first set of lines before moving everything
				// down to the shadows
				if (i == 0)
				{
					// Move all the old points down
					for (int j = NUM_SHADOWS - 2; j >= 0; j--)
						points[j + 1] = points[j];
					
					// Allocate a new set of points offset in the directions
					Point[] place = new Point[NUM_POINTS];
					for (int j = 0; j < NUM_POINTS; j++)
					{
						// Get base coordinates
						int newx = draw[j].x,
							newy = draw[j].y;
						
						// Limit to the bounds of the screen. If a point is
						// way off, just choose a random point on the
						// screen instead
						if (newx < 0 || newx >= w)
						{
							if (newx < -WAY_OFF || newy > w + WAY_OFF)
								newx = random.nextInt(w);
							else
							{
								if (newx < 0)
									newx = 0;
								else if (newx >= w)
									newx = w;
							}
						}
						
						if (newy < 0 || newy >= h)
						{
							if (newy < -WAY_OFF || newy > h + WAY_OFF)
								newy = random.nextInt(h);
							else
							{
								if (newy < 0)
									newy = 0;
								else if (newy >= h)
									newy = h;
							}
						}
						
						// Move point in the target direction
						newx += direction[j].x;
						newy += direction[j].y;
						
						// Previous positive position?
						boolean ppx = (direction[j].x > 0),
							ppy = (direction[j].y > 0);
						
						// Deflect points?
						boolean defx = (newx < 0 || newx >= w),
							defy = (newy < 0 || newy >= h);
						
						// Deflect direction
						this.__newDirection(direction[j],
							ppx ^ defx, ppy ^ defy);
						
						// Make sure the points are not on a bound!
						if (defx)
							if (ppx)
								newx--;
							else
								newx++;
						if (defy)
							if (ppy)
								newx--;
							else
								newy++;
						
						// Store the point
						place[j] = new Point(newx, newy);
					}
					
					// Use all these points
					points[0] = place;
				}
				
				// Set the color for this shadow
				__g.setColor(colors[i]);
				
				// Draw all the points
				for (int j = 0; j < NUM_POINTS; j++)
				{
					// Get A and B points
					Point a = draw[j],
						b = draw[(j + 1) % NUM_POINTS];
					
					// Draw line
					__g.drawLine(a.x, a.y, b.x, b.y);
				}
			}
		}
		
		/**
		 * Gives a new direction for the point.
		 *
		 * @param __p The input point to get a new direction for.
		 * @param __px Positive X?
		 * @param __py Positive Y?
		 * @return {@code __p}
		 * @throws NullPointerException On null arguments.
		 * @since 2018/11/22
		 */
		private Point __newDirection(Point __p, boolean __px, boolean __py)
			throws NullPointerException
		{
			if (__p == null)
				throw new NullPointerException("NARG");
			
			// Generate new speeds
			Random random = this.random;
			int x = random.nextInt(MAX_SPEED) + 1,
				y = random.nextInt(MAX_SPEED) + 1;
			
			// Flip signs?
			if (!__px)
				x = -x;
			if (!__py)
				y = -y;
			
			// set
			__p.x = x;
			__p.y = y;
			
			// Use that point
			return __p;
		}
	}
	
	/**
	 * Volatile point information.
	 *
	 * @since 2018/11/22
	 */
	static public final class Point
	{
		/** X position. */
		public int x;
		
		/** Y position. */
		public int y;
		
		/**
		 * Initializes the point.
		 *
		 * @since 2018/11/22
		 */
		public Point()
		{
		}
		
		/**
		 * Initializes the point from another point.
		 *
		 * @param __p The point to copy.
		 * @throws NullPointerException On null arguments.
		 * @since 2018/11/22
		 */
		public Point(Point __p)
			throws NullPointerException
		{
			if (__p == null)
				throw new NullPointerException("NARG");
			
			this.x = __p.x;
			this.y = __p.y;
		}
		
		/**
		 * Initializes the point from the given coordinates.
		 *
		 * @param __x The X coordinate.
		 * @param __y The Y coordinate.
		 * @since 2018/11/22
		 */
		public Point(int __x, int __y)
		{
			this.x = __x;
			this.y = __y;
		}
	}
}

