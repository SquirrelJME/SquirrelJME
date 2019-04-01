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
	DUP("a:aa"),
	
	/** dup_x1. */
	DUP_X1("ba:aba"),
	
	/** dup_x2. */
	DUP_X2("cba:acba"),
	
	/** dup2. */
	DUP2("ba:baba",
		"A:AA"),
	
	/** dup2_x1. */
	DUP2_X1("cba:bacba",
		"bA:AbA"),
	
	/** dup2_x2. */
	DUP2_X2("dcba:badcba",
		"cbA:Acba",
		"Cba:baCba",
		"BA:ABA"),
	
	/** pop. */
	POP("a:"),
	
	/** pop2. */
	POP2("ba:",
		"A:"),
	
	/** swap. */
	SWAP("ba:ab"),
	
	/** End. */
	;
	
	/** Forms of this operation. */
	private final String[] _forms;
	
	/**
	 * Initialize the shuffle form information.
	 *
	 * The forms consist of characters for the various items on the stack.
	 * A lowercase letter represents a narrow type while a capital letter
	 * represents a wide type. Input and output is separated by a colon. The
	 * operation is just that whatever is pushed to the stack has the same
	 * value as the items removed from the stack.
	 *
	 * @param __fs The forms.
	 * @since 2019/04/01
	 */
	private JavaStackShuffleType(String... __fs)
	{
		this._forms = __fs;
	}
}

