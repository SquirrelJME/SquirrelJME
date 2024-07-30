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
import cc.squirreljme.jvm.mle.scritchui.ScritchWindowInterface;
import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchMenuBarBracket;
import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchWindowBracket;
import cc.squirreljme.jvm.mle.scritchui.callbacks.ScritchCloseListener;
import cc.squirreljme.jvm.mle.scritchui.callbacks.ScritchMenuItemActivateListener;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.lang.ref.Reference;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Window interface.
 *
 * @since 2024/04/02
 */
public class DylibWindowInterface
	extends DylibBaseInterface
	implements ScritchWindowInterface
{
	/**
	 * Initializes the interface.
	 *
	 * @param __selfApi Reference to our own API.
	 * @param __dyLib The dynamic library interface.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/04/02
	 */
	public DylibWindowInterface(Reference<DylibScritchInterface> __selfApi,
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
	public void callAttention(ScritchWindowBracket __window)
		throws MLECallError
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/04/02
	 */
	@Override
	public int contentHeight(
		ScritchWindowBracket __window)
		throws MLECallError
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/04/02
	 */
	@Override
	public void contentMinimumSize(ScritchWindowBracket __window,
		int __w, int __h)
		throws MLECallError
	{
		if (__window == null)
			throw new MLECallError("Null arguments");
		if (__w <= 0 || __h <= 0)
			throw new MLECallError("Zero or negative size");
		
		if ((DylibWindowObject)__window == null)
			throw new MLECallError("Null arguments");
		if (__w <= 0 || __h <= 0)
			throw new MLECallError("Zero or negative size");
		
		NativeScritchDylib.__windowContentMinimumSize(this.dyLib._stateP,
			((DylibWindowObject)__window).objectPointer(), __w, __h);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/04/02
	 */
	@Override
	public int contentWidth(
		ScritchWindowBracket __window)
		throws MLECallError
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/04/02
	 */
	@Override
	public boolean hasFocus(ScritchWindowBracket __window)
		throws MLECallError
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/04/02
	 */
	@Override
	public boolean isVisible(ScritchWindowBracket __window)
		throws MLECallError
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/04/02
	 */
	@Override
	public int inputTypes(ScritchWindowBracket __window)
		throws MLECallError
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/04/02
	 */
	@Override
	public ScritchWindowBracket newWindow()
	{
		long windowP = NativeScritchDylib.__windowNew(this.dyLib._stateP);
		if (windowP == 0)
			throw new MLECallError("Could not create window.");
		
		return new DylibWindowObject(windowP);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/05/13
	 */
	@Override
	public void setCloseListener(@NotNull ScritchWindowBracket __window,
		@Nullable ScritchCloseListener __listener)
		throws MLECallError
	{
		if (__window == null)
			throw new MLECallError("Null arguments.");
		
		NativeScritchDylib.__windowSetCloseListener(this.dyLib._stateP,
			((DylibWindowObject)__window).objectPointer(), __listener);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/07/23
	 */
	@Override
	public void setMenuBar(@NotNull ScritchWindowBracket __window,
		@Nullable ScritchMenuBarBracket __menuBar)
		throws MLECallError
	{
		if (__window == null)
			throw new NullPointerException("NARG");
		
		// Forward call
		NativeScritchDylib.__windowSetMenuBar(this.dyLib._stateP,
			((DylibWindowObject)__window).objectPointer(),
			(__menuBar == null ? 0 :
				((DylibMenuBarObject)__menuBar).objectPointer()));
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/07/30
	 */
	@Override
	public void setMenuItemActivateListener(
		ScritchWindowBracket __window,
		ScritchMenuItemActivateListener __listener)
		throws MLECallError
	{
		if (__window == null)
			throw new MLECallError("Null arguments.");
		
		NativeScritchDylib.__windowSetMenuItemActivateListener(
			this.dyLib._stateP,
			((DylibWindowObject)__window).objectPointer(), __listener);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/04/02
	 */
	@Override
	public void setVisible(ScritchWindowBracket __window,
		boolean __visible)
		throws MLECallError
	{
		if (__window == null)
			throw new MLECallError("Null arguments.");
		
		NativeScritchDylib.__windowSetVisible(
			this.dyLib._stateP, ((DylibWindowObject)__window).objectPointer(),
			__visible);
	}
}
