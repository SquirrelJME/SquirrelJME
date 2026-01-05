// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.media.wav;

import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

public class WavDecoder
{

	/** Constant indicating that the left channel is being decoded */
	@SquirrelJMEVendorApi
	private static final byte LEFT_CHANNEL = 0;
	
	/** Constant indicating that the left channel is being decoded */
	@SquirrelJMEVendorApi
	private static final byte RIGHT_CHANNEL = 1;

	/** Step index table for IMA ADPCM decoding */
	@SquirrelJMEVendorApi
	private static final byte[] IMA_STEP_INDEX_TABLE = 
	{
		-1, -1, -1, -1, 2, 4, 6, 8, 
		-1, -1, -1, -1, 2, 4, 6, 8
	};

	/** Table of step sizes for IMA ADPCM decoding */
	@SquirrelJMEVendorApi
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
	@SquirrelJMEVendorApi
	private short[] _predictedSample = {0, 0};

	/** Array containing the last used table index for both channels */
	@SquirrelJMEVendorApi
	private byte[] _tableIndex = {0, 0};

	/**
	 * Creates a new WAV Decoder instance.
	 * 
	 * @since 2025/12/26
	 */
	@SquirrelJMEVendorApi
	WavDecoder()
	{
		this.resetADPCM(0, null, (byte) 1, 0);
	}


