// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.classfile.register;

/**
 * This represents the type of stack shuffle to perform. Since these
 * operations depend on the types on the stack, this is used to contain the
 * information to simplify the operations.
 *
 * @since 2019/03/30
 */
public enum JavaStackShuffleType
{
	/** dup. */
	DUP,
	
	/** dup_x1. */
	DUP_X1,
	
	/** dup_x2. */
	DUP_X2,
	
	/** dup2. */
	DUP2,
	
	/** dup2_x1. */
	DUP2_X1,
	
	/** dup2_x2. */
	DUP2_X2,
	
	/** pop. */
	POP,
	
	/** pop2. */
	POP2,
	
	/** swap. */
	SWAP,
	
	/** End. */
	;
}

