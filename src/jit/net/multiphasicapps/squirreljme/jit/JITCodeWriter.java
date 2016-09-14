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

import net.multiphasicapps.squirreljme.classformat.CodeDescriptionStream;
import net.multiphasicapps.squirreljme.classformat.StackMapType;
import net.multiphasicapps.squirreljme.jit.base.JITException;

/**
 * The implementation of this class is given raw instructional data which
 * determines which operations are used in the byte code of a method.
 *
 * @since 2016/09/10
 */
public interface JITCodeWriter
	extends AutoCloseable, CodeDescriptionStream
{
	/**
	 * {@inheritDoc}
	 * @since 2016/09/10
	 */
	@Override
	public abstract void close()
		throws JITException;
	
	/**
	 * Primes the argument methods, essentially the input used for the method
	 * as it varies between methods. If the method is an instance method
	 * then the first argument will be {@link StackMapType#OBJECT}.
	 *
	 * All of the input arguments map to virtual registers starting from
	 * {@code 0}.
	 *
	 * Any {@code null} elements in the type array specify that the area is
	 * not used and would be the top of a {@code float} or {@code double}.
	 *
	 * @param __eh Are there exception handlers present in the byte code? Note
	 * that this is only for traditional exception handlers,
	 * {@code synchronized} methods will have this flag as false although they
	 * do require an implicit {@code finally} block with a {@code monitorexit}.
	 * @param __t The variable types which are used in input.
	 * @throws JITException If the arguments could not be mapped.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/08/29
	 */
	public abstract void primeArguments(boolean __eh, StackMapType[] __t)
		throws JITException, NullPointerException;
}

