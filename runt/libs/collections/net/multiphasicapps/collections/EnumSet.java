// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.collections;

import cc.squirreljme.runtime.cldc.system.SystemCall;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

/**
 * This is a set which allows enumeration values to be stored in it efficiently
 * using only a bit field.
 *
 * @since 2018/03/21
 */
public final class EnumSet<T extends Enum<T>>
	extends AbstractSet
{
	/** The type of class being stored. */
	protected final Class<T> type;
	
	/**
	 * Initializes an empty enumeration set.
	 *
	 * @param __cl The class type to store.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/21
	 */
	public EnumSet(Class<T> __cl)
		throws NullPointerException
	{
		if (__cl == null)
			throw new NullPointerException("NARG");
		
		this.type = __cl;
	}
	
	/**
	 * Initializes an enumeration set with the given initial values.
	 *
	 * @param __cl The class type to store.
	 * @param __v The initial values to store.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/21
	 */
	public EnumSet(Class<T> __cl, Collection<T> __v)
		throws NullPointerException
	{
		if (__cl == null || __v == null)
			throw new NullPointerException("NARG");
		
		this.type = __cl;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/21
	 */
	@Override
	public final boolean contains(Object __o)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/21
	 */
	@Override
	public final Iterator<T> iterator()
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/21
	 */
	@Override
	public final int size()
	{
		throw new todo.TODO();
	}
}

