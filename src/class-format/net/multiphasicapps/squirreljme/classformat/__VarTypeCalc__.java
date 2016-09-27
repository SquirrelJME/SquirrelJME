// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.classformat;

/**
 * This calculates the variable types for all positions within the byte code
 * which may be used by the JIT to perform pre-determined stack size
 * determination.
 *
 * @since 2016/09/27
 */
class __VarTypeCalc__
{
	/** The returning type array. */
	protected final int[] types;
	
	__VarTypeCalc__(ExtendedDataInputStream __dis, int __cl,
		Map<Integer, __SMTState__> __smt)
		throws NullPointerException
	{
		// Check
		if (__dis == null || __smt == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.
	}
}

