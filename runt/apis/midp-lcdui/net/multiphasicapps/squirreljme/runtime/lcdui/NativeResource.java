// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.runtime.lcdui;

import java.lang.ref.Reference;

/**
 * This represents a link to a native resource which binds an object which is
 * managed by the virtual machine to one which is not managed by the virtual
 * machine.
 *
 * @since 2017/10/24
 */
public interface NativeResource
{
	/**
	 * Returns the object which is bound to this resource.
	 *
	 * @return The reference to the native object.
	 * @since 2017/10/25
	 */
	public abstract Reference<? extends Object> boundObject();
	
	/**
	 * This is called when the native object holding this resource has been
	 * garbage collected.
	 *
	 * @since 2017/10/25
	 */
	public abstract void freeResource();
}

