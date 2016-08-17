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

import net.multiphasicapps.squirreljme.jit.base.JITException;

/**
 * The JIT compiler is very ordered and as such this may be used by
 * implementation of the class writer to determine if such processing of the
 * code is in the correct order.
 *
 * @since 2016/07/18
 */
public enum JITCompilerOrder
{
	/** Set the class constant pool, but do not write it. */
	SET_CONSTANT_POOL,
	
	/** The flags for the class. */
	CLASS_FLAGS,
	
	/** The super class name. */
	SUPER_CLASS_NAME,
	
	/** Interface names. */
	INTERFACE_CLASS_NAMES,
	
	/** The number of fields. */
	FIELD_COUNT,
	
	/** Access flags for a field. */
	FIELD_FLAGS,
	
	/** End of field. */
	END_FIELD,
	
	/** The number of methods. */
	METHOD_COUNT,
	
	/** Access flags for a method. */
	METHOD_FLAGS,
	
	/** End of method. */
	END_METHOD,
	
	/** End of class. */
	END_CLASS,
	
	/** End. */
	;
	
	/** The first compiler piece to order. */
	public static final JITCompilerOrder FIRST =
		JITCompilerOrder.SET_CONSTANT_POOL;
	
	/** Internal values to get the next ordering. */
	private static final JITCompilerOrder[] _VALUES =
		JITCompilerOrder.values();
	
	/**
	 * Returns the next order, or {@code null} if processing is complete.
	 *
	 * @param __wf The current number of written fields.
	 * @param __nf The number of fields available.
	 * @apram __wm The number of written methods.
	 * @param __nm The number of methods.
	 * @return The next order or {@code null} if processing is complete.
	 * @since 2016/07/18
	 */
	public final JITCompilerOrder next(int __wf, int __nf, int __wm, int __nm)
	{
		// Get values
		JITCompilerOrder[] values = _VALUES;
		int n = values.length;
		
		// If there are no fields to write then go straight to methods
		if (this == FIELD_COUNT && __nf == 0)
			return METHOD_COUNT;
		
		// If there are no methods, end the class
		else if (this == METHOD_COUNT && __nm == 0)
			return END_CLASS;
		
		// If not enough fields were written then go back to wanting flags
		else if (this == END_FIELD && __wf < __nf)
			return FIELD_FLAGS;
		
		// The same goes for methods
		else if (this == END_METHOD && __wm < __nm)
			return METHOD_FLAGS;
		
		// More orderings?
		int i = ordinal() + 1;
		if (i < n)
			return values[i];
		
		// None are left
		return null;
	}
}

