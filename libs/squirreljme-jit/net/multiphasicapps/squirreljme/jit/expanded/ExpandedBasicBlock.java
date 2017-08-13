// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.expanded;

import net.multiphasicapps.squirreljme.jit.java.TypedVariable;
import net.multiphasicapps.squirreljme.jit.java.Variable;
import net.multiphasicapps.squirreljme.jit.JITException;

/**
 * This class is initialized by an implementation of {@link ExpandedByteCode}
 * and is used to translate expanded instructions to a target language
 * depending on the implementation.
 *
 * This is an {@code abstract class} rather than an {@link interface} so that
 * virtual methods can be used which expand upon primitives when needed.
 *
 * @see ExpandedByteCode
 * @since 2017/08/07
 */
public abstract class ExpandedBasicBlock
	implements AutoCloseable
{
	/**
	 * Closes the expanded basic block forcing all changes to be finalized.
	 * This may be needed by optimizing translators to perform all of their
	 * optimizations.
	 *
	 * @throws JITException If 
	 * @since 2017/08/09
	 */
	@Override
	public abstract void close()
		throws JITException;
	
	/**
	 * Copies from the source variable to the destination variable.
	 *
	 * @param __src The source variable.
	 * @param __dest The destination variable.
	 * @throws JITException If it could not be copied.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/08/13
	 */
	public abstract void copy(TypedVariable __src, Variable __dest)
		throws JITException, NullPointerException;
	
	/**
	 * Counts a reference either up or down.
	 *
	 * @param __obj The object to change the reference count for.
	 * @param __up If {@code true} then the reference is counted up meaning it
	 * is being used more, otherwise {@code false} means it is being used
	 * less and may be collected.
	 * @throws JITException If the variable is not an object or is not
	 * initialized.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/08/13
	 */
	public abstract void countReference(TypedVariable __obj, boolean __up)
		throws JITException, NullPointerException;
	
	/**
	 * Enters the monitor on the specified object causing it to lock.
	 *
	 * @param __obj The object to lock.
	 * @throws JITException If the object is not an object or is not
	 * initialized.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/08/13
	 */
	public abstract void monitorEnter(TypedVariable __obj)
		throws JITException, NullPointerException;
}

