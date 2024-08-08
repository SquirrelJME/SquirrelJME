// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.lcdui;

import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchMenuKindBracket;
import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchWindowBracket;
import cc.squirreljme.jvm.mle.scritchui.callbacks.ScritchMenuItemActivateListener;
import cc.squirreljme.runtime.lcdui.scritchui.DisplayState;
import cc.squirreljme.runtime.lcdui.scritchui.DisplayableState;
import cc.squirreljme.runtime.lcdui.scritchui.MenuActionApplicable;
import cc.squirreljme.runtime.lcdui.scritchui.MenuActionNodeOnly;
import cc.squirreljme.runtime.lcdui.scritchui.MenuActionTree;

/**
 * Handles activation of menu items.
 *
 * @since 2024/07/30
 */
class __ExecDisplayMenuItemActivate__
	implements ScritchMenuItemActivateListener
{
	/** The display state. */
	private final DisplayState _state;
	
	/**
	 * Initializes the handler.
	 *
	 * @param __state The display state.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/07/30
	 */
	__ExecDisplayMenuItemActivate__(DisplayState __state)
		throws NullPointerException
	{
		if (__state == null)
			throw new NullPointerException("NARG");
		
		this._state = __state;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/07/30
	 */
	@Override
	public void menuItemActivate(ScritchWindowBracket __window,
		ScritchMenuKindBracket __menuItem)
	{
		if (__window == null || __menuItem == null)
			throw new NullPointerException("NARG");
		
		// Get the current displayable, we will be looking in its menu!
		Display display = this._state.display();
		DisplayableState displayableState = this._state.current();
		if (displayableState == null)
			return;
		
		// Get actual displayable
		Displayable current = displayableState.displayable();
		if (current == null)
			return;
		
		// Find the leaf that belongs to this item
		MenuActionTree tree = MenuActionNodeOnly.rootTree(current);
		MenuActionTree.Leaf leaf = tree.find(__menuItem);
		
		// Not found? Just ignore then
		if (leaf == null)
			return;
		
		// Get the MIDP for this
		MenuActionApplicable node = leaf.owner();
		
		// If not a command, ignore
		if (!(node instanceof Command))
			return;
		
		// If there is no command listener, then do not bother
		CommandListener listener = current.__getCommandListener();
		if (listener == null)
			return;
		
		// Execute
		listener.commandAction((Command)node, current);
	}
}
