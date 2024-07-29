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
	public void getView(@NotNull ScritchViewBracket __view,
		@NotNull int[] __outRect)
		throws MLECallError
	{
		throw Debugging.todo();
	}
	
	@Override
	public void setArea(@NotNull ScritchViewBracket __view,
		@Range(from = 1, to = Integer.MAX_VALUE) int __width,
		@Range(from = 1, to = Integer.MAX_VALUE) int __height)
		throws MLECallError
	{
		throw Debugging.todo();
	}
	
	@Override
	public void setView(@NotNull ScritchViewBracket __view,
		@Range(from = 0, to = Integer.MAX_VALUE) int __x,
		@Range(from = 0, to = Integer.MAX_VALUE) int __y,
		@Range(from = 1, to = Integer.MAX_VALUE) int __width,
		@Range(from = 1, to = Integer.MAX_VALUE) int __height)
		throws MLECallError
	{
		throw Debugging.todo();
	}
	
	@Override
	public void setView(@NotNull ScritchViewBracket __view,
		@NotNull int[] __inRect)
		throws MLECallError
	{
		throw Debugging.todo();
	}
	
	@Override
	public void setSizeSuggestListener(@NotNull ScritchViewBracket __view,
		@Nullable ScritchSizeSuggestListener __listener)
		throws MLECallError
	{
		throw Debugging.todo();
	}
	
	@Override
	public void setViewListener(@NotNull ScritchViewBracket __view,
		@Nullable ScritchViewListener __listener)
		throws MLECallError
	{
		throw Debugging.todo();
	}
}
