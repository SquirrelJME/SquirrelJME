// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.springcoat.brackets;

import cc.squirreljme.jvm.mle.brackets.RefLinkBracket;
import cc.squirreljme.vm.springcoat.AbstractGhostObject;
import cc.squirreljme.vm.springcoat.SpringObject;

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
	volatile SpringObject _object;
	
	/** The next link in the chain. */
	volatile RefLinkObject _next;
	
	/** The previous link in the chain. */
	volatile RefLinkObject _prev;
	
	/**
	 * Initializes the ref link.
	 *
	 * @since 2021/01/03
	 */
	public RefLinkObject()
	{
		super(RefLinkBracket.class);
	}
	
	/**
	 * Gets the next link.
	 * 
	 * @return The link.
	 * @since 2020/06/29
	 */
	public RefLinkObject getNext()
	{
		return this._next;
	}
	
	/**
	 * Returns the object that this points to.
	 *
	 * @return The object.
	 * @since 2020/06/13
	 */
	public final SpringObject getObject()
	{
		return this._object;
	}
	
	/**
	 * Gets the previous link.
	 * 
	 * @return The link.
	 * @since 2020/06/29
	 */
	public RefLinkObject getPrev()
	{
		return this._prev;
	}
	
	/**
	 * Sets the next link.
	 * 
	 * @param __next The new link.
	 * @since 2020/06/29
	 */
	public void setNext(RefLinkObject __next)
	{
		this._next = __next;
	}
	
	/**
	 * Sets the object of this holder.
	 *
	 * @param __o The object to set.
	 * @since 2020/06/13
	 */
	public void setObject(SpringObject __o)
	{
		this._object = __o;
	}
	
	/**
	 * Sets the previous link.
	 * 
	 * @param __prev The new link.
	 * @since 2020/06/29
	 */
	public void setPrev(RefLinkObject __prev)
	{
		this._prev = __prev;
	}
}
