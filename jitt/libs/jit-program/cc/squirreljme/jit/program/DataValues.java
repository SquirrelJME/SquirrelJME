// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jit.program;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * This class is used to store a large number of data values which are
 * referred to by variables and are used to contain single values.
 *
 * @since 2017/10/19
 */
public final class DataValues
{
	/** Reference to the variables which own this data set. */
	private final Reference<Variables> _varsref;
	
	/**
	 * Initializes the data values.
	 *
	 * @param __vr Variable references to use.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/10/19
	 */
	DataValues(Reference<Variables> __vr)
		throws NullPointerException
	{
		if (__vr == null)
			throw new NullPointerException("NARG");
		
		this._varsref = __vr;
	}
}

