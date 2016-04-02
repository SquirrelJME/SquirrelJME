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

import net.multiphasicapps.descriptors.BinaryNameSymbol;

/**
 * This represents a raw exception as described by the code attribute.
 *
 * @since 2016/03/31
 */
public class JVMRawException
{
	/** The start PC address (inclusive). */
	protected final int startpc;
	
	/** The ending PC address (exclusive). */
	protected final int endpc;
	
	/** The handler PC address. */
	protected final int handlerpc;
	
	/** The exception to be caught. */
	protected final BinaryNameSymbol catches;
	
	/**
	 * Initializes the raw exception.
	 *
	 * @param __spc The starting PC address (inclusive).
	 * @param __epc The ending PC address (exclusive).
	 * @param __hpc The address which handles this exception.
	 * @param __catch The exception to handle, if {@code null} then
	 * {@link Throwable} is used instead.
	 * @since 2016/03/31
	 */
	public JVMRawException(int __spc, int __epc, int __hpc,
		BinaryNameSymbol __catch)
	{
		// Set
		startpc = __spc;
		endpc = __epc;
		handlerpc = __hpc;
		catches = (__catch != null ? __catch : BinaryNameSymbol.THROWABLE);
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
		return catches;
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
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/31
	 */
	@Override
	public String toString()
	{
		return String.format("[%d, %d] -> %d (%s)", startpc, endpc, handlerpc,
			catches);
	}
}

