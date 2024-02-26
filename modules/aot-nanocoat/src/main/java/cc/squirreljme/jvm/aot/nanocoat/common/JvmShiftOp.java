// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.aot.nanocoat.common;

/**
 * Represents the type of shift to perform.
 *
 * @since 2023/07/16
 */
public enum JvmShiftOp
{
	/** Signed shift left. */
	SIGNED_SHIFT_LEFT,
	
	/** Signed shift right. */
	SIGNED_SHIFT_RIGHT,
	
	/** Unsigned shift right. */
	UNSIGNED_SHIFT_RIGHT,
	
	/* End. */
	;
}
