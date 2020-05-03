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
import cc.squirreljme.jvm.ConfigRomType;
import cc.squirreljme.jvm.JVMFunction;
import cc.squirreljme.jvm.memory.ReadableBasicMemory;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.util.Iterator;

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
	 * {@inheritDoc}
	 * @since 2020/04/03
	 */
	@Override
	public Iterator<ConfigEntry> iterator()
	{
		throw Debugging.todo();
	}
	
	/**
	 * Returns the raw value of the configuration key.
	 *
	 * @param __key The key to use.
	 * @return The raw value.
	 * @since 2020/05/03
	 */
	public long rawValue(int __key)
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
	 * @since 2020/05/03
	 */
	public int type(int __key)
	{
		Assembly.breakpoint();
		throw Debugging.todo();
	}
}

