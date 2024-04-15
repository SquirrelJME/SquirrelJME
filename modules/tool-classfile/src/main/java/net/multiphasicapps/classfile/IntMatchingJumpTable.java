// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
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
	InstructionJumpTarget match(int __k);
	
	/**
	 * Returns all of the used jump targets.
	 *
	 * @return The used jump targets.
	 * @since 2019/03/31
	 */
	InstructionJumpTarget[] targets();
}

