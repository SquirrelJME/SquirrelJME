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
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import net.multiphasicapps.util.unmodifiable.UnmodifiableMap;

/**
 * This is the root of all components on the system, components essentially
 * have time based events that may be executed if requested.
 *
 * @since 2016/07/26
 */
public abstract class EmulatorComponent
{
	/** Internal lock. */
	protected final Object lock;
	
	/** The owning emulator group. */
	protected final EmulatorGroup group;
	
	/** The owning emulator system. */
	protected final EmulatorSystem system;
	
	/** The name of the component. */
	protected final String name;
	
	/** The input arguments. */
	protected final Map<String, String> arguments;
	
	/**
	 * Initializes the base component.
	 *
	 * @param __es The owning emulator system.
	 * @param __n The name identity of the component.
	 * @param __args Arguments to the component.
	 * @throws IllegalArgumentException
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/26
	 */
	public EmulatorComponent(EmulatorSystem __es, String __n, String... __args)
		throws NullPointerException
	{
		// Check
		if (__es == null || __n == null)
			throw new NullPointerException("NARG");
		
		// Defensive copy
		__args = (__args != null ? __args.clone() : new String[0]);
		
		// Set
		this.system = __es;
		this.group = __es.group();
		this.lock = __es.group()._lock;
		this.name = __n;
		
		// Copy arguments
		int n = __args.length;
		Map<String, String> args = new LinkedHashMap<>();
		for (int i = 0; i < n; i += 2)
			args.put(Objects.requireNonNull(__args[i], "NARG"),
				Objects.requireNonNull(__args[i + 1], "NARG"));
		this.arguments = UnmodifiableMap.<String, String>of(args);
	}
	
	/**
	 * Returns the next event time.
	 *
	 * @return The next event time or {@code -1} if there are no events to
	 * process.
	 * @since 2016/07/26
	 */
	protected abstract long nextEventTime();
	
	/**
	 * Runs the next event for the given component.
	 *
	 * @throws IOException On read/write errors.
	 * @since 2016/07/26
	 */
	protected abstract void run()
		throws IOException;
	
	/**
	 * Returns the owning emulator group.
	 *
	 * @return The emulator group.
	 * @since 2016/07/26
	 */
	public final EmulatorGroup group()
	{
		return this.group;
	}
	
	/**
	 * Returns the name of the component.
	 *
	 * @return The component name.
	 * @since 2016/07/26
	 */
	public final String name()
	{
		return this.name;
	}
	
	/**
	 * Returns the owning emulator system.
	 *
	 * @return The emulator system.
	 * @since 2016/07/26
	 */
	public final EmulatorSystem system()
	{
		return this.system;
	}
	
	/**
	 * Forwards the next event time call.
	 *
	 * @return The time of the next event or a negative value if none is to
	 * be run.
	 * @since 2016/07/26
	 */
	final long __nextEventTime()
	{
		// Lock
		synchronized (this.lock)
		{
			return this.nextEventTime();
		}
	}
	
	/**
	 * Forwards running the current event.
	 *
	 * @throws IOException On read/write errors.
	 * @since 2016/07/26
	 */
	final void __run()
		throws IOException
	{
		// Lock
		synchronized (this.lock)
		{
			this.run();
		}
	}
}

