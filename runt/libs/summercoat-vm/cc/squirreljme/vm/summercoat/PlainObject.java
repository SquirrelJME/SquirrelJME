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
 * This represents a plain object within the virtual machine.
 *
 * @since 2019/04/17
 */
public class PlainObject
	implements Instance
{
	/** The loaded class this is. */
	public final LoadedClass loadedclass;
	
	/** This object's count, starts at 1. */
	volatile int _count =
		1;
	
	/** The object's virtual pointer. */
	volatile int _vptr;
	
	/**
	 * Initializes the plain with no class.
	 *
	 * @since 2019/04/18
	 */
	public PlainObject()
	{
		// Set
		this.loadedclass = null;
	}
	
	/**
	 * Initializes the plain object.
	 *
	 * @param __cl The class this is.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/17
	 */
	public PlainObject(LoadedClass __cl)
		throws NullPointerException
	{
		if (__cl == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.loadedclass = __cl;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/18
	 */
	@Override
	public final boolean count(boolean __up)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/18
	 */
	@Override
	public final int currentCount()
	{
		return this._count;
	}
}

