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

import net.multiphasicapps.squirreljme.classformat.MethodDescriptionStream;
import net.multiphasicapps.squirreljme.classformat.MethodInvokeType;
import net.multiphasicapps.squirreljme.classformat.MethodReference;
import net.multiphasicapps.squirreljme.classformat.StackMapType;
import net.multiphasicapps.squirreljme.jit.base.JITException;

/**
 * This interface is used by the class writer to write output logic to the
 * resulting native machine code generator.
 *
 * The JIT itself converts the stack based machine to a completely register
 * based machine. As such, stack variables are offset by the number of local
 * variables meaning that if any value is greater than {@code maxlocals} then
 * it is a temporary stack item.
 *
 * @since 2016/08/19
 */
public interface JITMethodWriter
	extends AutoCloseable, MethodDescriptionStream
{
	/**
	 * {@inheritDoc}
	 * @since 2016/08/19
	 */
	@Override
	public abstract void close()
		throws JITException;
		
	/**
	 * {@inheritDoc}
	 * @since 2016/09/09
	 */
	@Override
	public abstract JITCodeWriter code()
		throws JITException;
}

