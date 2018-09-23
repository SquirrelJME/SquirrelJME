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

/**
 * This interface represents a base reference.
 *
 * @since 2018/09/23
 */
public abstract class SpringPrimitiveReference
	implements SpringObject
{
	/**
	 * Gets the object from this reference.
	 *
	 * @return The value of the reference.
	 * @since 2018/09/23
	 */
	public abstract SpringObject get();
	
	/**
	 * Sets the reference to the given object.
	 *
	 * @param __o The object to set.
	 * @since 2018/09/23
	 */
	public abstract void set(SpringObject __o);
	
	/**
	 * {@inheritDoc}
	 * @since 2018/09/23
	 */
	@Override
	public final SpringMonitor monitor()
	{
		// {@squirreljme.error BK1x Reference types are special and have no
		// monitor.}
		throw new SpringVirtualMachineException("BK1x");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/09/23
	 */
	@Override
	public final SpringClass type()
	{
		// {@squirreljme.error BK1w Reference types are special and have no
		// class.}
		throw new SpringVirtualMachineException("BK1w");
	}
}

