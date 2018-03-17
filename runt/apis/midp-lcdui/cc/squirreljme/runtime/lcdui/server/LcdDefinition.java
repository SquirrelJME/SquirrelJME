// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.server;

import cc.squirreljme.runtime.cldc.service.ServiceDefinition;
import cc.squirreljme.runtime.cldc.service.ServiceServer;
import cc.squirreljme.runtime.cldc.task.SystemTask;
import cc.squirreljme.runtime.lcdui.DisplayableType;
import cc.squirreljme.runtime.lcdui.LcdServiceCall;
import java.util.HashMap;
import java.util.Map;
import net.multiphasicapps.collections.SortedTreeMap;

/**
 * This class implements the base of the graphical LCDUI display system which
 * is used by the public facing LCDUI code to enable the use of graphics.
 *
 * All operations in the server are performed inside of a single lock because
 * LCDUI for the most part requires that behavior, additionally most operating
 * systems and display interfaces that exist only allow interaction with
 * graphical APIs in the same thread that is using them. Although this has a
 * speed loss, if most graphical operations are performed on buffers this will
 * not cause much issue.
 *
 * @since 2018/03/15
 */
public abstract class LcdDefinition
	extends ServiceDefinition
{
	/** The handler for requests to the LCD server. */
	protected final LcdRequestHandler requesthandler =
		new LcdRequestHandler();
	
	/**
	 * Initializes the base definition.
	 *
	 * @since 2018/03/15
	 */
	public LcdDefinition()
	{
		super(LcdServiceCall.Provider.class);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/15
	 */
	@Override
	public final ServiceServer newServer(SystemTask __task)
		throws NullPointerException
	{
		if (__task == null)
			throw new NullPointerException("NARG");
		
		return new LcdServer(__task, this.requesthandler);
	}
	
	/**
	 * Returns the request handler that is used to handle any requests made
	 * to the LCDUI interface.
	 *
	 * @return The request handler for events.
	 * @since 2018/03/17
	 */
	public final LcdRequestHandler requestHandler()
	{
		return this.requesthandler;
	}
	
	
	
	
	
	/** The lock for all operations. */
	protected final Object lock =
		new Object();
	
	/** Displays which are currently available. */
	private final Map<Integer, LcdDisplay> _displays =
		new SortedTreeMap<>();
	
	/** Displayables which currently exist. */
	private final Map<Integer, LcdDisplayable> _displayables =
		new SortedTreeMap<>();
	
	/** The next handle to use. */
	private volatile int _nexthandle;
	
	/**
	 * Initializes the base displayable.
	 *
	 * @param __lock The locking object used.
	 * @param __handle The handle for this displayable.
	 * @param __task The task owning this displayable.
	 * @param __type The type of displayable this is.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/17
	 */
	protected abstract LcdDisplayable internalCreateDisplayable(Object __lock,
		int __handle, SystemTask __task, DisplayableType __type)
		throws NullPointerException;
	
	/**
	 * Internally queries the displays which are present.
	 *
	 * @param __k An array containing the displays which are currently known
	 * about, this may be used to determine if any need to be re-initialized.
	 * @return An array containing the displays that are present.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/17
	 */
	protected abstract LcdDisplay[] internalQueryDisplays(LcdDisplay[] __k)
		throws NullPointerException;
	
	/**
	 * Initializes an implementation of the LCDUI server that clients
	 * directly interact with.
	 *
	 * @param __task The task using the service.
	 * @return The server for the task.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/16
	 */
	protected abstract LcdServer newLcdServer(SystemTask __task)
		throws NullPointerException;
	
	/**
	 * Creates the specified displayable.
	 *
	 * @param __task The owning task.
	 * @param __type The type of displayable to create.
	 * @return The create displayable.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/17
	 */
	public final LcdDisplayable createDisplayable(SystemTask __task,
		DisplayableType __type)
		throws NullPointerException
	{
		if (__task == null || __type == null)
			throw new NullPointerException("NARG");
		
		Object lock = this.lock;
		synchronized (lock)
		{
			// Generate a new handle
			int handle = this._nexthandle++;
			
			// Internally create it
			LcdDisplayable rv = this.internalCreateDisplayable(lock,
				handle, __task, __type);
			if (handle != rv.handle())
				throw new RuntimeException("OOPS");
			
			// Store active displayables
			this._displayables.put(handle, rv);
			
			// Use this
			return rv;
		}
	}
	
	/**
	 * Returns the displayable for the given handle.
	 *
	 * @param __handle The handle to get.
	 * @param __task The owning task.
	 * @return The displayable.
	 * @throws IllegalStateException If the given displayable does not exist
	 * or belongs to anothe task.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/17
	 */
	public final LcdDisplayable getDisplayable(int __handle, SystemTask __task)
		throws IllegalStateException, NullPointerException
	{
		if (__task == null)
			throw new NullPointerException("NARG");
		
		Map<Integer, LcdDisplayable> displayables = this._displayables;
		synchronized (this.lock)
		{
			// {@squirreljme.error EB1y The specified handle does not exist
			// for the given task. (The handle)}
			LcdDisplayable rv = displayables.get(__handle);
			if (rv == null || !__task.equals(rv.task()))
				throw new IllegalStateException(String.format("EB1y %d",
					__handle));
			
			return rv;
		}
	}
	
	/**
	 * Returns the lock for the display.
	 *
	 * @return The display lock.
	 * @since 2018/03/17
	 */
	public final Object lock()
	{
		return this.lock;
	}
	
	/**
	 * Queries all of the displays which are available for usage.
	 *
	 * @return The displays which are available.
	 * @since 2018/03/17
	 */
	public final LcdDisplay[] queryDisplays()
	{
		// Lock displays 
		Map<Integer, LcdDisplay> displays = this._displays;
		synchronized (this.lock)
		{
			// Get the displays which are known to the definition so that
			// lookup does not create any duplicates ones potentially
			LcdDisplay[] known = displays.values().<LcdDisplay>toArray(
				new LcdDisplay[displays.size()]);
			
			// Query all native displays
			LcdDisplay[] active = this.internalQueryDisplays(known);
			
			// Cache all displays by their index
			for (LcdDisplay d : active)
			{
				Integer dx = d.index();
				
				// If a display is not known about then make it known
				if (!displays.containsKey(dx))
					displays.put(dx, d);
			}
			
			// Return every display which is known about
			return displays.values().<LcdDisplay>toArray(
				new LcdDisplay[displays.size()]);
		}
	}
}

