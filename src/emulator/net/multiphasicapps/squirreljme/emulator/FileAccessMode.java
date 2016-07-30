// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.emulator;

/**
 * This determines how a file is to be accesed.
 *
 * @since 2016/07/30
 */
public enum FileAccessMode
{
	/** Reading. */
	READ,
	
	/** Writing. */
	WRITE,
	
	/** Appending. */
	APPEND,
	
	/** Truncating. */
	TRUNCATE,
	
	/** End. */
	;
	
	/**
	 * Returns {@code true} if the flag is set in the given bit field.
	 *
	 * @return {@code true} if the flag is set in the bit field.
	 * @since 2016/07/30
	 */
	public final boolean isSet(int __f)
	{
		return ((__f & mask()) != 0);
	}
	
	/**
	 * Returns the mask of the flag.
	 *
	 * @return The mask.
	 * @since 2016/07/30
	 */
	public final int mask()
	{
		return 1 << ordinal();
	}
}

