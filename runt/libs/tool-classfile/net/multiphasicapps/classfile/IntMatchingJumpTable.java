// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.classfile;

/**
 * This interface represents a jump table which matches based on an input
 * integer.
 *
 * @since 2018/09/20
 */
public interface IntMatchingJumpTable
{
	/**
	 * Matches the input key with the given jump target or returns the default.
	 *
	 * @param __k The key to match.
	 * @return The jump target for the match or the default if it was not
	 * found.
	 * @since 2018/09/20
	 */
	public abstract InstructionJumpTarget match(int __k);
}

