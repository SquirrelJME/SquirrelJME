// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.media.wav;

import cc.squirreljme.runtime.cldc.annotation.KeepWhenCompacting;
import cc.squirreljme.runtime.cldc.annotation.KeepWhenCompacting;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.io.EOFException;
import java.io.InputStream;
import java.io.IOException;
import net.multiphasicapps.io.ExtendedDataInputStream;

/**
 * WAV Utilities.
 *
 * @since 2025/12/31
 */
@KeepWhenCompacting
class __WavTools__ 
{
	/** The standard size of a PCM WAV's header. */
	private static final byte PCM_HEADER_SIZE =
		44;

	/** The standard size of an ADPCM WAV's header. */
	private static final byte ADPCM_HEADER_SIZE =
		60;

	/** Magic number for the header's 'RIFF' string. */
	private static final int RIFF_MAGIC =
		0x52494646;          

	/** Magic number for the header's 'WAVE' string. */
	private static final int WAVE_MAGIC =
		0x57415645;

	/** Magic number for the header's {@code 'fmt '} chunk delimiter. */
	private static final int FMT_MAGIC =
		0x666d7420;

	/** Magic number for the header's {@code 'data'} chunk delimiter. */
	private static final int DATA_MAGIC =
		0x64617461;

	/** Header format value that indicates a standard PCM WAV. */
	public static final int FORMAT_PCM_WAV =
		0x1;

	/** Header format value that indicates an IEEE-spec Float PCM WAV. */
	public static final int FORMAT_FLOAT_WAV =
		0x3;

	/** Header format value that indicates an 8-bit ITU-T G.711 A-law WAV. */
	public static final int FORMAT_ALAW_WAV =
		0x6;

	/** Header format value that indicates an 8-bit ITU-T G.711 u-law WAV. */
	public static final int FORMAT_MULAW_WAV =
		0x7;

	/** Header format value that indicates an IMA ADPCM WAV. */
	public static final int FORMAT_IMA_ADPCM =
		0x11;

	/**
	 * Builds a WAV header that describes a decoded PCM file on the first 44
	 * bytes.
	 * 
	 * Output buffer's audio data is expected to be offset by 44 bytes (as
	 * those positions will be taken by the new header).
	 * 
	 * @param __buffer The data array to build a header for
	 * @param __numChannels The amount of channels the data has
	 * @param __sampleRate The data's sample rate
	 * @param __numBits The data's amount of bits per sample
	 * @param __sampleDataLength The amount of samples in the data array
	 * @throws NullPointerException On null arguments.
	 * @since 2025/13/31
	 */
	public static void buildPCMWavHeader(byte[] __buffer,
		byte __numChannels, int __sampleRate,
		byte __numBits, int __sampleDataLength)
		throws NullPointerException
	{
		if (__buffer == null)
			throw new NullPointerException("NARG");
		
		// subChunkSize defaults to 16 for Wav PCM
		int subChunkSize = 16;

		// Frame size is fairly standard, and PCM's fixed sample size makes it
		// so the frameSize is either 2 bytes for mono, or 4 bytes for stereo.
		short frameSize = (short) (__numChannels * (__numBits / 8));

		// Represents how many bytes are streamed per second. With all of the
		// data above, it's trivial to calculate by getting the sample rate,
		// the amount of channels and bytes per sample (__numBits / 8).
		int bytesPerSec = __sampleRate * __numChannels *
			(__numBits / 8);
		
		// NOTE: ChunkSize is the total file size - 8 bytes
		__WavTools__.__writeIntBE(__buffer, 0, __WavTools__.RIFF_MAGIC);
		__WavTools__.__writeIntLE(__buffer, 4, 
			__buffer.length - 8);
		__WavTools__.__writeIntBE(__buffer, 8, __WavTools__.WAVE_MAGIC);
		__WavTools__.__writeIntBE(__buffer, 12, __WavTools__.FMT_MAGIC);
		__WavTools__.__writeIntLE(__buffer, 16, subChunkSize);
		__WavTools__.__writeShort(__buffer, 20, 
			(short)__WavTools__.FORMAT_PCM_WAV);
		__WavTools__.__writeShort(__buffer, 22, __numChannels);
		__WavTools__.__writeIntLE(__buffer, 24, __sampleRate);
		__WavTools__.__writeIntLE(__buffer, 28, bytesPerSec);
		__WavTools__.__writeShort(__buffer, 32, frameSize);
		__WavTools__.__writeShort(__buffer, 34, __numBits);
		__WavTools__.__writeIntBE(__buffer, 36, __WavTools__.DATA_MAGIC);
		__WavTools__.__writeIntLE(__buffer, 40, __sampleDataLength);
	}

