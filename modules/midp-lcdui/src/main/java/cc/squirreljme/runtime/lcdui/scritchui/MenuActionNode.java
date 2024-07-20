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
}
