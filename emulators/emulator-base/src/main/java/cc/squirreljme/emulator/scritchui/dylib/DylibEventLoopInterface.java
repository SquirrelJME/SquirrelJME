// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.emulator.scritchui.dylib;

import cc.squirreljme.jvm.mle.exceptions.MLECallError;
import cc.squirreljme.jvm.mle.scritchui.ScritchEventLoopInterface;
import java.lang.ref.Reference;
import org.jetbrains.annotations.NotNull;

/**
 * Not Described.
 *
 * @since 2024/04/02
 */
public class DylibEventLoopInterface
	extends DylibBaseInterface
	implements ScritchEventLoopInterface
{
	/**
	 * Initializes the interface.
	 *
	 * @param __selfApi Reference to our own API.
	 * @param __dyLib The dynamic library interface.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/04/02
	 */
	public DylibEventLoopInterface(Reference<DylibScritchInterface> __selfApi,
		NativeScritchDylib __dyLib)
		throws NullPointerException
	{
		super(__selfApi, __dyLib);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/04/02
	 */
	@Override
	public void loopExecute(Runnable __task)
		throws MLECallError
	{
		if (__task == null)
			throw new MLECallError("Null arguments.");
		
		if (__task == null)
			throw new MLECallError("NARG");
		
		NativeScritchDylib.__loopExecute(this.dyLib._stateP, __task);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/04/25
	 */
	@Override
	public void loopExecuteLater(@NotNull Runnable __task)
		throws MLECallError
	{
		if (__task == null)
			throw new MLECallError("Null arguments.");
		
		if (__task == null)
			throw new MLECallError("NARG");
		
		NativeScritchDylib.__loopExecuteLater(this.dyLib._stateP, __task);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/04/17
	 */
	@Override
	public void loopExecuteWait(@NotNull Runnable __task)
		throws MLECallError
	{
		if (__task == null)
			throw new MLECallError("Null arguments.");
		
		if (__task == null)
			throw new MLECallError("NARG");
		
		if (NativeScritchDylib.__loopIsInThread(this.dyLib._stateP))
			__task.run();
		else
			NativeScritchDylib.__loopExecuteWait(this.dyLib._stateP, __task);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/04/02
	 */
	@Override
	public boolean inLoop()
	{
		return NativeScritchDylib.__loopIsInThread(this.dyLib._stateP);
	}
}
