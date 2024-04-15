// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
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
	int CRC_POLYNOMIAL =
		0x04C11DB7;
	
	/** The initial CRC remainder. */
	int CRC_REMAINDER =
		0xFFFFFFFF;
	
	/** The final XOR value. */
	int CRC_FINALXOR =
		0xFFFFFFFF;
	
	/** Reflect the data? */
	boolean CRC_REFLECT_DATA =
		true;
	
	/** Reflect the remainder? */
	boolean CRC_REFLECT_REMAINDER =
		true;
}

