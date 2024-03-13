// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.emulator.scritchui;

import cc.squirreljme.jvm.mle.scritchui.ScritchLAFInterface;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import org.jetbrains.annotations.Range;

/**
 * Swing look and feel interface.
 *
 * @since 2024/03/13
 */
public class SwingLAFInterface
	implements ScritchLAFInterface
{
	/**
	 * {@inheritDoc}
	 * @since 2024/03/13
	 */
	@Override
	public int elementColor(int __element)
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/03/13
	 */
	@Override
	public int focusBorderStyle(boolean __focused)
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/03/13
	 */
	@Override
	public int imageSize(int __elem, boolean __height)
	{
		throw Debugging.todo();
	}
}
