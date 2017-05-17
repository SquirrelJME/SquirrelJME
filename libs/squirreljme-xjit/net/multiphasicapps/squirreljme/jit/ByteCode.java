// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit;

/**
 * This represents the byte code for a given method. It contains the actual
 * instructions, iterators over instructions, along with jump targets which
 * are available for usage.
 *
 * @since 2017/05/14
 */
public class ByteCode
{
	/** The byte code for the method. */
	private final byte[] _code;
	
	/** Instruction lengths at each position. */
	private final int[] _lengths;
	
	/**
	 * Represents the byte code.
	 *
	 * @param __ms The maximum number of stack entries.
	 * @param __ml The maximum number of local entries.
	 * @param __code The program's byte code, this is not copied.
	 * @param __eht The exception handler table.
	 * @throws JITException If the byte code is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/05/14
	 */
	ByteCode(int __ms, int __ml, byte[] __code, ExceptionHandlerTable __eht)
		throws JITException, NullPointerException
	{
		// Check
		if (__code == null || __eht == null)
			throw new NullPointerException("NARG");
		
		// Set
		this._code = __code;
		int codelen = __code.length;
		
		// Set all lengths initially to invalid positions, this used as a quick
		// marker to determine which positions have valid instructions
		int[] lengths = new int[codelen];
		for (int i = 0; i < codelen; i++)
			lengths[i] = -1;
		
		// Determine instruction lengths for each position
		for (int i = 0; i < codelen;)
		{
			// Store length
			int oplen;
			lengths[i] = (oplen = __opLength(__code, i));
			
			// {@squirreljme.error AQ2b The operation exceeds the bounds of
			// the method byte code. (The operation pointer; The operation
			// length; The code length)}
			if ((i += oplen) > codelen)
				throw new JITException(String.format("AQ2b %d %d %d", i, oplen,
					codelen));
		}
		this._lengths = lengths;
		
		throw new todo.TODO();
	}
	
	/**
	 * Returns the length of the operation at the given address.
	 *
	 * @param __code The method byte code.
	 * @param __a The address of the instruction to get the length of.
	 * @throws JITException If the instruction is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/05/17
	 */
	private static int __opLength(byte[] __code, int __a)
		throws JITException, NullPointerException
	{
		// Check
		if (__code == null)
			throw new NullPointerException("NARG");
		
		// Base instruction length is always 1
		int rv = 1;
		
		// Read operation
		int op = (__code[__a] & 0xFF);
		if (op == __OperandIndex__.WIDE)
		{
			op = (op << 8) | (__code[__a + 1] & 0xFF);
			rv = 2;
		}
		
		// Depends on the operation
		switch (op)
		{
				// Instructions which have no arguments
			case __OperandIndex__.NOP:
				break;
			
				// {@squirreljme.error AQ2c Cannot get the length of the
				// specified operation because it is not valid. (The operation;
				// The address)}
			default:
				throw new JITException(String.format("AQ2c %d %d", op, __a));
		}
		
		return rv;
	}
}

