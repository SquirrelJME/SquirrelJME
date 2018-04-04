// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.ui;

import cc.squirreljme.runtime.lcdui.CollectableType;

/**
 * This represents a collectable element which is still held onto strongly
 * from within a server but where the primary purpose is to allow server
 * resources to be cleaned up when no longer used as most system based UI
 * interfaces have no means of garbage collection similar to Java.
 *
 * @since 2018/04/04
 */
public interface UiCollectable
{
	/**
	 * Cleans up after this collectable since the client has informed us that
	 * there are no longer any strong references to it.
	 *
	 * @since 2018/04/04
	 */
	public abstract void cleanup();
	
	/**
	 * Returns the type of collectable that this is.
	 *
	 * @return The collectable type.
	 * @since 2018/04/04
	 */
	public abstract CollectableType collectableType();
	
	/**
	 * Returns the handle of this collectable.
	 *
	 * @return The collectable handle.
	 * @since 2018/04/04
	 */
	public abstract int handle();
}

