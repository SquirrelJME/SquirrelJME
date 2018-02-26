// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jit.compiler;

/**
 * This specifies the fields which are part of the class structure and is used
 * to store class information.
 *
 * @since 2018/02/26
 */
public enum ClassStructure
{
	/** Name of the class. */
	NAME,
	
	/** Superclass pointer. */
	SUPER,
	
	/** Number of implemented interfaces. */
	INTERFACES_COUNT.
	
	/** Pointer to implemented interfaces. */
	INTERFACES,
	
	/** Class flags. */
	FLAGS,
	
	/** Default constructor pointer (used for {@code newInstance()}). */
	DEFAULT_CONSTRUCTOR,
	
	/** The size of the class in bytes, for allocation. */
	ALLOCATION_SIZE,
	
	/** End. */
	;
}

