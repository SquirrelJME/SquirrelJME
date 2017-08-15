// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.arch;

import net.multiphasicapps.squirreljme.jit.JITConfig;
import net.multiphasicapps.squirreljme.jit.JITException;

/**
 * This class acts as the base for machine code output which is implemented by
 * each architecture. The machine code output does not care about any
 * optimizations that were performed, it just writes whatever instructions to
 * some output target. It should be noted that in most cases the output here
 * is intended really only to be written once rather than having multiple
 * variants of it. The machine code outputs are not intended in any way to
 * optimize what is input.
 *
 * @since 2017/08/07
 */
public interface MachineCodeOutput
	extends AutoCloseable
{
	/**
	 * This closes the machine code output which then causes any machine code
	 * which has been generated to be output if delayed output is utilized.
	 *
	 * @throws JITException If it could not be closed.
	 * @since 2017/08/13
	 */
	@Override
	public abstract void close()
		throws JITException;
	
	/**
	 * Compare the value within the specific register against zero using the
	 * given comparison then perform a relative branch.
	 *
	 * @param __t The type of comparison to make.
	 * @param __r The register to compare with zero.
	 * @param __a The target destination of the branch.
	 * @throws JITException If the branch is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/08/15
	 */
	public abstract void compareAndRelativeBranch(ZeroComparisonType __t,
		Object __r, FuturePositionMarker __a)
		throws JITException, NullPointerException;
	
	/**
	 * Returns the used JIT configuration.
	 *
	 * @return The JIT configuration.
	 * @since 2017/08/10
	 */
	public abstract JITConfig config();
	
	/**
	 * This creates a future position marker which may then be used to jump
	 * to another position in writing.
	 *
	 * @return A future position marker which may be used for a future jump to
	 * a given position.
	 * @since 2017/08/15
	 */
	public abstract FuturePositionMarker createFuturePositionMarker();
	
	/**
	 * This marks a future position which has previously been created. This can
	 * be used for jumps to instructions which are later referred to.
	 *
	 * @param __fpm The future position marker.
	 * @throws JITException If the position marker has already been claimed.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/08/15
	 */
	public abstract void markFuturePosition(FuturePositionMarker __fpm)
		throws JITException, NullPointerException;
	
	/**
	 * Returns a position marker which marks the current write position
	 * in the output code.
	 *
	 * @return The current position in the output.
	 * @since 2017/08/14
	 */
	public abstract PositionMarker positionMarker();
}

