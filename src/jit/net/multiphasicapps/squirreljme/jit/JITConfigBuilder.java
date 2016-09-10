// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit;

import java.util.Map;
import net.multiphasicapps.squirreljme.jit.base.JITException;
import net.multiphasicapps.squirreljme.jit.base.JITTriplet;
import net.multiphasicapps.util.sorted.SortedTreeMap;

/**
 * This class is used to build instances of {@link JITConfig} which is used
 * to configure the JIT compiler system.
 *
 * @since 2016/09/10
 */
public class JITConfigBuilder
{
	/** Internal lock. */
	protected final Object lock =
		new Object();
	
	/** Properties that are associated with the JIT, ones to configure it. */
	final Map<String, String> _properties =
		new SortedTreeMap<>();
	
	/**
	 * Builds the configuration to use.
	 *
	 * @return The resultant configuration.
	 * @throws JITException If the configuration is not valid.
	 * @since 2016/09/10
	 */
	public JITConfig build()
		throws JITException
	{
		// Lock
		synchronized (this.lock)
		{
			return new JITConfig(this);
		}
	}
	
	/**
	 * Sets the given property for the JIT.
	 *
	 * @param __k The key to use.
	 * @param __v The value to use.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/10
	 */
	public void setProperty(String __k, String __v)
		throws NullPointerException
	{
		// Check
		if (__k == null || __v == null)
			throw new NullPointerException("NARG");
		
		// Lock
		synchronized (this.lock)
		{
			this._properties.put(__k, __v);
		}
	}
	
	/**
	 * Sets the output factory to use during JIT compilation.
	 *
	 * @param __cl The output factory to use.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/10
	 */
	public void setOutputFactory(Class<? extends JITOutputFactory> __cl)
		throws NullPointerException
	{
		// Check
		if (__cl == null)
			throw new NullPointerException("NARG");
		
		// Set
		setProperty(JITConfig.FACTORY_PROPERTY, __cl.getName());
	}
	
	/**
	 * Sets the triplet which is used to determine the target to JIT for.
	 *
	 * @param __t The triplet to set.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/10
	 */
	public void setTriplet(JITTriplet __t)
		throws NullPointerException
	{
		// Check
		if (__t == null)
			throw new NullPointerException("NARG");
		
		// Just set the property
		setProperty(JITConfig.TRIPLET_PROPERTY, __t.toString());
	}
}

