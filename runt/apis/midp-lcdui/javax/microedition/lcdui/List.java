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

public class List
	extends Screen
	implements Choice
{
	/** The default select command used for lists. */
	public static final Command SELECT_COMMAND =
		new Command("Select", Command.SCREEN, 0, true);
	
	public List(String __a, int __b)
	{
		throw new todo.TODO();
	}
	
	public List(String __a, int __b, String[] __c, Image[] __d)
	{
		throw new todo.TODO();
	}
	
	public int append(String __a, Image __b)
	{
		throw new todo.TODO();
	}
	
	public void delete(int __a)
	{
		throw new todo.TODO();
	}
	
	public void deleteAll()
	{
		throw new todo.TODO();
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
		return this.__getHeight();
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
		return this.__getWidth();
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
}


