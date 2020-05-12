// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.boot;

import cc.squirreljme.jvm.Assembly;
import cc.squirreljme.jvm.ConfigRomKey;
import cc.squirreljme.jvm.ConfigRomType;
import cc.squirreljme.jvm.memory.ReadableBasicMemory;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * This is a helper class used to read the configuration.
 *
 * @see ConfigWriter
 * @since 2019/06/22
 */
public final class ConfigReader
	implements Iterable<ConfigEntry>
{
	/** Configuration memory accessor. */
	protected final ReadableBasicMemory memory;
	
	/**
	 * Initializes the configuration reader.
	 *
	 * @param __mem The configuration memory area.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/04/03
	 */
	public ConfigReader(ReadableBasicMemory __mem)
		throws NullPointerException
	{
		if (__mem == null)
			throw new NullPointerException("NARG");
		
		this.memory = __mem;
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
	public final String[] getStrings(int __key)
		throws IllegalArgumentException, NoSuchElementException
	{
		// {@squirreljme.error ZZ4B Key is not UTF list type. (The key)}
		if (this.type(__key) != ConfigRomType.UTF_LIST)
			throw new IllegalArgumentException("ZZ4B " + __key);
		
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/04/03
	 */
	@Override
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
	public final long rawValue(int __key)
		throws NoSuchElementException
	{
		Assembly.breakpoint();
		throw Debugging.todo();
	}
	
	/**
	 * Returns the number of configuration items.
	 *
	 * @return The number of configuration items.
	 * @since 2002/04/03
	 */
	public final int size()
	{
		throw Debugging.todo();
	}
	
	/**
	 * Returns the type that the key is.
	 *
	 * @param __key The key to use.
	 * @return The {@link ConfigRomType}.
	 * @throws NoSuchElementException If no such key exists.
	 * @since 2020/05/03
	 */
	public final int type(int __key)
		throws NoSuchElementException
	{
		Assembly.breakpoint();
		throw Debugging.todo();
	}
}

