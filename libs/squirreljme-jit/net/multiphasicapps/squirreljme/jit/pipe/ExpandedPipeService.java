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

import net.multiphasicapps.squirreljme.jit.arch.MachineCodeOutput;

/**
 * This is a service which allows for the creation of {@link ExpandedPipe}
 * which turns expanded byte code into machine instructions (or passes it to
 * another expanded pipe).
 *
 * @since 2017/08/13
 */
public interface ExpandedPipeService
{
	/**
	 * Creates the specified pipe which outputs to the given machine code
	 * target.
	 *
	 * @param __m The target machine code to write.
	 * @return The pipeline which handles expanded instructions.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/08/13
	 */
	public abstract ExpandedPipe createPipe(MachineCodeOutput __m)
		throws NullPointerException;
	
	/**
	 * Returns the name of this pipe.
	 *
	 * @return The name of this pipe.
	 * @since 2017/08/11
	 */
	public abstract String name();
}

