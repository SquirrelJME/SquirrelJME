// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.lcdui;

/**
 * This represents the base for an action which may be given a label, an
 * image, and could be enabled or disabled.
 *
 * @since 2018/03/31
 */
abstract class __Action__
{
	/** The priority to use for menu items. */
	static final int _MENU_PRIORITY =
		Integer.MIN_VALUE;
	
	/** The image used. */
	volatile Image _image;
	
	/** The short label. */
	volatile String _shortLabel;
	
	/** The long label. */
	volatile String _longLabel;
	
	/**
	 * This is called when the enabled state of the parent has changed.
	 *
	 * @param __e If the parent is enabled or disabled.
	 * @since 2018/04/01
	 */
	abstract void onParentEnabled(boolean __e);
	
	/**
	 * Returns the label of this item.
	 * 
	 * @return The label for this item.
	 * @since 2020/09/27
	 */
	final String __getLabel()
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
	static int __getPriority(__Action__ __action)
		throws NullPointerException
	{
		if (__action == null)
			throw new NullPointerException("NARG");
		
		// Commands use the given priorities set by the programmer but menus
		// have none
		if (__action instanceof Command)
			return ((Command)__action).getPriority();
		return __Action__._MENU_PRIORITY;
	}
}

