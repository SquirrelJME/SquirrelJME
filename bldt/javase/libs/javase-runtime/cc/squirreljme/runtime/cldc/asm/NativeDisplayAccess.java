// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc.asm;

import javax.microedition.lcdui.Display;

/**
 * Java SE implementation of the native display system using Swing.
 *
 * @since 2018/11/16
 */
public final class NativeDisplayAccess
{
	/** The maximum number of integers for event data. */
	public static final int EVENT_SIZE =
		5;
	
	/** The number of parameters available. */
	public static final int NUM_PARAMETERS =
		8;
	
	/** The pixel format. */
	public static final int PARAMETER_PIXELFORMAT =
		0;
	
	/** The buffer width. */
	public static final int PARAMETER_BUFFERWIDTH =
		1;
	
	/** The buffer height. */
	public static final int PARAMETER_BUFFERHEIGHT =
		2;
	
	/** Alpha channel is used? */
	public static final int PARAMETER_ALPHA =
		3;
	
	/** Buffer pitch. */
	public static final int PARAMETER_PITCH =
		4;
	
	/** Buffer offset. */
	public static final int PARAMETER_OFFSET =
		5;
	
	/** Virtual X offset. */
	public static final int PARAMETER_VIRTXOFF =
		6;
	
	/** Virtual Y offset. */
	public static final int PARAMETER_VIRTYOFF =
		7;
	
	/** The event queue size with the type. */
	public static final int EVENT_SIZE_WITH_TYPE =
		NativeDisplayAccess.EVENT_SIZE + 1;
	
	/** The number of events to store in the buffer. */
	public static final int QUEUE_SIZE =
		48;
	
	/** The limit of the event queue. */
	public static final int QUEUE_LIMIT =
		EVENT_SIZE_WITH_TYPE * QUEUE_SIZE;
	
	/** The event queue, a circular buffer. */
	private static final short[] _eventqueue =
		new short[EVENT_SIZE_WITH_TYPE * QUEUE_SIZE];
	
	/** The read position. */
	private static int _eventread;
	
	/** The write position. */
	private static int _eventwrite;
	
	/**
	 * Returns the capabilities of the display.
	 *
	 * @param __id The display ID.
	 * @return The capabilities of the display.
	 * @since 2018/11/17
	 */
	public static final int capabilities(int __id)
	{
		return Display.SUPPORTS_INPUT_EVENTS |
			Display.SUPPORTS_TITLE |
			Display.SUPPORTS_ORIENTATION_PORTRAIT |
			Display.SUPPORTS_ORIENTATION_LANDSCAPE;
	}
	
	/**
	 * Returns the object representing the framebuffer data.
	 *
	 * @param __id The display ID.
	 * @return The framebuffer array.
	 * @since 2018/11/18
	 */
	public static final Object framebufferObject(int __id)
	{
		if (__id != 0)
			return null;
		
		throw new todo.TODO();
	}
	
	/**
	 * Returns the palette of the framebuffer.
	 *
	 * @param __id The display ID.
	 * @return The palette of the framebuffer.
	 * @since 2018/11/18
	 */
	public static final int[] framebufferPalette(int __id)
	{
		if (__id != 0)
			return null;
		
		throw new todo.TODO();
	}
	
	/**
	 * Returns the parameters of the framebuffer.
	 *
	 * @param __id The display ID.
	 * @return The framebuffer parameters.
	 * @since 2018/11/18
	 */
	public static final int[] framebufferParameters(int __id)
	{
		if (__id != 0)
			return null;
		
		throw new todo.TODO();
	}
	
	/**
	 * Is the specified display upsidedown?
	 *
	 * @param __id The ID of the display.
	 * @return If the display is upsidedown.
	 * @since 2018/11/17
	 */
	public static final boolean isUpsideDown(int __id)
	{
		return false;
	}
	
	/**
	 * Returns the number of permanent displays which are currently attached to
	 * the system.
	 *
	 * @return The number of displays attached to the system.
	 * @since 2018/11/16
	 */
	public static final int numDisplays()
	{
		// There is ever only a single display that is supported
		return 1;
	}
	
	/**
	 * Polls the next event, blocking until the next one occurs.
	 *
	 * @param __ed Event data.
	 * @return The next event, this will be the even type.
	 * @since 2018/11/17
	 */
	public static final int pollEvent(short[] __ed)
	{
		if (__ed == null)
			throw new NullPointerException("NARG");
		
		// Maximum number of data points to write
		int edlen = Math.min(__ed.length, NativeDisplayAccess.EVENT_SIZE);
		
		// Constantly poll for events
		short[] eventqueue = NativeDisplayAccess._eventqueue;
		for (;;)
			synchronized (eventqueue)
			{
				// Read positions
				int eventread = NativeDisplayAccess._eventread,
					eventwrite = NativeDisplayAccess._eventwrite;
				
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
					NativeDisplayAccess._eventread = nexter;
					
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
	 * All fields only have the granularity of {@code short}.
	 *
	 * @param __type The event type to push.
	 * @param __d0 Datapoint 1.
	 * @param __d1 Datapoint 2.
	 * @param __d2 Datapoint 3.
	 * @param __d3 Datapoint 4.
	 * @param __d4 Datapoint 5.
	 * @since 2018/11/18
	 */
	public static final void postEvent(int __type,
		int __d0, int __d1, int __d2, int __d3, int __d4)
	{
		// Lock on the queue
		short[] eventqueue = NativeDisplayAccess._eventqueue;
		synchronized (eventqueue)
		{
			int eventwrite = NativeDisplayAccess._eventwrite;
			
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
			NativeDisplayAccess._eventwrite = eventwrite;
			
			// Notify that an event was put in the queue
			eventqueue.notifyAll();
		}
	}
}

