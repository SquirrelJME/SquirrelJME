// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
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
	
	/**
	 * Initializes the refresher.
	 * 
	 * @param __midletMain The main MIDlet.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/10/17
	 */
	public __Refresher__(MidletMain __midletMain)
	{
		super("LauncherRefresh");
		
		if (__midletMain == null)
			throw new NullPointerException("NARG");
		
		this.midletMain = __midletMain;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/10/17
	 */
	@Override
	public final void run()
	{
		this.midletMain.refresh();
	}
}
