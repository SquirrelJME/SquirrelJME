// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.lcdui;

import cc.squirreljme.jvm.mle.constants.UIListType;
import cc.squirreljme.jvm.mle.scritchui.ScritchInterface;
import cc.squirreljme.jvm.mle.scritchui.ScritchListInterface;
import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchListBracket;
import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchPanelBracket;
import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.annotation.ImplementationNote;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.lcdui.SerializedEvent;
import cc.squirreljme.runtime.lcdui.scritchui.ChoiceManager;
import cc.squirreljme.runtime.lcdui.scritchui.DisplayableState;
import org.jetbrains.annotations.Async;

@Api
public class List
	extends Screen
	implements Choice
{
	/** The default select command used for lists. */
	@Api
	public static final Command SELECT_COMMAND =
		new Command("Select", Command.SCREEN, 0, true);
	
	/** Manages and contains choice entries. */
	final ChoiceManager _choices;
	
	/** Selection command. */
	volatile Command _selCommand;
	
	/** The current locking code. */
	@Deprecated
	volatile int _lockingCode;
	
	/**
	 * Initializes the list.
	 *
	 * @param __title The list title.
	 * @param __type The type of list this is.
	 * @throws IllegalArgumentException If the type is not valid.
	 * @since 2018/11/16
	 */
	@Api
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
	@Api
	public List(String __title, int __type, String[] __strs, Image[] __imgs)
		throws IllegalArgumentException, NullPointerException
	{
		if (__strs == null)
			throw new NullPointerException("NARG");
		
		/* {@squirreljme.error EB2j String and image elements differ in
		size.} */
		int n = __strs.length;
		if (__imgs != null && n != __imgs.length)
			throw new IllegalArgumentException("EB2j");
		
		// Setup manager
		ChoiceManager choices = new ChoiceManager(__type);
		this._choices = choices;
		
		// Determine the native list type
		int nativeType;
		switch (__type)
		{
			case Choice.IMPLICIT:	nativeType = UIListType.IMPLICIT; break;
			case Choice.EXCLUSIVE:	nativeType = UIListType.EXCLUSIVE; break;
			case Choice.MULTIPLE:	nativeType = UIListType.MULTIPLE; break;
			
			default:
				throw Debugging.oops(__type);
		}
		
		DisplayableState state = this._state;
		ScritchInterface scritchApi = state.scritchApi();
		ScritchListInterface listApi = scritchApi.list();
		
		// Set title
		this._trackerTitle.set(__title);
		
		// Create new list
		ScritchListBracket newList = listApi.listNew(nativeType);
		
		// Put the list in the panel
		ScritchPanelBracket inPanel = state.scritchPanel();
		scritchApi.container().add(inPanel,
			newList);
		
		// Implicit lists have a specific select command used
		if (__type == Choice.IMPLICIT)
		{
			this._selCommand = List.SELECT_COMMAND;
			this.addCommand(List.SELECT_COMMAND);
		}
		
		// Append all elements
		for (int i = 0; i < n; i++)
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
		// Appending is just the same as inserting at the end of the list
		int dx = this.size();
		this.insert(dx, __s, __i);
		
		return dx;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/11/21
	 */
	@Override
	public void delete(int __dx)
	{
		throw Debugging.todo();
		/*
		this._items.remove(__dx);
		
		// Ensure it is up to date
		this.__refresh();
		
		 */
	}
	
	/**
	 * Deletes all of the items in the list.
	 *
	 * @since 2018/11/17
	 */
	@Override
	public void deleteAll()
	{
		throw Debugging.todo();
		/*
		this._items.clear();
		
		// Update UI
		this.__refresh();
		
		 */
	}
	
	@Override
	public int getFitPolicy()
	{
		throw Debugging.todo();
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
		throw Debugging.todo();
		/*
		return this._items.get(__i)._font;
		 */
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/05/24
	 */
	@Override
	public int getHeight()
	{
		throw Debugging.todo();
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
		throw Debugging.todo();
		/*
		return this._items.get(__i)._image;
		
		 */
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/10/31
	 */
	@Override
	public int getSelectedFlags(boolean[] __result)
		throws IllegalArgumentException, NullPointerException
	{
		return __Utils__.__getSelectedFlags(this, __result);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/10
	 */
	@Override
	public int getSelectedIndex()
	{
		throw Debugging.todo();
		/*
		return __Utils__.__getSelectedIndex(this, this._type);
		 */
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
		throw Debugging.todo();
		/*
		return this._items.get(__i)._label;
		
		 */
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/05/24
	 */
	@Override
	public int getWidth()
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/11/15
	 */
	@Override
	public void insert(int __at, String __s, Image __i)
	{
		if (__s == null)
			throw new NullPointerException("NARG");
		
		throw Debugging.todo();
		/*
		// Append item
		__ChoiceEntry__ e;
		__VolatileList__<__ChoiceEntry__> items = this._items;
		items.insert(__at, (e = new __ChoiceEntry__(__s, __i)));
		
		// If this is the first item and our list needs to have an item
		// selection then force it to be selected
		int lType = this._type;
		if (items.size() == 1 && (lType == Choice.EXCLUSIVE ||
			lType == Choice.IMPLICIT))
			e._selected = true;
		
		// Update display
		this.__refresh();
		
		 */
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/18
	 */
	@Override
	public boolean isEnabled(int __i)
		throws IndexOutOfBoundsException
	{
		throw Debugging.todo();
		/*
		return !this._items.get(__i)._disabled;
		
		 */
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/18
	 */
	@Override
	public boolean isSelected(int __i)
		throws IndexOutOfBoundsException
	{
		throw Debugging.todo();
		/*
		return this._items.get(__i)._selected;
		
		 */
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/11/14
	 */
	@Override
	public void set(int __i, String __label, Image __icon)
		throws IndexOutOfBoundsException, NullPointerException
	{
		throw Debugging.todo();
		/*
		if (__label == null)
			throw new NullPointerException("NARG");
		
		// Set properties
		__ChoiceEntry__ entry = this._items.get(__i);
		entry._label = __label;
		entry._image = __icon;
		
		// Update display
		this.__refresh();
		
		 */
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/09
	 */
	@Override
	public void setEnabled(int __i, boolean __e)
		throws IndexOutOfBoundsException
	{
		throw Debugging.todo();
		/*
		this._items.get(__i)._disabled = !__e;
		
		// Update display
		this.__refresh();
		
		 */
	}
	
	@Override
	public void setFitPolicy(int __a)
	{
		throw Debugging.todo();
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
		throw Debugging.todo();
		/*
		this._items.get(__i)._font = __f;
		
		// Update display
		this.__refresh();
		
		 */
	}
	
	@Api
	public void setSelectCommand(Command __a)
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/11/20
	 */
	@Override
	public void setSelectedFlags(boolean[] __flags)
		throws IllegalArgumentException, NullPointerException
	{
		throw Debugging.todo();
		/*
		if (__flags == null)
			throw new NullPointerException();
			
		java.util.List<__ChoiceEntry__> items = this._items.valuesAsList();
		
		/* {@squirreljme.error EB3n Array is longer than the list size.
		(The list size; the array size)} * /
		int n = items.size();
		if (n > __flags.length)
			throw new IllegalArgumentException("EB3n " + n + " " +
				__flags.length);
		
		// Limited selection?
		int type = this._type;
		boolean limitSelection = (type == Choice.IMPLICIT ||
			type == Choice.EXCLUSIVE);
		
		// Adjust selections
		int selCount = 0;
		for (int i = 0; i < n; i++)
		{
			// If selection is limited, then make sure no other items get
			// selected
			boolean flag = __flags[i];
			if (flag)
			{
				if (limitSelection && selCount >= 1)
					flag = false;
				else
					selCount++;
			}
			
			// Use the flag
			items.get(i)._selected = flag;
		}
		
		// If nothing was selected, force the first item to be selected
		if (limitSelection && selCount == 0 && n > 0)
			items.get(0)._selected = true;
		
		// Send updates to the UI
		this.__refresh();*/
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/09
	 */
	@Override
	public void setSelectedIndex(int __i, boolean __e)
		throws IndexOutOfBoundsException
	{
		throw Debugging.todo();
		/*
		__Utils__.__setSelectedIndex(this, this._type, __i, __e);
		
		 */
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/10
	 */
	@Override
	public int size()
	{
		throw Debugging.todo();
		/*
		return this._items.size();
		
		 */
	}
	
	/**
	 * Inserts the given item at the specified index.
	 *
	 * @param __at The index to insert at.
	 * @param __s The string for the item.
	 * @param __i The image for the item.
	 * @since 2024/07/17
	 */
	void __insert(int __at, String __s, Image __i)
	{
		throw Debugging.todo();
	}
	
	/**
	 * Selects the given item.
	 * 
	 * @param __keyCode The item being selected.
	 * @since 2020/11/14
	 */
	@SerializedEvent
	@Async.Execute
	final void __selectCommand(int __keyCode)
	{
		throw Debugging.todo();
		/*
		// This command is only executed for implicit lists only
		if (this._type != Choice.IMPLICIT)
			return;
		
		// These must both be set, otherwise nothing can be activated
		Command selCommand = this._selCommand;
		CommandListener listener = this._cmdListener;
		if (selCommand == null || listener == null)
			return;
		
		// Send in the command action
		listener.commandAction(selCommand, this);
		
		 */
	}
}

