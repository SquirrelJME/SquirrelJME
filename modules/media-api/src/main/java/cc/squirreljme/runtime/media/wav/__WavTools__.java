// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.media.wav;

import cc.squirreljme.jvm.mle.constants.AudioStreamFormat;
import cc.squirreljme.jvm.mle.constants.AudioStreamRate;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.cldc.util.StreamUtils;
import cc.squirreljme.runtime.gcf.InputStreamConnection;
import java.io.InputStream;
import java.io.IOException;
import org.intellij.lang.annotations.MagicConstant;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

@SquirrelJMEVendorApi
class __WavTools__ 
{
	/** The standard size of a PCM WAV's header. */
	private static final byte PCM_HEADER_SIZE
		= 44;

	/** The standard size of an ADPCM WAV's header. */
	private static final byte ADPCM_HEADER_SIZE
		= 60;

	/** Magic number for the header's 'RIFF' string. */
	private static final int RIFF_MAGIC
		= 0x52494646;          

	/** Magic number for the header's 'WAVE' string. */
	private static final int WAVE_MAGIC
		= 0x57415645;

	/** Magic number for the header's 'fmt ' chunk delimiter. */
	private static final int FMT_MAGIC
		= 0x666d7420;

	/** Magic number for the header's 'data' chunk delimiter. */
	private static final int DATA_MAGIC
		= 0x64617461;

	/** Header format value that indicates a standard PCM WAV. */
	public static final int FORMAT_PCM_WAV
		= 0x1;

	/** Header format value that indicates an IEEE-spec Float PCM WAV. */
	public static final int FORMAT_FLOAT_WAV
		= 0x3;

	/** Header format value that indicates an 8-bit ITU-T G.711 A-law WAV. */
	public static final int FORMAT_ALAW_WAV
		= 0x6;

	/** Header format value that indicates an 8-bit ITU-T G.711 u-law WAV. */
	public static final int FORMAT_MULAW_WAV
		= 0x7;

	/** Header format value that indicates an IMA ADPCM WAV. */
	public static final int FORMAT_IMA_ADPCM
		= 0x11;

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
	 */
	public static void buildPCMWavHeader(byte[] __buffer,
		byte __numChannels, int __sampleRate,
		byte __numBits, int __sampleDataLength)
	{ 
		short audioFormat = __WavTools__.FORMAT_PCM_WAV;
		
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
		__writeIntBE(__buffer, 0, __WavTools__.RIFF_MAGIC);
		__writeIntLE(__buffer, 4, __buffer.length - 8);
		__writeIntBE(__buffer, 8, __WavTools__.WAVE_MAGIC);
		__writeIntBE(__buffer, 12, __WavTools__.FMT_MAGIC);
		__writeIntLE(__buffer, 16, subChunkSize);
		__writeShort(__buffer, 20, audioFormat);
		__writeShort(__buffer, 22, __numChannels);
		__writeIntLE(__buffer, 24, __sampleRate);
		__writeIntLE(__buffer, 28, bytesPerSec);
		__writeShort(__buffer, 32, frameSize);
		__writeShort(__buffer, 34, __numBits);
		__writeIntBE(__buffer, 36, __WavTools__.DATA_MAGIC);
		__writeIntLE(__buffer, 40, __sampleDataLength);
	}

