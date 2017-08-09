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
}

