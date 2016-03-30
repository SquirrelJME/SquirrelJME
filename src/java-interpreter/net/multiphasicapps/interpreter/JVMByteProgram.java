// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.interpreter;

import net.multiphasicapps.descriptors.FieldSymbol;
import net.multiphasicapps.descriptors.MethodSymbol;

/**
 * This class is given a chunk of byte code .
 *
 * @since 2016/03/29
 */
public class JVMByteProgram
{
	/** Lock. */
	protected final Object lock =
		new Object();
	
	/** Maximum local variables. */
	protected final int maxlocals;
	
	/** Maximum stack variables. */
	protected final int maxstack;
	
	/** The buffer containing the raw byte code. */
	protected final byte[] code;
	
	/**
	 * This initializes the program using the specified code array.
	 *
	 * @param __ml Maximum number of local variables.
	 * @param __ms Maximum number of stack entries.
	 * @param __inst Is this an instance method?
	 * @param __desc The descriptor which describes the argument of this
	 * method, this is used to seed the initial stack.
	 * @param __ins Is this an instance method (which has a {@code this}?).
	 * @param __code The input byte code, note that it is not copied and that
	 * it is used directly.
	 * @throws JVMVerifyException If the maximum stack and local entries are
	 * negative, or there is not enough room
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/29
	 */
	public JVMByteProgram(int __ml, int __ms, MethodSymbol __desc,
		boolean __ins, byte... __code)
		throws JVMVerifyException, NullPointerException
	{
		// Check
		if (__desc == null || __code == null)
			throw new NullPointerException("NARG");
		
		// Set
		maxlocals = __ml;
		maxstack = __ms;
		code = __code;
		
		// Setup the initial program state based on the method descriptor.
		if (true)
			throw new Error("TODO");
	}
}

