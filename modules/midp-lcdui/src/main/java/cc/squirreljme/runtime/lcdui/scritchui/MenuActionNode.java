// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.scritchui;

import cc.squirreljme.jvm.mle.scritchui.ScritchInterface;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.microedition.lcdui.Image;
import net.multiphasicapps.collections.IdentityHashSet;

/**
 * A single node of a {@link MenuAction} tree.
 *
 * @since 2024/07/20
 */
public final class MenuActionNode
	implements StringTrackerListener, ImageTrackerListener
{
	/** The item that owns this node. */
	protected final Reference<MenuActionApplicable> owner;
	
	/** The ScritchUI API to use for updates. */
	protected final ScritchInterface scritch;
	
	/** Children actions. */
	final List<MenuActionHasParent> _children;
	
	/** Parent actions. */
	final Set<MenuActionHasChildren> _parents;
	
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
		
		// Scritch interface for updates
		this.scritch = DisplayManager.instance().scritch();
		
		// Parental storage
		if (__ref instanceof MenuActionHasParent)
			this._parents = new IdentityHashSet<>();
		else
			this._parents = null;
		
		// Children storage
		if (__ref instanceof MenuActionHasChildren)
			this._children = new ArrayList<>();
		else
			this._children = null;
	}
	
	/**
	 * Returns the children of this node.
	 *
	 * @return The children nodes.
	 * @throws IllegalStateException If this does not support children.
	 * @since 2024/07/21
	 */
	public MenuActionHasParent[] children()
		throws IllegalStateException
	{
		// Must have children
		/* {@squirreljme.error EB1n Menu does not support children}. */
		MenuActionHasParent[] children = this.childrenOptional();
		if (children == null)
			throw new IllegalStateException("EB1n");
		return children;
	}
	
	/**
	 * Returns the children of this node or {@code null}.
	 *
	 * @return The children nodes or {@code null} if not supported.
	 * @since 2024/07/21
	 */
	public MenuActionHasParent[] childrenOptional()
	{
		// Must have children
		/* {@squirreljme.error EB1n Menu does not support children}. */
		List<MenuActionHasParent> children = this._children;
		if (children == null)
			return null;
		
		// Get everything from it
		synchronized (this)
		{
			return children.toArray(new MenuActionHasParent[children.size()]);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/07/20
	 */
	@Override
	public void imageUpdated(Image __image)
	{
		// Enqueue an update of the entire menu tree
		this.__enqueueUpdate();
	}
	
	/**
	 * Inserts the item at the given index.
	 *
	 * @param __dx The index to add at, {@link Integer#MAX_VALUE} is an alias
	 * for the end of the item set.
	 * @param __item The item to add.
	 * @return The index of the added item.
	 * @throws IllegalArgumentException If the item already has this as
	 * a parent.
	 * @throws IllegalStateException If this node cannot have children.
	 * @throws IndexOutOfBoundsException If the index is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/07/20
	 */
	public final int insert(int __dx, MenuActionHasParent __item)
		throws IllegalArgumentException, IllegalStateException,
			IndexOutOfBoundsException, NullPointerException
	{
		if (__item == null)
			throw new NullPointerException("NARG");
		
		/* {@squirreljme.error EB1l Menu node does not support children.} */
		List<MenuActionHasParent> children = this._children;
		if (children == null)
			throw new IllegalStateException("EB1l");
		
		if (__dx < 0)
			throw new IndexOutOfBoundsException("IOOB");
		
		// Obtain owner
		MenuActionApplicable owner = this.owner();
		
		synchronized (this)
		{
			// Alias for end of list?
			int n = children.size();
			if (__dx == Integer.MAX_VALUE)
				__dx = n;
			
			// Out of bounds?
			if (__dx > n)
				throw new IndexOutOfBoundsException("IOOB");
			
			// Ensure it is not already in here
			/* {@squirreljme.error EB1m Item is already in this menu.} */
			for (int i = 0; i < n; i++)
				if (children.get(i) == __item)
					throw new IllegalArgumentException("EB1m");
			
			// Insert it here
			children.add(__dx, __item);
			
			// Link to parent
			((MenuAction)__item)._menuNode.__addParent(owner);
		}
		
		// Enqueue an update
		this.__enqueueUpdate();
		
		// Return the index where we added it
		return __dx;
	}
	
	/**
	 * Returns the owning node.
	 *
	 * @return The owning node.
	 * @throws IllegalStateException If the owner was garbage collected.
	 * @since 2024/07/20
	 */
	public final MenuActionApplicable owner()
		throws IllegalStateException
	{
		MenuActionApplicable owner = this.owner.get();
		if (owner == null)
			throw new IllegalStateException("GCGC");
		return owner;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/07/20
	 */
	@Override
	public final void stringUpdated(String __s)
	{
		// Enqueue an update of the entire menu tree
		this.__enqueueUpdate();
	}
	
	/**
	 * Adds a parent to this node. 
	 *
	 * @param __node The node to get added to as a parent.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/07/20
	 */
	private void __addParent(MenuActionApplicable __node)
		throws NullPointerException
	{
		if (__node == null)
			throw new NullPointerException("NARG");
		
		/* {@squirreljme.error EB1n Menu node does not support parents.} */
		Set<MenuActionHasChildren> parents = this._parents;
		if (parents == null)
			throw new IllegalStateException("EB1n");
		
		// Add into parent set
		synchronized (this)
		{
			parents.add((MenuActionHasChildren)__node);
		}
	}
	
	/**
	 * Enqueues an update for this node.
	 *
	 * @since 2024/07/20
	 */
	private void __enqueueUpdate()
	{
		// If we are at the top of the tree then we cannot go any further
		Set<MenuActionHasChildren> parents = this._parents;
		if (parents == null)
		{
			// Setup an actual menu updater
			this.scritch.eventLoop().executeLater(
				new __ExecUpdateMenuTree__(this));
			
			return;
		}
		
		synchronized (this)
		{
			MenuActionHasChildren[] copy = parents.toArray(
				new MenuActionHasChildren[parents.size()]);
			for (MenuActionHasChildren parent : copy)
				MenuAction.node(parent).__enqueueUpdate();
		}
	}
}
