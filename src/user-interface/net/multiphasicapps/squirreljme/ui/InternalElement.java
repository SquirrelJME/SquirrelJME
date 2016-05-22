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

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.Objects;

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
	
	/** The owning display manager. */
	protected final UIDisplayManager displaymanager;
	
	/** The linked display element (is a reference for cleanup). */
	protected final Reference<X> externalelement;
	
	/**
	 * Initializes the internal element.
	 *
	 * @param __dm The owning display manager.
	 * @param __ref The external element.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/05/21
	 */
	InternalElement(Reference<X> __ref)
		throws NullPointerException
	{
		// Check
		if (__ref == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.externalelement = __ref;
		
		// {@squirreljme.error BD01 Reference to the external UI element was
		// garbage collected as it was created, this is a bug in the virtual
		// machine.}
		X disp = __ref.get();
		UIDisplayManager displaymanager = Objects.<X>requireNonNull(
			disp, "BD01").displayManager();
		this.displaymanager = displaymanager;
		
		// Have the display manager link back this element
		displaymanager.__newElement(__ref, this);
		
		// Link to the external element (a back pointer)
		disp.__linkBack(this);
	}
	
	/**
	 * Returns the referenced external element or throws an exception if it
	 * was garbage collected.
	 *
	 * @return The external element.
	 * @throws UIGarbageCollectedException If the external element was garbage
	 * collected.
	 * @since 2016/05/22
	 */
	public final X external()
		throws UIGarbageCollectedException
	{
		X rv = externalelement.get();
		
		// {@squirreljme.error BD02 The element was garbage collected.}
		if (rv == null)
			throw new UIGarbageCollectedException("BD02");
		
		// Return it
		return rv;
	}
}

