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
 * Tests drawing a horizontal line.
 *
 * @since 2024/07/14
 */
public class TestDrawHoriz
	extends BaseOperation
{
	/**
	 * {@inheritDoc}
	 * @since 2024/07/14
	 */
	@Override
	protected void test(PencilBracket __g, int __w, int __h)
		throws Throwable
	{
		// Left side
		PencilShelf.hardwareDrawHoriz(__g, -1, 0, 3);
		PencilShelf.hardwareDrawHoriz(__g, 0, 1, 3);
		PencilShelf.hardwareDrawHoriz(__g, 1, 2, 3);
		
		// Right side
		PencilShelf.hardwareDrawHoriz(__g, __w - 3, 3, 3);
		PencilShelf.hardwareDrawHoriz(__g, __w - 2, 4, 3);
		
		// Nothing
		PencilShelf.hardwareDrawHoriz(__g, 6, 6, 0);
		
		// Clipping
		PencilShelf.hardwareDrawHoriz(__g, 0, -1, 3);
		PencilShelf.hardwareDrawHoriz(__g, 0, __h, 3);
		PencilShelf.hardwareDrawHoriz(__g, 0, __h + 1, 3);
		PencilShelf.hardwareDrawHoriz(__g, __w, 0, 3);
		PencilShelf.hardwareDrawHoriz(__g, __w + 1, 0, 3);
		
		// Transparency
		PencilShelf.hardwareSetAlphaColor(__g, 0x80_000000);
		PencilShelf.hardwareDrawHoriz(__g, 1, 5, 3);
		
		// Fully transparent
		PencilShelf.hardwareSetAlphaColor(__g, 0x00_000000);
		PencilShelf.hardwareDrawHoriz(__g, __w - 3, 0, 3);
	}
}
