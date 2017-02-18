// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit;

/**
 * This represents a hint on how a variable is used, whether it should be
 * active because a value is going to be used soon. Note that these are only
 * hints and not requirements, for example it is not required when using
 * the {@link #STORED} hint to place the value in memory outside of registers.
 *
 * @since 2017/02/18
 */
public enum VariableUseHint
{
	/**
	 * This indicates that the variable use is unspecified in that it is
	 * unknown if it will be active during a calculation or not.
	 */
	UNSPECIFIED,
	
	/**
	 * This indicates that the variable will actively be used to perform some
	 * work. Some architectures do not allow working with memory locations and
	 * as such require values to be within registers to operate on them.
	 */
	ACTIVE,
	
	/**
	 * This indicates that the variable will not actively be used and may be
	 * stored in a secondary location if applicable.
	 */
	STORED,
	
	/**
	 * This indicates that the value is to be dropped and it no longer has to
	 * have its value stored.
	 */
	DROP,
	
	/** End. */
	;
}

