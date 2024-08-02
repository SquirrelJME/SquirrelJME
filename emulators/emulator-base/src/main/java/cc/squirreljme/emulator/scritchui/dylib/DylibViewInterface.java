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
import cc.squirreljme.jvm.mle.scritchui.ScritchViewInterface;
import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchViewBracket;
import cc.squirreljme.jvm.mle.scritchui.callbacks.ScritchSizeSuggestListener;
import cc.squirreljme.jvm.mle.scritchui.callbacks.ScritchViewListener;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.lang.ref.Reference;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

/**
 * Interface for views.
 *
 * @since 2024/07/29
 */
public class DylibViewInterface
	extends DylibBaseInterface
	implements ScritchViewInterface
{
	/**
	 * Initializes the interface.
	 *
	 * @param __selfApi Reference to our own API.
	 * @param __dyLib The dynamic library interface.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/07/29
	 */
	public DylibViewInterface(
		Reference<DylibScritchInterface> __selfApi,
		NativeScritchDylib __dyLib)
	{
		super(__selfApi, __dyLib);
	}
	
	@Override
	public void getView(ScritchViewBracket __view,
		int[] __outRect)
		throws MLECallError
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/07/30
	 */
	@Override
	public void setArea(ScritchViewBracket __view,
		int __width, int __height)
		throws MLECallError
	{
		if (__view == null)
			throw new MLECallError("Null arguments.");
		
		NativeScritchDylib.__viewSetArea(this.dyLib._stateP,
			((DylibComponentObject)__view).objectPointer(),
			__width, __height);
	}
	
	@Override
	public void setView(ScritchViewBracket __view,
		int __x, int __y, int __width, int __height)
		throws MLECallError
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/07/29
	 */
	@Override
	public void setSizeSuggestListener(ScritchViewBracket __view,
		ScritchSizeSuggestListener __listener)
		throws MLECallError
	{
		if (__view == null || __listener == null)
			throw new MLECallError("NARG");
		
		NativeScritchDylib.__viewSetSizeSuggestListener(
			this.dyLib._stateP,
			((DylibComponentObject)__view).objectPointer(),
			__listener);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/07/29
	 */
	@Override
	public void setViewListener(ScritchViewBracket __view,
		ScritchViewListener __listener)
		throws MLECallError
	{
		NativeScritchDylib.__viewSetViewListener(
			this.dyLib._stateP,
			((DylibComponentObject)__view).objectPointer(),
			__listener);
	}
}
