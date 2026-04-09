// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------
package cc.squirreljme.runtime.media.pcm;

public class ALawDecoder
	implements PCMDecoder
{
	
	/**
	 * Creates a new A-Law Decoder instance.
	 * 
	 * @since 2026/01/05
	 */
	public ALawDecoder() 
	{
	}

	/**
	 * {@inheritDoc}
	 * @since 2026/01/05
	 */
	@Override
	public int decode(byte[] __input, int __inLen, int __inOff, byte __numCh,
		int __frameSize, short[] __output, int __outLen, int __outOff,
		byte __volMult)
		throws NullPointerException, IndexOutOfBoundsException
	{
		if (__input == null)
			throw new NullPointerException("NARG");
		
		if (__inOff > __inLen || __outOff > __outLen)
			throw new IndexOutOfBoundsException("Position out of bounds");

		// Decoding based on https://www.ti.com/lit/an/spra163a/spra163a.pdf.

		// A-Law is 1/2 compression, so each 8-bit sample is decompressed
		// to a 16-bit PCM one (byte -> short).

		boolean isNegative = false;
		int decodedSample;
		int step;
		int position;
		byte aLawSample;

		int i;
		int minLen = Math.min(__inLen - __inOff, __outLen);
		
		for (i = 0; i < minLen; i++)
		{
			// Most of the logic here is pretty similar for u-law.
			aLawSample = __input[__inOff + i];

			// A-law code has its even bits inverted for transmission, so we
			// need to invert them back first and foremost.
			aLawSample = (byte) (aLawSample ^ 0x55);

			// Get state of the most significant (sign) bit, as it indicates
			// whether the decoded sample should be positive or negative.
			isNegative = ((aLawSample & 0x80) != 0);

			// We have to invert the sign bit again if the sample is negative
			if (isNegative)
				aLawSample &= (byte)(~(1 << 7));

			// We now get the a-step (mantissa) as well as the channel and
			// shift exponent to use in the decoding formula.
			step = (aLawSample & 0x0F);
			position = (aLawSample & 0xF0) >> 4;

			// Based on 'Equation 25' from the PDF.
			// TODO: Out of bounds shifts are treated as if they were 0x1F
			// TODO: by the JVM.
			if (position != 4)
				decodedSample = (2 * step + 33) * (1 << position - 32) << 2;
			else
				decodedSample = (2 * step + 33) * (1 << position) << 2;

			// Instead of multiplying by the sign, we invert it here instead
			if (isNegative)
				decodedSample = -decodedSample;
			
			__output[__outOff + i] = (short) (decodedSample * __volMult / 100);
		}

		return i;
	}

	/**
	 * {@inheritDoc}
	 * @since 2026/01/05
	 */
	@Override
	public int reset(int __pos, byte[] __input, byte __numCh, int __frameSize)
		throws NullPointerException, IndexOutOfBoundsException
	{
		// A-Law does not need a reset
		throw new UnsupportedOperationException("A-Law does not reset");
	}
}
