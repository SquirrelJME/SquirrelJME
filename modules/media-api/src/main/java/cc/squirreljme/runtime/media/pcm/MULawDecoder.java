// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------
package cc.squirreljme.runtime.media.pcm;

public class MULawDecoder
	implements PCMDecoder
{
	
	/**
	 * Creates a new U-LAW Decoder instance.
	 * 
	 * @since 2026/01/05
	 */
	public MULawDecoder()
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
		
		// Also based on https://www.ti.com/lit/an/spra163a/spra163a.pdf.

		// u-Law is also 1/2 compression, so each 8-bit sample is decompressed
		// to a 16-bit PCM one.

		boolean isNegative = false;
		int decodedSample;
		int step;
		int position;
		byte uLawSample;

		int i;
		int minLen = Math.min(__inLen - __inOff, __outLen);
		
		for (i = 0; i < minLen; i++)
		{
			uLawSample = __input[__inOff + i];

			// u-law code is inverted for transmission, so we need to invert
			// the sample back first and foremost.
			uLawSample = (byte) ~uLawSample;

			// Get state of the most significant (sign) bit, as it indicates
			// whether the decoded sample should be positive or negative.
			isNegative = ((uLawSample & 0x80) != 0);

			// We have to invert the sign bit again if the sample is negative
			if (isNegative)
				uLawSample &= (byte)(~(1 << 7));

			// We now get the u-step (mantissa) as well as the channel and
			// shift exponent to use in the decoding formula.
			step = (uLawSample & 0x0F);
			position = (uLawSample & 0xF0) >> 4;

			// This is 'Equation 17' from the PDF above.
			// TODO: Out of bounds shifts are treated as if they were 0x1F
			// TODO: by the JVM.
			decodedSample = ((2 * step + 33) * (1 << position - 33)) << 3;

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
		throw new UnsupportedOperationException("MU-Law does not reset");
	}
}
