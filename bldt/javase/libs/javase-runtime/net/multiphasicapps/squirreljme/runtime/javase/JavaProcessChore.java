// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.runtime.javase;

import net.multiphasicapps.squirreljme.runtime.cldc.chore.Chore;

/**
 * This represents a chore which is associated with a given process rather
 * than being purely local.
 *
 * @since 2017/12/08
 */
public class JavaProcessChore
	extends JavaChore
{
	/** The remote process. */
	protected final Process process;
	
	/**
	 * Initializes the process backed chore.
	 *
	 * @param __group The group the chore is in.
	 * @param __proc The process to watch.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/12/08
	 */
	public JavaProcessChore(JavaChoreGroup __group, Process __proc)
		throws NullPointerException
	{
		super(__group);
		
		if (__proc == null)
			throw new NullPointerException("NARG");
		
		this.process = __proc;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/12/08
	 */
	@Override
	public long memoryUsed()
	{
		// The only way to get the details of another process memory usage is
		// to query it or use native system methods to access it
		// So for simplicity just return no memory used
		return 0L;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/12/08
	 */
	@Override
	public int status()
	{
		// Treat living processes as running
		Process process = this.process;
		if (process.isAlive())
			return Chore.STATUS_RUNNING;
		
		// A zero exit value is success
		int ev = process.exitValue();
		if (ev == 0)
			return Chore.STATUS_EXITED_REGULAR;
		return Chore.STATUS_EXITED_FATAL;
	}
}

