// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
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
