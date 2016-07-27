// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.os.generic;

/**
 * These are constants which are used in the generic blob format.
 *
 * @since 2016/07/27
 */
public interface GenericBlobConstants
{
	/** The first magic number. */
	public static final long FIRST_MAGIC =
		0x005371756972726CL;
	
	/** The second magic number. */
	public static final long SECOND_MAGIC =
		0x564A4D455F4D5041L;
}

