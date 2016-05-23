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
 * @since 2016/05/21
 */
public interface PIBase
{
	/**
	 * Cleans up this platform interface item so that it does not consume
	 * any graphical resources on the native platform.
	 *
	 * @throws UIException If cleanup failed.
	 * @since 2016/05/23
	 */
	public abstract void cleanup()
		throws UIException;
	
	/**
	 * Returns the manager for the current platform.
	 *
	 * @return The native platform manager which owns this base.
	 * @throws UIException If the manager could not be obtained. 
	 * @since 2016/05/23
	 */
	public abstract PIManager platformManager()
		throws UIException;
}

