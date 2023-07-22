// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.lcdui;

import cc.squirreljme.jvm.mle.brackets.UIItemBracket;
import cc.squirreljme.jvm.mle.constants.UIItemType;
import cc.squirreljme.jvm.mle.constants.UIWidgetProperty;
import cc.squirreljme.runtime.lcdui.SerializedEvent;
import cc.squirreljme.runtime.lcdui.mle.DisplayWidget;
import cc.squirreljme.runtime.lcdui.mle.StaticDisplayState;
import cc.squirreljme.runtime.lcdui.mle.UIBackend;
import cc.squirreljme.runtime.lcdui.mle.UIBackendFactory;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import org.jetbrains.annotations.Async;

/**
 * This is a widget that maps to a command or menu, for it to be displayed.
 *
 * @since 2020/09/27
 */
@Deprecated
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
	@Deprecated
	__CommandWidget__(Displayable __owner, Command __action)
		throws NullPointerException
	{
		if (__owner == null || __action == null)
			throw new NullPointerException("NARG");
		
		this.action = __action;
		this.owner = new WeakReference<>(__owner);
		
		UIBackend backend = UIBackendFactory.getInstance(true);

		// Setup and register new item
		UIItemBracket item = backend.itemNew(UIItemType.BUTTON);
		StaticDisplayState.register(this, item);
		
		this._uiItem = item;
		
		// Register with the command to get updates
		__action.__register(this);
		
		// Perform an update of the used text/images
		this.__update();
	}
	
	/**
	 * Activates the command.
	 * 
	 * @since 2020/09/27
	 */
	@Deprecated
	@SerializedEvent
	@Async.Execute
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
	
	/**
	 * Updates the state of the widget.
	 * 
	 * @since 2021/11/30
	 */
	@Deprecated
	final void __update()
	{
		// Set item text
		UIBackend backend = UIBackendFactory.getInstance(true);
		backend.widgetProperty(this._uiItem, UIWidgetProperty.STRING_LABEL,
			0, this.action.__getLabel());
	}
}
