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
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.lang.ref.Reference;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

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
	public PencilFontBracket font(int __element)
		throws MLECallError
	{
		throw Debugging.todo();
	}
	
	@Override
	public int elementColor(int __element)
	{
		return 0;
	}
	
	@Override
	public int focusBorderStyle(boolean __focused)
	{
		return 0;
	}
	
	@Override
	public int imageSize(int __elem, boolean __height)
		throws MLECallError
	{
		return 16;
	}
	
	@Override
	public boolean isDarkMode()
	{
		return false;
	}
	
	@Override
	public int panelBackgroundColor()
	{
		return 0xFFFFFFFF;
	}
	
	@Override
	public int panelForegroundColor()
	{
		return 0;
	}
}
