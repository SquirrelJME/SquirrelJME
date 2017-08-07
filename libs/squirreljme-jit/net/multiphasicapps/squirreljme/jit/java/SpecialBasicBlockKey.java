// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.java;

/**
 * This is a basic block key for special basic blocks if they are needed.
 *
 * @since 2017/08/07
 */
public enum SpecialBasicBlockKey
	implements BasicBlockKey
{
	/**
	 * The synchronized entry point of a method. For methods which are
	 * {@code synchronized} they have an implicit {@code monitorenter}
	 * instruction before the start of the method, however it is not to be
	 * treated as address zero (which means that jumps to address zero must
	 * not perform a {@code monitorenter}). Additionally the this class must
	 * be moved to a special variable so that it can be recalled when the
	 * synchronized method exits.
	 */
	SYNCHRONIZED_ENTER,
	
	/** End. */
	;
}

