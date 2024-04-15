// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.midlet;

import java.util.LinkedList;
import java.util.Queue;

/**
 * This manages the cleanup of MIDlet related methods.
 *
 * @since 2020/07/03
 */
public final class CleanupHandler
{
	/** Queue of handles waiting to be closed. */
	private static final Queue<AutoCloseable> _QUEUE =
		new LinkedList<>();
	
	/**
	 * Not used.
	 * 
	 * @since 2020/07/03
	 */
	private CleanupHandler()
	{
	}
	
	/**
	 * Adds a task to be called when the MIDlet exits.
	 * 
	 * @param __task The task to add.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/07/03
	 */
	public static void add(AutoCloseable __task)
		throws NullPointerException
	{
		if (__task == null)
			throw new NullPointerException("NARG");
		
		synchronized (CleanupHandler.class)
		{
			CleanupHandler._QUEUE.add(__task);
		}
	}
	
	/**
	 * Runs all of the cleanup handlers and drains from the queue.
	 * 
	 * @since 2020/07/03
	 */
	public static void runAll()
	{
		// Clear out the queue and drain everything to an array
		AutoCloseable[] drain;
		synchronized (CleanupHandler.class)
		{
			Queue<AutoCloseable> queue = CleanupHandler._QUEUE;
			drain = queue.<AutoCloseable>toArray(
				new AutoCloseable[queue.size()]);
			queue.clear();
		}
		
		// Close all those items now
		for (AutoCloseable closing : drain)
			try
			{
				closing.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
	}
}
