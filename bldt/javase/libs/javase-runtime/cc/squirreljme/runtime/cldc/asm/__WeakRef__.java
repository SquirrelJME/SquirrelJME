// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc.asm;

import cc.squirreljme.runtime.cldc.ref.PrimitiveWeakReference;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * Primitive weak reference.
 *
 * @since 2018/12/05
 */
class __WeakRef__
	implements PrimitiveWeakReference
{
	/** The reference. */
	volatile Reference<Object> _ref;
	
	/**
	 * Gets the value.
	 *
	 * @return The value.
	 * @since 2018/12/05
	 */
	final Object __get()
	{
		synchronized (this)
		{
			Reference<Object> ref = this._ref;
			if (ref == null)
				return null;
			return ref.get();
		}
	}
	
	/**
	 * Sets the value.
	 *
	 * @param __v The value.
	 * @since 2018/12/05
	 */
	final void __set(Object __v)
	{
		synchronized (this)
		{
			Reference<Object> ref = this._ref;
			
			// {@squirreljme.error AF0f Cannot set weak reference which has
			// already been set.}
			if (__v == null)
				if (ref == null)
				{
					// Do nothing
				}
				else
					ref.clear();
			else
				if (ref == null)
					this._ref = new WeakReference<>(__v);
				else
					throw new Error("AF0f");
		}
	}
}

