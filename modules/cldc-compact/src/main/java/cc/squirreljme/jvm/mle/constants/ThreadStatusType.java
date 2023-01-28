// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.mle.constants;

import cc.squirreljme.runtime.cldc.annotation.Exported;

/**
 * The status on what a {@link Thread} is doing.
 *
 * @since 2021/03/15
 */
@Exported
public interface ThreadStatusType
{
	/** Running. */
	@Exported
	byte RUNNING =
		0;
	
	/** Sleeping. */
	@Exported
	byte SLEEPING =
		1;
	
	/** Waiting on a monitor. */
	@Exported
	byte MONITOR_WAIT =
		2;
}
