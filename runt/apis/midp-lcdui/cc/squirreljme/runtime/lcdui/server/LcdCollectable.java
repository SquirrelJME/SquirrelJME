// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.server;

import cc.squirreljme.runtime.lcdui.CollectableType;

/**
 * This is the base for anything which can be collected by the resource system.
 * When the remote client side garbage collects a given collectable a cleanup
 * method will be called within this collectable in which any resources it uses
 * can be freed up since most GUI frameworks do not have a garbage collection
 * mechanism.
 *
 * @since 2018/03/26
 */
public abstract class LcdCollectable
{
	/** The handle of this widget. */
	protected final int handle;
	
	/** The type of collectable this is. */
	protected final CollectableType type;
	
	/**
	 * Initializes the base collectable.
	 *
	 * @param __handle The handle of the collectable.
	 * @param __type The type of collectable this is.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/26
	 */
	public LcdCollectable(int __handle, CollectableType __type)
		throws NullPointerException
	{
		if (__type == null)
			throw new NullPointerException("NARG");
		
		this.handle = __handle;
		this.type = __type;
	}
	
	/**
	 * Returns the collectable handle.
	 *
	 * @return The collectable handle.
	 * @since 2018/03/18
	 */
	public final int handle()
	{
		return this.handle;
	}
	
	/**
	 * Returns the collectable type.
	 *
	 * @return The collectable type.
	 * @since 2108/03/26
	 */
	public final CollectableType type()
	{
		return this.type;
	}
}

