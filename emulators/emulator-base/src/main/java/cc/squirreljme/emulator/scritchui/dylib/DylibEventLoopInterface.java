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
import cc.squirreljme.runtime.cldc.debug.Debugging;
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
	 * @param __stateP The current state pointer.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/04/02
	 */
	public DylibEventLoopInterface(Reference<DylibScritchInterface> __selfApi,
		NativeScritchDylib __dyLib, long __stateP)
		throws NullPointerException
	{
		super(__selfApi, __dyLib, __stateP);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/04/02
	 */
	@Override
	public void execute(Runnable __task)
		throws MLECallError
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/04/02
	 */
	@Override
	public boolean inLoop()
	{
		throw Debugging.todo();
	}
}
