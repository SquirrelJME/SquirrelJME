// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.test.io;

import cc.squirreljme.test.SubTest;
import cc.squirreljme.test.TestGroup;

import static cc.squirreljme.test.Parameters.parms;

/**
 * This class tests the base64 decoder.
 *
 * @since 2018/03/05
 */
public class TestBase64Decoder
	extends TestGroup
{
	/**
	 * Initializes the group.
	 *
	 * @since 2018/03/05
	 */
	public TestBase64Decoder()
	{
		super(Decode.class);
	}
	
	/**
	 * Decodes the input sequence.
	 *
	 * @since 2018/03/05
	 */
	public static class Decode
		extends SubTest
	{
		/**
		 * Initializes the decoder test.
		 *
		 * @since 2018/03/05
		 */
		public Decode()
		{
			super(
				parms("Hello World!", "SGVsbG8gV29ybGQh"),
				parms("I love squirrels!", "SSBsb3ZlIHNxdWlycmVscyE="),
				parms("I love you!!!", "SSBsb3ZlIHlvdSEhIQ=="));
		}
	}
}

