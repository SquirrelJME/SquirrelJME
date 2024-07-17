// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.lcdui;

import cc.squirreljme.jvm.mle.brackets.UIFormBracket;
import cc.squirreljme.jvm.mle.brackets.UIItemBracket;
import cc.squirreljme.jvm.mle.constants.UIItemType;
import cc.squirreljme.jvm.mle.constants.UIListType;
import cc.squirreljme.jvm.mle.constants.UIWidgetProperty;
import cc.squirreljme.jvm.mle.scritchui.ScritchInterface;
import cc.squirreljme.jvm.mle.scritchui.ScritchListInterface;
import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchListBracket;
import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchPanelBracket;
import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.annotation.ImplementationNote;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.lcdui.SerializedEvent;
import cc.squirreljme.runtime.lcdui.font.FontUtilities;
import cc.squirreljme.runtime.lcdui.mle.DisplayWidget;
import cc.squirreljme.runtime.lcdui.mle.StaticDisplayState;
import cc.squirreljme.runtime.lcdui.mle.UIBackend;
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
	final ChoiceManager _choices =
		new ChoiceManager();
	
	/** Items on the list. */
	@Deprecated
	final __VolatileList__<__ChoiceEntry__> _items =
		new __VolatileList__<>();
	
	/** The type of list this is. */
	private final int _type;
	
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
		if (__imgs != null && __strs.length != __imgs.length)
			throw new IllegalArgumentException("EB2j");
		
		/* {@squirreljme.error EB2k Invalid list type. (The list type)} */
		if (__type != Choice.IMPLICIT && __type != Choice.EXCLUSIVE &&
			__type != Choice.MULTIPLE)
			throw new IllegalArgumentException("EB2k " + __type);
		
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
		
		// Create new list
		ScritchListBracket newList = listApi.listNew(nativeType);
		
		// Put the list in the panel
		ScritchPanelBracket inPanel = state.scritchPanel();
		scritchApi.container().add(inPanel,
			newList);
		
		throw Debugging.todo();
		/*
		// Set
		this._userTitle = __title;
		this._type = __type;
		
		// Append all the items to the list
		for (int i = 0, n = __strs.length; i < n; i++)
			this.append(__strs[i], (__imgs == null ? null : __imgs[i]));
		
		// Implicit lists have a specific select command used
		if (__type == Choice.IMPLICIT)
		{
			this._selCommand = List.SELECT_COMMAND;
			this.addCommand(List.SELECT_COMMAND);
		}
		
		// Inform the backend that this is the kind of list we want
		__ListState__ state = this.<__ListState__>__state(
			__ListState__.class);
		state._backend.widgetProperty(state._uiList,
			UIWidgetProperty.INT_LIST_TYPE, 0, nativeType);
			
		 */
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
		this._items.remove(__dx);
		
		// Ensure it is up to date
		this.__refresh();
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
		
		// Update UI
		this.__refresh();
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
		return __Utils__.__getSelectedIndex(this, this._type);
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
	
	/**
	 * {@inheritDoc}
	 * @since 2020/11/15
	 */
	@Override
	public void insert(int __at, String __s, Image __i)
	{
		if (__s == null)
			throw new NullPointerException("NARG");
		
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
	
	/**
	 * {@inheritDoc}
	 * @since 2020/11/14
	 */
	@Override
	public void set(int __i, String __label, Image __icon)
		throws IndexOutOfBoundsException, NullPointerException
	{
		if (__label == null)
			throw new NullPointerException("NARG");
		
		// Set properties
		__ChoiceEntry__ entry = this._items.get(__i);
		entry._label = __label;
		entry._image = __icon;
		
		// Update display
		this.__refresh();
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
		this.__refresh();
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
		this._items.get(__i)._font = __f;
		
		// Update display
		this.__refresh();
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
		if (__flags == null)
			throw new NullPointerException();
			
		java.util.List<__ChoiceEntry__> items = this._items.valuesAsList();
		
		/* {@squirreljme.error EB3n Array is longer than the list size.
		(The list size; the array size)} */
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
		this.__refresh();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/09
	 */
	@Override
	public void setSelectedIndex(int __i, boolean __e)
		throws IndexOutOfBoundsException
	{
		__Utils__.__setSelectedIndex(this, this._type, __i, __e);
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
	 * {@inheritDoc}
	 * @since 2020/11/17
	 */
	@Override
	boolean __isPainted()
	{
		return true;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/11/17
	 */
	@Override
	void __paint(Graphics __gfx, int __sw, int __sh, int __special)
	{
		__ChoiceEntry__ entry;
		try
		{
			entry = this._items.get(__special);
		}
		catch (IndexOutOfBoundsException e)
		{
			e.printStackTrace();
			
			return;
		}
		
		// Draw the image over the buffer
		Image image = entry._image;
		if (image != null)
		{
			__gfx.drawImage(image, 0, 0,
				Graphics.TOP | Graphics.LEFT);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/11/15
	 */
	@Override
	boolean __propertyChange(UIFormBracket __form, UIItemBracket __item,
		int __intProp, int __sub, int __old, int __new)
	{
		switch (__intProp)
		{
			case UIWidgetProperty.INT_UPDATE_LIST_SELECTION_LOCK:
				{
					// Only allow locking code changes if the old code is zero
					// and is our matching code
					int oldCode = this._lockingCode;
					if (oldCode == __old)
						this._lockingCode = __new;
				}
				return true;
			
				// List selection changes, this only updates if the locking
				// code is a match
			case UIWidgetProperty.INT_LIST_ITEM_SELECTED:
				if (__old == this._lockingCode)
					this.__updateSelection(__sub, __new != 0);
				return true;
		}
		
		return super.__propertyChange(__form, __item, __intProp, __sub,
			__old, __new);
	}
	
	/**
	 * Performs graphical refreshing of the list.
	 * 
	 * @since 2020/10/18
	 */
	private void __refresh()
	{
		UIItemBracket uiList = this.__state(__ListState__.class)._uiList;
		UIBackend backend = this.__backend();
		java.util.List<__ChoiceEntry__> choices = this._items.valuesAsList();
		
		// Set new size of the list
		int n = choices.size();
		backend.widgetProperty(uiList, UIWidgetProperty.INT_NUM_ELEMENTS,
			0, n); 
		
		// Detect any changes that have occurred
		for (int i = 0; i < n; i++)
		{
			// Check to see if the item has changed from what we have
			// previously cached, used to quickly determine if it needs to
			// actually be updated.
			__ChoiceEntry__ current = choices.get(i); 
			int mId = backend.widgetPropertyInt(uiList,
				UIWidgetProperty.INT_LIST_ITEM_ID_CODE, i);
			if (current.hashCode() == mId)
				continue;
			
			// Forward basic properties to the list
			backend.widgetProperty(uiList,
				UIWidgetProperty.STRING_LIST_ITEM_LABEL, i, current._label);
			
			// Images are always square and based on their highest dimension
			Image image = current._image;
			backend.widgetProperty(uiList,
				UIWidgetProperty.INT_LIST_ITEM_ICON_DIMENSION, i,
				(image == null ? 0 :
					Math.max(image.getWidth(), image.getHeight())));
			
			// Flag states
			backend.widgetProperty(uiList,
				UIWidgetProperty.INT_LIST_ITEM_DISABLED, i,
				(current._disabled ? 1 : 0));
			backend.widgetProperty(uiList,
				UIWidgetProperty.INT_LIST_ITEM_SELECTED, i,
				(current._selected ? 1 : 0));
			
			// Description of the font, if any
			Font font = current._font;
			backend.widgetProperty(uiList,
				UIWidgetProperty.INT_LIST_ITEM_FONT, i, (font == null ? 0 :
					FontUtilities.fontToSystemFont(font)));
			
			// Update the ID code to check for future changes to the list
			backend.widgetProperty(uiList,
				UIWidgetProperty.INT_LIST_ITEM_ID_CODE, i, current.hashCode());
		}
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
	}
	
	/**
	 * Updates the selection of the list.
	 * 
	 * @param __i The index to set.
	 * @param __b Is this set?
	 * @since 2020/11/15
	 */
	@SerializedEvent
	@Async.Execute
	private void __updateSelection(int __i, boolean __b)
	{
		// Drop any attempts to clear selection if not on multiple choice lists
		// as implicit/explicit always have a single item selected and we do
		// not want to end up in a state where only a single item gets selected
		int type = this._type;
		if (!__b && type != Choice.MULTIPLE)
			return;
		
		// Determine how items should be selected
		boolean[] flags = __Utils__.__calculateSetSelectedIndexFlags(this,
			type, __i, __b);
		
		// Update the flags on all the items
		__VolatileList__<__ChoiceEntry__> items = this._items;
		for (int i = 0, n = flags.length; i < n; i++)
			items.get(i)._selected = flags[i];
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/01/14
	 */
	@Override
	__CommonState__ __stateInit(UIBackend __backend)
		throws NullPointerException
	{
		return new __ListState__(__backend, this);
	}
	
	/**
	 * File selector state.
	 * 
	 * @since 2023/01/14
	 */
	static class __ListState__
		extends Screen.__ScreenState__
	{	
		/** The user interface list. */
		final UIItemBracket _uiList;
		
		/**
		 * Initializes the backend state.
		 *
		 * @param __backend The backend used.
		 * @param __self Self widget.
		 * @since 2023/01/14
		 */
		__ListState__(UIBackend __backend, DisplayWidget __self)
		{
			super(__backend, __self);
			
			// Build new list
			UIItemBracket uiList = __backend.itemNew(UIItemType.LIST);
			this._uiList = uiList;
			
			// Register self for future events
			StaticDisplayState.register(__self, uiList);
			
			// Show it on the form for this displayable
			__backend.formItemPosition(this._uiForm, uiList, 0);
		}
	}
}

