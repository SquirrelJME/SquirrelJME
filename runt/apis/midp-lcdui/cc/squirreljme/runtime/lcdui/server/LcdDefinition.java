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
	/** The lock for all operations. */
	protected final Object lock =
		new Object();
	
	/** Displays which are currently available. */
	private final Map<Integer, LcdDisplay> _displays =
		new SortedTreeMap<>();
	
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
	 * {@inheritDoc}
	 * @since 2018/03/15
	 */
	@Override
	public final ServiceServer newServer(SystemTask __task)
		throws NullPointerException
	{
		if (__task == null)
			throw new NullPointerException("NARG");
		
		return this.newLcdServer(__task);
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

