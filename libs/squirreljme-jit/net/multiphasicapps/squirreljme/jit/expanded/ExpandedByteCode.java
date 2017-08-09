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

import net.multiphasicapps.squirreljme.jit.JITException;

/**
 * This interface is used to create expanded basic block interfaces which is
 * used to generate a much simpler instruction set.
 *
 * @see ExpandedBasicBlock
 * @since 2017/08/07
 */
public interface ExpandedByteCode
	extends AutoCloseable
{
	/**
	 * Closes the expanded byte code forcing all changes to be finalized. This
	 * may be needed by optimizing translators to perform all of their
	 * optimizations.
	 *
	 * @throws JITException If 
	 * @since 2017/08/09
	 */
	@Override
	public abstract void close()
		throws JITException;
}

