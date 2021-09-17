// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.lcdui;

import cc.squirreljme.jvm.mle.brackets.UIItemBracket;
import cc.squirreljme.jvm.mle.constants.UIItemType;
import cc.squirreljme.jvm.mle.constants.UIWidgetProperty;
import cc.squirreljme.runtime.lcdui.mle.DisplayWidget;
import cc.squirreljme.runtime.lcdui.mle.StaticDisplayState;
import cc.squirreljme.runtime.lcdui.mle.UIBackend;
import cc.squirreljme.runtime.lcdui.mle.UIBackendFactory;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * This is a widget that maps to a command or menu, for it to be displayed.
 *
 * @since 2020/09/27
 */
final class __CommandWidget__
	implements DisplayWidget
{
	/** The action this maps to. */
	protected final Command action;
	
	/** The owning displayable. */
	protected final Reference<Displayable> owner;
	
	/** The user interface item to use. */
	final UIItemBracket _uiItem;
	
	/**
	 * Initializes the command widget.
	 * 
	 * @param __owner The owner of this widget.
	 * @param __action The action this maps to.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/09/27
	 */
	__CommandWidget__(Displayable __owner, Command __action)
		throws NullPointerException
	{
		if (__owner == null || __action == null)
			throw new NullPointerException("NARG");
		
		this.action = __action;
		this.owner = new WeakReference<>(__owner);
		
		UIBackend backend = UIBackendFactory.getInstance();

		// Setup and register new item
		UIItemBracket item = backend.itemNew(UIItemType.BUTTON);
		StaticDisplayState.register(this, item);
		
		this._uiItem = item;
		
		// Set item text
		backend.widgetProperty(item, UIWidgetProperty.STRING_LABEL,
			0, __action.__getLabel());
	}
	
	/**
	 * Activates the command.
	 * 
	 * @since 2020/09/27
	 */
	final void __activate()
	{
		// Do nothing if the owner is gone.
		Displayable owner = this.owner.get();
		if (owner == null)
			return;
		
		// Do nothing if there is no command listener
		CommandListener listener = owner._cmdListener;
		if (listener == null)
			return;
		
		// Perform the command's action
		listener.commandAction(this.action, owner);
	}
}
