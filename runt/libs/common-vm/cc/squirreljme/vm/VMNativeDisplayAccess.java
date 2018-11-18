// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm;

import cc.squirreljme.runtime.cldc.asm.NativeDisplayAccess;
import cc.squirreljme.runtime.lcdui.event.EventType;
import cc.squirreljme.runtime.lcdui.gfx.PixelFormat;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Graphics;

/**
 * This class provides the framebuffer needed by SquirrelJME which is backed
 * on top of LCDUI itself. This just provides a single display.
 *
 * @since 2018/11/17
 */
public class VMNativeDisplayAccess
{
	/** The event queue size with the type. */
	public static final int EVENT_SIZE_WITH_TYPE =
		NativeDisplayAccess.EVENT_SIZE + 1;
	
	/** The number of events to store in the buffer. */
	public static final int QUEUE_SIZE =
		256;
	
	/** The limit of the event queue. */
	public static final int QUEUE_LIMIT =
		EVENT_SIZE_WITH_TYPE * QUEUE_SIZE;
	
	/** The event queue, a circular buffer. */
	private final short[] _eventqueue =
		new short[EVENT_SIZE_WITH_TYPE * QUEUE_SIZE];
	
	/** The read position. */
	private int _eventread;
	
	/** The write position. */
	private int _eventwrite;
	
	/** The display to back on, lazily initialized to prevent crashing. */
	Display _usedisplay;
	
	/** The framebuffer RGB data. */
	volatile int[] _fbrgb;
	
	/** The framebuffer width. */
	volatile int _fbw =
		-1;
	
	/** The framebuffer height. */
	volatile int _fbh =
		-1;
	
	/** The canvas for display. */
	volatile VMCanvas _canvas;

	/**
	 * Returns the capabilities of the display.
	 *
	 * @param __id The display ID.
	 * @return The capabilities of the display.
	 * @since 2018/11/17
	 */
	public final int capabilities(int __id)
	{
		// Only a single display is supported
		if (__id != 0)
			return 0;
		
		// Just directly pass the capabilities of this display
		return this.__display().getCapabilities();
	}
	
	/**
	 * Returns the object representing the framebuffer data.
	 *
	 * @param __id The display ID.
	 * @return The framebuffer array.
	 * @since 2018/11/18
	 */
	public final Object framebufferObject(int __id)
	{
		if (__id != 0)
			return null;
		
		// Check for framebuffer update
		this.__checkFramebuffer();
		
		// Return the raw RGB array
		return this._fbrgb;
	}
	
	/**
	 * Specifies that the framebuffer has been painted.
	 *
	 * @param __id The display ID.
	 * @since 2018/11/18
	 */
	public final void framebufferPainted(int __id)
	{
		if (__id != 0)
			return;
		
		this.__checkFramebuffer().repaint();
	}
	
	/**
	 * Returns the palette of the framebuffer.
	 *
	 * @param __id The display ID.
	 * @return The palette of the framebuffer.
	 * @since 2018/11/18
	 */
	public final int[] framebufferPalette(int __id)
	{
		if (__id != 0)
			return null;
		
		// No palette is used
		return null;
	}
	
	/**
	 * Returns the parameters of the framebuffer.
	 *
	 * @param __id The display ID.
	 * @return The framebuffer parameters.
	 * @since 2018/11/18
	 */
	public final int[] framebufferParameters(int __id)
	{
		if (__id != 0)
			return null;
		
		// Check for framebuffer update
		this.__checkFramebuffer();
		
		// Fill in basic parameters
		int[] rv = new int[NativeDisplayAccess.NUM_PARAMETERS];
		
		// These are the only used fields
		int fbw = this._fbw,
			fbh = this._fbh;
		
		// Build parameters
		rv[NativeDisplayAccess.PARAMETER_PIXELFORMAT] =
			PixelFormat.INTEGER_RGB888.ordinal();
		rv[NativeDisplayAccess.PARAMETER_BUFFERWIDTH] = fbw;
		rv[NativeDisplayAccess.PARAMETER_BUFFERHEIGHT] = fbh;
		rv[NativeDisplayAccess.PARAMETER_ALPHA] = 0;
		rv[NativeDisplayAccess.PARAMETER_PITCH] = fbw;
		rv[NativeDisplayAccess.PARAMETER_OFFSET] = 0;
		rv[NativeDisplayAccess.PARAMETER_VIRTXOFF] = 0;
		rv[NativeDisplayAccess.PARAMETER_VIRTYOFF] = 0;
		
		return rv;
	}
	
	/**
	 * Is the specified display upsidedown?
	 *
	 * @param __id The ID of the display.
	 * @return If the display is upsidedown.
	 * @since 2018/11/17
	 */
	public final boolean isUpsideDown(int __id)
	{
		if (__id != 0)
			return false;
		
		// Only certain orientations are considered upside-down
		switch (this.__display().getOrientation())
		{
			case Display.ORIENTATION_LANDSCAPE_180:
			case Display.ORIENTATION_PORTRAIT_180:
				return true;
			
				// Not upsidedown
			default:
				return false;
		}
	}
	
	/**
	 * Returns the number of displays which are available.
	 *
	 * @return The number of available displays.
	 * @since 2018/11/17
	 */
	public final int numDisplays()
	{
		return 1;
	}
	
