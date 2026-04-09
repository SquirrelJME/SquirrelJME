// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------
package cc.squirreljme.runtime.media.pcm;

import org.jetbrains.annotations.Range;

public class IMAADPCMDecoder 
	implements PCMDecoder
{

	/** Constant indicating that the left channel is being decoded */
	private static final byte LEFT_CHANNEL = 0;
	
	/** Constant indicating that the left channel is being decoded */
	private static final byte RIGHT_CHANNEL = 1;

	/** Step index table for IMA ADPCM decoding */
	private static final byte[] IMA_STEP_INDEX_TABLE =
	{
		-1, -1, -1, -1, 2, 4, 6, 8,
		-1, -1, -1, -1, 2, 4, 6, 8
	};

	/** Table of step sizes for IMA ADPCM decoding */
	private static final short[] IMA_STEP_SIZE_TABLE =
	{
		7, 8, 9, 10, 11, 12, 13, 14, 16, 17,
		19, 21, 23, 25, 28, 31, 34, 37, 41, 45,
		50, 55, 60, 66, 73, 80, 88, 97, 107, 118,
		130, 143, 157, 173, 190, 209, 230, 253, 279, 307,
		337, 371, 408, 449, 494, 544, 598, 658, 724, 796,
		876, 963, 1060, 1166, 1282, 1411, 1552, 1707, 1878, 2066,
		2272, 2499, 2749, 3024, 3327, 3660, 4026, 4428, 4871, 5358,
		5894, 6484, 7132, 7845, 8630, 9493, 10442, 11487, 12635, 13899,
		15289, 16818, 18500, 20350, 22385, 24623, 27086, 29794, 32767
	};

	/** Array containing the last predicted sample for both channels */
	private short[] _predictedSample = {0, 0};

	/** Array containing the last used table index for both channels */
	private byte[] _tableIndex = {0, 0};
	
	/**
	 * Creates a new IMA ADPCM Decoder instance.
	 * 
	 * @since 2026/01/05
	 */
	public IMAADPCMDecoder()
	{
		this.reset(0, null, (byte) 1, 0);
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

		// Format specification: https://wiki.multimedia.cx/index.php/IMA_ADPCM

		byte adpcmSample;
		byte curChannel;
		short decodedSample;
		
		int inputIndex = __inOff;
		int outputIndex = 0;

		short[] predictedSample = this._predictedSample;
		byte[] tableIndex = this._tableIndex;

		while (inputIndex < __inLen && (__numCh == 2 ? (outputIndex <
			__outLen - 2) : (outputIndex < __outLen - 8)))
		{
			// Check if the decoder is at the beginning of a new chunk to see
			// if the preamble needs to be read.
			if (inputIndex % __frameSize == 0)
			{
				// For each 4 bits used in IMA ADPCM,
				// 16 must be used for PCM so adjust indices and sizes
				// accordingly. Byte 3 is reserved and has no practical
				// use for us.

				// Bytes 0 and 1 describe the chunk's initial predictor value
				// (little-endian), clamp it even in case of issues such as to
				// try and preserve the decoded stream's quality.
				predictedSample[IMAADPCMDecoder.LEFT_CHANNEL] = (short)
					Math.max( Short.MIN_VALUE, Math.min(((__input[inputIndex]))
					| ((__input[inputIndex+1]) << 8), Short.MAX_VALUE));
				
				// Byte 2 is the chunk's initial index on the step_size_table.
				// Clamp as well
				tableIndex[IMAADPCMDecoder.LEFT_CHANNEL] = (byte)
					Math.max(0, Math.min(__input[inputIndex+2], 88));

				inputIndex += 4;
				
				// If we're dealing with stereo IMA ADPCM:
				if (__numCh == 2)
				{
					predictedSample[IMAADPCMDecoder.RIGHT_CHANNEL] =
						(short) Math.max(Short.MIN_VALUE,
						Math.min(((__input[inputIndex])) |
						((__input[inputIndex+1]) << 8), Short.MAX_VALUE));

					tableIndex[IMAADPCMDecoder.RIGHT_CHANNEL] = (byte)
						Math.max(0, Math.min(__input[inputIndex+2], 88));

					inputIndex += 4;
				}
			}

			// In the very rare cases where some j2me app might use stereo
			// IMA ADPCM, we should decode each audio channel.
			// If the format is stereo, it is interleaved, which means that
			// the stream will have a left channel sample followed by a
			// right channel sample, followed by a left... and so on. In
			// ADPCM those samples are setup so that 4 bytes (8 nibbles)
			// from the left channel are followed by 4 bytes of the right.
			// https://wiki.multimedia.cx/index.php/Microsoft_IMA_ADPCM.

			if (__numCh == 2)
			{
				// So in the case it's a stereo stream, decode 8 nibbles from
				// both left and right channels, interleaving them in the
				// resulting PCM stream.
				for (byte i = 0; i < 8; i++)
				{
					curChannel = (i < 4) ? IMAADPCMDecoder.LEFT_CHANNEL :
						IMAADPCMDecoder.RIGHT_CHANNEL;

					adpcmSample = (byte) (__input[inputIndex] & 0x0f);
					decodedSample = (short) (this.__decodeADPCMSample(
						curChannel, adpcmSample) * __volMult / 100);

					__output[outputIndex + ((i & 3) << 2) +
						(curChannel << 1)] = decodedSample;

					adpcmSample = (byte) ((__input[inputIndex] >> 4) & 0x0f);
					decodedSample = (short) (this.__decodeADPCMSample(
						curChannel, adpcmSample) * __volMult / 100);

					__output[outputIndex + ((i & 3) << 2) +
						(curChannel << 1) + 1] = decodedSample;

					inputIndex++;
				}
				outputIndex += 16;
			}
			else
			{
				// If it's mono, just decode nibbles from ADPCM into PCM data
				// sequentially, there's no sample interleaving to worry about,
				// much less multiple channels, so we only use channel 0.
				adpcmSample = (byte) (__input[inputIndex] & 0x0f);
				decodedSample = (short) (this.__decodeADPCMSample(
					IMAADPCMDecoder.LEFT_CHANNEL,
					adpcmSample) * __volMult / 100);

				__output[outputIndex++] = decodedSample;

				adpcmSample = (byte) ((__input[inputIndex] >> 4) & 0x0f);
				decodedSample = (short) (this.__decodeADPCMSample(
					IMAADPCMDecoder.LEFT_CHANNEL,
					adpcmSample) * __volMult / 100);

				__output[outputIndex++] = decodedSample;
				
				inputIndex++;
			}
		}
		this._predictedSample = predictedSample;
		this._tableIndex = tableIndex;
		return inputIndex - __inOff;
	}

	/**
	 * {@inheritDoc}
	 * @since 2026/01/05
	 */
	@Override
	public int reset(int __pos, byte[] __input, byte __numCh, int __frameSize)
		throws NullPointerException, IndexOutOfBoundsException
	{
		// We first reset to the start.
		short[] predictedSample = this._predictedSample;
		byte[] tableIndex = this._tableIndex;
		
		// Initialize the predictor's sample and step values.
		predictedSample[IMAADPCMDecoder.LEFT_CHANNEL] = 0;
		predictedSample[IMAADPCMDecoder.RIGHT_CHANNEL] = 0;
		tableIndex[IMAADPCMDecoder.LEFT_CHANNEL] = 0;
		tableIndex[IMAADPCMDecoder.RIGHT_CHANNEL] = 0;

		// If we are resetting to a later position, fast-forward the decoder
		// until we reach the closest position to the one we received (IMA
		// ADPCM holds two samples per byte)
		if (__pos > 0)
		{
			if (__input == null)
				throw new NullPointerException("NARG");

			// The wav player will pass the expected position in the byte array
			// considering it has standard PCM samples, however, adpcm has 2
			// samples per byte, so divide by 2 here.
			__pos /= 2;
			
			short[] dummyArray = new short[16];
			int fwdSamples = 0;

			while (fwdSamples < __pos)
				fwdSamples += this.decode(__input, __pos, fwdSamples,
					__numCh, __frameSize, dummyArray, 16, 0,
					(byte) 0);

			return fwdSamples;
		}

		return 0;
	}

	/**
	 * This method will decode a single IMA ADPCM sample into PCM S16LE.
	 * 
	 * @param __channel The audio channel this sample belongs to.
	 * @param __adpcmSample The ADPCM sample to decode
	 * @return A PCM S16LE sample generated from the input arguments.
	 * @since 2026/01/05
	 */
	@Range(from = 0, to = Integer.MAX_VALUE)
	private int __decodeADPCMSample(
		@Range(from = IMAADPCMDecoder.LEFT_CHANNEL,
			to = IMAADPCMDecoder.RIGHT_CHANNEL) byte __channel,
		@Range(from = 0, to = 255) byte __adpcmSample)
	{
		// This decode procedure is mostly based on the following document:
		// https://www.cs.columbia.edu/~hgs/audio/dvi/IMA_ADPCM.pdf

		// Get the step size from the last table index saved for this channel,
		// to be used when decoding the new given sample.
		short predictedSample = this._predictedSample[__channel];
		byte tableIndex = this._tableIndex[__channel];
		int stepSize = IMAADPCMDecoder.IMA_STEP_SIZE_TABLE[tableIndex];
		
		// This follows the first optimization of the original IMA ADPCM diff
		// calculation formula found in
		// https://wiki.multimedia.cx/index.php/IMA_ADPCM
		int diff = ((stepSize * (__adpcmSample & 0x07)) + (stepSize >> 1)) >>
			2;

		// Negate if the sign bit is set
		if ((__adpcmSample & 8) != 0)
			diff = -diff;

		// Clamps the value of decodedSample to that of a short data type. At
		// this point, the decoded sample should already fit nicely into a
		// short type value range as per columbia's doc.
		predictedSample = (short) Math.max(Short.MIN_VALUE,
			Math.min(predictedSample + diff, Short.MAX_VALUE));

		// Basically columbia doc's "calculate stepsize" snippet
		tableIndex += IMAADPCMDecoder.IMA_STEP_INDEX_TABLE[__adpcmSample];
		tableIndex = (byte) Math.max(0, Math.min(tableIndex, 88));

		this._tableIndex[__channel] = tableIndex;
		return this._predictedSample[__channel] = predictedSample;
	}
}
