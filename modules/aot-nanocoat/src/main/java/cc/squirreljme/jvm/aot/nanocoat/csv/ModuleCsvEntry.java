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
import java.util.Set;

/**
 * Module CSV entries.
 *
 * @since 2023/09/12
 */
public class ModuleCsvEntry
	implements Comparable<ModuleCsvEntry>
{
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
	
	/**
	 * Parses CSV data.
	 *
	 * @param __modulesCsv The input CSV file.
	 * @return The resultant output.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/09/12
	 */
	public static Set<ModuleCsvEntry> fromCsv(Set<String> __modulesCsv)
		throws NullPointerException
	{
		if (__modulesCsv == null)
			throw new NullPointerException("NARG");
		
		throw Debugging.todo();
	}
}
