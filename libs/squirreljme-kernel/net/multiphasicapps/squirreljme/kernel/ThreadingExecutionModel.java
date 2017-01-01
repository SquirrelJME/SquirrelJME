// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.kernel;

/**
 * This represents how threads are executed on a given system, whether
 * SquirrelJME has to time slice and manage them individually or if an
 * operating system above manages such things.
 *
 * @since 2016/10/31
 */
public enum ThreadingExecutionModel
{
	/**
	 * This specifies that SquirrelJME does not manage actual thread timing
	 * (other than priority, sleeping, or interrupting) and that an operating
	 * system or virtual machine above SquirrelJME manages this.
	 */
	EXTERNAL_THREADING,
	
	/**
	 * This specifies that SquirrelJME performs the actual threading and
	 * context switching. SquirrelJME will execute threads that need to be ran
	 * along with context switching into them as needed.
	 */
	SLICE_THREADING,
	
	/** End. */
	;
}

