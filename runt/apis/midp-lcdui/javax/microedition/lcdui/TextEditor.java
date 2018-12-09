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
import cc.squirreljme.runtime.lcdui.ui.UIStack;

public class TextEditor
	extends CanvasItem
{
	/** The common text editor this is associated with. */
	private final EditableText _editabletext =
		new EditableText();
	
	public TextEditor(String __text, int __maxsz, int __cons, int __w, int __h)
	{
		throw new todo.TODO();
	}
	
	public void delete(int __o, int __l)
	{
		throw new todo.TODO();
	}
	
	public int getBackgroundColor()
	{
		throw new todo.TODO();
	}
	
	public int getCaretPosition()
	{
		throw new todo.TODO();
	}
	
	public int getConstraints()
	{
		throw new todo.TODO();
	}
	
	public int getContentHeight()
	{
		throw new todo.TODO();
	}
	
	public boolean getFocus()
	{
		throw new todo.TODO();
	}
	
	public Font getFont()
	{
		throw new todo.TODO();
	}
	
	public int getForegroundColor()
	{
		throw new todo.TODO();
	}
	
	public int getHighlightColor()
	{
		throw new todo.TODO();
	}
	
	public int getLineMarginHeight()
	{
		throw new todo.TODO();
	}
	
	public int getMaxSize()
	{
		throw new todo.TODO();
	}
	
	public String getSelection()
	{
		throw new todo.TODO();
	}
	
	public String getString()
	{
		throw new todo.TODO();
	}
	
	public boolean getVisible()
	{
		throw new todo.TODO();
	}
	
	public int getVisibleContentPosition()
	{
		throw new todo.TODO();
	}
	
	public void insert(String __t, int __p)
	{
		throw new todo.TODO();
	}
	
	public void setBackgroundColor(int __a, int __r, int __g, int __b)
	{
		throw new todo.TODO();
	}
	
	public void setCaretPosition(int __i)
	{
		throw new todo.TODO();
	}
	
	public void setConstraints(int __cons)
	{
		throw new todo.TODO();
	}
	
	public void setFocus(boolean __f)
	{
		throw new todo.TODO();
	}
	
	public void setFont(Font __f)
	{
		throw new todo.TODO();
	}
	
	public void setForegroundColor(int __a, int __r, int __g, int __b)
	{
		throw new todo.TODO();
	}
	
	public void setHighlightColor(int __a, int __r, int __g, int __b)
	{
		throw new todo.TODO();
	}
	
	public void setInitialInputMode(String __cs)
	{
		throw new todo.TODO();
	}
	
	public int setMaxSize(int __sz)
	{
		throw new todo.TODO();
	}
	
	public void setSelection(int __i, int __l)
	{
		throw new todo.TODO();
	}
	
	public void setString(String __s)
	{
		throw new todo.TODO();
	}
	
	public void setTextEditorListener(TextEditorChangeListener __cl)
	{
		throw new todo.TODO();
	}
	
	public void setVisible(boolean __v)
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
	void __draw(UIStack __parent, UIStack __self, Graphics __g)
	{
		__g.drawString(this.getClass().getName().toString(),
			__g.getClipX(), __g.getClipY(), 0);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/08
	 */
	@Override
	final void __updateUIStack(UIStack __parent)
	{
		throw new todo.TODO();
	}
}

