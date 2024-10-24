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
import cc.squirreljme.jvm.mle.scritchui.callbacks.ScritchActivateListener;
import cc.squirreljme.jvm.mle.scritchui.callbacks.ScritchSizeListener;
import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchComponentBracket;
import cc.squirreljme.jvm.mle.scritchui.callbacks.ScritchValueUpdateListener;
import cc.squirreljme.jvm.mle.scritchui.callbacks.ScritchVisibleListener;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.lang.ref.Reference;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Dynamic library interfaced for components.
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
	 * @since 2024/07/29
	 */
	@Override
	public @Nullable ScritchComponentBracket componentGetParent(
		@NotNull ScritchComponentBracket __component)
		throws MLECallError
	{
		if (__component == null)
			throw new MLECallError("Null arguments.");
	
		// Forward
		return NativeScritchDylib.__componentGetParent(this.dyLib._stateP,
			((DylibComponentObject)__component).objectPointer());
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/04/02
	 */
	@Override
	public int componentGetHeight(
		ScritchComponentBracket __component)
		throws MLECallError
	{
		if (__component == null)
			throw new MLECallError("Null arguments.");
		
		// Forward
		return NativeScritchDylib.__componentHeight(this.dyLib._stateP,
			((DylibComponentObject)__component).objectPointer());
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/04/02
	 */
	@Override
	public void componentRevalidate(ScritchComponentBracket __component)
		throws MLECallError
	{
		if (__component == null)
			throw new MLECallError("Null arguments.");
		
		NativeScritchDylib.__componentRevalidate(this.dyLib._stateP,
			((DylibComponentObject)__component).objectPointer());
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/07/17
	 */
	@Override
	public void componentSetActivateListener(ScritchComponentBracket __component,
		ScritchActivateListener __listener)
		throws MLECallError
	{
		if (__component == null)
			throw new NullPointerException("NARG");
		
		NativeScritchDylib.__componentSetActivateListener(
			this.dyLib._stateP,
			((DylibComponentObject)__component).objectPointer(),
			__listener);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/04/28
	 */
	@Override
	public void componentSetSizeListener(ScritchComponentBracket __component,
		ScritchSizeListener __listener)
		throws MLECallError
	{
		if (__component == null || __listener == null)
			throw new MLECallError("Null arguments.");
		
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/07/17
	 */
	@Override
	public void componentSetValueUpdateListener(ScritchComponentBracket __component,
		ScritchValueUpdateListener __listener)
		throws MLECallError
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/06/28
	 */
	@Override
	public void componentSetVisibleListener(ScritchComponentBracket __component,
		ScritchVisibleListener __listener)
		throws MLECallError
	{
		if (__component == null)
			throw new MLECallError("Null arguments.");
		
		// Forward
		NativeScritchDylib.__componentSetVisibleListener(this.dyLib._stateP,
			((DylibBaseObject)__component).objectPointer(),
			__listener);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/04/02
	 */
	@Override
	public int componentWidth(
		ScritchComponentBracket __component)
		throws MLECallError
	{
		if (__component == null)
			throw new MLECallError("Null arguments.");
		
		// Forward
		return NativeScritchDylib.__componentWidth(this.dyLib._stateP,
			((DylibComponentObject)__component).objectPointer());
	}
}
