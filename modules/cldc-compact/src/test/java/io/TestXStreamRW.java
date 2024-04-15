// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package io;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import net.multiphasicapps.tac.TestFunction;

/**
 * Tests that {@link OutputStreamWriter} can link right into
 * {@link InputStreamReader}.
 *
 * @since 2022/07/12
 */
public class TestXStreamRW
	extends TestFunction<String, String>
{
	/** The test message. */
	public static final String MESSAGE =
		"Squirrels are adorable and cute! Wiewi\u00F3rki s\u0105 urocze i " +
		"urocze! \u30EA\u30B9\u304C\u53EF\u611B\u304F\u3066\u304B\u308F" +
		"\u3044\u3044\uFF01";
	
	/**
	 * {@inheritDoc}
	 * @since 2022/07/12
	 */
	@Override
	public String test(String __charset)
		throws IOException
	{
		// Encode message
		byte[] raw;
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
			Writer w = new OutputStreamWriter(baos, __charset))
		{
			// Write it out
			w.write(TestXStreamRW.MESSAGE);
			w.flush();
			
			// Get the raw bytes
			raw = baos.toByteArray();
		}
		
		// Decode message
		StringBuilder sb = new StringBuilder();
		try (Reader r = new InputStreamReader(new ByteArrayInputStream(raw),
			__charset))
		{
			for (;;)
			{
				int c = r.read();
				
				// EOF?
				if (c < 0)
					break;
				
				sb.append((char)c);
			}
		}
		
		// Is the string the same?
		String result = sb.toString();
		this.secondary("bytes", raw);
		this.secondary("equals", TestXStreamRW.MESSAGE.equals(result));
		return result;
	}
}
