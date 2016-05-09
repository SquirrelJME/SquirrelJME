// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.narf.program;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import net.multiphasicapps.narf.classinterface.NCILookup;
import net.multiphasicapps.narf.classinterface.NCIMethod;

/**
 * This represents a Java byte code program.
 *
 * @since 2016/04/27
 */
public class NRProgram
	extends AbstractList<NROp>
{
	/**
	 * Initializes the program representation.
	 *
	 * @param __lu The program lookup.
	 * @param __m The method to represent a program for.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/05/08
	 */
	public NRProgram(NCILookup __lu, NCIMethod __m)
		throws NullPointerException
	{
		// Check
		if (__lu == null || __m == null)
			throw new NullPointerException("NARG");
		
		// Setup operations
		List<NROp> target = new ArrayList<>();
		__OpParser__ opr = new __OpParser__(__lu, __m);
		try
		{
			// Add all operations
			for (;;)
				target.add(opr.next());
		}
		
		// Ignore
		catch (NoSuchElementException e)
		{
		}
		
		// Debug
		System.err.printf("DEBUG -- Operations: %s%n", opr);
		
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/08
	 */
	@Override
	public NROp get(int __i)
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/08
	 */
	@Override
	public int size()
	{
		throw new Error("TODO");
	}
}

