// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.narf.codeparse;

import net.multiphasicapps.narf.bytecode.NBCByteCode;
import net.multiphasicapps.narf.bytecode.NBCOperation;
import net.multiphasicapps.narf.classinterface.NCILookup;
import net.multiphasicapps.narf.program.NRProgram;

/**
 * This performs parsing operations from Java byte code to native programs.
 *
 * @since 2016/05/08
 */
public final class NCPCodeParser
{
	/** The library for class lookup (optimization). */
	protected final NCILookup lookup;
	
	/** The program byte code. */
	protected final NBCByteCode bytecode;
	
	/**
	 * Parses all operations.
	 *
	 * @param __lu The lookup for other classes.
	 * @param __bc The program byte code.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/05/08
	 */
	public NCPCodeParser(NCILookup __lu, NBCByteCode __bc)
		throws NullPointerException
	{
		// Check
		if (__lu == null || __bc == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.lookup = __lu;
		this.bytecode = __bc;
		
		// Debug
		System.err.printf("DEBUG -- Parse Program: %s%n", __bc);
		
		throw new Error("TODO");
	}
	
	/**
	 * Returns the parsed program.
	 *
	 * @return The parsed program.
	 * @since 2016/05/08
	 */
	public NRProgram get()
	{
		throw new Error("TODO");
	}
}

