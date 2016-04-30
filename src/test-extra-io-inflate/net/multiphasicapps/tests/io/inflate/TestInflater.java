// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.tests.io.inflate;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Reader;
import net.multiphasicapps.io.datapipe.DataPipeInputStream;
import net.multiphasicapps.io.inflate.InflateDataPipe;
import net.multiphasicapps.tests.TestChecker;
import net.multiphasicapps.tests.TestInvoker;

/**
 * This contains tests for the extra IO inflate decompression algorithm.
 *
 * @since 2016/03/10
 */
public class TestInflater
	implements TestInvoker
{
	/**
	 * {@inheritDoc}
	 * @since 2016/03/03
	 */
	@Override
	public String invokerName()
	{
		return "net.multiphasicapps.io.inflate.InflateDataPipe";
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/03
	 */
	@Override
	public void runTests(TestChecker __tc)
		throws NullPointerException
	{
		// Check
		if (__tc == null)
			throw new NullPointerException("NARG");
		
		// Go through samples, which are resources
		for (char c = 'a'; c <= 'z'; c++)
		{
			// Sample names
			String in = "test-" + c + ".in";
			String on = "test-" + c + ".out";
			
			// Try opening resources for them
			try (InputStream ii = getClass().getResourceAsStream(in);
				InputStream oo = getClass().getResourceAsStream(on))
			{
				// Ends
				if (ii == null || oo == null)
					break;
				
				// Read in both files to arrays
				byte[] xi = __readToArray(new InputStreamReader(ii, "utf-8"));
				byte[] xo = __readToArray(new InputStreamReader(oo, "utf-8"));
				
				// Call checker
				__check(__tc, Character.toString(c), xi, xo);
			}
			
			// Problem
			catch (IOException ioe)
			{
				__tc.exception(Character.toString(c), ioe);
			}
		}
	}
	
	/**
	 * Checks whether the inflater works for the given input.
	 *
	 * @param __tc The test checker.
	 * @param __id The identification of this test.
	 * @param __in The input bytes.
	 * @param __out The output bytes.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/10
	 */
	private void __check(TestChecker __tc, String __id, byte[] __in,
		byte[] __out)
		throws NullPointerException
	{
		// Check
		if (__tc == null || __id == null || __in == null || __out == null)
			throw new NullPointerException();
		
		// Open the input
		try (InputStream in = new DataPipeInputStream(
			new ByteArrayInputStream(__in), new InflateDataPipe());
			ByteArrayOutputStream out = new ByteArrayOutputStream())
		{
			// Read input to the output
			for (;;)
			{
				// Read in
				int b = in.read();
				
				// EOF?
				if (b < 0)
					break;
				
				// Write
				out.write(b);
			}
			
			// Check the array
			__tc.checkEquals(__id, __out, out.toByteArray());
		}
		
		// Failed
		catch (IOException ioe)
		{
			__tc.exception(__id, ioe);
		}
	}
	
	/**
	 * Reads input hex data to a byte array.
	 *
	 * @param __is The stream to read hex characters from.
	 * @return The read byte array data.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/11
	 */
	private byte[] __readToArray(Reader __r)
		throws IOException, NullPointerException
	{
		// Check
		if (__r == null)
			throw new NullPointerException();
		
		// Open output array target
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream())
		{
			// Read input constantly
			byte b = 0;
			for (boolean hi = true;;)
			{
				// Read
				int v = __r.read();
				
				// EOF?
				if (v < 0)
					break;
				
				// Read a hex digit
				int digit = Character.digit((char)v, 16);
				
				// Not a valid digit?
				if (digit < 0)
					continue;
				
				// Write high?
				if (hi)
					b |= (byte)((digit & 0xF) << 4);
				
				// Write low
				else
				{
					b |= (byte)(digit & 0xF);
					
					// Give to output also
					baos.write(b);
					
					// Clear
					b = 0;
				}
				
				// Invert
				hi = !hi;
			}
			
			// Return the array
			return baos.toByteArray();
		}
	}
}

