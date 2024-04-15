// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.launcher.ui;

import cc.squirreljme.jvm.mle.TaskShelf;
import cc.squirreljme.jvm.mle.brackets.TaskBracket;
import cc.squirreljme.jvm.mle.constants.TaskStatusType;
import cc.squirreljme.jvm.mle.exceptions.MLECallError;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.util.TimerTask;
import javax.microedition.lcdui.Display;

/**
 * This is used to regain control of tasks and make it so the launcher is
 * brought back up after it has lost hardware control.
 *
 * @since 2018/12/11
 */
final class __ReControlTask__
	extends TimerTask
{
	/** The display used. */
	protected final Display display;
	
	/** The task information. */
	private final __ActiveTask__ _active;
	
	/**
	 * Initializes the re-control task.
	 *
	 * @param __display The display used.
	 * @param __task The task to check.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/12/11
	 */
	__ReControlTask__(Display __display, __ActiveTask__ __task)
		throws NullPointerException
	{
		if (__display == null || __task == null)
			throw new NullPointerException("NARG");
		
		this.display = __display;
		this._active = __task;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/11
	 */
	@Override
	public final void run()
	{
		Display display = this.display;
		__ActiveTask__ active = this._active;
		
		// Need to check the task status
		boolean recover = false;
		synchronized (active)
		{
			TaskBracket task = active._task;
			
			// If null, then we should recover because it was cleared
			if (task == null)
				recover = true;
			
			// If the task has stopped for any reason, we recover
			if (!recover)
				try
				{
					int status = TaskShelf.status(task);
					recover = (status != TaskStatusType.ALIVE);
				}
				catch (MLECallError e)
				{
					e.printStackTrace();
					
					// Probably crashed?
					recover = true;
				}
			
			// Recover our display?
			if (recover)
			{
				// Note it
				Debugging.debugNote("Recovering launcher...");
				
				// We no longer have a task launched
				active._task = null;
				
				// Cancel self since we did it!
				this.cancel();
				
				// Make it current which takes the display over again, we
				// do not need to specify which displayable it was because the
				// display remembers it
				display.setCurrent(display.getCurrent());
			}
		}
	}
}

