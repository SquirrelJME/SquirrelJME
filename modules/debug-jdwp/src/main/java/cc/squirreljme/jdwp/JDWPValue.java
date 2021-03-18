// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jdwp;

import cc.squirreljme.runtime.cldc.debug.Debugging;

/**
 * Represents a value for storage.
 *
 * @since 2021/03/17
 */
public final class JDWPValue
	implements AutoCloseable
{
	/** The type used for the value. */
	private volatile JDWPValueTag _type;
	
	/**
	 * {@inheritDoc}
	 * @since 2021/03/17
	 */
	@Override
	public void close()
	{
		throw Debugging.todo();
	}
	
	/**
	 * Returns the number value.
	 * 
	 * @return The number value.
	 * @throws IllegalStateException If this is not number compatible or no
	 * value is set.
	 * @since 2021/03/17
	 */
	public long getNumber()
		throws IllegalStateException
	{
		throw Debugging.todo();
	}
	
	/**
	 * Sets the object value.
	 * 
	 * @return The object value.
	 * @throws IllegalStateException If this is not object compatible or no
	 * value is set.
	 * @since 2021/03/17
	 */
	public JDWPObjectLike getObject()
		throws IllegalStateException
	{
		throw Debugging.todo();
	}
	
	/**
	 * Returns the value type stored here.
	 * 
	 * @return The type used for this value.
	 * @throws IllegalStateException If no value is set.
	 * @since 2021/03/17
	 */
	public JDWPValueTag getType()
		throws IllegalStateException
	{
		throw Debugging.todo();
	}
	
	/**
	 * Sets the given value.
	 * 
	 * @param __v The value to set.
	 * @throws IllegalStateException If a value is already set or this is
	 * not open.
	 * @since 2021/03/17
	 */
	public void setBoolean(boolean __v)
		throws IllegalStateException
	{
		throw Debugging.todo();
	}
}
