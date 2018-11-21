// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.springcoat;

/**
 * This is a virtual machine representation of a static method.
 *
 * @since 2018/11/20
 */
public final class SpringVMStaticMethod
	implements SpringObject
{
	/** The method to execute. */
	protected final SpringMethod method;
	
	/**
	 * Initializes the static method.
	 *
	 * @param __m The method to execute.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/11/20
	 */
	public SpringVMStaticMethod(SpringMethod __m)
		throws NullPointerException
	{
		if (__m == null)
			throw new NullPointerException("NARG");
		
		this.method = __m;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/20
	 */
	@Override
	public final SpringMonitor monitor()
	{
		// {@squirreljme.error BK34 StaticMethod does not have a monitor.}
		throw new SpringFatalException("BK34");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/20
	 */
	@Override
	public final SpringClass type()
	{
		// {@squirreljme.error BK35 StaticMethod does not have a type.}
		throw new SpringFatalException("BK35");
	}
}

