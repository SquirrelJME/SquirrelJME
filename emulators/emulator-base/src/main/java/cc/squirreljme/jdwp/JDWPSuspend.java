// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jdwp;

/**
 * Which kind of suspend is being performed?
 *
 * @since 2021/03/13
 */
public enum JDWPSuspend
{
	/** Query only. */
	QUERY,
	
	/** Suspend execution. */
	SUSPEND,
	
	/** Resume execution. */
	RESUME,
	
	/* End. */
	;
}
