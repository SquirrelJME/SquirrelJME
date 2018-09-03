// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.springcoat.vm;

import net.multiphasicapps.classfile.Method;

/**
 * This contains and stores the definition of a single method.
 *
 * @since 2018/07/22
 */
public final class SpringMethod
{
	/** The backing method and its information. */
	protected final Method method;
	
	/**
	 * Initializes the method representation.
	 *
	 * @param __m The method to wrap.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/09/03
	 */
	SpringMethod(Method __m)
		throws NullPointerException
	{
		if (__m == null)
			throw new NullPointerException("NARG");
		
		this.method = __m;
	}
	
	/**
	 * Returns whether this is a constructor or not.
	 *
	 * @return Whether this is a constructor or not.
	 * @since 2018/09/03
	 */
	public final boolean isInstanceInitializer()
	{
		return this.method.isInstanceInitializer();
	}
	
	/**
	 * Returns if this method is static.
	 *
	 * @return {@code true} if the method is static.
	 * @since 2018/09/03
	 */
	public final boolean isStatic()
	{
		return this.method.isStatic();
	}
	
	/**
	 * Returns whether this is a static initializer or not.
	 *
	 * @return Whether this is a static initializer or not.
	 * @since 2018/09/03
	 */
	public final boolean isStaticInitializer()
	{
		return this.method.isStaticInitializer();
	}
}

