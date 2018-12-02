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

import cc.squirreljme.runtime.lcdui.common.CommonColors;
import cc.squirreljme.runtime.lcdui.common.CommonMetrics;

public class List
	extends Screen
	implements Choice
{
	/** The default select command used for lists. */
	public static final Command SELECT_COMMAND =
		new Command("Select", Command.SCREEN, 0, true);
	
	/** Items on the list. */
	final __VolatileList__<__ChoiceEntry__> _items =
		new __VolatileList__<>();
	
	/** The type of list this is. */
	private final int _type;
	
	/**
	 * Initializes the list.
	 *
	 * @param __title The list title.
	 * @param __type The type of list this is.
	 * @throws IllegalArgumentException If the type is not valid.
	 * @since 2018/11/16
	 */
	public List(String __title, int __type)
		throws IllegalArgumentException
	{
		this(__title, __type, new String[0], new Image[0]);
	}
	
	/**
	 * Initializes the list.
	 *
	 * @param __title The list title.
	 * @param __type The type of list this is.
	 * @param __strs The initial string elements to add.
	 * @param __imgs The initial image elements to add.
	 * @throws IllegalArgumentException If the type is not valid.
	 * @throws NullPointerException If the string elements is null or contains
	 * a null element.
	 * @since 2018/11/16
	 */
	public List(String __title, int __type, String[] __strs, Image[] __imgs)
		throws IllegalArgumentException, NullPointerException
	{
		if (__strs == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error EB25 String and image elements differ in
		// size.}
		if (__imgs != null && __strs.length != __imgs.length)
			throw new IllegalArgumentException("EB25");
		
		// {@squirreljme.error EB26 Invalid list type. (The list type)}
		if (__type != Choice.IMPLICIT && __type != Choice.EXCLUSIVE &&
			__type != Choice.MULTIPLE)
			throw new IllegalArgumentException("EB26 " + __type);
		
		// Set
		this._title = __title;
		this._type = __type;
		
		// Append all of the items to the list
		for (int i = 0, n = __strs.length; i < n; i++)
			this.append(__strs[i], (__imgs == null ? null : __imgs[i]));
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/01
	 */
	@Override
	public int append(String __s, Image __i)
		throws NullPointerException
	{
		if (__s == null)
			throw new NullPointerException("NARG");
		
		return this._items.append(new __ChoiceEntry__(__s, __i));
	}
	
	public void delete(int __a)
	{
		throw new todo.TODO();
	}
	
	/**
	 * Deletes all of the items in the list.
	 *
	 * @since 2018/11/17
	 */
	public void deleteAll()
	{
		this._items.clear();
	}
	
	public int getFitPolicy()
	{
		throw new todo.TODO();
	}
	
	public Font getFont(int __a)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/05/24
	 */
	@Override
	public int getHeight()
	{
		return this.__defaultHeight();
	}
	
	public Image getImage(int __a)
	{
		throw new todo.TODO();
	}
	
	public int getSelectedFlags(boolean[] __a)
	{
		throw new todo.TODO();
	}
	
	public int getSelectedIndex()
	{
		throw new todo.TODO();
	}
	
	public String getString(int __a)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/05/24
	 */
	@Override
	public int getWidth()
	{
		return this.__defaultWidth();
	}
	
	public void insert(int __a, String __b, Image __c)
	{
		throw new todo.TODO();
	}
	
	public boolean isSelected(int __a)
	{
		throw new todo.TODO();
	}
	
	@Override
	public void removeCommand(Command __a)
	{
		throw new todo.TODO();
	}
	
	public void set(int __a, String __b, Image __c)
	{
		throw new todo.TODO();
	}
	
	public void setFitPolicy(int __a)
	{
		throw new todo.TODO();
	}
	
	public void setFont(int __a, Font __b)
	{
		throw new todo.TODO();
	}
	
	public void setSelectCommand(Command __a)
	{
		throw new todo.TODO();
	}
	
	public void setSelectedFlags(boolean[] __a)
	{
		throw new todo.TODO();
	}
	
	public void setSelectedIndex(int __a, boolean __b)
	{
		throw new todo.TODO();
	}
	
	public int size()
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/18
	 */
	@Override
	void __drawChain(Graphics __g)
	{
		// Get the properties of this chain
		__DrawChain__ chain = this._drawchain;
		int x = chain.x,
			y = chain.y,
			w = chain.w,
			h = chain.h;
		
		// Draw a rectangle in the list area with the background color
		__g.setColor(CommonColors.BACKGROUND);
		__g.fillRect(x, y, w, h);
		
		// Leave room for the scroll bar
		w -= CommonMetrics.SCROLLBAR_WIDTH;
		
		// Draw border
		__g.setColor(CommonColors.BORDER);
		__g.drawRect(x, y, w, h);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/17
	 */
	@Override
	int __supportBit()
	{
		return Display.SUPPORTS_LISTS;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/18
	 */
	@Override
	void __updateDrawChain(__DrawSlice__ __sl)
	{
		// We will be updating our own draw chain
		__DrawChain__ chain = this._drawchain;
		chain.reset();
		
		// Just grab the entire slice because this is a full-screen widget
		// anyway
		int x, y, w, h;
		chain.x = (x = __sl.x);
		chain.y = (y = __sl.y);
		chain.w = (w = __sl.w);
		chain.h = (h = __sl.h);
		
		// Need to get the default font because there may be per-entry fonts
		// but we use the default otherwise...
		Font defaultfont = Font.getDefaultFont();
		
		// Go through each item in the list
		for (Object re : this._items.values())
		{
			__ChoiceEntry__ e = (__ChoiceEntry__)re;
			
			throw new todo.TODO();
		}
	}
}


