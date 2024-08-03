// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.emulator.scritchui.dylib;

import cc.squirreljme.jvm.mle.brackets.PencilFontBracket;
import cc.squirreljme.jvm.mle.exceptions.MLECallError;
import cc.squirreljme.jvm.mle.scritchui.ScritchLAFInterface;
import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchComponentBracket;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.lang.ref.Reference;

/**
 * Look and feel interface.
 *
 * @since 2024/04/24
 */
public class DylibLookAndFeelInterface
	extends DylibBaseInterface
	implements ScritchLAFInterface
{
	/**
	 * Initializes the interface.
	 *
	 * @param __selfApi Self reference.
	 * @param __dyLib The dynamic library to access.
	 * @since 2024/04/24
	 */
	public DylibLookAndFeelInterface(
		Reference<DylibScritchInterface> __selfApi,
		NativeScritchDylib __dyLib)
	{
		super(__selfApi, __dyLib);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/05/17
	 */
	@Override
	public PencilFontBracket lafFont(int __element)
		throws MLECallError
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/07/27
	 */
	@Override
	public int lafElementColor(ScritchComponentBracket __context, int __element)
		throws MLECallError
	{
		return NativeScritchDylib.__lafElementColor(this.dyLib._stateP,
			(__context == null ? 0L :
				((DylibComponentObject)__context).objectPointer()), __element);
	}
	
	@Override
	public int lafFocusBorderStyle(boolean __focused)
	{
		return 0;
	}
	
	@Override
	public int lafImageSize(int __elem, boolean __height)
		throws MLECallError
	{
		return 16;
	}
	
	@Override
	public boolean lafIsDarkMode()
	{
		return false;
	}
}
