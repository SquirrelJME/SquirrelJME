// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.summercoat;

/**
 * This represents a plain array within the virtual machine.
 *
 * @since 2019/04/17
 */
public class PlainArray
	extends PlainObject
	implements ArrayInstance
{
	/** The array length. */
	public final int length;
	
	/**
	 * Initializes the plain array.
	 *
	 * @param __cl The class this is.
	 * @param __len The length of this array.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/17
	 */
	public PlainArray(LoadedClass __cl, int __len)
		throws NullPointerException
	{
		super(__cl);
		
		this.length = __len;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/07
	 */
	@Override
	public final void set(int __i, Value __v)
		throws NullPointerException
	{
		throw new todo.TODO();
	}
}

