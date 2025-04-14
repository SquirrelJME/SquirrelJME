// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.lcdui;

import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.lcdui.scritchui.MenuAction;
import cc.squirreljme.runtime.lcdui.scritchui.MenuActionHasChildren;
import cc.squirreljme.runtime.lcdui.scritchui.MenuActionHasParent;
import cc.squirreljme.runtime.lcdui.scritchui.MenuActionNodeOnly;

/**
 * This is a menu which contains sub-menus and menu items made up of
 * any {@link Command}.
 * 
 * @since 2024/07/18
 */
@Api
public class Menu
	extends MenuAction
	implements MenuActionHasChildren, MenuActionHasParent
{
	@Api
	public Menu(String __short, String __long, Image __i)
	{
		super(__short, __long, __i);
	}
	
	/**
	 * Appends the given command to the menu.
	 *
	 * @param __c The command to append.
	 * @return The index of where the item was added.
	 * @throws IllegalArgumentException If the command is already part of
	 * the menu.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/07/18
	 */
	@Api
	public int append(Command __c)
		throws IllegalArgumentException, NullPointerException
	{
		if (__c == null)
			throw new NullPointerException("NARG");
		
		return MenuActionNodeOnly.node(this)
			.insert(Integer.MAX_VALUE, __c);
	}
	
	/**
	 * Appends the given menu to the menu.
	 *
	 * @param __m The menu to append.
	 * @return The index of where the item was added.
	 * @throws IllegalArgumentException If the menu is already part of
	 * the menu.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/07/18
	 */
	@Api
	public int append(Menu __m)
		throws IllegalArgumentException, NullPointerException
	{
		if (__m == null)
			throw new NullPointerException("NARG");
		
		return MenuActionNodeOnly.node(this).insert(Integer.MAX_VALUE, __m);
	}
	
	@Api
	public Command getCommand(int __i)
	{
		throw Debugging.todo();
	}
	
	/**
	 * Returns the font that is used as a hint for rendering the text in
	 * a menu, it may be disregarded by the implementation.
	 *
	 * @return The font that should be used as a hint.
	 * @since 2018/04/01
	 */
	@Api
	public Font getFont()
	{
		throw Debugging.todo();
	}
	
	/**
	 * Returns the image the menu.
	 *
	 * @return The image of the command or {@code null} if it has none.
	 * @since 2018/04/01
	 */
	@Api
	public Image getImage()
	{
		throw Debugging.todo();
	}
	
	/**
	 * Returns the label used for this menu.
	 *
	 * @return The label used for the menu.
	 * @since 2018/04/01
	 */
	@Api
	public String getLabel()
	{
		throw Debugging.todo();
	}
	
	/**
	 * Returns the long label of the menu.
	 *
	 * @return The long label of the menu or {@code null} if it has none.
	 * @since 2018/04/01
	 */
	@Api
	public String getLongLabel()
	{
		throw Debugging.todo();
	}
	
	@Api
	public Menu getMenu(int __i)
	{
		throw Debugging.todo();
	}
	
	/**
	 * Returns the depth of this menu, note that this value might not be
	 * reliable to use as {@link Menu} may be added to
	 * multiple {@link Displayable}s, and as such this should only be used
	 * as a suggestion or approximated.
	 *
	 * @return The menu depth.
	 * @since 2024/07/18
	 */
	@Api
	public int getMenuDepth()
	{
		synchronized (this)
		{
			return MenuAction.approxDepth(this);
		}
	}
	
	/**
	 * Appends the given command to the menu.
	 *
	 * @param __at The index to insert at.
	 * @param __c The command to append.
	 * @throws IllegalArgumentException If the command is already part of
	 * the menu.
	 * @throws IndexOutOfBoundsException If the index is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/07/18
	 */
	@Api
	public void insert(int __at, Command __c)
		throws IllegalArgumentException, IndexOutOfBoundsException,
			NullPointerException
	{
		if (__c == null)
			throw new NullPointerException("NARG");
		
		// Our internal binding uses this as a special value, so if this is
		// passed internally we should fail on it
		if (__at == Integer.MAX_VALUE)
			throw new IndexOutOfBoundsException("IOOB");
		
		MenuActionNodeOnly.node(this).insert(__at, __c);
	}
	
	/**
	 * Appends the given menu to the menu.
	 *
	 * @param __at The index to insert at.
	 * @param __m The menu to append.
	 * @throws IllegalArgumentException If the menu is already part of
	 * the menu.
	 * @throws IndexOutOfBoundsException If the index is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/07/18
	 */
	@Api
	public void insert(int __at, Menu __m)
		throws IllegalArgumentException, IndexOutOfBoundsException,
			NullPointerException
	{
		if (__m == null)
			throw new NullPointerException("NARG");
		
		// Our internal binding uses this as a special value, so if this is
		// passed internally we should fail on it
		if (__at == Integer.MAX_VALUE)
			throw new IndexOutOfBoundsException("IOOB");
		
		MenuActionNodeOnly.node(this).insert(__at, __m);
	}
	
	@Api
	public boolean isCommand(int __i)
	{
		throw Debugging.todo();
	}
	
	@Api
	public boolean isEnabled()
	{
		throw Debugging.todo();
	}
	
	@Api
	public boolean isVisible()
	{
		throw Debugging.todo();
	}
	
	/**
	 * This is called when the enabled state of the parent has changed.
	 *
	 * @param __e The new state of the parent.
	 * @since 2018/04/01
	 */
	@Override
	public void onParentEnabled(boolean __e)
	{
		// The default implementation does nothing
	}
	
	/**
	 * Removes the given command.
	 *
	 * @param __c The command to remove.
	 * @throws IllegalArgumentException If it is not in the menu.
	 * @since 2024/08/11
	 */
	@Api
	public void remove(Command __c)
		throws IllegalArgumentException
	{
		if (__c == null)
			throw new IllegalArgumentException("NOPE");
		
		MenuActionNodeOnly.node(this).remove(__c);
	}
	
	@Api
	public void remove(Menu __m)
	{
		throw Debugging.todo();
	}
	
	/**
	 * Sets whether this menu is enabled or disabled.
	 *
	 * @param __e If the menu is enabled or not.
	 * @since 2018/04/01
	 */
	@Api
	public void setEnabled(boolean __e)
	{
		throw Debugging.todo();
	}
	
	/**
	 * Sets the font used to be used as a hint when rendering the menu.
	 *
	 * @param __f The font to use as a hint when rendering the menu,
	 * {@code null} will use the default.
	 * @since 2018/04/01
	 */
	@Api
	public void setFont(Font __f)
	{
		throw Debugging.todo();
	}
	
	/**
	 * Sets the image to be displayed for this menu. If the image is mutable
	 * then this will take a snapshot of the image and use that snapshot
	 * instead of the normal image.
	 *
	 * A new snapshot from a mutable image can be created by performing:
	 * {@code menu.setImage(menu.getImage())}.
	 *
	 * @param __i The image to set or {@code null} to clear it.
	 * @since 2018/04/06
	 */
	@Api
	public void setImage(Image __i)
	{
		throw Debugging.todo();
		/*
		Image clone = (__i != null && __i.isMutable() ?
			Image.createImage(__i) : __i);
		LcdServiceCall.voidCall(LcdFunction.SET_IMAGE, this._handle,
			(__i == null ? -1 : __i._handle),
			(clone == null ? -1 : clone._handle));
		this._image = __i;
		*/
	}
	
	/**
	 * Sets the label to be displayed.
	 *
	 * @param __s The label to display.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/04/01
	 */
	@Api
	public void setLabel(String __s)
	{
		throw Debugging.todo();
		/*
		this.__setLabels(__s, this._longlabel, this._image);
		*/
	}
	
	/**
	 * Sets the long label of the menu.
	 *
	 * @param __s The long label to use, {@code null} clears it.
	 * @since 2018/04/01
	 */
	@Api
	public void setLongLabel(String __s)
	{
		throw Debugging.todo();
		/*
		this.__setLabels(this._shortlabel, __s, this._image);
		*/
	}
	
	@Api
	public int size()
	{
		throw Debugging.todo();
	}
	
	/**
	 * Returns the max supported menu depth, this is always at least {@code 5}.
	 * 
	 * @return The max supported menu depth.
	 * @since 2024/07/18
	 */
	@Api
	public static int getMaxMenuDepth()
	{
		return 5;
	}
}

