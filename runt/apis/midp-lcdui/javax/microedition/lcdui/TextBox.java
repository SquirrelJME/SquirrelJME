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

import cc.squirreljme.runtime.lcdui.common.EditableText;
import cc.squirreljme.runtime.lcdui.SerializedEvent;
import cc.squirreljme.runtime.lcdui.ui.UIPersist;
import cc.squirreljme.runtime.lcdui.ui.UIStack;

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
			setTitle(__title);
		
		// Initialize editor with the given set of values
		this._editabletext.initialize(__text, __max, __con);
	}
	
	public void delete(int __a, int __b)
	{
		throw new todo.TODO();
	}
	
	public int getCaretPosition()
	{
		throw new todo.TODO();
	}
	
	public int getChars(char[] __a)
	{
		throw new todo.TODO();
	}
	
	public int getConstraints()
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
	
	public int getMaxSize()
	{
		throw new todo.TODO();
	}
	
	public String getString()
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
	
	public void insert(char[] __a, int __b, int __c, int __d)
	{
		throw new todo.TODO();
	}
	
	public void insert(String __a, int __b)
	{
		throw new todo.TODO();
	}
	
	public void setCaret(int __i)
	{
		throw new todo.TODO();
	}
	
	public void setChars(char[] __a, int __b, int __c)
	{
		throw new todo.TODO();
	}
	
	public void setConstraints(int __a)
	{
		throw new todo.TODO();
	}
	
	public void setHighlight(int __i, int __l)
	{
		throw new todo.TODO();
	}
	
	public void setInitialInputMode(String __a)
	{
		throw new todo.TODO();
	}
	
	public int setMaxSize(int __a)
	{
		throw new todo.TODO();
	}
	
	public void setString(String __a)
	{
		throw new todo.TODO();
	}
	
	public int size()
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
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/08
	 */
	@Override
	final void __updateUIStack(UIPersist __keep, UIStack __parent)
	{
		throw new todo.TODO();
	}
}


