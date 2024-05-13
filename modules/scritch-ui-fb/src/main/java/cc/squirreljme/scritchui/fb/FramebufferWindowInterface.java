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
import cc.squirreljme.jvm.mle.scritchui.ScritchInterface;
import cc.squirreljme.jvm.mle.scritchui.ScritchWindowInterface;
import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchWindowBracket;
import cc.squirreljme.jvm.mle.scritchui.callbacks.ScritchCloseListener;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.lang.ref.Reference;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Interface for windows.
 *
 * @since 2024/03/24
 */
@Deprecated
@SquirrelJMEVendorApi
public class FramebufferWindowInterface
	extends FramebufferBaseInterface
	implements ScritchWindowInterface
{
	/**
	 * Initializes this interface. 
	 *
	 * @param __self The framebuffer self interface.
	 * @param __core The core interface.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/03/24
	 */
	public FramebufferWindowInterface(
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
	public void callAttention(ScritchWindowBracket __window)
		throws MLECallError
	{
		if (__window == null)
			throw new MLECallError("NARG");
	
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/03/24
	 */
	@Override
	public int contentHeight(ScritchWindowBracket __window)
		throws MLECallError
	{
		if (__window == null)
			throw new MLECallError("NARG");
	
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/03/24
	 */
	@Override
	public void contentMinimumSize(ScritchWindowBracket __window,
		int __w, int __h)
		throws MLECallError
	{
		if (__window == null)
			throw new MLECallError("NARG");
		
		// Forward to wrapped window
		FramebufferWindowObject window = (FramebufferWindowObject)__window;
		
		window.__contentMinimumSize(__w, __h);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/03/24
	 */
	@Override
	public int contentWidth(ScritchWindowBracket __window)
		throws MLECallError
	{
		if (__window == null)
			throw new MLECallError("NARG");
		
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/03/24
	 */
	@Override
	public boolean hasFocus(ScritchWindowBracket __window)
		throws MLECallError
	{
		if (__window == null)
			throw new MLECallError("NARG");
	
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/03/24
	 */
	@Override
	public boolean isVisible(ScritchWindowBracket __window)
		throws MLECallError
	{
		if (__window == null)
			throw new MLECallError("NARG");
	
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/03/24
	 */
	@Override
	public int inputTypes(ScritchWindowBracket __window)
		throws MLECallError
	{
		if (__window == null)
			throw new MLECallError("NARG");
	
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/03/24
	 */
	@Override
	public ScritchWindowBracket newWindow()
	{
		ScritchInterface coreApi = this.coreApi;
		return new FramebufferWindowObject(this.selfApi, coreApi,
			coreApi.window().newWindow());
	}
	
	@Override
	public void setCloseListener(@NotNull ScritchWindowBracket __window,
		@Nullable ScritchCloseListener __listener)
		throws MLECallError
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/03/24
	 */
	@Override
	public void setVisible(ScritchWindowBracket __window,
		boolean __visible)
		throws MLECallError
	{
		if (__window == null)
			throw new MLECallError("NARG");
	
		// Forward to wrapped window
		FramebufferWindowObject window = (FramebufferWindowObject)__window;
		
		window.__setVisible(__visible);
	}
}
