// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.mle.constants;

/**
 * The status on what a {@link Thread} is doing.
 *
 * @since 2021/03/15
 */
public interface ThreadStatusType
{
	/** Running. */
	byte RUNNING =
		0;
	
	/** Sleeping. */
	byte SLEEPING =
		1;
	
	/** Waiting on a monitor. */
	byte MONITOR_WAIT =
		2;
}
