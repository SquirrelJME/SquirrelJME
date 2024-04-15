// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.lcdui;

import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.lcdui.common.EditableText;

@Api
public class TextEditor
	extends CanvasItem
{
	/** The common text editor this is associated with. */
	private final EditableText _editabletext =
		new EditableText();
	
	@Api
	public TextEditor(String __text, int __maxsz, int __cons, int __w, int __h)
	{
		throw Debugging.todo();
	}
	
	@Api
	public void delete(int __o, int __l)
	{
		throw Debugging.todo();
	}
	
	@Api
	public int getBackgroundColor()
	{
		throw Debugging.todo();
	}
	
	@Api
	public int getCaretPosition()
	{
		throw Debugging.todo();
	}
	
	@Api
	public int getConstraints()
	{
		throw Debugging.todo();
	}
	
	@Api
	public int getContentHeight()
	{
		throw Debugging.todo();
	}
	
	@Api
	public boolean getFocus()
	{
		throw Debugging.todo();
	}
	
	@Api
	public Font getFont()
	{
		throw Debugging.todo();
	}
	
	@Api
	public int getForegroundColor()
	{
		throw Debugging.todo();
	}
	
	@Api
	public int getHighlightColor()
	{
		throw Debugging.todo();
	}
	
	@Api
	public int getLineMarginHeight()
	{
		throw Debugging.todo();
	}
	
	@Api
	public int getMaxSize()
	{
		throw Debugging.todo();
	}
	
	@Api
	public String getSelection()
	{
		throw Debugging.todo();
	}
	
	@Api
	public String getString()
	{
		throw Debugging.todo();
	}
	
	@Api
	public boolean getVisible()
	{
		throw Debugging.todo();
	}
	
	@Api
	public int getVisibleContentPosition()
	{
		throw Debugging.todo();
	}
	
	@Api
	public void insert(String __t, int __p)
	{
		throw Debugging.todo();
	}
	
	@Api
	public void setBackgroundColor(int __a, int __r, int __g, int __b)
	{
		throw Debugging.todo();
	}
	
	@Api
	public void setCaretPosition(int __i)
	{
		throw Debugging.todo();
	}
	
	@Api
	public void setConstraints(int __cons)
	{
		throw Debugging.todo();
	}
	
	@Api
	public void setFocus(boolean __f)
	{
		throw Debugging.todo();
	}
	
	@Api
	public void setFont(Font __f)
	{
		throw Debugging.todo();
	}
	
	@Api
	public void setForegroundColor(int __a, int __r, int __g, int __b)
	{
		throw Debugging.todo();
	}
	
	@Api
	public void setHighlightColor(int __a, int __r, int __g, int __b)
	{
		throw Debugging.todo();
	}
	
	@Api
	public void setInitialInputMode(String __cs)
	{
		throw Debugging.todo();
	}
	
	@Api
	public int setMaxSize(int __sz)
	{
		throw Debugging.todo();
	}
	
	@Api
	public void setSelection(int __i, int __l)
	{
		throw Debugging.todo();
	}
	
	@Api
	public void setString(String __s)
	{
		throw Debugging.todo();
	}
	
	@Api
	public void setTextEditorListener(TextEditorChangeListener __cl)
	{
		throw Debugging.todo();
	}
	
	@Override
	public void setVisible(boolean __v)
	{
		throw Debugging.todo();
	}
	
	@Api
	public int size()
	{
		throw Debugging.todo();
	}
}

