// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.emulator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * This represents a single system which is being emulated, each system has
 * their own filesystem representation and memory pool. All
 * {@link EmulatorProcess}es run on this class.
 *
 * It is possible to directly read the memory that the emulated system is using
 * although it is undefined what the layout is.
 *
 * @since 2016/07/25
 */
public final class EmulatorSystem
{
	/** The group lock. */
	protected final Object lock;
	
	/** The owning group. */
	protected final EmulatorGroup group;
	
	/** The system index. */
	protected final int index;
	
	/** Components which are part of the system. */
	private final List<EmulatorComponent> _components =
		new ArrayList<>();
	
	/**
	 * Initializes the emulator system.
	 *
	 * @param __eg The owning group.
	 * @param __dx The system index.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/26
	 */
	EmulatorSystem(EmulatorGroup __eg, int __dx)
		throws NullPointerException
	{
		// Check
		if (__eg == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.group = __eg;
		this.lock = __eg._lock;
		this.index = __dx;
	}
	
	/**
	 * Adds a component to the given system.
	 *
	 * @param <C> The type of component to add.
	 * @param __cl The class type of the component to add.
	 * @param __id The component identity.
	 * @param __args The arguments to the component.
	 * @return The newly created component.
	 * @throws IOException On read/write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/26
	 */
	public final <C extends EmulatorComponent> C addComponent(Class<C> __cl,
		String __id, String... __args)
		throws IOException, NullPointerException
	{
		// Check
		if (__cl == null || __id == null)
			throw new NullPointerException("NARG");
		
		// Force to exist
		if (__args == null)
			__args = new String[0];
		
		// Handled by the group
		return this.group.<C>__addComponent(this, __cl, __id, __args);
	}
	
	/**
	 * Returns the group which owns this system.
	 *
	 * @return The owning group.
	 * @since 2016/07/26
	 */
	public final EmulatorGroup group()
	{
		return this.group;
	}
	
	/**
	 * Returns the index of the system.
	 *
	 * @return The system index.
	 * @since 2016/07/26
	 */
	public final int index()
	{
		return this.index;
	}
	
	/**
	 * Adds a component to the current system.
	 *
	 * @param __bc The component to add.
	 * @throws IllegalArgumentException If it belongs to another system,
	 * was already included in the component list, or has a name conflict with
	 * another component.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/26
	 */
	final void __addComponent(EmulatorComponent __bc)
		throws IllegalArgumentException, NullPointerException
	{
		// Check
		if (__bc == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error AR07 Cannot add a component which belongs to
		// another system.}
		if (__bc.system() != this)
			throw new IllegalArgumentException("AR07");
		
		// Lock
		List<EmulatorComponent> components = this._components;
		synchronized (this.lock)
		{
			// Go through all
			int n = components.size();
			int freeslot = -1;
			for (int i = 0; i < n; i++)
			{
				// Get component here
				EmulatorComponent comp = components.get(i);
				
				// Free slot?
				if (comp == null)
				{
					if (freeslot < 0)
						freeslot = i;
				}
				
				// {@squirreljme.error AR08 The component to be added to the
				// system is already in the component list.}
				else if (comp == __bc)
					throw new IllegalArgumentException("AR08");
			}
			
			// Add to end?
			if (freeslot < 0)
				components.add(__bc);
			
			// Specific slot
			else
				components.set(freeslot, __bc);
		}
	}
	
	/**
	 * Calculates the time of the next event.
	 *
	 * @param __nc The next component which is to be ran on the system.
	 * @return The next event time index, or a negative value if one is not
	 * to be ran.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/25
	 */
	final long __nextEventTime(EmulatorComponent[] __nc)
		throws NullPointerException
	{
		// Check
		if (__nc == null)
			throw new NullPointerException("NARG");
		
		// Lock
		List<EmulatorComponent> components = this._components;
		synchronized (this.lock)
		{
			long next = -1L;
			EmulatorComponent usecomp = null;
			
			// Get group time
			long gtime = this.group.currentTimeIndex();
			
			// Go through all
			int n = components.size();
			for (int i = 0; i < n; i++)
			{
				// Get component
				EmulatorComponent bc = components.get(i);
				
				// Get the next time
				long maybe = bc.__nextEventTime();
				
				// Use this event
				if (maybe >= 0 && (next < 0 || maybe < next))
				{
					// {@squirreljme.error AR0b The next event occurs in the
					// past before the emulator's global time index. (The
					// next event time; The global emulator time index)}
					if (maybe < gtime)
						throw new IllegalStateException(String.format(
							"AR0b %d %d", maybe, gtime));
					
					// Use
					next = maybe;
					usecomp = bc;
				}
			}
			
			// Using an event
			if (usecomp != null)
			{
				__nc[0] = usecomp;
				return next;
			}
		}
		
		// No events being ran
		return -1L;
	}
}

