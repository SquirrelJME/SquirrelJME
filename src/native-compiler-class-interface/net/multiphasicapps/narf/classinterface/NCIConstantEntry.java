// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.narf.classinterface;

/**
 * This interface is used to represent constant pool entries.
 *
 * @since 2016/04/24
 */
public interface NCIConstantEntry
{
	/**
	 * Returns the tag of the entry.
	 *
	 * @return The constant pool tag.
	 * @since 2016/04/24
	 */
	public abstract NCIConstantTag tag();
}

