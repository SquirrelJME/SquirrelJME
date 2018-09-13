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
 * This is a representation of an object within the virtual machine.
 *
 * @since 2018/09/08
 */
public final class SpringSimpleObject
	implements SpringObject
{
	/** The type of object this is. */
	protected final SpringClass type;
	
	/** Field storage in the class. */
	private final Object[] _fields;
	
	/**
	 * Initializes the object.
	 *
	 * @param __cl The class of the object.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/09/08
	 */
	public SpringSimpleObject(SpringClass __cl)
		throws NullPointerException
	{
		if (__cl == null)
			throw new NullPointerException("NARG");
		
		this.type = __cl;
		this._fields = new Object[__cl.instanceFieldCount()];
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/09/09
	 */
	@Override
	public final SpringClass type()
	{
		return this.type;
	}
}

