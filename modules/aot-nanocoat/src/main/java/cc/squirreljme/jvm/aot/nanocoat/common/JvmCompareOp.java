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
 * Comparison operations for the virtual machine.
 *
 * @since 2023/07/16
 */
public enum JvmCompareOp
{
	/** Normal comparison. */
	CMP,
	
	/** Compare, positive on NaN. */
	CMPG,
	
	/** Compare, negative on NaN. */
	CMPL,
	
	/* End. */
	;
}