	/**
	 * Reads a WAV file's header in order to get its properties for playback.
	 * 
	 * @param __in The input stream to read from
	 * @return An array containing relevant wav data to begin processing, such
	 * as sample rate, bits per sample, amount of channels, and so on.
	 * @throws IOException If data could not be read.
	 * @throws NullPointerException On null arguments.
	 * @since 2025/13/31
	 */
	public static int[] readHeader(ExtendedDataInputStream __in)
		throws IOException, NullPointerException
	{
		if (__in == null)
			throw new NullPointerException("NARG");
		
		// The header of a WAV (RIFF) file has the following format:

		// CHAR[4] "RIFF" header
		// UINT32  Size of the file (chunkSize).
		//	CHAR[4] "WAVE" format
		//	CHAR[4] "fmt " header
		//	UINT32  SubChunkSize
		//		UINT16 AudioFormat (ex: 1/0x1 [PCM], 17/0x11 [IMA ADPCM] )
		//		UINT16 NumChannels
		//		UINT32 SampleRate
		//		UINT32 BytesPerSec (samplerate*frame size)
		//		UINT16 frameSize or blockAlign
		//		UINT16 BitsPerSample
		//      [optional extra fmt bytes if SubChunkSize > 16]
		// [optional sub-chunks: "fact", "LIST", etc.]
		//	CHAR[4] "data" header
		//	UINT32 Length of sample data.
		//	<Sample data>

		// IMA ADPCM usually carries extra fmt bytes and often a fact chunk
		// before data; parser scans chunks dynamically instead of relying on
		// fixed offsets.

		// UINT16 ByteExtraData
		// UINT16 ExtraData
		// CHAR[4] "fact" header
		// UINT32 SubChunk2Size
		// UINT32 NumOfSamples
		
		String riff = __WavTools__.__readString(__in, 4);
		int dataSize = __in.readInt();  
		String format = __WavTools__.__readString(__in, 4);
		String fmt = __WavTools__.__readString(__in, 4);
		int chunkSize = __in.readInt();
		short audioFormat = __in.readShort();
		short audioChannels = __in.readShort();
		int sampleRate = __in.readInt();
		int bytesPerSec = __in.readInt();
		short frameSize = __in.readShort();
		short bitsPerSample = __in.readShort();
		
		// Read extra fmt bytes for IMA ADPCM, then skip any remaining extra
		// 'fmt' bytes
		short ByteExtraData = 0;
		short ExtraData = 0;
		String factHeader = "";
		int SubChunk2Size = 0;
		int numOfSamples = 0;
		String dataHeader = "";
		int dataLen = 0;

		// If the 'junk' header is present, we will read these
		String junkHeader = null;
		int junkSize = 0;

		if (audioFormat == __WavTools__.FORMAT_IMA_ADPCM && chunkSize > 16)
		{
			ByteExtraData = __in.readShort();
			ExtraData = __in.readShort();
			int extraFmtRemaining = chunkSize - 20;
			if(extraFmtRemaining > 0)
				__in.skip(extraFmtRemaining); 
		}
		else if(chunkSize > 16)
		{
			int extraFmtBytes = chunkSize - 16;
			__in.skip(extraFmtBytes);
		}

		while(true)
		{
			String chunkID = __WavTools__.__readString(__in, 4);
			int chunkSz = __in.readInt();

			if(chunkID.equals("data"))
			{
				dataHeader = chunkID;
				dataLen = chunkSz;
				break;
			}
			else if(chunkID.equals("fact"))
			{
				factHeader = chunkID;
				SubChunk2Size = chunkSz;
				if(chunkSz >= 4)
				{
					numOfSamples = __in.readInt();

					// NOTE: For IMA ADPCM, it could be something with
					// audacity/ocenaudio, but mono streams end up with
					// numOfSamples being the exact same number as a stereo
					// stream (test being the same file, but one is downmixed
					// to mono), which makes no sense, as mono is supposed to 
					// have the exact half of stereo's samples.
					if(audioChannels == 1)
						numOfSamples /= 2;

					int factRemaining = chunkSz - 4;
					if(factRemaining > 0)
						__in.skip(factRemaining);
					
				}
				else
					__in.skip(chunkSz);
				
			}
			else if(chunkID.equals("junk"))
			{
				junkHeader = chunkID;
				junkSize = __in.readInt();
				__in.skip(junkSize);

				// Skip one extra byte if junkSize is odd
				if(junkSize % 2 == 1)
					__in.skip(1);
			}
			else
				__in.skip(chunkSz);
			
		}

		if (Debugging.VERBOSE)
		{
			Debugging.debugNote("%s WAV HEADER_START",
				audioFormat == __WavTools__.FORMAT_IMA_ADPCM ? "IMA ADPCM" :
					"PCM");

			Debugging.debugNote("%s", riff);
			Debugging.debugNote("FileSize:%d", dataSize);
			Debugging.debugNote("Format: %s", format);

			Debugging.debugNote("---'%s' header---", fmt);
			Debugging.debugNote(
				"Header ChunkSize:%d", chunkSize);

			Debugging.debugNote("AudioFormat: %s",
				Integer.toString(audioFormat));

			Debugging.debugNote("AudioChannels:%s",
				Integer.toString(audioChannels));

			Debugging.debugNote("SampleRate:%d", sampleRate);
				
			Debugging.debugNote("BytesPerSec:%d", bytesPerSec);

			Debugging.debugNote(
				"FrameSize:%s", Integer.toString(frameSize));

			Debugging.debugNote("BitsPerSample:%s",
				Integer.toString(bitsPerSample));
			
			if (audioFormat == __WavTools__.FORMAT_IMA_ADPCM) 
			{
				Debugging.debugNote("ByteExtraData:%s",
					Integer.toString(ByteExtraData));

				Debugging.debugNote(
					"ExtraData:%s", Integer.toString(ExtraData));

				Debugging.debugNote(
					"---'%s' header---", factHeader);

				Debugging.debugNote(
					"SubChunk2Size:%d", SubChunk2Size);

				Debugging.debugNote(
					"numOfSamples:%d", numOfSamples);
			}

			if(junkHeader != null)
			{
				Debugging.debugNote("---'%s' header---", junkHeader);
				Debugging.debugNote("JunkSize:%d", junkSize);
			}
			
			Debugging.debugNote("---'%s' header---", dataHeader);
			Debugging.debugNote("SampleDataLength:%d", dataLen);

			Debugging.debugNote("%sWAV HEADER_END",
				audioFormat == __WavTools__.FORMAT_IMA_ADPCM ? "IMA ADPCM" :
					"PCM");
		}
		
		// Return everything we need to write into a scritchaudio buffer
		return new int[] {audioFormat, sampleRate, audioChannels, frameSize,
			numOfSamples == 0 ? dataLen : numOfSamples,
			bitsPerSample, bytesPerSec};
	}
	
