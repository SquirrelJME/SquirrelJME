// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.link;

/**
 * These are flags which are used by methods.
 *
 * @since 2016/04/23
 */
public enum MethodFlag
	implements MemberFlag
{
	/** Public method. */
	PUBLIC,
	
	/** Private method. */
	PRIVATE,
	
	/** Protected method. */
	PROTECTED,
	
	/** Static method. */
	STATIC,
	
	/** Final method. */
	FINAL,
	
	/** Synchronized method. */
	SYNCHRONIZED,
	
	/** Bridge method. */
	BRIDGE,
	
	/** Variable argument method. */
	VARARGS,
	
	/** Native method. */
	NATIVE,
	
	/** Abstract method. */
	ABSTRACT,
	
	/** Strict floating point method. */
	STRICT,
	
	/** Synthetic method. */
	SYNTHETIC,
	
	/** End. */
	;
}

