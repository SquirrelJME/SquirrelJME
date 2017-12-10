// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.runtime.kernel;

/**
 * This is used to specify which kind of metric to obtain from a task.
 *
 * @since 2017/12/10
 */
public interface KernelTaskMetric
{
	/** Used memory. */
	public static final int MEMORY_USED =
		1;
	
	/** Free memory. */
	public static final int MEMORY_FREE =
		2;
	
	/** Total memory. */
	public static final int MEMORY_TOTAL =
		3;
}

