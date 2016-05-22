// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.ui;

/**
 * This is the base class for all internal elements and is used to link
 *
 * @param <X> The external element type used.
 * @since 2016/05/21
 */
public abstract class InternalElement<X extends UIElement>
{
	/** Lock. */
	protected final Object lock =
		new Object();
	
	/** The linked display element. */
	private volatile X _link;
	
	/**
	 * Initializes the internal element.
	 *
	 * @since 2016/05/21
	 */
	InternalElement()
	{
	}
	
	/**
	 * Links the display element to an external one.
	 *
	 * @param __e The external element which is to link to this.
	 * @throws IllegalStateException If this internal element has already been
	 * linked to an external one.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/05/21
	 */
	final void __setLink(X __x)
		throws IllegalStateException, NullPointerException
	{
		// Check
		if (__x == null)
			throw new NullPointerException("NARG");
		
		// Lock
		synchronized (this.lock)
		{
			// {@squirreljme.error BD01 The internal element has
			// already been linked to an external element.
			if (this._link != null)
				throw new IllegalStateException("BD01");
			
			// Set
			this._link = __x;
		}
	}
}

