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
import cc.squirreljme.jvm.mle.scritchui.ScritchViewInterface;
import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchComponentBracket;
import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchListBracket;
import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchPanelBracket;
import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchViewBracket;
import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.lcdui.scritchui.ChoiceManager;
import cc.squirreljme.runtime.lcdui.scritchui.DisplayableState;

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
	
	/** The viewport for this component. */
	private final ScritchViewBracket _scritchView;
	
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
		ScritchViewInterface viewApi = scritchApi.view();
		
		// Set title
		this._trackerTitle.set(__title);
		
		// Create new list
		ScritchListBracket newList = listApi.listNew(nativeType);
		this._scritchList = newList;
		
		// Setup viewport where the list will be in
		ScritchViewBracket newView = Screen.__setupView(scritchApi, newList);
		this._scritchView = newView;
		
		// Put the view in the panel
		ScritchPanelBracket inPanel = state.scritchPanel();
		scritchApi.container().containerAdd(inPanel, newView);
		
		// Setup list activation
		scritchApi.component().componentSetActivateListener(newList,
			new __ExecListActivate__(this));
		
		// Setup manager
		ChoiceManager choices = new ChoiceManager(__type, scritchApi, newList);
		this._choices = choices;
		
		// Implicit lists have a specific select command used
		this.__setSelectCommand(List.SELECT_COMMAND);
		
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
	
	/**
	 * {@inheritDoc}
	 * @since 2024/07/28
	 */
	@Override
	public int getFitPolicy()
	{
		// Always returns no text wrapping
		return Choice.TEXT_WRAP_OFF;
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
		return this.__getHeight();
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
		return this._choices.getSelectedIndex();
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
		return this.__getWidth();
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
	
	/**
	 * {@inheritDoc}
	 * @since 2024/07/28
	 */
	@Override
	public void setFitPolicy(int __fitPolicy)
		throws IllegalArgumentException
	{
		/* {@squirreljme.error EB1d Invalid fit policy.} */
		if (__fitPolicy < Choice.TEXT_WRAP_DEFAULT ||
			__fitPolicy > Choice.TEXT_WRAP_ON)
			throw new IllegalArgumentException("EB1d");
		
		// Ignored, this is not supported
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
	
	/**
	 * Sets the command to call on list activation/selection.
	 *
	 * @param __command The command ot call,
	 * @since 2024/07/28
	 */
	@Api
	public void setSelectCommand(Command __command)
	{
		this.__setSelectCommand(__command);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/11/20
	 */
	@Override
	public void setSelectedFlags(boolean[] __flags)
		throws IllegalArgumentException, NullPointerException
	{
		this._choices.setSelectedFlags(__flags);
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
		return this._choices.size();
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
	
	/**
	 * {@inheritDoc}
	 * @since 2024/07/29
	 */
	@Override
	ScritchViewBracket __scritchView()
	{
		return this._scritchView;
	}
	
	/**
	 * Sets the select command.
	 *
	 * @param __command The command to set.
	 * @since 2024/07/28
	 */
	void __setSelectCommand(Command __command)
	{
		// Does nothing if not implicit
		if (this._choices.type != Choice.IMPLICIT)
			return;
		
		// Set new command
		synchronized (this)
		{
			// If unchanged, do nothing
			Command oldCommand = this._selCommand;
			if (oldCommand == __command)
				return;
			
			// Remove old command?
			if (oldCommand != null)
			{
				this._selCommand = null;
				this.removeCommand(oldCommand);
			}
			
			// Set new command and add to the display implicitly
			this._selCommand = __command;
			if (__command != null)
				this.addCommand(__command);
		}
	}
}

