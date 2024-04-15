// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.springcoat.brackets;

import cc.squirreljme.jvm.mle.brackets.RefLinkBracket;
import cc.squirreljme.vm.springcoat.AbstractGhostObject;
import cc.squirreljme.vm.springcoat.SpringMachine;
import cc.squirreljme.vm.springcoat.SpringObject;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * This contains the storage for reference links, these chain to each other.
 *
 * @see RefLinkBracket
 * @since 2020/05/30
 */
public final class RefLinkObject
	extends AbstractGhostObject
{
	/** The object this links. */
	volatile Reference<SpringObject> _object;
	
	/** The next link in the chain. */
	volatile RefLinkObject _next;
	
	/** The previous link in the chain. */
	volatile RefLinkObject _prev;
	
	/**
	 * Initializes the ref link.
	 *
	 * @param __machine The machine used.
	 * @since 2021/01/03
	 */
	public RefLinkObject(SpringMachine __machine)
	{
		super(__machine, RefLinkBracket.class);
	}
	
	/**
	 * Gets the next link.
	 * 
	 * @return The link.
	 * @since 2020/06/29
	 */
	public RefLinkObject getNext()
	{
		synchronized (this)
		{
			return this._next;
		}
	}
	
	/**
	 * Returns the object that this points to.
	 *
	 * @return The object.
	 * @since 2020/06/13
	 */
	public final SpringObject getObject()
	{
		synchronized (this)
		{
			Reference<SpringObject> ref = this._object;
			if (ref == null)
				return null;
			return ref.get();
		}
	}
	
	/**
	 * Gets the previous link.
	 * 
	 * @return The link.
	 * @since 2020/06/29
	 */
	public RefLinkObject getPrev()
	{
		synchronized (this)
		{
			return this._prev;
		}
	}
	
	/**
	 * Sets the next link.
	 * 
	 * @param __next The new link.
	 * @since 2020/06/29
	 */
	public void setNext(RefLinkObject __next)
	{
		synchronized (this)
		{
			this._next = __next;
		}
	}
	
	/**
	 * Sets the object of this holder.
	 *
	 * @param __o The object to set.
	 * @since 2020/06/13
	 */
	public void setObject(SpringObject __o)
	{
		synchronized (this)
		{
			this._object = new WeakReference<>(__o);
		}
	}
	
	/**
	 * Sets the previous link.
	 * 
	 * @param __prev The new link.
	 * @since 2020/06/29
	 */
	public void setPrev(RefLinkObject __prev)
	{
		synchronized (this)
		{
			this._prev = __prev;
		}
	}
}
