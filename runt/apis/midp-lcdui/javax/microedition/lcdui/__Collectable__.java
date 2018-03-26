// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.lcdui;

/**
 * This acts as the base for anything which is to be garbage collected by the
 * remote end when it is no longer referenced.
 *
 * @since 2018/03/26
 */
abstract class __Collectable__
{
	/** The handle of this item. */
	final int _handle;
	
	/**
	 * Initializes the collectable using a handle which is registered on the
	 * remote end.
	 *
	 * @param __h The handle to use.
	 * @since 2018/03/23
	 */
	__Collectable__()
	{
		this._handle = __Queue__.INSTANCE.__register(this);
	}
	
	/**
	 * Initializes the collectable using the given handle.
	 *
	 * @param __h The handle to use.
	 * @since 2018/03/23
	 */
	__Collectable__(int __h)
	{
		this._handle = __h;
	}
}

