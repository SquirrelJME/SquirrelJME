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
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Image;

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
		
		MenuActionNode node = this._menuNode;
		
		// Setup trackers
		this._shortLabel = new StringTracker(loop, __short);
		this._longLabel = new StringTracker(loop, __long);
		this._image = new ImageTracker(loop, __image);
		
		// Connect signals
		this._shortLabel.connect(node);
		this._longLabel.connect(node);
		this._image.connect(node);
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
	 * Gets the priority of the given action.
	 * 
	 * @param __action The action to get the priority of.
	 * @return The priority of the given action.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/09/27
	 */
	@SquirrelJMEVendorApi
	public static int getPriority(MenuAction __action)
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
	@SquirrelJMEVendorApi
	protected static int approxDepth(MenuAction __action)
		throws NullPointerException
	{
		if (__action == null)
			throw new NullPointerException("NARG");
		
		return __action._approxDepth;
	}
	
	/**
	 * Sets the specified label.
	 *
	 * @param __action The action to set for.
	 * @param __long Should the long or short label be set?
	 * @param __string The string to set.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/07/27
	 */
	@SquirrelJMEVendorApi
	protected static void setLabel(MenuAction __action, boolean __long,
		String __string)
		throws NullPointerException
	{
		if (__action == null)
			throw new NullPointerException("NARG");
		
		if (__long)
			__action._longLabel.set(__string);
		else
			__action._shortLabel.set(__string);
	}
}
