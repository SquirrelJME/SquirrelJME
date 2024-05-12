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
import cc.squirreljme.jvm.mle.scritchui.ScritchComponentInterface;
import cc.squirreljme.jvm.mle.scritchui.callbacks.ScritchSizeListener;
import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchComponentBracket;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.lang.ref.Reference;

/**
 * Not Described.
 *
 * @since 2024/04/02
 */
public class DylibComponentInterface
	extends DylibBaseInterface
	implements ScritchComponentInterface
{
	/**
	 * Initializes the interface.
	 *
	 * @param __selfApi Reference to our own API.
	 * @param __dyLib The dynamic library interface.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/04/02
	 */
	public DylibComponentInterface(Reference<DylibScritchInterface> __selfApi,
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
	public int height(
		ScritchComponentBracket __component)
		throws MLECallError
	{
		if (__component == null)
			throw new MLECallError("Null arguments.");
		
		return this.dyLib.componentHeight((DylibComponentObject)__component);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/04/02
	 */
	@Override
	public void revalidate(ScritchComponentBracket __component)
		throws MLECallError
	{
		if (__component == null)
			throw new MLECallError("Null arguments.");
		
		this.dyLib.componentRevalidate((DylibComponentObject)__component);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/04/28
	 */
	@Override
	public void setSizeListener(ScritchComponentBracket __component,
		ScritchSizeListener __listener)
		throws MLECallError
	{
		if (__component == null || __listener == null)
			throw new MLECallError("Null arguments.");
		
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/04/02
	 */
	@Override
	public int width(
		ScritchComponentBracket __component)
		throws MLECallError
	{
		if (__component == null)
			throw new MLECallError("Null arguments.");
		
		return this.dyLib.componentWidth((DylibComponentObject)__component);
	}
}
