// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jdwp;

/**
 * Indicates the direction of {@link JDWPCommLink}.
 *
 * @since 2024/01/19
 */
public enum JDWPCommLinkDirection
{
	/** Connecting the debugger to the client, we are debugging the client. */
	DEBUGGER_TO_CLIENT,
	
	/**
	 * Connecting the client to the debugger, we are the client connection to
	 * the debugger.
	 */
	CLIENT_TO_DEBUGGER,
	
	/* End. */
	;
}
