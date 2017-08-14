// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.pipe;

import net.multiphasicapps.squirreljme.jit.java.BasicBlockKey;
import net.multiphasicapps.squirreljme.jit.java.TypedVariable;
import net.multiphasicapps.squirreljme.jit.java.Variable;
import net.multiphasicapps.squirreljme.jit.JITException;

/**
 * This is an expanded pipe which is given expanded byte code.
 *
 * It is {@link AutoCloseable} so that the compiler can instantly tell the
 * pipeline when it has been closed so that if delayed instruction generation
 * is required it can output such instructions at the final stage.
 *
 * @since 2017/08/13
 */
public interface ExpandedPipe
	extends AutoCloseable
{
	/**
	 * Closes the expanded pipe forcing all changes to be finalized. This
	 * may be needed by optimizing pipes to perform all of their
	 * optimizations.
	 *
	 * @throws JITException If it could not be closed.
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
	 * This specifies that the given basic block is being entered any any
	 * expanded byte codes passed pertain to this given block. Basic blocks
	 * will generally be used as jumping points although a compiler could
	 * perform specific optimizations.
	 *
	 * @param __k The key which identifies the basic block being entered.
	 * @throws JITException If the block could not be entered.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/08/13
	 */
	public abstract void enterBlock(BasicBlockKey __k)
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

