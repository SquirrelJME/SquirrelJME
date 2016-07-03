// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit;

/**
 * This indicates the endianess of a given CPU.
 *
 * @since 2016/07/02
 */
public enum JITCPUEndian
{
	/** Big endian (most significant bit first). */
	BIG,
	
	/** Little endian (least significant bit first). */
	LITTLE,
	
	/** End. */
	;
	
	/**
	 * Returns the endianess which is associated with the given string.
	 *
	 * @param __s The string to get the endian for.
	 * @return The endian associated with the given string.
	 * @throws IllegalArgumentException If the input string is not known.
	 * @since 2016/07/03
	 */
	public static JITCPUEndian of(String __s)
		throws IllegalArgumentException
	{
		switch (__s)
		{
				// Big endian
			case "big":
				return JITCPUEndian.BIG;
			
				// Little endian
			case "little":
				return JITCPUEndian.LITTLE;
				
				// {@squirreljme.error ED01 Unknown endian.
				// (The endian string)}
			default:
				throw new IllegalArgumentException(String.format("ED01 %s",
					__s));
		}
	}
}

