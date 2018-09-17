// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc.task;

/**
 * This is used to specify which kind of metric to obtain from a task.
 *
 * @since 2017/12/10
 */
@Deprecated
public enum SystemTaskMetric
{
	/** The task priority. */
	@Deprecated
	PRIORITY,
	
	/** Used memory. */
	@Deprecated
	MEMORY_USED,
	
	/** Free memory. */
	@Deprecated
	MEMORY_FREE,
	
	/** Total memory. */
	@Deprecated
	MEMORY_TOTAL,
	
	/** End. */
	;
}