	/**
	 * Polls the next event, blocking until the next one occurs.
	 *
	 * @param __ed Event data.
	 * @return The next event, this will be the even type.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/11/17
	 */
	public final int pollEvent(short[] __ed)
		throws NullPointerException
	{
		if (__ed == null)
			throw new NullPointerException("NARG");
		
		// Maximum number of data points to write
		int edlen = Math.min(__ed.length, NativeDisplayAccess.EVENT_SIZE);
		
		// Constantly poll for events
		short[] eventqueue = this._eventqueue;
		for (;;)
			synchronized (eventqueue)
			{
				// Read positions
				int eventread = this._eventread,
					eventwrite = this._eventwrite;
				
				// This a circular buffer, so if the values do not match
				// then that means an event was found.
				if (eventread != eventwrite)
				{
					// Base pointer for reading events
					int baseptr = eventread;
					
					// Read the type for later return
					int type = eventqueue[baseptr++];
					
					// Copy event data
					for (int o = 0; o < edlen;)
						__ed[o++] = eventqueue[baseptr++];
					
					// Make sure the read position does not overflow the
					// buffer
					int nexter = eventread + EVENT_SIZE_WITH_TYPE;
					if (nexter >= QUEUE_LIMIT)
						nexter = 0;
					this._eventread = nexter;
					
					// And the type of the event
					return type;
				}
				
				// Wait for an event to appear
				try
				{
					eventqueue.wait();
				}
				
				// Just treat like an event might have happened
				catch (InterruptedException e)
				{
				}
			}
	}
	
	/**
	 * Posts the specified event to the end of the event queue.
	 *
	 * @param __type The event type to push.
	 * @param __d0 Datapoint 1.
	 * @param __d1 Datapoint 2.
	 * @param __d2 Datapoint 3.
	 * @param __d3 Datapoint 4.
	 * @param __d4 Datapoint 5.
	 * @since 2018/11/18
	 */
	public final void postEvent(int __type,
		int __d0, int __d1, int __d2, int __d3, int __d4)
	{
		// Lock on the queue
		short[] eventqueue = this._eventqueue;
		synchronized (eventqueue)
		{
			int eventwrite = this._eventwrite;
			
			// Overwrite all the data
			eventqueue[eventwrite++] = (short)__type;
			eventqueue[eventwrite++] = (short)__d0;
			eventqueue[eventwrite++] = (short)__d1;
			eventqueue[eventwrite++] = (short)__d2;
			eventqueue[eventwrite++] = (short)__d3;
			eventqueue[eventwrite++] = (short)__d4;
			
			// Go back to the start?
			if (eventwrite >= QUEUE_LIMIT)
				eventwrite = 0;
			
			// Overflowed?
			if (eventwrite == this._eventread)
			{
				todo.DEBUG.note("Event loop overflow!");
				return;
			}
			
			// Set new position
			this._eventwrite = eventwrite;
			
			// Notify that an event was put in the queue
			eventqueue.notifyAll();
		}
	}
	
	/**
	 * Checks if the framebuffer needs updating.
	 *
	 * @return The canvas used.
	 * @since 2018/11/18
	 */
	private final VMCanvas __checkFramebuffer()
	{
		// Need to create the canvas?
		VMCanvas canvas = this._canvas;
		if (canvas == null)
		{
			// Display this canvas
			this._canvas = (canvas = new VMCanvas());
			this._usedisplay.setCurrent(canvas);
			
			// Add exit command
			canvas.setCommandListener(new __CommandListener__());
			canvas.addCommand(new Command("Exit", Command.EXIT,
				Integer.MAX_VALUE));
		}
		
		// Properties have changed? Recreate the buffer data
		int cw = canvas.getWidth(),
			ch = canvas.getHeight();
		if (this._fbrgb == null || cw != this._fbw || ch != this._fbh)
		{
			this._fbrgb = new int[cw * ch];
			this._fbw = cw;
			this._fbh = ch;
		}
		
		return canvas;
	}
	
	/**
	 * Returns the display this is currently using.
	 *
	 * @return The currently backed display.
	 * @since 2018/11/17
	 */
	private final Display __display()
	{
		Display rv = this._usedisplay;
		if (rv == null)
			this._usedisplay = (rv = Display.getDisplays(0)[0]);
		return rv;
	}
	
	/**
	 * Listens for commands.
	 *
	 * @since 2018/11/18
	 */
	final class __CommandListener__
		implements CommandListener
	{
		/**
		 * {@inheritDoc}
		 * @since 2018/11/18
		 */
		@Override
		public void commandAction(Command __c, Displayable __d)
		{
			// Exiting the VM?
			if (__c.getCommandType() == Command.EXIT)
				VMNativeDisplayAccess.this.postEvent(
					EventType.EXIT_REQUEST.ordinal(),
					0, -1, -1, -1, -1);
		}
	}
	
	/**
	 * Contains the canvas which is drawn on.
	 *
	 * @since 2018/11/18
	 */
	public final class VMCanvas
		extends Canvas
	{
		/**
		 * {@inheritDoc}
		 * @since 2018/11/18
		 */
		@Override
		public void paint(Graphics __g)
		{
			// Just draw the raw RGB data
			int fbw = VMNativeDisplayAccess.this._fbw;
			__g.drawRGB(VMNativeDisplayAccess.this._fbrgb,
				0,
				fbw,
				0,
				0,
				fbw,
				VMNativeDisplayAccess.this._fbh,
				false);
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2018/11/18
		 */
		@Override
		public void sizeChanged(int __w, int __h)
		{
			// The framebuffer will need to be redone
			VMNativeDisplayAccess.this.__checkFramebuffer();
			
			// Post event
			VMNativeDisplayAccess.this.postEvent(
				EventType.DISPLAY_SIZE_CHANGED.ordinal(),
				0, __w, __h, -1, -1);
		}
	}
}

