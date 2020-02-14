// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc.lang;

import cc.squirreljme.runtime.cldc.asm.SystemProperties;

/**
 * This is used to return an appropriate class to use for the implemenation
 * of something. This is similar to {@link java.util.ServiceLoader} except
 * that it is specific to the virtual machine itself.
 *
 * @since 2018/12/13
 */
public final class ImplementationClass
{
	/**
	 * Not used.
	 *
	 * @since 2018/12/17
	 */
	private ImplementationClass()
	{
	}
	
	/**
	 * The class to use for a given implementation of something.
	 *
	 * @param __n The class name to lookup.
	 * @return The class that should get its instance created or {@code null}
	 * if there is no implementation.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/12/13
	 */
	public static final String implementationClass(String __n)
		throws NullPointerException
	{
		if (__n == null)
			throw new NullPointerException("NARG");
		
		// Check to see if there is a system property for the implementation
		// to allow it to be replaced
		try
		{
			String rv = System.getProperty("cc.squirreljme.implclass." + __n);
			if (rv != null)
				return rv;
		}
		catch (SecurityException e)
		{
		}
		
		// Otherwise use whatever the VM says to use
		return SystemProperties.implementationClass(__n);
	}
}

