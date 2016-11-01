// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
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

