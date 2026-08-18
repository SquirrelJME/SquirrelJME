// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.rts;

import cc.squirreljme.jvm.mle.scritchui.NativeScritchInterface;
import cc.squirreljme.jvm.mle.scritchui.ScritchInterface;
import cc.squirreljme.rts.map.WorldMapGenerator;
import cc.squirreljme.rts.rate.RateController;
import cc.squirreljme.rts.ui.WindowedFullScreen;
import cc.squirreljme.runtime.cldc.annotation.KeepWhenCompacting;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

/**
 * Main entry point for the strategy game.
 *
 * @since 2026/06/10
 */
@KeepWhenCompacting
public class StrategyGame
	extends MIDlet
{
	/**
	 * {@inheritDoc}
	 * @since 2026/06/10
	 */
	@Override
	protected void destroyApp(boolean __uc)
		throws MIDletStateChangeException
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2026/06/10
	 */
	@Override
	protected void startApp()
		throws MIDletStateChangeException
	{
		// Get the native ScritchUI interface
		ScritchInterface scritch = NativeScritchInterface.nativeInterface();
		if (scritch == null)
			throw new MIDletStateChangeException("NOUI");
		
		// Setup global game frame rate loop
		RateController rate = new RateController();
		
		// Initialize the fullscreen loop and make it run indefinitely
		WindowedFullScreen screen = new WindowedFullScreen(scritch,
			rate.reference());
		rate.screen(screen);
		
		// Start the run loop
		rate.startThread();
		
		// Setup a basic map
		WorldMapGenerator mapGen = new WorldMapGenerator();
		mapGen.size(64, 64);
		
		// Use this map for the game
		rate.worldMap(mapGen.finish());
	}
}
