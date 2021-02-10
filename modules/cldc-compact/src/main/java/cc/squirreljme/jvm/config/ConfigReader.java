// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.config;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * This is a helper class used to read the configuration.
 *
 * @see ConfigWriter
 * @since 2019/06/22
 */
@Deprecated
public final class ConfigReader
	implements Iterable<ConfigEntry>
{
	/** Supplier for input streams. */
	@Deprecated
	protected final InputStreamSupplier streamSupplier;
	
	/**
	 * Initializes the configuration reader.
	 *
	 * @param __stream The supplier for input streams.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/04/03
	 */
	@Deprecated
	public ConfigReader(InputStreamSupplier __stream)
		throws NullPointerException
	{
		if (__stream == null)
			throw new NullPointerException("NARG");
		
		this.streamSupplier = __stream;
	}
	
	/**
	 * Gets the configuration entry by the given key.
	 *
	 * @param __key The key to lookup.
	 * @return The config entry for the given key.
	 * @throws NoSuchElementException If the key is not found in the
	 * configuration.
	 * @since 2020/05/12
	 */
	@Deprecated
	public final ConfigEntry getByKey(int __key)
		throws NoSuchElementException
	{
		for (ConfigEntry entry : this)
			if (entry.key() == __key)
				return entry;
		
		// {@squirreljme.error ZZ4D No configuration entry exists for
		// the given key. (Key type)}
		throw new NoSuchElementException("ZZ4D " + __key);
	}
	
	/**
	 * Gets the string array from a key.
	 *
	 * @param __key The {@link ConfigRomKey} to obtain.
	 * @return The string array.
	 * @throws IllegalArgumentException If the key does not refer to a value
	 * that is compatible with a string array.
	 * @throws NoSuchElementException If no such key exists.
	 * @since 2020/05/12
	 */
	@Deprecated
	public final String[] getStrings(int __key)
		throws IllegalArgumentException, NoSuchElementException
	{
		return this.getByKey(__key).getStrings();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/04/03
	 */
	@Override
	@Deprecated
	public final Iterator<ConfigEntry> iterator()
	{
		throw Debugging.todo();
	}
	
	/**
	 * Returns the raw value of the configuration key.
	 *
	 * @param __key The key to use.
	 * @return The raw value.
	 * @throws NoSuchElementException If no such key exists.
	 * @since 2020/05/03
	 */
	@Deprecated
	public final long rawValue(int __key)
		throws NoSuchElementException
	{
		return this.getByKey(__key).rawValue();
	}
	
	/**
	 * Returns the number of configuration items.
	 *
	 * @return The number of configuration items.
	 * @since 2002/04/03
	 */
	@Deprecated
	public final int size()
	{
		int count = 0;
		
		for (ConfigEntry ignored : this)
			count++;
		
		return count;
	}
	
	/**
	 * Returns the type that the key is.
	 *
	 * @param __key The key to use.
	 * @return The {@link ConfigRomType}.
	 * @throws NoSuchElementException If no such key exists.
	 * @since 2020/05/03
	 */
	@Deprecated
	public final int type(int __key)
		throws NoSuchElementException
	{
		return this.getByKey(__key).type();
	}
}

