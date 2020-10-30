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

import cc.squirreljme.jvm.mle.UIFormShelf;
import cc.squirreljme.jvm.mle.brackets.UIItemBracket;
import cc.squirreljme.jvm.mle.constants.UIItemType;
import cc.squirreljme.jvm.mle.constants.UIWidgetProperty;
import cc.squirreljme.runtime.cldc.annotation.ImplementationNote;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.lcdui.mle.StaticDisplayState;
import cc.squirreljme.runtime.lcdui.mle.UIBackend;
import cc.squirreljme.runtime.lcdui.mle.UIBackendFactory;

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
	
	/** The user interface list. */
	final UIItemBracket _uiList;
	
	/** The type of list this is. */
	private final int _type;
	
	/** The focal index. */
	volatile int _focalindex;
	
	/** Selection command. */
	volatile Command _selcommand =
		List.SELECT_COMMAND;
	
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
		
		// {@squirreljme.error EB2j String and image elements differ in
		// size.}
		if (__imgs != null && __strs.length != __imgs.length)
			throw new IllegalArgumentException("EB2j");
		
		// {@squirreljme.error EB2k Invalid list type. (The list type)}
		if (__type != Choice.IMPLICIT && __type != Choice.EXCLUSIVE &&
			__type != Choice.MULTIPLE)
			throw new IllegalArgumentException("EB2k " + __type);
		
		// Set
		this._userTitle = __title;
		this._type = __type;
		
		// Build new list
		UIItemBracket uiList = UIFormShelf.itemNew(UIItemType.LIST);
		this._uiList = uiList;
		
		// Register self for future events
		StaticDisplayState.register(this, uiList);
		
		// Show it on the form for this displayable
		UIFormShelf.formItemPosition(this._uiForm, uiList, 0);
		
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
		
		// Append item
		__ChoiceEntry__ e;
		__VolatileList__<__ChoiceEntry__> items = this._items;
		int rv = items.append((e = new __ChoiceEntry__(__s, __i)));
		
		// If this is the only item and this is an exclusive list, select it
		int lType = this._type;
		if (items.size() == 1 && (lType == Choice.EXCLUSIVE ||
			lType == Choice.IMPLICIT))
			e._selected = true;
		
		// Update display
		this.__refresh();
		
		return rv;
	}
	
	@Override
	public void delete(int __a)
	{
		throw new todo.TODO();
	}
	
	/**
	 * Deletes all of the items in the list.
	 *
	 * @since 2018/11/17
	 */
	@Override
	public void deleteAll()
	{
		this._items.clear();
	}
	
	@Override
	public int getFitPolicy()
	{
		throw new todo.TODO();
	}
	
	/**
	 * Gets the font for the given index.
	 *
	 * @param __i The index to get.
	 * @return The font of the index.
	 * @throws IndexOutOfBoundsException On null arguments.
	 * @since 2019/05/18
	 */
	@Override
	public Font getFont(int __i)
		throws IndexOutOfBoundsException
	{
		return this._items.get(__i)._font;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/05/24
	 */
	@Override
	public int getHeight()
	{
		return Displayable.__getHeight(this, null);
	}
	
	/**
	 * Returns the image of the given index.
	 *
	 * @param __i The index to get.
	 * @return The image for this index.
	 * @throws IndexOutOfBoundsException If the index is not valid.
	 * @since 2019/05/18
	 */
	@Override
	public Image getImage(int __i)
		throws IndexOutOfBoundsException
	{
		return this._items.get(__i)._image;
	}
	
	/**
	 * Returns the command to use when an item is selected.
	 *
	 * @return The select command.
	 * @since 2019/05/18
	 */
	@ImplementationNote("This is a SquirrelJME specific method.")
	public Command getSelectCommand()
	{
		return this._selcommand;
	}
	
	@Override
	public int getSelectedFlags(boolean[] __a)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/10
	 */
	@Override
	public int getSelectedIndex()
	{
		// Multiple choice is always invalid
		if (this._type == Choice.MULTIPLE)
			return -1;
		
		// Find the first entry!
		int at = 0;
		for (__ChoiceEntry__ e : this._items)
		{
			if (e._selected)
				return at;
			at++;
		}
		
		// Not found
		return -1;
	}
	
	/**
	 * Returns the string at the given index.
	 *
	 * @param __i The string to get.
	 * @return The string.
	 * @throws IndexOutOfBoundsException If the index is out of bounds.
	 * @since 2019/05/18
	 */
	@Override
	public String getString(int __i)
		throws IndexOutOfBoundsException
	{
		return this._items.get(__i)._label;
	}
	
	/**
	 * Returns the list type.
	 *
	 * @return The list type.
	 * @since 2019/05/18
	 */
	@ImplementationNote("This is a SquirrelJME specific method.")
	public int getType()
	{
		return this._type;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/05/24
	 */
	@Override
	public int getWidth()
	{
		return Displayable.__getWidth(this, null);
	}
	
	@Override
	public void insert(int __a, String __b, Image __c)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/18
	 */
	@Override
	public boolean isEnabled(int __i)
		throws IndexOutOfBoundsException
	{
		return !this._items.get(__i)._disabled;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/18
	 */
	@Override
	public boolean isSelected(int __i)
		throws IndexOutOfBoundsException
	{
		return this._items.get(__i)._selected;
	}
	
	@Override
	public void removeCommand(Command __a)
	{
		throw new todo.TODO();
	}
	
	@Override
	public void set(int __a, String __b, Image __c)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/09
	 */
	@Override
	public void setEnabled(int __i, boolean __e)
		throws IndexOutOfBoundsException
	{
		this._items.get(__i)._disabled = !__e;
		
		// Update display
		throw Debugging.todo();
		/*
		Display d = this._display;
		if (d != null)
			UIState.getInstance().repaint();*/
	}
	
	@Override
	public void setFitPolicy(int __a)
	{
		throw new todo.TODO();
	}
	
	/**
	 * Sets the font of the given item.
	 *
	 * @param __i The item to set.
	 * @param __f The font to use, {@code null} clears it and uses the default.
	 * @throws IndexOutOfBoundsException If the item is not within bounds.
	 * @since 2019/05/20
	 */
	@Override
	public void setFont(int __i, Font __f)
		throws IndexOutOfBoundsException
	{
		this._items.get(__i)._font = __f;
		
		// Update display
		throw Debugging.todo();
		/*
		Display d = this._display;
		if (d != null)
			UIState.getInstance().repaint();*/
	}
	
	public void setSelectCommand(Command __a)
	{
		throw new todo.TODO();
	}
	
	@Override
	public void setSelectedFlags(boolean[] __a)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/09
	 */
	@Override
	public void setSelectedIndex(int __i, boolean __e)
		throws IndexOutOfBoundsException
	{
		// Check bounds
		__VolatileList__<__ChoiceEntry__> items = this._items;
		int n = items.size();
		if (__i < 0 || __i >= n)
			throw new IndexOutOfBoundsException("IOOB");
		
		// If deselecting or using this on multiple lists, just direct set
		if (!__e || this._type == Choice.MULTIPLE)
			items.get(__i)._selected = __e;
		
		// Otherwise only select the item that matches the index
		else
		{
			for (int i = 0; i < n; i++)
				this._items.get(i)._selected = (__i == i);
		}
		
		// Update display
		throw Debugging.todo();
		/*
		Display d = this._display;
		if (d != null)
			UIState.getInstance().repaint();*/
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/10
	 */
	@Override
	public int size()
	{
		return this._items.size();
	}
	
	/**
	 * Performs graphical refreshing of the list.
	 * 
	 * @since 2020/10/18
	 */
	private void __refresh()
	{
		UIItemBracket uiList = this._uiList;
		UIBackend backend = UIBackendFactory.getInstance();
		java.util.List<__ChoiceEntry__> choices = this._items.valuesAsList();
		
		// Set new size of the list
		int n = choices.size();
		backend.widgetProperty(uiList, UIWidgetProperty.INT_NUM_ELEMENTS,
			0, n); 
		
		throw Debugging.todo();
	}
}

