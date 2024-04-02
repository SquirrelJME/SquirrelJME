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
import cc.squirreljme.jvm.mle.scritchui.ScritchContainerInterface;
import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchComponentBracket;
import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchContainerBracket;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.lang.ref.Reference;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

/**
 * Not Described.
 *
 * @since 2024/04/02
 */
public class DylibContainerInterface
	extends DylibBaseInterface
	implements ScritchContainerInterface
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
	public DylibContainerInterface(Reference<DylibScritchInterface> __selfApi,
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
	public void add(ScritchContainerBracket __container,
		ScritchComponentBracket __component)
		throws MLECallError
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/04/02
	 */
	@Override
	public void removeAll(ScritchContainerBracket __container)
		throws MLECallError
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/04/02
	 */
	@Override
	public void setBounds(ScritchContainerBracket __container,
		ScritchComponentBracket __component, int __x, int __y,
		int __w,
		int __h)
		throws MLECallError
	{
		throw Debugging.todo();
	}
}
