// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.scritchui.fb;

import cc.squirreljme.jvm.mle.exceptions.MLECallError;
import cc.squirreljme.jvm.mle.scritchui.ScritchEventLoopInterface;
import cc.squirreljme.jvm.mle.scritchui.ScritchInterface;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.lang.ref.Reference;
import org.jetbrains.annotations.NotNull;

/**
 * Interface for the event loop.
 *
 * @since 2024/03/24
 */
@SquirrelJMEVendorApi
public class FramebufferEventLoopInterface
	extends FramebufferBaseInterface
	implements ScritchEventLoopInterface
{
	/**
	 * Initializes this interface. 
	 *
	 * @param __self The framebuffer self interface.
	 * @param __core The core interface.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/03/24
	 */
	public FramebufferEventLoopInterface(
		Reference<FramebufferScritchInterface> __self,
		ScritchInterface __core)
		throws NullPointerException
	{
		super(__self, __core);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/03/24
	 */
	@Override
	public void execute(Runnable __task)
		throws MLECallError
	{
		if (__task == null)
			throw new MLECallError("NARG");
		
		// Forward to the native event loop
		this.coreApi.eventLoop().execute(__task);
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
			throw new MLECallError("NARG");
		
		// Forward to the native event loop
		this.coreApi.eventLoop().executeLater(__task);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/04/17
	 */
	@Override
	public void executeWait(@NotNull Runnable __task)
		throws MLECallError
	{
		if (__task == null)
			throw new MLECallError("NARG");
		
		// Forward to the native event loop
		this.coreApi.eventLoop().executeWait(__task);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/03/24
	 */
	@Override
	public boolean inLoop()
	{
		// Use forwarded determination
		return this.coreApi.eventLoop().inLoop();
	}
}
