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
import cc.squirreljme.runtime.lcdui.SerializedEvent;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.IdentityHashMap;
import net.multiphasicapps.collections.IdentityHashSet;

public abstract class FormLayoutPolicy
{
	/** Left to right direction. */
	public static int DIRECTION_LTR =
		0;
	
	/** Right to left direction. */
	public static int DIRECTION_RTL =
		1;
	
	/** The form this refers to. */
	final Reference<Form> _form;
	
	/** Are we in a layout update? */
	volatile boolean _inUpdate;
	
	/**
	 * Initializes the form layout policy.
	 * 
	 * @param __form The form this refers to.
	 * @since 2021/11/26
	 */
	protected FormLayoutPolicy(Form __form)
	{
		if (__form == null)
			throw new NullPointerException("NARG");
		
		this._form = new WeakReference<>(__form);
	}
	
	@SerializedEvent
	protected abstract void doLayout(int __vpx, int __vpy, int __vpw,
		int __vph, int[] __ts);
	
	protected abstract Item getTraverse(Item __i, int __dir);
	
	/**
	 * Returns the form this is providing a layout for.
	 * 
	 * @return The form this is providing a layout for.
	 * @since 2021/11/26
	 */
	protected final Form getForm()
	{
		// {@squirreljme.error EB0a The form has been garbage collected.}
		Form rv = this._form.get();
		if (rv == null)
			throw new IllegalStateException("EB0a");
		
		return rv;
	}
	
	protected final int getHeight(Item __i)
	{
		throw new todo.TODO();
	}
	
	protected final int getWidth(Item __i)
	{
		throw new todo.TODO();
	}
	
	protected final int getX(Item __i)
	{
		throw new todo.TODO();
	}
	
	protected final int getY(Item __i)
	{
		throw new todo.TODO();
	}
	
	protected final boolean isValid(Item __i)
	{
		throw new todo.TODO();
	}
	
	protected final void setPosition(Item __i, int __x, int __y)
	{
		throw new todo.TODO();
	}
	
	protected final void setSize(Item __i, int __w, int __h)
	{
		throw new todo.TODO();
	}
	
	protected final void setValid(Item __i)
	{
		throw new todo.TODO();
	}
	
	public static final int getLayoutDirection()
	{
		throw new todo.TODO();
	}
	
	/**
	 * Initializes the layout for the given items.
	 * 
	 * @param __items The items to initialize with.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/11/28
	 */
	final void __init(Item... __items)
		throws NullPointerException
	{
		if (__items == null)
			throw new NullPointerException("NARG");
		
		for (Item item : __item)
		
		throw Debugging.todo();
	}
}

