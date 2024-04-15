// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

import net.multiphasicapps.io.CRC32Calculator;
import net.multiphasicapps.tac.TestSupplier;
import net.multiphasicapps.zip.ZipCRCConstants;

/**
 * Testing for {@link CRC32Calculator}.
 *
 * @since 2021/11/13
 */
public class TestCrcCalculate
	extends TestSupplier<Integer>
{
	/** The data to test. */
	private static final byte[] _DATA =
		{'T', 'h', 'e', ' ', 'q', 'u', 'i', 'c', 'k', ' ', 'g', 'r', 'a', 'y',
		' ', 's', 'q', 'u', 'i', 'r', 'r', 'e', 'l', ' ', 'j', 'u', 'm', 'p',
		's', ' ', 'o', 'v', 'e', 'r', ' ', 't', 'h', 'e', ' ', 'l', 'a', 'z',
		'y', ' ', 'r', 'e', 'd', ' ', 'p', 'a', 'n', 'd', 'a', '!'};
	
	/**
	 * {@inheritDoc}
	 * @since 2021/11/13
	 */
	@Override
	public Integer test()
	{
		// Perform the same calculation as ZIP.
		CRC32Calculator crc = new CRC32Calculator(
			ZipCRCConstants.CRC_REFLECT_DATA,
			ZipCRCConstants.CRC_REFLECT_REMAINDER,
			ZipCRCConstants.CRC_POLYNOMIAL, ZipCRCConstants.CRC_REMAINDER,
			ZipCRCConstants.CRC_FINALXOR);
		
		// Run the calculation.
		crc.offer(TestCrcCalculate._DATA);
		
		// Return the calculated checksum.
		return crc.checksum();
	}
}
