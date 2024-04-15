// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.emulator;

/**
 * Kills the process.
 *
 * @since 2020/12/31
 */
public class ProcessKiller
	implements Runnable
{
	/** The process to be terminated. */
	protected final Process process;
	
	/**
	 * Initializes the process killer.
	 * 
	 * @param __process The process to be terminated.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/12/31
	 */
	public ProcessKiller(Process __process)
		throws NullPointerException
	{
		if (__process == null)
			throw new NullPointerException("NARG");
		
		this.process = __process;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/12/31
	 */
	@Override
	public void run()
	{
		this.process.destroy();
	}
}
