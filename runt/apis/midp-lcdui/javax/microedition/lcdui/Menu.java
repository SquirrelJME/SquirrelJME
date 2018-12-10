// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.lcdui;

import cc.squirreljme.runtime.lcdui.ui.UIPersist;
import cc.squirreljme.runtime.lcdui.ui.UIStack;

public class Menu
	extends __Action__
{
	/** The image used. */
	private volatile Image _image;
	
	public Menu(String __short, String __long, Image __i)
	{
		throw new todo.TODO();
	}
	
	public int append(Command __c)
	{
		throw new todo.TODO();
	}
	
	public int append(Menu __m)
	{
		throw new todo.TODO();
	}
	
	public Command getCommand(int __i)
	{
		throw new todo.TODO();
	}
	
	/**
	 * Returns the font that is used as a hint for rendering the text in
	 * a menu, it may be disregarded by the implementation.
	 *
	 * @return The font that should be used as a hint.
	 * @since 2018/04/01
	 */
	public Font getFont()
	{
		throw new todo.TODO();
	}
	
	/**
	 * Returns the image the menu.
	 *
	 * @return The image of the command or {@code null} if it has none.
	 * @since 2018/04/01
	 */
	public Image getImage()
	{
		throw new todo.TODO();
	}
	
	/**
	 * Returns the label used for this menu.
	 *
	 * @return The label used for the menu.
	 * @since 2018/04/01
	 */
	public String getLabel()
	{
		throw new todo.TODO();
	}
	
	/**
	 * Returns the long label of the menu.
	 *
	 * @return The long label of the menu or {@code null} if it has none.
	 * @since 2018/04/01
	 */
	public String getLongLabel()
	{
		throw new todo.TODO();
	}
	
	public Menu getMenu(int __i)
	{
		throw new todo.TODO();
	}
	
	public int getMenuDepth()
	{
		throw new todo.TODO();
	}
	
	public void insert(int __i, Command __c)
	{
		throw new todo.TODO();
	}
	
	public void insert(int __i, Menu __m)
	{
		throw new todo.TODO();
	}
	
	public boolean isCommand(int __i)
	{
		throw new todo.TODO();
	}
	
	public boolean isEnabled()
	{
		throw new todo.TODO();
	}
	
	public boolean isVisible()
	{
		throw new todo.TODO();
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
	
	public void remove(Command __c)
	{
		throw new todo.TODO();
	}
	
	public void remove(Menu __m)
	{
		throw new todo.TODO();
	}
	
	/**
	 * Sets whether this menu is enabled or disabled.
	 *
	 * @param __e If the menu is enabled or not.
	 * @since 2018/04/01
	 */
	public void setEnabled(boolean __e)
	{
		throw new todo.TODO();
	}
	
	/**
	 * Sets the font used to be used as a hint when rendering the menu.
	 *
	 * @param __f The font to use as a hint when rendering the menu,
	 * {@code null} will use the default.
	 * @since 2018/04/01
	 */
	public void setFont(Font __f)
	{
		throw new todo.TODO();
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
	public void setImage(Image __i)
	{
		throw new todo.TODO();
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
	public void setLabel(String __s)
	{
		throw new todo.TODO();
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
	public void setLongLabel(String __s)
	{
		throw new todo.TODO();
		/*
		this.__setLabels(this._shortlabel, __s, this._image);
		*/
	}
	
	public int size()
	{
		throw new todo.TODO();
	}
	
	public static int getMaxMenuDepth()
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/08
	 */
	@Override
	final void __draw(UIPersist __persist, UIStack __parent, UIStack __self,
		Graphics __g)
	{
		__g.drawString(this.getClass().getName().toString(),
			__g.getClipX(), __g.getClipY(), 0);
	}
}

