// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.scritchui;

import cc.squirreljme.jvm.mle.scritchui.ScritchEventLoopInterface;
import cc.squirreljme.jvm.mle.scritchui.ScritchInterface;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import net.multiphasicapps.collections.IdentityHashSet;

/**
 * A bindable menu that can also contain submenu items.
 *
 * @param <M> The MIDP menu type.
 * @since 2024/07/18
 */
@SquirrelJMEVendorApi
public abstract class MenuLayoutBindableSub<M>
	extends MenuLayoutBindable<M>
{
	/** Items which are bound to this menu. */
	final List<__MenuBind__> _items =
		new ArrayList<>();
	
	/**
	 * Initializes the bindable.
	 *
	 * @param __scritch The ScritchUI interface.
	 * @param __item The item to bind to.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/07/18
	 */
	protected MenuLayoutBindableSub(ScritchInterface __scritch, M __item)
		throws NullPointerException
	{
		super(__scritch, __item);
	}
	
	/**
	 * Inserts the given menu action.
	 *
	 * @param __at The index to add at, {@link Integer#MAX_VALUE} means to
	 * add at the very end of the menu.
	 * @param __action The menu action to add.
	 * @return The index the item was added at, in most cases this will match
	 * the value of {@code __at} except in the case it specifies the end of
	 * the menu.
	 * @throws IllegalArgumentException If the action is already a part of
	 * this menu.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/07/18
	 */
	@SquirrelJMEVendorApi
	public int insert(int __at, MenuAction<?> __action)
		throws IllegalArgumentException, NullPointerException
	{
		if (__action == null)
			throw new NullPointerException("NARG");
		if (__at < 0)
			throw new IndexOutOfBoundsException("IOOB");
		
		// Insert into menu
		List<__MenuBind__> items = this._items;
		synchronized (this)
		{
			// End of list alias?
			int n = items.size();
			if (__at == Integer.MAX_VALUE)
				__at = n;
			
			// Must be within bounds
			if (__at > n)
				throw new IndexOutOfBoundsException("IOOB");
			
			// We need to ensure that the item is not already here
			for (int i = 0; i < n; i++)
			{
				// Actions cannot be added twice
				/* {@squirreljme.error EB1l Menu item cannot exist multiple
				times within a menu.} */
				__MenuBind__ item = items.get(i);
				if (item.action == __action)
					throw new IllegalArgumentException("EB1l");
			}
				
			// Setup new binding and add to the correct slot
			__MenuBind__ item = new __MenuBind__(__action);
			items.add(__at, item);
			
			// Add us to their parent list
			__action._bind.__parentAdd(this);
		}
		
		// We changed our own state, so trigger an enqueue to occur
		Set<MenuLayoutBindable<?>> traversed = new IdentityHashSet<>();
		this.__execTriggerEnqueue(traversed);
		traversed.clear();
		
		// Where it was added
		return __at;
	}
}
