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
 * This is an object which acts as an array, which stores some kind of data.
 *
 * @since 2018/09/15
 */
public final class SpringArrayObject
	implements SpringObject
{
	/** The monitor for this array. */
	protected final SpringMonitor monitor =
		new SpringMonitor();
	
	/** The component type. */
	protected final SpringClass component;
	
	/** The length of the array. */
	protected final int length;
	
	/**
	 * Initializes the array.
	 *
	 * @param __cl The component type.
	 * @param __l The array length.
	 * @throws NullPointerException On null arguments.
	 * @throws SpringNegativeArraySizeException If the array size is negative.
	 * @since 2018/09/15
	 */
	public SpringArrayObject(SpringClass __cl, int __l)
		throws NullPointerException
	{
		if (__cl == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error BK16 Attempt to allocate an array of a
		// negative size. (The length requested)}
		if (__l < 0)
			throw new SpringNegativeArraySizeException(
				String.format("BK16 %d", __l));
		
		this.component = __cl;
		this.length = __l;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/09/15
	 */
	@Override
	public final SpringMonitor monitor()
	{
		return this.monitor;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/09/15
	 */
	@Override
	public final SpringClass type()
	{
		throw new todo.TODO();
	}
}

