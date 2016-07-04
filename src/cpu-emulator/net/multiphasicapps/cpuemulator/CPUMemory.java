// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.cpuemulator;

/**
 * This class represents memory which is available to a system for usage.
 *
 * Before regions of memory must be used, specific regions must be setup and
 * bound in a way where accessing the bytes either goes through special
 * handlers or through generic storage mechanisms.
 *
 * @since 2016/07/04
 */
public class CPUMemory
{
	/**
	 * Initializes emulated CPU memory.
	 *
	 * @since 2016/07/04
	 */
	public CPUMemory()
	{
	}
}

