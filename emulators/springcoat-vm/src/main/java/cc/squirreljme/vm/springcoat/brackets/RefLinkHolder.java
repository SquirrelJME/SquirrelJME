// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.springcoat.brackets;

/**
 * Contains the holder for reference links.
 *
 * @since 2020/05/31
 */
public final class RefLinkHolder
{
	/** The associated link. */
	RefLinkObject _link;
	
	/**
	 * Returns the ref link.
	 *
	 * @return The link.
	 * @since 2020/05/31
	 */
	public RefLinkObject get()
	{
		synchronized (this)
		{
			return this._link;
		}
	}
	
	/**
	 * Sets the ref link.
	 *
	 * @param __link The link.
	 * @since 2020/05/31
	 */
	public void set(RefLinkObject __link)
	{
		synchronized (this)
		{
			this._link = __link;
		}
	}
}
