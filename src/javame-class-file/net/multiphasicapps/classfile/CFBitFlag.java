// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.classfile;

/**
 * This interface is used for flags to represent that bits are used.
 *
 * @since 2016/03/15
 */
public interface JVMBitFlag
{
	/**
	 * Returns the mask of the flag.
	 *
	 * @return The flag's mask.
	 * @since 2016/03/15
	 */
	public abstract int mask();
}

