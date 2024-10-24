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
import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchScreenBracket;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.lang.ref.Reference;

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
	 * @since 2024/04/02
	 */
	@Override
	public int screenHeight(
		ScritchScreenBracket __screen)
		throws MLECallError
	{
		throw Debugging.todo();
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
		
		if ((DylibScreenObject)__screen == null)
			throw new MLECallError("NARG");
		
		return NativeScritchDylib.__screenId(
			this.dyLib._stateP, ((DylibScreenObject)__screen).objectPointer());
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/04/02
	 */
	@Override
	public int screenWidth(
		ScritchScreenBracket __screen)
		throws MLECallError
	{
		throw Debugging.todo();
	}
}