	/**
	 * Reads a WAV file's header in order to get its properties for playback.
	 * 
	 * @param __in The input stream to read from
	 * @return An array containing relevant wav data to begin processing, such
	 * as sample rate, bits per sample, amount of channels, and so on.
	 * @throws IOException If data could not be read.
	 */
	public static int[] readHeader(InputStream __in)
		throws IOException
	{
		
		// The header of a WAV (RIFF) file is 44 bytes long and has the
		// following format:

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
		//	CHAR[4] "data" header
		//	UINT32 Length of sample data.
		//	<Sample data>

		// IMA ADPCM introduces the following before "data" header, and after
		// BitsPerSample:

		// UINT16 ByteExtraData
		// UINT16 ExtraData
		// CHAR[4] "fact" header
		// UINT32 SubChunk2Size
		// UINT32 NumOfSamples
		
		String riff = __readString(__in, 4);
		int dataSize = __readInt(__in);  
		String format = __readString(__in, 4);
		String fmt = __readString(__in, 4);
		int chunkSize = __readInt(__in);
		short audioFormat = __readShort(__in);
		short audioChannels = __readShort(__in);
		int sampleRate = __readInt(__in);
		int bytesPerSec = __readInt(__in);
		short frameSize = __readShort(__in);
		short bitsPerSample = __readShort(__in);
		
		// These are conditionally read depending on IMA ADPCM
		short ByteExtraData = 0;
		short ExtraData = 0;
		String factHeader = "";
		int SubChunk2Size = 0;
		int numOfSamples = 0;

		if(audioFormat == __WavTools__.FORMAT_IMA_ADPCM) 
		{
			ByteExtraData = __readShort(__in);
			ExtraData = __readShort(__in);
			factHeader = __readString(__in, 4);
			SubChunk2Size = __readInt(__in);
			numOfSamples = __readInt(__in);
		}

		String dataHeader = __readString(__in, 4);

		int dataLen = __readInt(__in);

		if (Debugging.VERBOSE)
		{
			Debugging.debugNote((audioFormat == __WavTools__.FORMAT_IMA_ADPCM ?
				"IMA ADPCM" : "PCM") + " WAV HEADER_START");

			Debugging.debugNote(riff);
			Debugging.debugNote("FileSize:" + dataSize);
			Debugging.debugNote("Format: " + format);

			Debugging.debugNote("---'" + fmt + "' header---");
			Debugging.debugNote("Header ChunkSize:" +
				Integer.toString(chunkSize));

			Debugging.debugNote("AudioFormat: " +
				Integer.toString(audioFormat));

			Debugging.debugNote("AudioChannels:" +
				Integer.toString(audioChannels));

			Debugging.debugNote("SampleRate:" +
				Integer.toString(sampleRate));
				
			Debugging.debugNote("BytesPerSec:" +
				Integer.toString(bytesPerSec));

			Debugging.debugNote("FrameSize:" +
				Integer.toString(frameSize));

			Debugging.debugNote("BitsPerSample:" +
				Integer.toString(bitsPerSample));
			
			if(audioFormat == __WavTools__.FORMAT_IMA_ADPCM) 
			{
				Debugging.debugNote("ByteExtraData:" +
					Integer.toString(ByteExtraData));

				Debugging.debugNote("ExtraData:" +
					Integer.toString(ExtraData));

				Debugging.debugNote("---'" + factHeader +"' header---");

				Debugging.debugNote("SubChunk2Size:" +
					Integer.toString(SubChunk2Size));

				Debugging.debugNote("numOfSamples:" +
					Integer.toString(numOfSamples));
			}
			
			Debugging.debugNote("---'" + dataHeader +"' header---");
			Debugging.debugNote("SampleDataLength:" +
				Integer.toString(dataLen));

			Debugging.debugNote((audioFormat == __WavTools__.FORMAT_IMA_ADPCM ?
				"IMA ADPCM" : "PCM") + "WAV HEADER_END");
		}
		
		// Return everything we need to write into a scritchaudio buffer
		return new int[] {audioFormat, sampleRate, audioChannels, frameSize,
			dataLen, bitsPerSample};
	}

	/**
	 * Read a 16-bit little-endian unsigned integer from input.
	 * 
	 * @param __in Data stream to read from.
	 * @return
	 * @throws IOException If data could not be read.
	 */
	private static short __readShort(InputStream __in)
		throws IOException
	{ 
		return (short) ((__in.read() & 0xFF) | (( __in.read() & 0xFF) << 8));
	}

	/**
	 * Read a 32-bit little-endian signed integer from input.
	 * 
	 * @param __in Data stream to read from.
	 * @return
	 * @throws IOException If data could not be read.
	 */
	private static int __readInt(InputStream __in)
		throws IOException
	{
		return (__in.read() & 0xFF) | (( __in.read() & 0xFF) << 8)
			| ((__in.read() & 0xFF) << 16) | ((__in.read() & 0xFF) << 24);
	}

	/**
	 * Return a String containing 'n' Characters of ASCII/ISO-8859-1 text
	 * from the input stream.
	 * 
	 * @param __in Data stream to read from.
	 * @param __n The amount of characters/bytes to read
	 * @return
	 * @throws IOException If data could not be read.
	 */
	private static String __readString(InputStream __in, int __n)
		throws IOException
	{
		byte[] chars = new byte[__n];
		int pos = 0;
		while(pos < __n) 
		{
			int read = __in.read(chars, pos, __n - pos);
			if(read < 0)
				throw new java.io.EOFException();
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
	 */
	private static void __writeShort(byte[] __buffer, int __index, int __value)
	{
		__buffer[__index] = (byte) (__value & 0xFF);
		__buffer[__index + 1] = (byte) ((__value >> 8) & 0xFF);
	}
}
