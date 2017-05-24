// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.lcdui;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;

/**
 * This is the base class for all native displayables.
 *
 * @since 2017/05/24
 */
public abstract class NativeDisplayable
{
	/** Reference to the the LCDUI displayable. */
	protected final Reference<Displayable> displayable;
	
	/**
	 * Initializes the base native displayable which has a back reference to
	 * the LCDUI displayable which created this native displayable.
	 *
	 * @param __ref The reference to the native displayable.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/05/24
	 */
	public NativeDisplayable(Reference<Displayable> __ref)
		throws NullPointerException
	{
		// Check
		if (__ref == null)
			throw new NullPointerException("NARG");
		
		//Set
		this.displayable = __ref;
	}
}

