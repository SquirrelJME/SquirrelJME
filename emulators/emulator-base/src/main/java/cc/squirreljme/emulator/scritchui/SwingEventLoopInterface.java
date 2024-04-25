// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.emulator.scritchui;

import cc.squirreljme.jvm.mle.exceptions.MLECallError;
import cc.squirreljme.jvm.mle.scritchui.ScritchEventLoopInterface;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import javax.swing.SwingUtilities;
import org.jetbrains.annotations.NotNull;

/**
 * Event loop interface for Swing.
 *
 * @since 2024/03/16
 */
public class SwingEventLoopInterface
	implements ScritchEventLoopInterface
{
	/**
	 * {@inheritDoc}
	 * @since 2024/03/16
	 */
	@Override
	public void execute(@NotNull Runnable __task)
		throws MLECallError
	{
		if (__task == null)
			throw new MLECallError("Null arguments.");
		
		// If in the loop run now!
		if (this.inLoop())
			try
			{
				__task.run();
			}
			catch (Throwable __any)
			{
				throw new MLECallError("Execute failed.", __any);
			}
		
		// Invoke later in the Swing thread
		else
			SwingUtilities.invokeLater(__task);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/04/25
	 */
	@Override
	public void executeLater(@NotNull Runnable __task)
		throws MLECallError
	{
		if (__task == null)
			throw new MLECallError("Null arguments.");
		
		SwingUtilities.invokeLater(__task);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/04/17
	 */
	@Override
	public void executeWait(@NotNull Runnable __task)
		throws MLECallError
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/03/16
	 */
	@Override
	public boolean inLoop()
	{
		return SwingUtilities.isEventDispatchThread();
	}
}
