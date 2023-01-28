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
 * This represents the status of a task.
 *
 * @since 2020/07/02
 */
@Exported
public interface TaskStatusType
{
	/** The task has exited. */
	@Exported
	byte EXITED =
		0;
	
	/** The task is alive. */
	@Exported
	byte ALIVE =
		1;
	
	/** The number of status types. */
	@Exported
	byte NUM_TAS_STATUSES =
		2;
}
