// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.scritchui;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Menu;

/**
 * A single node of a {@link MenuAction} tree.
 *
 * @since 2024/07/20
 */
public class MenuActionNode
{
	/** The item that owns this node. */
	protected final Reference<MenuActionApplicable> owner;
	
	/** Children actions. */
	final List<MenuActionHasParent> _children;
	
	/** Parent actions. */
	final List<MenuActionHasChildren> _parents;
	
	/**
	 * Initializes the node reference.
	 *
	 * @param __ref The item to refer to.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/07/20
	 */
	public MenuActionNode(MenuActionApplicable __ref)
		throws NullPointerException
	{
		if (__ref == null)
			throw new NullPointerException("NARG");
		
		// Reference to owning action item
		this.owner = new WeakReference<>(__ref);
		
		// Parental storage
		if (__ref instanceof MenuActionHasParent)
			this._parents = new ArrayList<>();
		else
			this._parents = null;
		
		// Children storage
		if (__ref instanceof MenuActionHasChildren)
			this._children = new ArrayList<>();
		else
			this._children = null;
	}
	
	/**
	 * Inserts the item at the given index.
	 *
	 * @param __dx The index to add at.
	 * @param __item The item to add.
	 * @throws IndexOutOfBoundsException If the index is not valid.
	 * @throws IllegalStateException If this node cannot have children.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/07/20
	 */
	public void insert(int __dx, MenuActionHasParent __item)
		throws IndexOutOfBoundsException, NullPointerException
	{
		if (__item == null)
			throw new NullPointerException("NARG");
		
		/* {@squirreljme.error EB1l Menu node does not support children.} */
		List<MenuActionHasParent> children = this._children;
		if (children == null)
			throw new IllegalStateException("EB1l");
		
		throw cc.squirreljme.runtime.cldc.debug.Debugging.todo();
	}
}
