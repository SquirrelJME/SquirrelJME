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
	 * There is no {@code executeLater} as that may produce deadlocks between
	 * the native UI and whatever task is running, additionally this may also
	 * cause cooperatively tasked systems to deadlock more easily.
	 *
	 * @param __task The task to run.
	 * @throws MLECallError On null arguments or execution of the given task
	 * failed.
	 * @since 2024/03/16
	 */
	@SquirrelJMEVendorApi
	void execute(@NotNull Runnable __task)
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
