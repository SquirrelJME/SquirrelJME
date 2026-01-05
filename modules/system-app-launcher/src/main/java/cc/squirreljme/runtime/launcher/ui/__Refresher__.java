// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.launcher.ui;

/**
 * This is a running thread that refreshes the launcher's state.
 *
 * @since 2020/10/17
 */
class __Refresher__
	extends Thread
{
	/** The main midlet to access. */
	protected final MidletMain midletMain;
	
	/** The canvas to repaint on refresh. */
	protected final SplashScreen refreshCanvas;
	
	/**
	 * Initializes the refresher.
	 * 
	 * @param __midletMain The main MIDlet.
	 * @param __canvas Optional canvas to repaint on refresh.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/10/17
	 */
	public __Refresher__(MidletMain __midletMain, SplashScreen __canvas)
	{
		super("LauncherRefresh");
		
		if (__midletMain == null)
			throw new NullPointerException("NARG");
		
		this.midletMain = __midletMain;
		this.refreshCanvas = __canvas;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/10/17
	 */
	@Override
	public final void run()
	{
		this.midletMain.refresh(this.refreshCanvas);
	}
}
