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
import cc.squirreljme.runtime.lcdui.SerializedEvent;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Iterator;
import net.multiphasicapps.collections.Identity;
import net.multiphasicapps.collections.IdentityHashSet;
import net.multiphasicapps.collections.IdentityMap;
import org.jetbrains.annotations.Async;

@Api
public abstract class FormLayoutPolicy
{
	/** Left to right direction. */
	@Api
	public static int DIRECTION_LTR =
		0;
	
	/** Right to left direction. */
	@Api
	public static int DIRECTION_RTL =
		1;
	
	/** Lock for the layout. */
	final __LayoutLock__ _lock =
		new __LayoutLock__();
	
	/** The items to be tracked in the map. */
	final IdentityMap<Item, __LayoutItem__> _tracked =
		new IdentityMap<>(new HashMap<Identity<Item>, __LayoutItem__>());
	
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
	@Api
	protected FormLayoutPolicy(Form __form)
	{
		if (__form == null)
			throw new NullPointerException("NARG");
		
		this._form = new WeakReference<>(__form);
	}
	
	/**
	 * Performs the layout management, which should place all the items
	 * on the form at a given position.
	 * 
	 * Items should not overlap in positions, otherwise that may cause
	 * errors and invalid forms to be displayed.
	 * 
	 * The initial position of all items is {@code (0, 0)}.
	 * 
	 * @param __viewportX The X offset of the viewport.
	 * @param __viewportY The Y offset of the viewport.
	 * @param __viewportW The width of the viewport.
	 * @param __viewportH The height of the viewport.
	 * @param __totalSize The full width ({@code [0]}) and full height
	 * ({@code [1]}) that 
	 * @throws ArrayIndexOutOfBoundsException Should be thrown if
	 * {@code __totalSize} has a length smaller than 2.
	 * @since 2022/07/20
	 */
	@Api
	@SerializedEvent
	@Async.Execute
	protected abstract void doLayout(int __viewportX, int __viewportY,
		int __viewportW, int __viewportH, int[] __totalSize)
		throws ArrayIndexOutOfBoundsException;
	
	@Api
	protected abstract Item getTraverse(Item __i, int __dir);
	
	/**
	 * Returns the form this is providing a layout for.
	 * 
	 * @return The form this is providing a layout for.
	 * @since 2021/11/26
	 */
	@Api
	protected final Form getForm()
	{
		/* {@squirreljme.error EB0a The form has been garbage collected.} */
		Form rv = this._form.get();
		if (rv == null)
			throw new IllegalStateException("EB0a");
		
		return rv;
	}
	
	@Api
	protected final int getHeight(Item __i)
	{
		try (__LayoutLock__ lock = this._lock.utilize())
		{
			throw Debugging.todo();
		}
	}
	
	@Api
	protected final int getWidth(Item __i)
	{
		try (__LayoutLock__ lock = this._lock.utilize())
		{
			throw Debugging.todo();
		}
	}
	
	@Api
	protected final int getX(Item __i)
	{
		try (__LayoutLock__ lock = this._lock.utilize())
		{
			throw Debugging.todo();
		}
	}
	
	@Api
	protected final int getY(Item __i)
	{
		try (__LayoutLock__ lock = this._lock.utilize())
		{
			throw Debugging.todo();
		}
	}
	
	@Api
	protected final boolean isValid(Item __i)
	{
		try (__LayoutLock__ lock = this._lock.utilize())
		{
			throw Debugging.todo();
		}
	}
	
	@Api
	protected final void setPosition(Item __i, int __x, int __y)
	{
		try (__LayoutLock__ lock = this._lock.utilize())
		{
			throw Debugging.todo();
		}
	}
	
	@Api
	protected final void setSize(Item __i, int __w, int __h)
	{
		try (__LayoutLock__ lock = this._lock.utilize())
		{
			throw Debugging.todo();
		}
	}
	
	@Api
	protected final void setValid(Item __i)
	{
		try (__LayoutLock__ lock = this._lock.utilize())
		{
			throw Debugging.todo();
		}
	}
	
	@Api
	public static final int getLayoutDirection()
	{
		throw Debugging.todo();
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
		
		// Initialize the tracking state for each item
		IdentityHashSet<Item> got = new IdentityHashSet<>();
		IdentityMap<Item, __LayoutItem__> tracked = this._tracked;
		for (Item item : __items)
		{
			// We did get this item
			got.add(item);
			
			// Get the layout item for this, be sure to create it if it is
			// missing so it can keep using the same one
			__LayoutItem__ info = tracked.get(item);
			if (info == null)
				tracked.put(item, (info = new __LayoutItem__()));
		}
		
		// Remove the old tracking state for the item
		for (Iterator<Item> it = tracked.keySet().iterator(); it.hasNext();)
			if (!got.contains(it.next()))
				it.remove();
	}
}

