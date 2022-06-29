// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.lcdui;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.lcdui.common.EditableText;

public class TextBox
	extends Screen
{
	/** The common text editor this is associated with. */
	private final EditableText _editabletext =
		new EditableText();
	
	/**
	 * Initializes the text box.
	 *
	 * @param __title The title of the text box.
	 * @param __text The initial text in the box
	 * @param __max The maximum number of characters which may be specified in
	 * the text box.
	 * @param __con The constraints of the text box.
	 * @throws IllegalArgumentException See the exceptions for
	 * {@link EditableText#initialize(String, int, int)}.
	 * @since 2017/10/20
	 */
	public TextBox(String __title, String __text, int __max, int __con)
		throws IllegalArgumentException
	{
		// Set title if there is one
		if (__title != null)
			this.setTitle(__title);
		
		// Initialize editor with the given set of values
		this._editabletext.initialize(__text, __max, __con);
	}
	
	public void delete(int __a, int __b)
	{
		throw Debugging.todo();
	}
	
	public int getCaretPosition()
	{
		throw Debugging.todo();
	}
	
	public int getChars(char[] __a)
	{
		throw Debugging.todo();
	}
	
	public int getConstraints()
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/05/24
	 */
	@Override
	public int getHeight()
	{
		throw Debugging.todo();
		/*
		return this.__defaultHeight();
		*/
	}
	
	public int getMaxSize()
	{
		throw Debugging.todo();
	}
	
	public String getString()
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/05/24
	 */
	@Override
	public int getWidth()
	{
		throw Debugging.todo();
		/*
		return this.__defaultWidth();
		*/
	}
	
	public void insert(char[] __a, int __b, int __c, int __d)
	{
		throw Debugging.todo();
	}
	
	public void insert(String __a, int __b)
	{
		throw Debugging.todo();
	}
	
	public void setCaret(int __i)
	{
		throw Debugging.todo();
	}
	
	public void setChars(char[] __a, int __b, int __c)
	{
		throw Debugging.todo();
	}
	
	public void setConstraints(int __a)
	{
		throw Debugging.todo();
	}
	
	public void setHighlight(int __i, int __l)
	{
		throw Debugging.todo();
	}
	
	public void setInitialInputMode(String __a)
	{
		throw Debugging.todo();
	}
	
	public int setMaxSize(int __a)
	{
		throw Debugging.todo();
	}
	
	public void setString(String __a)
	{
		throw Debugging.todo();
	}
	
	public int size()
	{
		throw Debugging.todo();
	}
}


