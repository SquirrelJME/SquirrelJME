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
import cc.squirreljme.jvm.mle.scritchui.ScritchPaintableInterface;
import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchComponentBracket;
import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchPaintableBracket;
import cc.squirreljme.jvm.mle.scritchui.callbacks.ScritchPaintListener;
import java.lang.ref.Reference;

/**
 * Dylib support for paintables.
 *
 * @since 2024/07/16
 */
public class DylibPaintableInterface
	extends DylibBaseInterface
	implements ScritchPaintableInterface
{
	/**
	 * Initializes the interface.
	 *
	 * @param __selfApi Reference to our own API.
	 * @param __dyLib The dynamic library interface.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/07/16
	 */
	public DylibPaintableInterface(Reference<DylibScritchInterface> __selfApi,
		NativeScritchDylib __dyLib)
	{
		super(__selfApi, __dyLib);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/04/02
	 */
	@Override
	public void repaint(ScritchComponentBracket __component)
		throws MLECallError
	{
		if (__component == null)
			throw new MLECallError("Null arguments.");
		
		if ((DylibComponentObject)__component == null)
			throw new MLECallError("NARG");
		
		NativeScritchDylib.__componentRepaint(this.dyLib._stateP,
			((DylibComponentObject)__component).objectPointer(), 0, 0,
			Integer.MAX_VALUE, Integer.MAX_VALUE);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/04/02
	 */
	@Override
	public void setPaintListener(ScritchPaintableBracket __component,
		ScritchPaintListener __listener)
		throws MLECallError
	{
		if (__component == null)
			throw new MLECallError("No component given.");
		
		// Forward
		if ((DylibPaintableObject)__component == null)
			throw new MLECallError("NARG");
		
		// Forward
		NativeScritchDylib.__componentSetPaintListener(this.dyLib._stateP,
			((DylibBaseObject)(DylibPaintableObject)__component).objectPointer(),
			__listener);
	}
}
