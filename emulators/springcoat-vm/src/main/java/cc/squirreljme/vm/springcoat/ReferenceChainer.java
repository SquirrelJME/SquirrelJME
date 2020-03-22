// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.springcoat;

/**
 * This class manages the reference chain.
 *
 * @since 2020/03/13
 */
@Deprecated
public final class ReferenceChainer
{
	/** The pointer value. */
	@Deprecated
	private SpringPointer _pointer;
	
	/**
	 * Gets the chain.
	 *
	 * @return The chain pointer.
	 * @since 2020/03/13
	 */
	@Deprecated
	public final SpringPointer get()
	{
		return this._pointer;
	}
	
	/**
	 * Sets the chain.
	 *
	 * @param __v The new value to set.
	 * @since 2020/03/13
	 */
	@Deprecated
	public final void set(SpringPointer __v)
	{
		this._pointer = __v;
	}
}
