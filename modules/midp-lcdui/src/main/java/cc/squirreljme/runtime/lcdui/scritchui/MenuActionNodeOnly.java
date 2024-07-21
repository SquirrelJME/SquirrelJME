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
import javax.microedition.lcdui.Displayable;

/**
 * A menu action which only has a single node.
 *
 * @since 2024/07/20
 */
public abstract class MenuActionNodeOnly
	implements MenuActionApplicable
{
	/** The node of this menu. */
	@SquirrelJMEVendorApi
	final MenuActionNode _menuNode =
		new MenuActionNode(this);
	
	/** Root menu tree state. */
	final MenuActionTree _menuRootTree;
	
	/**
	 * Initializes the base node-only menu tree.
	 *
	 * @since 2024/07/21
	 */
	protected MenuActionNodeOnly()
	{
		if (this instanceof Displayable)
			this._menuRootTree = new MenuActionTree();
		else
			this._menuRootTree = null;
	}
	
	/**
	 * Returns the node of the action.
	 *
	 * @param __action The action's node.
	 * @return The node for the action.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/07/20
	 */
	public static MenuActionNode node(MenuActionApplicable __action)
		throws NullPointerException
	{
		if (__action == null)
			throw new NullPointerException("NARG");
		
		return ((MenuActionNodeOnly)__action)._menuNode;
	}
}
