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
import cc.squirreljme.jvm.mle.scritchui.ScritchScreenInterface;
import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchComponentBracket;
import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchScreenBracket;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.lang.ref.Reference;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * DyLib based screen interface for ScritchUI.
 *
 * @since 2024/04/02
 */
public class DylibScreenInterface
	extends DylibBaseInterface
	implements ScritchScreenInterface
{
	/** Default number of screens to request. */
	static final int _REQUEST_SCREENS =
		16;
	
	/**
	 * Initializes the interface.
	 *
	 * @param __selfApi Reference to our own API.
	 * @param __dyLib The dynamic library interface.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/04/02
	 */
	public DylibScreenInterface(Reference<DylibScritchInterface> __selfApi,
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
	public int screenDpi(
		ScritchScreenBracket __screen)
		throws MLECallError
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/12/23
	 */
	@Override
	public void screenGetBounds(@NotNull ScritchScreenBracket __screen,
		@Nullable ScritchComponentBracket __for,
		@NotNull int[] __pixels, @NotNull int[] __mm)
		throws MLECallError
	{
		if (__screen == null)
			throw new MLECallError("NARG");
		
		NativeScritchDylib.__screenGetBounds(
			this.dyLib._stateP,
			((DylibScreenObject)__screen).objectPointer(),
			(__for == null ? null : ((DylibComponentObject)__for).objectPointer()),
			__pixels, __mm);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/04/02
	 */
	@Override
	public boolean screenIsBuiltIn(ScritchScreenBracket __screen)
		throws MLECallError
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/04/02
	 */
	@Override
	public boolean screenIsPortrait(ScritchScreenBracket __screen)
		throws MLECallError
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/04/02
	 */
	@Override
	public int screenId(ScritchScreenBracket __screen)
		throws MLECallError
	{
		if (__screen == null)
			throw new MLECallError("NARG");
		
		return NativeScritchDylib.__screenId(
			this.dyLib._stateP, ((DylibScreenObject)__screen).objectPointer());
	}
}
