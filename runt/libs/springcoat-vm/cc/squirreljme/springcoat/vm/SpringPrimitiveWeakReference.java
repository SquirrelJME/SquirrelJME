// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.springcoat.vm;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * This class stores information for weak references.
 *
 * @since 2018/09/23
 */
public final class SpringPrimitiveWeakReference
	extends SpringPrimitiveReference
{
	/** The reference used. */
	private volatile Reference<SpringObject> _ref;
	
	/**
	 * {@inheritDoc}
	 * @since 2018/09/23
	 */
	@Override
	public final SpringObject get()
	{
		Reference<SpringObject> ref = this._ref;
		if (ref != null)
			return ref.get();
		return null;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/09/23
	 */
	@Override
	public final void set(SpringObject __o)
	{
		this._ref = new WeakReference<>(__o);
	}
}

