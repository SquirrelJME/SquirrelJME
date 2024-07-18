// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.scritchui;

import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchMenuBracket;
import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchMenuItemBracket;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a node within the menu tree.
 *
 * @since 2024/07/18
 */
public final class MenuLayoutBarNode
{
	/** The action this is for. */
	protected final MenuAction<?> action;
	
	/** Submenu items from this node. */
	private final List<MenuLayoutBarNode> _nodes =
		new ArrayList<>();
	
	/** The ScritchUI item. */
	private volatile ScritchMenuItemBracket _item;
	
	public MenuLayoutBarNode(MenuAction<?> __action)
	{
		throw Debugging.todo();
	}
}
