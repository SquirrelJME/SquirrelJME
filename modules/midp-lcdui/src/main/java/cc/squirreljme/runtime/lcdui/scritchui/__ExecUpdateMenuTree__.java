// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.scritchui;

import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import org.jetbrains.annotations.Async;

/**
 * Performs update of the menu tree.
 *
 * @since 2024/07/21
 */
@SquirrelJMEVendorApi
final class __ExecUpdateMenuTree__
	implements Runnable
{
	/** The menu root to update. */
	@SquirrelJMEVendorApi
	protected final MenuActionNode root;
	
	/**
	 * Initializes the menu tree updater.
	 *
	 * @param __root The root menu.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/07/21
	 */
	@SquirrelJMEVendorApi
	__ExecUpdateMenuTree__(MenuActionNode __root)
		throws NullPointerException
	{
		if (__root == null)
			throw new NullPointerException("NARG");
			
		this.root = __root;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/07/21
	 */
	@Override
	@Async.Execute
	public void run()
	{
		// Update the menu tree
		MenuActionNodeOnly owner = (MenuActionNodeOnly)this.root.owner();
		owner._menuRootTree.update(owner._menuNode);
	}
}
