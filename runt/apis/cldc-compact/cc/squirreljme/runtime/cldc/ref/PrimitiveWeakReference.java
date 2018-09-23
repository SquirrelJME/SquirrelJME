// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc.ref;

/**
 * This represents a primitive weak reference which should be garbage
 * collected when it does not point to anything.
 *
 * @since 2018/09/23
 */
public final class PrimitiveWeakReference
	extends PrimitiveReference
{
	/**
	 * Not used.
	 *
	 * @since 2018/09/23
	 */
	private PrimitiveWeakReference()
	{
	}
}

