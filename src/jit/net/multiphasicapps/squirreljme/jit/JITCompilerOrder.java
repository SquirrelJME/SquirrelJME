// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit;

/**
 * The JIT compiler is very ordered and as such this may be used by
 * implementation of the class writer to determine if such processing of the
 * code is in the correct order.
 *
 * @since 2016/07/18
 */
public enum JITCompilerOrder
{
	/** The flags for the class. */
	CLASS_FLAGS,
	
	/** End. */
	;
	
	/** The first compiler piece to order. */
	public static final JITCompilerOrder FIRST =
		JITCompilerOrder.CLASS_FLAGS;
	
	/** Internal values to get the next ordering. */
	private static final JITCompilerOrder[] _VALUES =
		JITCompilerOrder.values();
	
	/**
	 * Returns the next order, or {@code null} if processing is complete.
	 *
	 * @return The next order or {@code null} if processing is complete.
	 * @since 2016/07/18
	 */
	public final JITCompilerOrder next()
	{
		// Get values
		JITCompilerOrder[] values = _VALUES;
		int n = values.length;
		
		// More orderings?
		int i = ordinal() + 1;
		if (i < n)
			return values[i];
		
		// None are left
		return null;
	}
}

