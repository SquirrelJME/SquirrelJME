// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.aot.nanocoat.csv;

import cc.squirreljme.runtime.cldc.debug.Debugging;

/**
 * Module CSV entries.
 *
 * @since 2023/09/12
 */
public class ModuleCsvEntry
	implements Comparable<ModuleCsvEntry>
{
	/** The name of the module. */
	public final String name;
	
	/** The module identifier. */
	public final String identifier;
	
	/** The header file. */
	public final String header;
	
	/** The source file. */
	public final String source;
	
	/**
	 * Initializes the module entry.
	 *
	 * @param __name The name of the module.
	 * @param __identifier The module identifier.
	 * @param __header The module header.
	 * @param __source The module source.
	 * @throws NullPointerException
	 * @since 2023/09/25
	 */
	public ModuleCsvEntry(String __name, String __identifier, String __header,
		String __source)
		throws NullPointerException
	{
		if (__name == null || __identifier == null || __header == null ||
			__source == null)
			throw new NullPointerException("NARG");
		
		this.name = __name;
		this.identifier = __identifier;
		this.header = __header;
		this.source = __source;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/09/12
	 */
	@Override
	public int compareTo(ModuleCsvEntry __b)
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/09/12
	 */
	@Override
	public boolean equals(Object __o)
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/09/12
	 */
	@Override
	public int hashCode()
	{
		throw Debugging.todo();
	}
}
