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
 * This determines how compatible one type is with another.
 *
 * @since 2017/09/19
 */
@Deprecated
public enum TypeCompatibility
{
	/** Not compatible. */
	NONE,
	
	/** Requires an instance check. */
	CHECK_INSTANCE,
	
	/** Compatible. */
	COMPATIBLE,
	
	/** End. */
	;
}

