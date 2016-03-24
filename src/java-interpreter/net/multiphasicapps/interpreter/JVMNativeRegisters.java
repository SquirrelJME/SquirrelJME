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

/**
 * This contains the set of native registers which exist in a program for
 * usage.
 *
 * If all of these are exhausted then the stack is used instead..
 *
 * @since 2016/03/24
 */
public class JVMNativeRegisters
{
	/** The number of integer registers. */
	protected final int numint;
	
	/** The number of floating point registers. */
	protected final	int numfloat;
	
	/** The size of integer registers. */
	protected final int sizeint;
	
	/** The size of floating point registers. */
	protected final int sizefloat;
	
	/**
	 * Initializes native registers with the given register total and size of
	 * the given registers.
	 *
	 * @para,
	 * @throws IllegalArgumentException If the number of integer registers is
	 * less than 1; if the number of floating point registers is less than
	 * zero; If the size of any register type
	 * @since 2016/03/24
	 */
	public JVMNativeRegisters(int __numint, int __sizeint, int __numfloat,
		int __sizefloat)
		throws IllegalArgumentException
	{
		// Check
		if (__numint <= 0 || __numfloat < 0)
			throw new IllegalArgumentException(String.format(
				"IN1o %d %d", __numint, __numfloat));
		if (((__sizeint % 8) != 0) || ((__sizefloat % 8 != 0)))
			throw new IllegalArgumentException(String.format(
				"IN1p %d %d", __sizeint, __sizefloat));
		if (__sizeint <= 0 || (__numfloat > 0 && __sizefloat <= 0))
			throw new IllegalArgumentException(String.format(
				"IN1q %d %d", __sizeint, __sizefloat));
		
		// Set
		numint = __numint;
		numfloat = __numfloat;
		sizeint = __sizeint;
		sizefloat = __sizefloat;
	}
}

