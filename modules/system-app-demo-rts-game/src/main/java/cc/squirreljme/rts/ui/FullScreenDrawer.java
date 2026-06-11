// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.rts.ui;

import cc.squirreljme.jvm.mle.PencilShelf;
import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchComponentBracket;
import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchPencilBracket;
import cc.squirreljme.jvm.mle.scritchui.callbacks.ScritchPaintListener;
import cc.squirreljme.rts.rate.RateController;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.lcdui.mle.PencilGraphics;
import java.lang.ref.Reference;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

/**
 * Performs the actual main game drawing.
 *
 * @since 2026/06/10
 */
public class FullScreenDrawer
	implements ScritchPaintListener
{
	/** The frame rate controller. */
	protected final Reference<RateController> rate;
	
	/**
	 * Initializes the drawer.
	 *
	 * @param __rate The frame rate controller.
	 * @throws NullPointerException On null arguments.
	 * @since 2026/06/10
	 */
	public FullScreenDrawer(Reference<RateController> __rate)
		throws NullPointerException
	{
		if (__rate == null)
			throw new NullPointerException("NARG");
		
		this.rate = __rate;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2026/06/10
	 */
	@Override
	public void paint(@NotNull ScritchComponentBracket __component,
		@NotNull ScritchPencilBracket __g,
		@Range(from = 0, to = Integer.MAX_VALUE) int __sw,
		@Range(from = 0, to = Integer.MAX_VALUE) int __sh, int __special)
	{
		// Pencil graphics is a bit easier to use here
		try (PencilGraphics g = PencilGraphics.of(__g, __sw, __sh))
		{
			g.setColor(0xFF00FF);
			g.drawLine(0, 0, 100, 100);
			
			g.setColor(0x00FF00);
			g.drawLine(0, 100, 100, 0);
		}
	}
}