	/**
	 * This method will decode A-Law 8-bit PCM samples into PCM S16LE ones.
	 * 
	 * @param __input The input A-Law data.
	 * @param __inLen The input A-Law data size in valid audio samples.
	 * @param __inOff The input A-Law data's starting point
	 * @param __output The PCM S16LE output array.
	 * @param __outLen The PCM S16LE output array's size.
	 * @param __outOff The PCM S16LE output array's starting offset.
	 * @param __volMult The volume multiplier for the generated samples.
	 * @return The amount of samples that were decoded.
	 * @throws NullPointerException If {@code __input} is null.
	 * @throws IndexOutOfBoundsException If {@__inOff} is outside
	 * {@code __input}'s bounds or {@code __outOff} is outside
	 * {@code __output}'s bounds.
	 * @since 2025/12/26
	 */
	@SquirrelJMEVendorApi
	@Range(from = 0, to = Integer.MAX_VALUE)
	public int decodeALaw(@NotNull byte[] __input,
		@Range(from = 0, to = Integer.MAX_VALUE) int __inLen,
		@Range(from = 0, to = Integer.MAX_VALUE) int __inOff,
		@Nullable short[] __output,
		@Range(from = 0, to = Integer.MAX_VALUE) int __outLen,
		@Range(from = 0, to = Integer.MAX_VALUE) int __outOff,
		@Range(from = 0, to = 100) byte __volMult)
	{
		if (__input == null)
			throw new NullPointerException("NARG");
		
		if (__inOff > __inLen || __outOff > __outLen)
			throw new IndexOutOfBoundsException("Position out of bounds");

		// Nothing to write into yet.
		if (__output == null || __outLen == 0)
			return 0;

		// Decoding based on https://www.ti.com/lit/an/spra163a/spra163a.pdf.

		// A-Law is 1/2 compression, so each 8-bit sample is decompressed
		// to a 16-bit PCM one (byte -> short).

		boolean isNegative = false;
		int decodedSample;
		int step;
		int position;
		byte aLawSample;

		int i;
		int minLen = Math.min(__inLen, __outLen);
		
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
	 * This method will decode IMA ADPCM samples into PCM S16LE ones.
	 * 
	 * @param __input The input ADPCM data.
	 * @param __inLen The input ADPCM data size in valid audio samples.
	 * @param __numCh The amount of audio channels the ADPCM data has.
	 * @param __frameSize The amount of samples in each frame of ADPCM data.
	 * @param __output The PCM S16LE output array.
	 * @param __outLen The PCM S16LE output array's size.
	 * @param __outOff The PCM S16LE output array's starting offset.
	 * @param __volMult The volume multiplier for the generated samples.
	 * @return The position in the input ADPCM data array that this decoder
	 * stopped at. Used to resume decoding if the output buffer is not big
	 * enough to decode everything in a single run.
	 * @throws NullPointerException If {@code __input} is null.
	 * @throws IndexOutOfBoundsException If {@__inOff} is outside
	 * {@code __input}'s bounds or {@code __outOff} is outside
	 * {@code __output}'s bounds.
	 * @since 2025/12/26
	 */
	@SquirrelJMEVendorApi
	@Range(from = 0, to = Integer.MAX_VALUE)
	public int decodeIMAADPCM(@NotNull byte[] __input,
		@Range(from = 0, to = Integer.MAX_VALUE) int __inLen,
		@Range(from = 0, to = Integer.MAX_VALUE) int __inOff,
		@Range(from = 1, to = 2) byte __numCh,
		@Range(from = 0, to = Integer.MAX_VALUE) int __frameSize,
		@Nullable short[] __output,
		@Range(from = 0, to = Integer.MAX_VALUE) int __outLen,
		@Range(from = 0, to = Integer.MAX_VALUE) int __outOff,
		@Range(from = 0, to = 100) byte __volMult)
		throws NullPointerException, IndexOutOfBoundsException
	{
		if (__input == null)
			throw new NullPointerException("NARG");
		
		if (__inOff > __inLen || __outOff > __outLen)
			throw new IndexOutOfBoundsException("Position out of bounds");

		// Nothing to write into yet.
		if (__output == null || __outLen == 0)
			return 0;

		if (__inLen > __input.length)
			__inLen = __input.length;

		// Format specification: https://wiki.multimedia.cx/index.php/IMA_ADPCM

		byte adpcmSample;
		byte curChannel;
		short decodedSample;
		
		int inputIndex = __inOff;
		int outputIndex = 0;

		short[] predictedSample = this._predictedSample;
		byte[] tableIndex = this._tableIndex;

		while (inputIndex < __inLen && (__numCh == 2 ? (outputIndex <
			__outLen - 2) : (outputIndex < __outLen - 16))) 
		{
			// If we don't have enough data left to do another stereo run here,
			// return (or else we risk an OOB access and the whole stream gets
			// invalidated). TODO: Check if this is expected behavior.
			if (__numCh == 2 && (__inLen - inputIndex) < 16) 
			{
				Debugging.debugNote(
					"Remaining Bytes:%d < 16, cannot decode the last" +
						"few stereo samples. Adding silence instead.",
					__inLen - inputIndex); 
				break;
			} 

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
				predictedSample[WavDecoder.LEFT_CHANNEL] = (short) Math.max(
					Short.MIN_VALUE, Math.min(((__input[inputIndex])) |
					((__input[inputIndex+1]) << 8), Short.MAX_VALUE));
				
				// Byte 2 is the chunk's initial index on the step_size_table.
				// Clamp as well
				tableIndex[WavDecoder.LEFT_CHANNEL] = (byte) Math.max(0,
					Math.min(__input[inputIndex+2], 88));

				inputIndex += 4;
				
				// If we're dealing with stereo IMA ADPCM:
				if (__numCh == 2)
				{
					predictedSample[WavDecoder.RIGHT_CHANNEL] = (short) Math.max(
						Short.MIN_VALUE, Math.min(((__input[inputIndex])) |
						((__input[inputIndex+1]) << 8), Short.MAX_VALUE));

					tableIndex[WavDecoder.RIGHT_CHANNEL] = (byte) Math.max(0,
						Math.min(__input[inputIndex+2], 88));

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
					curChannel = (i < 4) ? WavDecoder.LEFT_CHANNEL :
						WavDecoder.RIGHT_CHANNEL;

					adpcmSample = (byte) (__input[inputIndex] & 0x0f);
					decodedSample = (short) (this.__decodeADPCMSample(curChannel,
						adpcmSample) * __volMult / 100);

					__output[outputIndex + ((i & 3) << 2) +
						(curChannel << 1)] = decodedSample;

					adpcmSample = (byte) ((__input[inputIndex] >> 4) & 0x0f);
					decodedSample = (short) (this.__decodeADPCMSample(curChannel,
						adpcmSample) * __volMult / 100);

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
				// Decode the entire block here and only get out of the loop
				// for preamble reads, or if we reached the end of the stream,
				// because we don't really need to keep going back up to check
				// all those if cases for every sample. 
				while(inputIndex % __frameSize != 0 && inputIndex <
					__inLen)
				{
					adpcmSample = (byte) (__input[inputIndex] & 0x0f);
					decodedSample = (short) (this.__decodeADPCMSample(
						WavDecoder.LEFT_CHANNEL,
						adpcmSample) * __volMult / 100);

					__output[outputIndex++] = decodedSample;

					adpcmSample = (byte) ((__input[inputIndex] >> 4) & 0x0f);
					decodedSample = (short) (this.__decodeADPCMSample(
						WavDecoder.LEFT_CHANNEL,
						adpcmSample) * __volMult / 100);

					__output[outputIndex++] = decodedSample;
					
					inputIndex++;
				}
			}
		}
		this._predictedSample = predictedSample;
		this._tableIndex = tableIndex;
		return inputIndex - __inOff;
	}

	/**
	 * This method will decode A-Law 8-bit PCM samples into PCM S16LE ones.
	 * 
	 * @param __input The input u-Law data.
	 * @param __inLen The input u-Law data size in valid audio samples.
	 * @param __inOff The input u-Law data's starting point
	 * @param __output The PCM S16LE output array.
	 * @param __outLen The PCM S16LE output array's size.
	 * @param __outOff The PCM S16LE output array's starting offset.
	 * @param __volMult The volume multiplier for the generated samples.
	 * @return The amount of samples that were decoded.
	 * @throws NullPointerException If {@code __input} is null.
	 * @throws IndexOutOfBoundsException If {@__inOff} is outside
	 * {@code __input}'s bounds or {@code __outOff} is outside
	 * {@code __output}'s bounds.
	 * @since 2025/12/26
	 */
	@SquirrelJMEVendorApi
	@Range(from = 0, to = Integer.MAX_VALUE)
	public int decodeULaw(@NotNull byte[] __input,
		@Range(from = 0, to = Integer.MAX_VALUE) int __inLen,
		@Range(from = 0, to = Integer.MAX_VALUE) int __inOff,
		@Nullable short[] __output,
		@Range(from = 0, to = Integer.MAX_VALUE) int __outLen,
		@Range(from = 0, to = Integer.MAX_VALUE) int __outOff,
		@Range(from = 0, to = 100) byte __volMult)
	{
		if (__input == null)
			throw new NullPointerException("NARG");
		
		if (__inOff > __inLen || __outOff > __outLen)
			throw new IndexOutOfBoundsException("Position out of bounds");
		
		// Nothing to write into yet.
		if (__output == null || __outLen == 0)
			return 0;
		
		// Also based on https://www.ti.com/lit/an/spra163a/spra163a.pdf.

		// u-Law is also 1/2 compression, so each 8-bit sample is decompressed
		// to a 16-bit PCM one.

		boolean isNegative = false;
		int decodedSample;
		int step;
		int position;
		byte uLawSample;

		int i;
		int minLen = Math.min(__inLen, __outLen);
		
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
	 * Resets the IMA ADPCM decoder to the specified sample's position.
	 * 
	 * If {@code __pos} is more than 0, this method expects an ADPCM data array
	 * as well as its number of channels and frame size in order to properly
	 * reset the marker to a specific point in its data. This is because ADPCM
	 * relies on the prior samples to update its index table and predictor.
	 * 
	 * @param __pos The position to reset to.
	 * @param __input The ADPCM data array to be used for fast-forwarding.
	 * @param __numCh THe amount of audio channels the ADPCM data has.
	 * @param __frameSize The amount of samples in each ADPCM frame.
	 * @return The sample index that this decoder was actually reset to.
	 * @throws NullPointerException If {@code __pos > 0} and {@code __input} is
	 * null.
	 * @throws IndexOutOfBoundsException If {@code __pos} is outside of {@code
	 * __input}'s bounds.
	 * @since 2025/12/26
	 */
	@SquirrelJMEVendorApi
	@Range(from = 0, to = Integer.MAX_VALUE)
	public int resetADPCM(@Range(from = 0, to = Integer.MAX_VALUE) int __pos, 
		@Nullable byte[] __input,
		@Range(from = 1, to = 2) byte __numCh,
		@Range(from = 0, to = Integer.MAX_VALUE) int __frameSize)
		throws NullPointerException, IndexOutOfBoundsException
	{
		// We first reset to the start.
		int inputIndex = 0;
		short[] predictedSample = this._predictedSample;
		byte[] tableIndex = this._tableIndex;
		
		// Initialize the predictor's sample and step values.
		predictedSample[WavDecoder.LEFT_CHANNEL] = 0;
		predictedSample[WavDecoder.RIGHT_CHANNEL] = 0;
		tableIndex[WavDecoder.LEFT_CHANNEL] = 0;
		tableIndex[WavDecoder.RIGHT_CHANNEL] = 0;

		// If we are resetting to a later position, fast-forward the decoder
		// until we reach the closest position to the one we received (IMA
		// ADPCM holds two samples per byte)
		if (__pos > 0)
		{
			if (__input == null)
				throw new NullPointerException("NARG");
			
			if (__pos > __input.length)
				throw new IndexOutOfBoundsException("Position out of bounds");

			short[] dummyArray = new short[__pos];
			return this.decodeIMAADPCM(__input, __pos, 0, __numCh, __frameSize,
				dummyArray, __pos, 0, (byte) 0);
		}

		return 0;
	}

	/**
	 * This method will decode a single IMA ADPCM sample into PCM S16LE.
	 * 
	 * @param __channel The audio channel this sample belongs to.
	 * @param __adpcmSample The ADPCM sample to decode
	 * @return A PCM S16LE sample generated from the input arguments.
	 * @since 2025/12/26
	 */
	@SquirrelJMEVendorApi
	@Range(from = 0, to = Integer.MAX_VALUE)
	private int __decodeADPCMSample(
		@Range(from = WavDecoder.LEFT_CHANNEL, to = WavDecoder.RIGHT_CHANNEL) byte __channel, 
		@Range(from = 0, to = 255) byte __adpcmSample)
	{ 
		// This decode procedure is mostly based on the following document:
		// https://www.cs.columbia.edu/~hgs/audio/dvi/IMA_ADPCM.pdf

		// Get the step size from the last table index saved for this channel,
		// to be used when decoding the new given sample. 
		short predictedSample = this._predictedSample[__channel];
		byte tableIndex = this._tableIndex[__channel];
		int stepSize = WavDecoder.IMA_STEP_SIZE_TABLE[tableIndex];
		
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
		tableIndex += WavDecoder.IMA_STEP_INDEX_TABLE[__adpcmSample];
		tableIndex = (byte) Math.max(0, Math.min(tableIndex, 88));

		this._tableIndex[__channel] = tableIndex;
		return this._predictedSample[__channel] = predictedSample;
	}
}
