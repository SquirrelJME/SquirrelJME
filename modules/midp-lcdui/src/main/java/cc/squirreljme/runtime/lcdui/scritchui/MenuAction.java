// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.scritchui;

import cc.squirreljme.jvm.mle.scritchui.ScritchEventLoopInterface;
import cc.squirreljme.jvm.mle.scritchui.ScritchInterface;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.Menu;
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
	@ApiStatus.Internal
	final B _bind;
	
	/** The image used. */
	volatile Image _image;
	
	/** The short label. */
	volatile String _shortLabel;
	
	/** The long label. */
	volatile String _longLabel;
	
	/** The last calculated approximated depth for this action. */
	volatile int _approxDepth;
	
	/**
	 * Initializes the base action.
	 *
	 * @param __short The short string.
	 * @param __long The long string.
	 * @param __image The image to use for the action.
	 * @since 2024/07/18
	 */
	protected MenuAction(String __short, String __long, Image __image)
	{
		ScritchInterface api = DisplayManager.instance().scritch;
		if (this instanceof Menu)
			this._bind = (B)new MenuLayoutMenu(api, (Menu)this);
		else
			this._bind = (B)new MenuLayoutItem(api, (Command)this);
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
	public static int __getPriority(MenuAction<?> __action)
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
	
	/**
	 * Returns the menu bind.
	 *
	 * @param __action The action to get from.
	 * @return The bind from the action.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/07/19
	 */
	public static int approxDepth(MenuAction<?> __action)
		throws NullPointerException
	{
		if (__action == null)
			throw new NullPointerException("NARG");
		
		return __action._approxDepth;
	}
	
	/**
	 * Returns the menu bind.
	 *
	 * @param <B> The bind type.
	 * @param __cl The bind type.
	 * @param __action The action to get from.
	 * @return The bind from the action.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/07/19
	 */
	public static <B extends MenuLayoutBindable<?>> B bind(
		Class<? extends B> __cl, MenuAction<? extends B> __action)
		throws NullPointerException
	{
		if (__cl == null || __action == null)
			throw new NullPointerException("NARG");
		
		return __cl.cast(__action._bind);
	}
}

