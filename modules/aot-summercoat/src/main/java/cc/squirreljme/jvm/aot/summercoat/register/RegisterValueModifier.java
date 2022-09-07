// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.aot.summercoat.register;

/**
 * This represents a modifier to a register on how a previous event has
 * affected the value of a register.
 *
 * @since 2022/09/06
 */
public enum RegisterValueModifier
{
	/** Value is a copy of another register. */
	COPY,
	
	/** Value is a mutation of another register. */
	MUTATE,
	
	/** Value is within a given range of a value, it now has bounds. */
	LIMIT,
	
	/** Value is from a future point jointing to a time previously. */
	BACK_FROM_FUTURE,
	
	/* End. */
	;
}
