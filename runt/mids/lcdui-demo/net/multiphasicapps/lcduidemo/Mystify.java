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
		5;
	
	/** The number of shadows to draw. */
	public static final int NUM_SHADOWS =
		7;
	
	/** The maximum line speed. */
	public static final int MAX_SPEED =
		9;
	
	/** Use to detect way off coordinates. */
	public static final int WAY_OFF =
		50;
	
	/** How oftens the colors shift. */
	public static final int COLOR_SHIFT =
		2;
	
	/** The delay time. */
	public static final int DELAY_TIME =
		250;
	
	/** Delay time in nanoseconds. */
	public static final long DELAY_TIME_NS =
		DELAY_TIME * 1_000_000L;
	
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
		
		// Exit command
		cv.addCommand(Exit.command);
		cv.setCommandListener(new Exit());
		
		// Set display to the canvas
		Display.getDisplay(this).setCurrent(cv);
		
		// Setup thread to force repaints on canvas
		new RepaintTimer(cv, DELAY_TIME).start();
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
		
		/** Update lock to prevent multiple threads updating at once. */
		private volatile boolean _lockflag;
		
		/** The last time an update happened. */
		private volatile long _nextnano =
			Long.MIN_VALUE;
		
		/**
		 * Initializes the canvas state.
		 *
		 * @since 2018/11/22
		 */
		{
			// Setup title
			this.setTitle("Mystify Your Squirrels");
			
			// Draw as transparent
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
			
			// Used to limit update rate
			long now = System.nanoTime(),
				nextnano = this._nextnano;
			
			// Draw every point, from older shadows to the newer shape
			for (int i = NUM_SHADOWS - 1; i >= 0; i--)
			{
				Point[] draw = points[i];
				
				// Update the state of the demo, but keep it consistent so
				// that it is not always updating
				if (i == 0 && now >= nextnano)
				{
					// Only allow a single run to update this
					boolean lockflag;
					synchronized (this)
					{
						// Set the lock flag
						lockflag = this._lockflag;
						if (!lockflag)
							this._lockflag = true;
					}
					
					// If we did hit a false, we can update
					if (!lockflag)
						try
						{
							// Update the state
							this.__updateState(w, h);
						}
						finally
						{
							// Always clear the flag so another run can
							// have a go
							this._lockflag = false;
							
							// Update some other time in the future
							this._nextnano = System.nanoTime() + DELAY_TIME_NS;
						}
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
		
		/**
		 * Updates the state of the demo.
		 *
		 * @param __w The width.
		 * @param __h The height.
		 * @since 2018/11/22
		 */
		private void __updateState(int __w, int __h)
		{
			// Needed for cycling	
			Random random = this.random;
			
			// Base points to modify
			Point[] draw = points[0];
			
			// Needed for drawing
			Point[][] points = this.points;
			Point[] direction = this.direction;
			int[] colors = this.colors;
			
			// Move all the old points and colors down
			for (int j = NUM_SHADOWS - 2; j >= 0; j--)
			{
				points[j + 1] = points[j];
				colors[j + 1] = colors[j];
			}
			
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
				if (newx < 0 || newx >= __w)
				{
					if (newx < -WAY_OFF || newy > __w + WAY_OFF)
						newx = random.nextInt(__w);
					else
					{
						if (newx < 0)
							newx = 0;
						else if (newx >= __w)
							newx = __w;
					}
				}
				
				if (newy < 0 || newy >= __h)
				{
					if (newy < -WAY_OFF || newy > __h + WAY_OFF)
						newy = random.nextInt(__h);
					else
					{
						if (newy < 0)
							newy = 0;
						else if (newy >= __h)
							newy = __h;
					}
				}
				
				// Move point in the target direction
				newx += direction[j].x;
				newy += direction[j].y;
				
				// Previous positive position?
				boolean ppx = (direction[j].x >= 0),
					ppy = (direction[j].y >= 0);
				
				// Deflect points?
				boolean defx = (newx <= 0 || newx >= __w),
					defy = (newy <= 0 || newy >= __h);
				
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
				
				// Extract the color
				int color = colors[j];
				byte r = (byte)((color & 0xFF0000) >>> 16),
					g = (byte)((color & 0x00FF00) >>> 8),
					b = (byte)((color & 0x0000FF));
				
				// Cycle the colors depending on the direction of travel
				r += (ppx ^ ppy ? +COLOR_SHIFT : -COLOR_SHIFT);
				g += (ppy | ppy ? -COLOR_SHIFT : +COLOR_SHIFT);
				b += (ppx ^ ppy ? -COLOR_SHIFT : +COLOR_SHIFT);
				
				// Recombine the color
				colors[j] = ((r & 0xFF) << 16) |
					((g & 0xFF) << 8) |
					(b & 0xFF);
				
				// Store the point
				place[j] = new Point(newx, newy);
			}
			
			// Use all these points
			points[0] = place;
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

