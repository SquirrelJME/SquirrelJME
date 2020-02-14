// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.zip;

/**
 * This class constants constants which are used to initialize the CRC
 * algorithm parameters.
 *
 * @since 2016/07/19
 */
public interface ZipCRCConstants
{
	/** The polynomial for the CRC algorithm. */
	public static final int CRC_POLYNOMIAL =
		0x04C11DB7;
	
	/** The initial CRC remainder. */
	public static final int CRC_REMAINDER =
		0xFFFFFFFF;
	
	/** The final XOR value. */
	public static final int CRC_FINALXOR =
		0xFFFFFFFF;
	
	/** Reflect the data? */
	public static final boolean CRC_REFLECT_DATA =
		true;
	
	/** Reflect the remainder? */
	public static final boolean CRC_REFLECT_REMAINDER =
		true;
}

