// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.aot.nanocoat.table;

import cc.squirreljme.c.CVariable;
import cc.squirreljme.jvm.aot.nanocoat.csv.SharedCsvEntry;

/**
 * Table entry information.
 *
 * @since 2023/10/15
 */
final class __TableEntry__
{
	/** The variable declaration. */
	final CVariable variable;
	
	/** The shared CSV entry. */
	final SharedCsvEntry csvEntry;
	
	/**
	 * Initializes the table entry.
	 *
	 * @param __variable The variable to store.
	 * @param __csvEntry The CSV entry for this.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/10/15
	 */
	__TableEntry__(CVariable __variable, SharedCsvEntry __csvEntry)
		throws NullPointerException
	{
		if (__variable == null || __csvEntry == null)
			throw new NullPointerException("NARG");
		
		this.variable = __variable;
		this.csvEntry = __csvEntry;
	}
}
