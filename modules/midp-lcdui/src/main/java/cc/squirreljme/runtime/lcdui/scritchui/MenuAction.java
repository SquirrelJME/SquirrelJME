// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.scritchui;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Image;
import org.jetbrains.annotations.ApiStatus;

/**
 * This represents the base for an action which may be given a label, an
 * image, and could be enabled or disabled.
 *
 * @param <B> The bindable to use for the item.
 * @since 2018/03/31
 */
public abstract class MenuAction<B extends MenuLayoutBindable<?>>
{
	/** The priority to use for menu items. */
	static final int _MENU_PRIORITY =
		Integer.MIN_VALUE;
	
	/** The bindable menu item. */
	@SuppressWarnings("FieldNamingConvention")
	@ApiStatus.Internal
	protected final B __squirreljmeBind;
	
	/** The image used. */
	volatile Image _image;
	
	/** The short label. */
	volatile String _shortLabel;
	
	/** The long label. */
	volatile String _longLabel;
	
	/**
	 * Initializes the base action.
	 *
	 * @param __bind The bind to use.
	 * @param __short The short string.
	 * @param __long The long string.
	 * @param __image The image to use for the action.
	 * @throws NullPointerException If no bind is specified.
	 * @since 2024/07/18
	 */
	protected MenuAction(B __bind, String __short, String __long,
		Image __image)
	{
		if (__bind == null)
			throw new NullPointerException("NARG");
		
		this.__squirreljmeBind = __bind;
	}
	
	/**
	 * This is called when the enabled state of the parent has changed.
	 *
	 * @param __e If the parent is enabled or disabled.
	 * @since 2018/04/01
	 */
	protected abstract void onParentEnabled(boolean __e);
	
	/**
	 * Returns the label of this item.
	 * 
	 * @return The label for this item.
	 * @since 2020/09/27
	 */
	public final String __getLabel()
	{
		String label = this._longLabel;
		return (label != null ? label : this._shortLabel);
	}
	
	/**
	 * Gets the priority of the given action.
	 * 
	 * @param __action The action to get the priority of.
	 * @return The priority of the given action.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/09/27
	 */
	public static int __getPriority(MenuAction __action)
		throws NullPointerException
	{
		if (__action == null)
			throw new NullPointerException("NARG");
		
		// Commands use the given priorities set by the programmer but menus
		// have none
		if (__action instanceof Command)
			return ((Command)__action).getPriority();
		return MenuAction._MENU_PRIORITY;
	}
}

