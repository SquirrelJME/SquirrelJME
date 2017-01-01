// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.kernel;

import java.util.HashMap;
import java.util.Map;

/**
 * This is used to build immutable instances of kernel parameters without
 * requiring that a class be implemented to provide parameters.
 *
 * @since 2016/11/07
 */
public class KernelLaunchParametersBuilder
{
	/** Change lock. */
	protected final Object lock =
		new Object();
	
	/** System properties to define. */
	final Map<String, String> _properties =
		new HashMap<>();
	
	/** The kernel command line. */
	volatile String[] _cmdline =
		new String[0];
	
	/**
	 * Adds a system property to be used on launch.
	 *
	 * @param __k The property key.
	 * @param __v The property value.
	 * @return The old value, may be {@code null} if none was previously
	 * set.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/11/08
	 */
	public final String addSystemProperty(String __k, String __v)
		throws NullPointerException
	{
		// Check
		if (__k == null || __v == null)
			throw new NullPointerException("NARG");
		
		// Lock
		synchronized (this.lock)
		{
			return this._properties.put(__k, __v);
		}
	}
	
	/**
	 * Constructs the final immutabel kernel launch parameters.
	 *
	 * @return The launch parameters for the kernel.
	 * @since 2016/11/07
	 */
	public final KernelLaunchParameters build()
	{
		// Lock
		synchronized (this.lock)
		{
			return new __ImmutableLaunchParameters__(this);
		}
	}
	
	/**
	 * Sets and additionally parses the command line arguments specified to
	 * the kernel.
	 *
	 * @param __args The kernel arguments
	 * @throws NullPointerException On null arguments.
	 * @since 2016/12/16
	 */
	public final void parseCommandLine(String... __args)
		throws NullPointerException
	{
		// Check
		if (__args == null)
			throw new NullPointerException("NARG");
		
		// Lock
		synchronized (this.lock)
		{
			// Set the command line
			setCommandLine(__args);
			String[] cline = this._cmdline;
			
			System.err.println("TODO -- Parse command line.");
		}
	}
	
	/**
	 * Sets the command line arguments for the kernel.
	 *
	 * @param __args The arguments to use.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/12/16
	 */
	public final void setCommandLine(String... __args)
		throws NullPointerException
	{
		// Check
		if (__args == null)
			throw new NullPointerException("NARG");
		
		// Lock
		synchronized (this.lock)
		{
			// Do not allow null strings
			String[] c = __args.clone();
			int n = c.length;
			for (int i = 0; i < n; i++)
				if (c[i] == null)
					c[i] = "";
			this._cmdline = c;
		}
	}
}

