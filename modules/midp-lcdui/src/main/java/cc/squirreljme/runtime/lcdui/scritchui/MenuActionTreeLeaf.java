// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.scritchui;

import cc.squirreljme.jvm.mle.scritchui.ScritchInterface;
import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchMenuKindBracket;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Menu;

/**
 * A single leaf within the tree.
 *
 * @since 2024/07/21
 */
@SquirrelJMEVendorApi
public final class MenuActionTreeLeaf
{
	/** The node this maps to. */
	@SquirrelJMEVendorApi
	final MenuActionNode _node;
	
	/** The ScritchUI bracket used. */
	@SquirrelJMEVendorApi
	final ScritchMenuKindBracket _scritch;
	
	/**
	 * Initializes the leaf from the node.
	 *
	 * @param __node The node to map to.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/07/21
	 */
	@SquirrelJMEVendorApi
	MenuActionTreeLeaf(MenuActionNode __node)
		throws NullPointerException
	{
		if (__node == null)
			throw new NullPointerException("NARG");
		
		MenuActionApplicable owner = __node.owner();
		ScritchInterface api = DisplayManager.instance().scritch();
		
		// Determine the item that should be created
		ScritchMenuKindBracket scritch;
		if (owner instanceof Displayable)
			scritch = api.menu().menuBarNew();
		else if (owner instanceof Menu)
			scritch = api.menu().menuNew();
		else
			scritch = api.menu().menuItemNew();
		
		// Store
		this._node = __node;
		this._scritch = scritch;
	}
	
	/**
	 * Returns the leaf's owner.
	 *
	 * @return The leaf's owner.
	 * @since 2024/07/30
	 */
	@SquirrelJMEVendorApi
	public final MenuActionApplicable owner()
	{
		return this._node.owner();
	}
	
	/**
	 * Returns the native widget.
	 *
	 * @param <K> The widget type.
	 * @param __cl The widget type.
	 * @return The resultant widget.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/07/23
	 */
	@SquirrelJMEVendorApi
	public <K extends ScritchMenuKindBracket> K scritchWidget(
		Class<? extends K> __cl)
		throws NullPointerException
	{
		if (__cl == null)
			throw new NullPointerException("NARG");
		
		return __cl.cast(this._scritch);
	}
}
