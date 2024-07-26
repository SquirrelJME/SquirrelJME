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
import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchComponentBracket;
import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchListBracket;
import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchPanelBracket;
import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.annotation.ImplementationNote;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.lcdui.SerializedEvent;
import cc.squirreljme.runtime.lcdui.scritchui.ChoiceManager;
import cc.squirreljme.runtime.lcdui.scritchui.DisplayScale;
import cc.squirreljme.runtime.lcdui.scritchui.DisplayState;
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
	
	/** The ScritchUI list to use. */
	private final ScritchListBracket _scritchList;
	
	/** Selection command. */
	volatile Command _selCommand;
	
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
		this._scritchList = newList;
		
		// Setup manager
		ChoiceManager choices = new ChoiceManager(__type, scritchApi, newList);
		this._choices = choices;
		
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
			choices.insert(Integer.MAX_VALUE, __strs[i],
				(__imgs == null ? null : __imgs[i]));
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/01
	 */
	@Override
	public int append(String __s, Image __i)
		throws NullPointerException
	{
		return this._choices.insert(Integer.MAX_VALUE, __s, __i);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/11/21
	 */
	@Override
	public void delete(int __atIndex)
	{
		this._choices.delete(__atIndex);
	}
	
	/**
	 * Deletes all the items in the list.
	 *
	 * @since 2018/11/17
	 */
	@Override
	public void deleteAll()
	{
		this._choices.deleteAll();
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
		throw Debugging.todo();
		/*
		return __Utils__.__getSelectedFlags(this, __result);
		 */
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
	public void insert(int __at, String __str, Image __img)
	{
		if (__str == null)
			throw new NullPointerException("NARG");
		
		if (__at == Integer.MAX_VALUE)
			throw new IndexOutOfBoundsException("IOOB");
		
		this._choices.insert(__at, __str, __img);
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
	public void set(int __at, String __str, Image __img)
		throws IndexOutOfBoundsException, NullPointerException
	{
		if (__str == null)
			throw new NullPointerException("NARG");
		
		this._choices.set(__at, __str, __img);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/09
	 */
	@Override
	public void setEnabled(int __atIndex, boolean __enabled)
		throws IndexOutOfBoundsException
	{
		this._choices.setEnabled(__atIndex, __enabled);
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
		Debugging.todoNote("Impl setFont()?");
		/*
		throw Debugging.todo();
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
	public void setSelectedIndex(int __atIndex, boolean __selected)
		throws IndexOutOfBoundsException
	{
		this._choices.setSelected(__atIndex, __selected);
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
	 * {@inheritDoc}
	 * @since 2024/07/25
	 */
	@Override
	ScritchComponentBracket __scritchComponent()
	{
		return this._scritchList;
	}
}

