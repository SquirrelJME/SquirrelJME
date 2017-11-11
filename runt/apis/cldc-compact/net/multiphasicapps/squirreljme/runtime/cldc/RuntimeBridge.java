// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.runtime.cldc;

/**
 * This class contains the bridge to the internal SquirrelJME run-time
 * classes which provide special run-time functionality.
 *
 * @since 2017/11/10
 */
public final class RuntimeBridge
{
	/** Controls objects. */
	public static final ObjectFunctions OBJECT =
		__object();
	
	/**
	 * Only contains static instances.
	 *
	 * @since 2017/11/10
	 */
	private RuntimeBridge()
	{
	}
	
	/**
	 * Returns the {@link #OBJECT} field.
	 *
	 * @return {@link #OBJECT}.
	 * @since 2017/11/10
	 */
	private static final ObjectFunctions __object()
	{
		return OBJECT;
	}
}

