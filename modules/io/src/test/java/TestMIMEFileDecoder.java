// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Objects;
import net.multiphasicapps.io.MIMEFileDecoder;
import net.multiphasicapps.tac.TestSupplier;

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
		
		// The decoded is a fixed length, even with the raw data
		this.secondary("dlen", decoded.length);
		
		// Compare all lines
		int linecount = 0;
		try (BufferedReader dec = new BufferedReader(new InputStreamReader(
				new ByteArrayInputStream(decoded), "utf-8"));
			BufferedReader exp = new BufferedReader(new InputStreamReader(
				new ByteArrayInputStream(expected), "utf-8")))
		{
			for (;; linecount++)
			{
				String dl = dec.readLine();
				String xl = exp.readLine();
				
				// Compare equality first
				if (!Objects.equals(dl, xl))
				{
					// Add these in case
					this.secondary("decoded", dl);
					this.secondary("expected", xl);
					
					// Fail
					return false;
				}
				
				if (dl == null || xl == null)
					break;
			}
		}
		
		// Line count
		this.secondary("linecount", linecount);
		
		// Would be a mis-match if not reached
		return true;
	}
}

