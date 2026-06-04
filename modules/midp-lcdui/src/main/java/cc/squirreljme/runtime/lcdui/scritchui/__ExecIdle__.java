// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.scritchui;

import cc.squirreljme.jvm.mle.scritchui.ScritchEventLoopInterface;
import cc.squirreljme.runtime.cldc.annotation.KeepWhenCompacting;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import org.jetbrains.annotations.NotNull;

/**
 * Runs loop iteration for ScritchUI.
 *
 * @since 2024/12/22
 */
@KeepWhenCompacting
final class __ExecIdle__
	implements Runnable
{
	/** The loop interface. */
	@SquirrelJMEVendorApi
	final ScritchEventLoopInterface loop;
	
	/**
	 * Initializes the idle handler.
	 *
	 * @param __loop The loop handler.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/12/22
	 */
	@KeepWhenCompacting
	__ExecIdle__(ScritchEventLoopInterface __loop)
		throws NullPointerException
	{
		if (__loop == null)
			throw new NullPointerException("NARG");
		
		this.loop = __loop;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/12/22
	 */
	@Override
	public void run()
	{
		// Run the ScritchUI event loop for anything that needs it
		this.loop.iterate();
	}
}
