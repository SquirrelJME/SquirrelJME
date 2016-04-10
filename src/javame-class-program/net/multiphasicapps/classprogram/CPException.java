// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.classprogram;

import net.multiphasicapps.descriptors.BinaryNameSymbol;

/**
 * This represents an exception which is handled within a method.
 *
 * The addressing is logical.
 *
 * @since 2016/03/31
 */
public class CPException
{
	/** The owning program. */
	protected final CPProgram program;
	
	/** The logical inclusive start address. */
	protected final int startpc;
	
	/** The logical inclusive end address. */
	protected final int endpc;
	
	/** The logical handler address. */
	protected final int handlerpc;
	
	/** The type of exception to handle. */
	protected final BinaryNameSymbol handles;
	
	/**
	 * This initializes an exception.
	 *
	 * @param __prg The owning program.
	 * @param __rx The raw exception.
	 * @throws CPProgramException If the input exception is not mappable to
	 * program instructions.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/31
	 */
	CPException(CPProgram __prg, CPRawException __rx)
		throws CPProgramException, NullPointerException
	{
		// Check
		if (__prg == null || __rx == null)
			throw new NullPointerException("NARG");
		
		// Set
		program = __prg;
		handles = __rx.handles();
		
		// Determine addresses
		startpc = program.physicalToLogical(__rx.startPC());
		handlerpc = program.physicalToLogical(__rx.handlerPC());
		
		// The end address is special
		int xe = __rx.endPC();
		if (xe == program.physicalSize())
			endpc = program.size() - 1;
		else
			endpc = program.physicalToLogical(xe) - 1;
		
		// {@squirreljme.error CP04 The exception does not map to any known
		// address. (The current exception)}
		if (startpc < 0 || endpc < 0 || handlerpc < 0)
			throw new CPProgramException(String.format("CP04 %s", __rx));
	}
	
	/**
	 * Returns the ending PC address.
	 *
	 * @return The ending PC address.
	 * @since 2016/03/31
	 */
	public int endPC()
	{
		return endpc;
	}
	
	/**
	 * Returns the address of the exception handler.
	 *
	 * @return The address of the exception handler code.
	 * @since 2016/03/31
	 */
	public int handlerPC()
	{
		return handlerpc;
	}
	
	/**
	 * Returns the name of the exception to catch.
	 *
	 * @return The exception to catch.
	 * @since 2016/03/31
	 */
	public BinaryNameSymbol handles()
	{
		return handles;
	}
	
	/**
	 * Returns the program that this exception handler is within.
	 *
	 * @return The program containing this exception handler.
	 * @since 2016/04/10
	 */
	public CPProgram program()
	{
		return program;
	}
	
	/**
	 * Returns the starting PC address.
	 *
	 * @return The starting PC address.
	 * @since 2016/03/31
	 */
	public int startPC()
	{
		return startpc;
	}
}

