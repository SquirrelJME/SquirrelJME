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
import cc.squirreljme.runtime.cldc.debug.Debugging;

/**
 * Runnable for updating menus.
 *
 * @since 2024/07/18
 */
final class __ExecMenuLayoutTrigger__
	implements Runnable
{
	/** The bind to execute. */
	protected final MenuLayoutBindable bind;
	
	/** The loop interface to execute into. */
	protected final ScritchEventLoopInterface loop;
	
	/** Should the trigger be executed? */
	protected final boolean exec;
	
	/**
	 * Initializes the layout trigger.
	 *
	 * @param __loop The loop to call update.
	 * @param __bind The bind to update.
	 * @param __exec Should the trigger be executed?
	 * @throws NullPointerException On null arguments.
	 * @since 2024/07/18
	 */
	__ExecMenuLayoutTrigger__(ScritchEventLoopInterface __loop,
		MenuLayoutBindable __bind, boolean __exec)
		throws NullPointerException
	{
		if (__loop == null || __bind == null)
			throw new NullPointerException("NARG");
		
		this.loop = __loop;
		this.bind = __bind;
		this.exec = __exec;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/07/18
	 */
	@Override
	public void run()
	{
		MenuLayoutBindable bind = this.bind;
		
		// Executing an actual menu update?
		if (this.exec)
			bind.__execTrigger();
		
		// Execute actual update later, so all menu update requests should
		// follow the multiple requests for updates
		else
			this.loop.executeLater(new __ExecMenuLayoutTrigger__(this.loop,
				bind, true));
	}
}
