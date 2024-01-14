// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.aot.nanocoat.csv;

import cc.squirreljme.c.CFileName;
import cc.squirreljme.c.CIdentifier;
import cc.squirreljme.runtime.cldc.debug.Debugging;

/**
 * Represents a shared entry.
 *
 * @since 2023/09/25
 */
public final class SharedCsvEntry
	implements Comparable<SharedCsvEntry>
{
	/** The prefix of the entry. */
	public final String prefix;
	
	/** The identifier of the entry. */
	public final CIdentifier identifier;
	
	/** The source file. */
	public final CFileName source;
	
	/** The header this is in. */
	public final CFileName header;
	
	/**
	 * Initializes the entry.
	 *
	 * @param __prefix The prefix of the entry.
	 * @param __identifier The identifier that this uses.
	 * @param __header The header for inclusion.
	 * @param __source The source containing the shared code.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/10/14
	 */
	public SharedCsvEntry(String __prefix, CIdentifier __identifier,
		CFileName __header, CFileName __source)
		throws NullPointerException
	{
		if (__prefix == null || __identifier == null ||
			__header == null || __source == null)
			throw new NullPointerException("NARG");
		
		this.prefix = __prefix;
		this.identifier = __identifier;
		this.header = __header;
		this.source = __source;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/10/15
	 */
	@Override
	public int compareTo(SharedCsvEntry __b)
	{
		int rv = this.prefix.compareTo(__b.prefix);
		if (rv != 0)
			return rv;
		
		rv = this.identifier.compareTo(__b.identifier);
		if (rv != 0)
			return rv;
		
		rv = this.header.compareTo(__b.header);
		if (rv != 0)
			return rv;
		
		return this.source.compareTo(__b.source);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/10/14
	 */
	@Override
	public boolean equals(Object __o)
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/10/14
	 */
	@Override
	public int hashCode()
	{
		throw Debugging.todo();
	}
}
