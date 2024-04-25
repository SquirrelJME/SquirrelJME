// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.mle.scritchui;

import cc.squirreljme.jvm.mle.exceptions.MLECallError;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import org.jetbrains.annotations.Async;
import org.jetbrains.annotations.NotNull;

/**
 * Interface for event loops.
 *
 * @since 2024/03/16
 */
@SquirrelJMEVendorApi
public interface ScritchEventLoopInterface
{
	/**
	 * Executes the given task in the event loop, if the current thread is
	 * the event loop then this will run it immediately.
	 *
	 * @param __task The task to run.
	 * @throws MLECallError On null arguments or execution of the given task
	 * failed.
	 * @since 2024/03/16
	 */
	@SquirrelJMEVendorApi
	@Async.Execute
	@Async.Schedule
	void execute(@NotNull Runnable __task)
		throws MLECallError;
	
	/**
	 * Executes a call for a run later in the event loop.
	 *
	 * @param __task The task to execute later.
	 * @throws MLECallError On null arguments.
	 * @since 2024/04/25
	 */
	@SquirrelJMEVendorApi
	@Async.Schedule
	void executeLater(@NotNull Runnable __task)
		throws MLECallError;
	
	/**
	 * Executes the given task in the event loop, if the current thread is
	 * the event loop then this will run it immediately otherwise it will
	 * wait for the given method to finish execution.
	 *
	 * @param __task The task to run.
	 * @throws MLECallError On null arguments or execution of the given task
	 * failed.
	 * @since 2024/04/17
	 */
	@SquirrelJMEVendorApi
	@Async.Execute
	@Async.Schedule
	void executeWait(@NotNull Runnable __task)
		throws MLECallError;
	
	/**
	 * Returns whether the current thread is the event loop thread.
	 *
	 * @return If the current thread is the event loop thread.
	 * @since 2024/03/16
	 */
	@SquirrelJMEVendorApi
	boolean inLoop();
}
