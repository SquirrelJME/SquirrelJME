// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.config;

import cc.squirreljme.jvm.config.ConfigRomKey;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.util.NoSuchElementException;

/**
 * Represents a configuration entry.
 *
 * @since 2020/04/03
 */
public final class ConfigEntry
{
	/** The type of entry this is. */
	protected final int type;
	
	/** The raw value pointer. */
	protected final long rawValue;
	
	/**
	 * Initializes the configuration entry.
	 *
	 * @param __type The type used.
	 * @param __rawValue The raw pointer value.
	 * @since 2020/05/12
	 */
	public ConfigEntry(int __type, long __rawValue)
	{
		this.type = __type;
		this.rawValue = __rawValue;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/04/03
	 */
	@Override
	public boolean equals(Object __o)
	{
		throw Debugging.todo();
	}
	
	/**
	 * Returns the integer value of this configuration key.
	 *
	 * @return The value of the integer.
	 * @throws IllegalArgumentException If this entry does not refer to a value
	 * that is a integer.
	 * @since 2020/05/12
	 */
	public final int getInteger()
		throws IllegalArgumentException
	{
		// {@squirreljme.error ZZ4D Entry is not compatible with integer.}
		if (this.type() != ConfigRomType.UTF_LIST)
			throw new IllegalArgumentException("ZZ4D");
		
		throw Debugging.todo();
	}
	
	/**
	 * Gets the string array from this configuration entry.
	 *
	 * @return The string array.
	 * @throws IllegalArgumentException If this entry does not refer to a value
	 * that is compatible with a string array.
	 * @since 2020/05/12
	 */
	public final String[] getStrings()
		throws IllegalArgumentException
	{
		// {@squirreljme.error ZZ4B Entry is not compatible with UTF list.}
		if (this.type() != ConfigRomType.UTF_LIST)
			throw new IllegalArgumentException("ZZ4B");
		
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/04/03
	 */
	@Override
	public int hashCode()
	{
		throw Debugging.todo();
	}
	
	/**
	 * Returns the key for this type.
	 *
	 * @return The key that is used for this entry.
	 * @since 2020/05/12
	 */
	public final int key()
	{
		throw Debugging.todo();
	}
	
	/**
	 * Returns the raw value of this configuration entry.
	 *
	 * @return The raw value.
	 * @since 2020/05/03
	 */
	public final long rawValue()
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/04/03
	 */
	@Override
	public String toString()
	{
		throw Debugging.todo();
	}
	
	/**
	 * Returns the type that this entry is.
	 *
	 * @return The {@link ConfigRomType}.
	 * @throws NoSuchElementException If no such key exists.
	 * @since 2020/05/12
	 */
	public final int type()
	{
		throw Debugging.todo();
	}
}
