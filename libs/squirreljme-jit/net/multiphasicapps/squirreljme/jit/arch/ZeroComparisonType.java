// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.arch;

/**
 * This is the type of comparison to perform against zero.
 *
 * @since 2017/08/15
 */
public enum ZeroComparisonType
{
	/** Value is zero. */
	ZERO,
	
	/** Value is not zero. */
	NOT_ZERO,
	
	/** Value is positive. */
	POSITIVE,
	
	/** Value is negative. */
	NEGATIVE,
	
	/** End. */
	;
}

