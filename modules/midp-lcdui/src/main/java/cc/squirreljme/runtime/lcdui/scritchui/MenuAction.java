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
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.Menu;

/**
 * This represents the base for an action which may be given a label, an
 * image, and could be enabled or disabled.
 *
 * @see MenuActionHasChildren
 * @see MenuActionHasParent
 * @since 2018/03/31
 */
@SquirrelJMEVendorApi
public abstract class MenuAction
	extends MenuActionNodeOnly
	implements MenuActionApplicable
{
	/** The priority to use for menu items. */
	@SquirrelJMEVendorApi
	static final int _MENU_PRIORITY =
		Integer.MIN_VALUE;
	
	/** The short label. */
	@SquirrelJMEVendorApi
	final StringTracker _shortLabel;
	
	/** The long label. */
	@SquirrelJMEVendorApi
	final StringTracker _longLabel;
	
	/** The image used. */
	@SquirrelJMEVendorApi
	final ImageTracker _image;
	
	/** The last calculated approximated depth for this action. */
	@SquirrelJMEVendorApi
	volatile int _approxDepth;
	
	/**
	 * Initializes the base action.
	 *
	 * @param __short The short string.
	 * @param __long The long string.
	 * @param __image The image to use for the action.
	 * @since 2024/07/18
	 */
	@SquirrelJMEVendorApi
	protected MenuAction(String __short, String __long, Image __image)
	{
		ScritchInterface scritch = DisplayManager.instance().scritch();
		ScritchEventLoopInterface loop = scritch.eventLoop();
		
		this._shortLabel = new StringTracker(loop, __short);
		this._longLabel = new StringTracker(loop, __long);
		this._image = new ImageTracker(loop, __image);
	}
	
	/**
	 * This is called when the enabled state of the parent has changed.
	 *
	 * @param __e If the parent is enabled or disabled.
	 * @since 2018/04/01
	 */
	@SquirrelJMEVendorApi
	protected abstract void onParentEnabled(boolean __e);
	
	/**
	 * Returns the label of this item.
	 * 
	 * @return The label for this item.
	 * @since 2020/09/27
	 */
	public final String __getLabel()
	{
		throw Debugging.todo();
		/*
		String label = this._longLabel;
		return (label != null ? label : this._shortLabel);
		
		 */
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
	
	/**
	 * Returns the menu bind.
	 *
	 * @param __action The action to get from.
	 * @return The bind from the action.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/07/19
	 */
	protected static int approxDepth(MenuAction __action)
		throws NullPointerException
	{
		if (__action == null)
			throw new NullPointerException("NARG");
		
		return __action._approxDepth;
	}
	
	/**
	 * Returns the node of the action.
	 *
	 * @param __action The action's node.
	 * @return The node for the action.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/07/20
	 */
	protected static MenuActionNode node(MenuActionApplicable __action)
		throws NullPointerException
	{
		if (__action == null)
			throw new NullPointerException("NARG");
		
		return ((MenuActionNodeOnly)__action)._menuNode;
	}
}
