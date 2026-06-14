// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.rts.ui;

import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchComponentBracket;
import cc.squirreljme.jvm.mle.scritchui.callbacks.ScritchInputListener;
import cc.squirreljme.rts.rate.RateController;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.lang.ref.Reference;
import org.jetbrains.annotations.NotNull;

/**
 * Handles input events.
 *
 * @since 2026/06/13
 */
public class InputEventHandler
	implements ScritchInputListener
{
	/** The rate handler. */
	protected final Reference<RateController> rate;
	
	/** The game viewports. */
	private final Viewport[] _views;
	
	/**
	 * Initializes the input handler.
	 *
	 * @param __rate The frame rate manager.
	 * @param __views The viewports.
	 * @throws NullPointerException On null arguments.
	 * @since 2026/06/13
	 */
	public InputEventHandler(Reference<RateController> __rate, 
		Viewport[] __views)
		throws NullPointerException
	{
		if (__rate == null || __views == null)
			throw new NullPointerException("NARG");
		
		this.rate = __rate;
		this._views = __views;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2026/06/13
	 */
	@Override
	public void inputEvent(@NotNull ScritchComponentBracket __component,
		int __type, long __time, int __a, int __b, int __c, int __d, int __e,
		int __f, int __g, int __h, int __i, int __j, int __k, int __l)
	{
	}
}
