// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

import net.multiphasicapps.tac.TestSupplier;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import net.multiphasicapps.io.CompressionLevel;
import net.multiphasicapps.io.InflaterInputStream;
import net.multiphasicapps.io.DeflaterOutputStream;

/**
 * Tests the deflater and then the inflater, making sure that compressed
 * data ends up the same when decompressed.
 *
 * @since 2018/11/10
 */
public class TestDeflaterInflater
	extends TestSupplier<Integer>
{
	/**
	 * {@inheritDoc}
	 * @since 2018/11/10
	 */
	@Override
	public Integer test()
		throws Throwable
	{
		// Read in the message first
		byte[] message;
		try (InputStream in = TestDeflaterInflater.class.getResourceAsStream(
			"message");
			ByteArrayOutputStream baos = new ByteArrayOutputStream(1024))
		{
			// Copy in data
			byte[] buf = new byte[128];
			for (;;)
			{
				int rc = in.read(buf);
				
				if (rc < 0)
					break;
				
				baos.write(buf, 0, rc);
			}
			
			// Finalize
			message = baos.toByteArray();
		}
		
		// Original message as a string for comparison
		String original = new String(message, "iso-8859-1");
		
		// Compression is hopefully smaller so use a buffer size matching this
		int gn = message.length;
		
		// For each compression level since they vary
		int rv = 0;
		for (CompressionLevel cl : CompressionLevel.values())
		{
			// Compress the data with my compression code first
			byte[] compressed;
			try (ByteArrayOutputStream baos = new ByteArrayOutputStream(gn))
			{
				// Compress the input message
				try (DeflaterOutputStream dos = new DeflaterOutputStream(baos))
				{
					dos.write(message);
				}
				
				// Need to keep this message
				compressed = baos.toByteArray();
			}
			
			// Then it will be decompressed accordingly
			byte[] decompressed;
			try (ByteArrayOutputStream baos = new ByteArrayOutputStream(gn))
			{
				// Decompress the message
				try (InflaterInputStream iis = new InflaterInputStream(
					new ByteArrayInputStream(compressed)))
				{
					byte[] buf = new byte[128];
					for (;;)
					{
						int rc = iis.read(buf);
						
						if (rc < 0)
							break;
						
						baos.write(buf, 0, rc);
					}
				}
				
				// Store decompressed message
				decompressed = baos.toByteArray();
			}
			
			// Return string as processed through the algorithm
			String result = new String(decompressed, "iso-8859-1");
			
			// If the strings are the same then it is okay!
			if (original.equals(result))
				rv++;
			
			// Otherwise flag it and note it
			else
				this.secondary("failed-" + cl, result);
		}
		
		return rv;
	}
}