	/**
	 * Return a String containing 'n' Characters of ASCII/ISO-8859-1 text
	 * from the input stream.
	 * 
	 * @param __in Data stream to read from.
	 * @param __n The amount of characters/bytes to read
	 * @return The resultant string.
	 * @throws IOException If data could not be read.
	 * @since 2025/13/31
	 */
	private static String __readString(InputStream __in, int __n)
		throws IOException
	{
		byte[] chars = new byte[__n];
		int pos = 0;
		while(pos < __n) 
		{
			int read = __in.read(chars, pos, __n - pos);
			if (read < 0)
				throw new EOFException();
			pos += read;
		}
		return new String(chars, "ISO-8859-1");
	}

	/**
	 * Writes Little-Endian 32-bit data into the specified buffer.
	 * 
	 * @param __buffer The buffer to write data
	 * @param __index The buffer index to write into
	 * @param __value The value to write
	 * @since 2025/13/31
	 */
	private static void __writeIntLE(byte[] __buffer, int __index, int __value)
	{
		__buffer[__index] = (byte) (__value & 0xFF);
		__buffer[__index + 1] = (byte) ((__value >> 8) & 0xFF);
		__buffer[__index + 2] = (byte) ((__value >> 16) & 0xFF);
		__buffer[__index + 3] = (byte) ((__value >> 24) & 0xFF);
	}

	/**
	 * Writes Big-Endian 32-bit data into the specified buffer. Used only for
	 * the wav header chunk delimiters, which are strings.
	 * 
	 * @param __buffer The buffer to write data
	 * @param __index The buffer index to write into
	 * @param __value The value to write
	 * @since 2025/13/31
	 */
	private static void __writeIntBE(byte[] __buffer, int __index, int __value)
	{
		__buffer[__index] = (byte) ((__value >> 24) & 0xFF);
		__buffer[__index + 1] = (byte) ((__value >> 16) & 0xFF);
		__buffer[__index + 2] = (byte) ((__value >> 8) & 0xFF);
		__buffer[__index + 3] = (byte) (__value & 0xFF);
	}

	/**
	 * Writes Little-Endian 16-bit data into the specified buffer.
	 * 
	 * @param __buffer The buffer to write data
	 * @param __index The buffer index to write into
	 * @param __value The value to write
	 * @since 2025/13/31
	 */
	private static void __writeShort(byte[] __buffer, int __index, int __value)
	{
		__buffer[__index] = (byte) (__value & 0xFF);
		__buffer[__index + 1] = (byte) ((__value >> 8) & 0xFF);
	}
}
