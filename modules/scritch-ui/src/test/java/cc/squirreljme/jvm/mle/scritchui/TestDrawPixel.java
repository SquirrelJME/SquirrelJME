// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.mle.scritchui;

import cc.squirreljme.jvm.mle.PencilShelf;
import cc.squirreljme.jvm.mle.brackets.PencilBracket;
import cc.squirreljme.runtime.cldc.debug.Debugging;

/**
 * Tests drawing of a pixel.
 *
 * @since 2024/07/12
 */
public class TestDrawPixel
	extends BaseOperation
{
	/**
	 * {@inheritDoc}
	 * @since 2024/07/12
	 */
	@Override
	protected void test(PencilBracket __g, int __w, int __h)
		throws Throwable
	{
		// Full
		PencilShelf.hardwareSetAlphaColor(__g, 0xFF_000000);
		PencilShelf.hardwareDrawPixel(__g, 0, 0);
		PencilShelf.hardwareDrawPixel(__g, 1, 1);
		
		// Half
		PencilShelf.hardwareSetAlphaColor(__g, 0x80_000000);
		PencilShelf.hardwareDrawPixel(__g, 1, 0);
		PencilShelf.hardwareDrawPixel(__g, 2, 1);
		
		// None
		PencilShelf.hardwareSetAlphaColor(__g, 0x00_000000);
		PencilShelf.hardwareDrawPixel(__g, 2, 0);
		PencilShelf.hardwareDrawPixel(__g, 3, 1);
	}
}
