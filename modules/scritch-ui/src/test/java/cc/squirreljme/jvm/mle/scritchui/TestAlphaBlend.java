// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.mle.scritchui;

import cc.squirreljme.jvm.mle.PencilShelf;
import cc.squirreljme.jvm.mle.brackets.PencilBracket;
import cc.squirreljme.jvm.mle.constants.UIPixelFormat;
import cc.squirreljme.runtime.cldc.util.StreamUtils;
import cc.squirreljme.runtime.lcdui.mle.PencilGraphics;
import java.io.ByteArrayInputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import net.multiphasicapps.tac.TestConsumer;
import net.multiphasicapps.zip.streamreader.ZipStreamEntry;
import net.multiphasicapps.zip.streamreader.ZipStreamReader;

/**
 * Tests alpha blending of images.
 *
 * @since 2024/07/11
 */
public class TestAlphaBlend
	extends TestConsumer<String>
{
	/**
	 * {@inheritDoc}
	 * @since 2024/07/11
	 */
	@Override
	public void test(String __fsTs)
		throws Throwable
	{
		// Split
		int sp = __fsTs.indexOf('@');
		if (sp < 0)
			throw new IllegalArgumentException();
		
		// We need this interface for image operations
		ScritchInterface scritch = NativeScritchInterface.nativeInterface();
		
		// Determine the from and to alpha values
		int fA = Integer.decode(__fsTs.substring(0, sp)) << 24;
		int tA = Integer.decode(__fsTs.substring(sp + 1)) << 24;
		
		// Load in base RGB data
		int[] from = TestAlphaBlend.__load("______.rgb");
		
		// The destination pixels are a copy in the opposite order
		// We can also at the same time, set the source and target alphas
		int numPixels = from.length;
		int[] to = new int[numPixels];
		for (int w = 0, r = numPixels - 1; w < numPixels; w++, r--)
		{
			to[w] = from[r] | tA;
			from[r] |= fA;
		}
		
		// Get bracket for drawing
		PencilBracket g = scritch.hardwareGraphics(UIPixelFormat.INT_ARGB8888,
			numPixels, 1, from, null,
			0, 0, numPixels, 1);
		
		// Render to onto from
		PencilShelf.hardwareDrawXRGB32Region(g,
			to, 0, numPixels, true,
			0, 0, numPixels, 1,
			0,
			0, 0,
			0,
			numPixels, 1,
			numPixels, 1);
		
		// Read in expected values
		int[] expected = TestAlphaBlend.__load(
			String.format("_%02x_%02x.rgb", fA >>> 24, tA >>> 24));
		
		// Check each
		boolean failing = false;
		for (int i = 0; i < numPixels; i++)
			if (expected[i] != from[i])
			{
				failing = true;
				this.secondary(String.format("i%04d-%08x", i, expected[i]),
					String.format("%08x", from[i]));
			}
		
		// Just throw this to fail the test
		if (failing)
			throw new RuntimeException("Comparison failed.");
	}
	
	/**
	 * Loads in resource data.
	 *
	 * @param __rc The resource to load.
	 * @return The integer buffer data.
	 * @throws IOException On read errors.
	 * @since 2024/07/11
	 */
	static final int[] __load(String __rc)
		throws IOException
	{
		// Read in raw RGB data first
		byte[] rawData;
		try (InputStream in = TestAlphaBlend.class.getResourceAsStream(__rc))
		{
			if (in == null)
				throw new IOException("Test data not found? " + __rc);
			
			try (ZipStreamReader zsr = new ZipStreamReader(in))
			{
				try (ZipStreamEntry entry = zsr.nextEntry())
				{
					if (entry == null)
						throw new IOException("Test entry not found?" + __rc);
					
					rawData = StreamUtils.readAll(entry);
				}
			}
		}
		
		// Determine pixel count
		int rawLen = rawData.length;
		int numPixels = rawLen / 4;
		
		// Read everything into an array
		int[] result = new int[numPixels];
		try (ByteArrayInputStream bais = new ByteArrayInputStream(rawData);
			 DataInputStream dis = new DataInputStream(bais))
		{
			for (int i = 0; i < numPixels; i++)
				result[i] = dis.readInt();
		}
		
		// Give the read data
		return result;
	}
}
