// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.debugger;

/**
 * How frame locations should be interpreted.
 *
 * @since 2024/01/26
 */
public enum FrameLocationInterpret
{
	/** Byte code address. */
	ADDRESS,
	
	/** Byte code index. */
	INDEX,
	
	/* End. */
	;
}
