// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.narf.classinterface;

import net.multiphasicapps.descriptors.BinaryNameSymbol;
import net.multiphasicapps.descriptors.ClassNameSymbol;

/**
 * This describes a single exception that is handled by the byte code.
 *
 * @since 2016/05/08
 */
public final class NCICodeException
{
	/** The starting PC address. */
	protected final int startpc;
	
	/** The ending PC address. */
	protected final int endpc;
	
	/** The handler PC address. */
	protected final int handlerpc;
	
	/** The class type to handle. */
	protected final ClassNameSymbol handletype;
	
	/**
	 * Initializes the code exception.
	 *
	 * @param __spc Starting address.
	 * @param __epc Ending address.
	 * @param __hpc The handler address.
	 * @param __t The type of exception to handle, if {@code null} then this
	 * means {@link Throwable}.
	 * @throws NCIException If the addresses are negative or the end is at or
	 * before the start.
	 * @since 2016/05/08
	 */
	NCICodeException(int __spc, int __epc, int __hpc, BinaryNameSymbol __t)
		throws NCIException
	{
		// {@squirreljme.error AO01 The input addresses are either negative or
		// the end is at or before the start. (The start address; The end
		// address; The handler address)}
		if (__spc < 0 || __epc < 0 || __hpc < 0 || __epc <= __spc)
			throw new NCIException(NCIException.Issue.
				INVALID_EXCEPTION_ADDRESS, String.format("AO01 %d %d %d",
				__spc, __epc, __hpc));
		
		// Set
		startpc = __spc;
		endpc = __epc;
		handlerpc = __hpc;
		handletype = (__t == null ? BinaryNameSymbol.THROWABLE : __t).
			asClassName();
	}
}

