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
	 * Sets the object of this holder.
	 *
	 * @param __o The object to set.
	 * @since 2020/06/13
	 */
	public void setObject(SpringObject __o)
	{
		this._object = __o;
	}
}
