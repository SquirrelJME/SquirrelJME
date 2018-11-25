// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import net.multiphasicapps.tac.TestSupplier;
import net.multiphasicapps.io.MIMEFileDecoder;

/**
 * Tests decoding of MIME files.
 *
 * @since 2018/11/25
 */
public class TestMIMEFileDecoder
	extends TestSupplier<Boolean>
{
	/**
	 * {@inheritDoc}
	 * @since 2018/11/25
	 */
	@Override
	public Boolean test()
		throws Throwable
	{
		byte[] buf = new byte[512];
		
		// Read the expected message
		byte[] expected;
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
			InputStream in = this.getClass().getResourceAsStream("message"))
		{
			for (;;)
			{
				int rc = in.read(buf);
				
				if (rc < 0)
					break;
				
				baos.write(buf, 0, rc);
			}
			
			// Read it
			expected = baos.toByteArray();
		}
		
		// Decode
		byte[] decoded;
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
			InputStream in = new MIMEFileDecoder(new InputStreamReader(
			this.getClass().getResourceAsStream("mimemessage"), "utf-8")))
		{
			for (;;)
			{
				int rc = in.read(buf);
				
				if (rc < 0)
					break;
				
				baos.write(buf, 0, rc);
			}
			
			// Read it
			decoded = baos.toByteArray();
		}
		
		// They must changed
		return Arrays.equals(expected, decoded);
	}
}

