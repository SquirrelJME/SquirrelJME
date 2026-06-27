// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.media.wav;

import cc.squirreljme.jvm.mle.AudioStreamShelf;
import cc.squirreljme.jvm.mle.brackets.AudioConnectionBracket;
import cc.squirreljme.jvm.mle.brackets.AudioStreamBracket;
import cc.squirreljme.jvm.mle.constants.AudioStreamChannels;
import cc.squirreljme.jvm.mle.constants.AudioStreamFormat;
import cc.squirreljme.jvm.mle.callbacks.AudioStreamRenderer;
import cc.squirreljme.jvm.mle.constants.AudioStreamRate;
import cc.squirreljme.jvm.mle.exceptions.MLECallError;
import cc.squirreljme.runtime.cldc.annotation.KeepWhenCompacting;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.cldc.util.StreamUtils;
import cc.squirreljme.runtime.media.AbstractPlayer;
import cc.squirreljme.runtime.media.AbstractVolumeControl;
import cc.squirreljme.runtime.media.pcm.*;
import javax.microedition.io.InputConnection;
import net.multiphasicapps.io.DataEndianess;
import net.multiphasicapps.io.ExtendedDataInputStream;
import org.jetbrains.annotations.NotNull;
import java.io.InputStream;
import java.io.IOException;
import javax.microedition.media.MediaException;

@KeepWhenCompacting
public class WavPlayer 
	extends AbstractPlayer
	implements AudioStreamRenderer
{
	/** The audio connection. */
	@KeepWhenCompacting
	private volatile AudioConnectionBracket _connection;

	/** The audio stream used. */
	@KeepWhenCompacting
	private volatile AudioStreamBracket _stream;

	/** The decoder instance for compressed PCM wav data */
	@KeepWhenCompacting
	private PCMDecoder _decoder;

	/** The un-realized input stream. */
	@KeepWhenCompacting
	private volatile InputConnection _unrealizedIn;

	/** The sample rate of the wav data in use */
	@KeepWhenCompacting
	private int _wavSampleRate;

	/** The amount of audio channels of the wav data in use */
	@KeepWhenCompacting
	private byte _wavChannels;

	/** The underlying format of the wav data in use */
	@KeepWhenCompacting
	private int _wavFormat;

	/** The amount samples in each frame of the wav data */
	@KeepWhenCompacting
	private int _wavFrameSize;

	/** Bit-depth of samples in the wav data in use */
	@KeepWhenCompacting
	private byte _wavBits;

	/** How many valid audio samples the wav data in use has */
	@KeepWhenCompacting
	private int _wavSampleLen;

	/** How many bytes are used for one second of playback */
	@KeepWhenCompacting
	private int _wavBytesPerSec;

	/** Current sample marker for wav audio rendering. */
	@KeepWhenCompacting
	private int _curSample;

	/** Volume multiplier for rendered samples (0-100%). */
	@KeepWhenCompacting
	private byte _volumeMult;

	/** The wav sample data array. */
	private volatile byte[] _wavData;

	/**
	 * Creates a new WavPlayer instance from the received {@link InputStream}.
	 * 
	 * @param __in The wav input data.
	 * @throws NullPointerException If {@code __in} is null.
	 * @since 2025/12/25
	 */
	@KeepWhenCompacting
	public WavPlayer(@NotNull InputConnection __in)
		throws NullPointerException
	{
		super("audio/wav");

		if (__in == null)
			throw new NullPointerException("NARG");
				
		// For later realization
		this._unrealizedIn = __in;

		// Start at max volume
		this._volumeMult = 100;

		// Register volume control so we can properly set the gain
		this.registerControl(new AbstractVolumeControl(this));
	}

	/**
	 * {@inheritDoc}
	 * @since 2025/12/25
	 */
	@Override
	public void render(int __format, int __rate, int __channels, Object __buf,
		int __off, int __len)
	{
		// Nothing to write into yet. Return early.
		if (__buf == null || __len == 0)
			return;

		try
		{
			int curSample = this._curSample;
			int sampleLen = this._wavSampleLen;
			byte volMult = this._volumeMult;
			long wavSample;
			int format = this._wavFormat;
			byte[] wavData = this._wavData;

			// ADPCM has 2 samples per byte, which means we pass sampleLen / 2
			if (format == __WavTools__.FORMAT_IMA_ADPCM)
				sampleLen /= 2;

			// All of these end up converted to a 16-bit PCM WAV.
			if (format == __WavTools__.FORMAT_ALAW_WAV ||
				format == __WavTools__.FORMAT_MULAW_WAV ||
				format == __WavTools__.FORMAT_IMA_ADPCM)
			{
				short[] sbuf = (short[])__buf;
				curSample += this._decoder.decode(this._wavData, sampleLen,
					curSample, this._wavChannels,this._wavFrameSize, sbuf,
					__len, __off, this._volumeMult);
			}

			// Else, it's a bog-standard PCM wav, we just need to move it to
			// output
			else
			{
				int toCopy = Math.min(__len, sampleLen - curSample);
				switch (this._wavBits)
				{
					case 8:
						byte[] bbuf = (byte[])__buf;

						for (int i = 0; i < toCopy; i++)
						{
							// We have each sample taking two bytes here
							wavSample = (wavData[curSample++] & 0xFF) * volMult
								/ 100;

							bbuf[__off + i] = (byte) (wavSample & 0xFF);
						}
						break;

					case 16:
						short[] sbuf = (short[])__buf;
						for (int i = 0; i < toCopy; i++)
						{
							// We have each sample taking two bytes here
							wavSample = (((wavData[curSample++] & 0xFF) << 8) |
								((wavData[curSample++] & 0xFF))) * volMult /
								100;

							sbuf[__off + i] = (short) wavSample;
						}
						break;

					case 32:
						if (__format == AudioStreamFormat.INT_S32)
						{
							int[] ibuf = (int[]) __buf;

							for (int i = 0; i < toCopy; i++)
							{
								// We have each sample taking 4 bytes here
								wavSample = (((wavData[curSample++] & 0xFF)
									<< 24) | ((wavData[curSample++] & 0xFF)
									<< 16) | ((wavData[curSample++] & 0xFF)
									<< 8) | (wavData[curSample++] & 0xFF)) *
									volMult / 100;

								ibuf[__off + i] = (int) wavSample;
							}
						}
						else if (__format == AudioStreamFormat.FLOAT_F32)
						{
							float[] fbuf = (float[]) __buf;

							for (int i = 0; i < toCopy; i++)
							{
								wavSample = (((wavData[curSample++] & 0xFF)
									<< 24) | ((wavData[curSample++] & 0xFF)
									<< 16) | ((wavData[curSample++] & 0xFF)
									<< 8) | (wavData[curSample++] & 0xFF)) *
									volMult / 100;

								fbuf[__off + i] = (wavSample /
									(float) Integer.MAX_VALUE);
							}
						}
						break;
				}
			}
			this._curSample = curSample;

			// Check if we reached the end of the wav file.
			if ((curSample >= sampleLen))
			{
				try
				{
					if (super.decrementLoop())
						this.stopViaMedia();
					else
						this.loopViaMedia();
				}
				catch (MediaException __e)
				{
					__e.printStackTrace();
				}
			}
		}

		// If we're accessing out bounds, it means the header's sample length
		// does not represent the actual amount of samples in the data array,
		// as such, we must stop (or loop) when it happens.
		catch (ArrayIndexOutOfBoundsException e)
		{
			try
			{
				if (super.decrementLoop())
					this.stopViaMedia();
				else
					this.loopViaMedia();
			}
			catch (MediaException __e)
			{
				__e.printStackTrace();
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * @since 2025/12/25
	 */
	@Override
	protected void becomingPrefetched()
		throws MediaException
	{
		int format = this._wavFormat;
		
		if (format == __WavTools__.FORMAT_PCM_WAV ||
			format == __WavTools__.FORMAT_FLOAT_WAV)
			return;
		else if (format == __WavTools__.FORMAT_ALAW_WAV)
			this._decoder = new ALawDecoder();
		else if (format == __WavTools__.FORMAT_MULAW_WAV)
			this._decoder = new MULawDecoder();
		else if (format == __WavTools__.FORMAT_IMA_ADPCM)
			this._decoder = new IMAADPCMDecoder();
		// {@squirreljme.error EA0u Unsupported Media Format
		else
			throw new MediaException("EA0u");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/12/25
	 */
	@Override
	protected void becomingPrimed()
		throws MediaException
	{
		synchronized (this)
		{
			// If the native stream already exists, we can return right away
			if (this._stream != null)
				return;

			// Create the native audio stream
			this._stream = AbstractPlayer.stream(AudioStreamFormat.AUTOMATIC,
				AudioStreamRate.AUTOMATIC, AudioStreamChannels.MONO);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/12/25
	 */
	@Override
	protected void becomingRealized()
		throws MediaException
	{
		try
		{
			// If data is already here, stop
			byte[] data = this._wavData;
			if (data != null)
				return;
			
			// Otherwise 
			synchronized (this)
			{
				// Double check?
				data = this._wavData;
				if (data != null)
					return;
				
				// Data is already destroyed?
				if (this._unrealizedIn == null)
					throw new MediaException("GONE");
				
				// Read in the data and drop the unrealized stream
				try (ExtendedDataInputStream in = new ExtendedDataInputStream(
					this._unrealizedIn.openDataInputStream(),
					DataEndianess.LITTLE))
				{
					// readHeader will give us all the data we need to render,
					// while also leaving the inputStream right at the start
					// of the actual sample data
					int[] wavProps = __WavTools__.readHeader(in);

					this._wavFormat = wavProps[0];
					this._wavSampleRate = wavProps[1];
					this._wavChannels = (byte) wavProps[2];
					this._wavFrameSize = wavProps[3];
					this._wavSampleLen = wavProps[4];
					this._wavBytesPerSec = wavProps[6];

					if (this._wavFormat == __WavTools__.FORMAT_PCM_WAV)
						this._wavBits = (byte) wavProps[5];

					// These must be converted to 16-bit PCM when rendering
					else if (this._wavFormat == __WavTools__.FORMAT_ALAW_WAV ||
						this._wavFormat == __WavTools__.FORMAT_MULAW_WAV ||
						this._wavFormat == __WavTools__.FORMAT_IMA_ADPCM)
					{
						this._wavBits = (byte) 16;
					}

					// Now we're able to get only the sample data
					this._wavData = StreamUtils.readAll(in);
				}
			}
		}
		catch (IOException e)
		{
			// {@squirreljme.error EA0f Failed to realize Wav data.}
			MediaException toss = new MediaException("EA0f");
			toss.initCause(e);
			throw toss;
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2026/01/08
	 */
	@Override
	protected void becomingSolvent()
		throws MediaException
	{
		synchronized (this)
		{
			// Close the native audio stream, we won't be using it
			AudioStreamBracket stream = this._stream;
			this._stream = null;
			
			if (stream != null)
				AbstractPlayer.streamDisconnect(stream, false);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/12/25
	 */
	@Override
	protected boolean becomingStarted()
		throws MediaException
	{
		synchronized (this)
		{
			// Start rendering the stream, which will cause the audio to be
			// played until an end of media event is received, or the
			// application requests it to stop.
			try
			{
				int streamOutFmt = 0;
				
				switch(this._wavBits)
				{
					case 8:
						streamOutFmt = AudioStreamFormat.BYTE_U8;
						break;

						// Anything above 8-bit depth is signed.
					case 16:
						streamOutFmt = AudioStreamFormat.SHORT_S16;
						break;

						// These do exist, but Java has no support for any of
						// them. Kept for completeness sake, as vendors out
						// there might use 24-bit WAV.
					case 24:
						throw Debugging.todo("24-bit WAV is unsupported");

					case 32:
						if (this._wavFormat == __WavTools__.FORMAT_FLOAT_WAV)
							streamOutFmt = AudioStreamFormat.FLOAT_F32;
						else
							streamOutFmt = AudioStreamFormat.INT_S32;
				}

				this._connection =
					AudioStreamShelf.attach(this._stream, this,
						streamOutFmt,
						this._wavSampleRate,
						this._wavChannels);
			}
			catch (MLECallError __e)
			{
				__e.printStackTrace();

				MediaException mex = new MediaException(__e.getMessage());
				mex.initCause(__e);
				throw mex;
			}
		}
		return true;
	}

	/**
	 * {@inheritDoc}
	 * @since 2025/12/25
	 */
	@Override
	protected void becomingStopped()
		throws MediaException
	{
		synchronized (this)
		{
			try
			{
				AudioConnectionBracket connection = this._connection;
				if (connection != null)
				{
					this._connection = null;
					AudioStreamShelf.disconnect(connection);
				}
			}
			catch (MLECallError __e)
			{
				__e.printStackTrace();
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * @since 2025/12/28
	 */
	@Override
	public void becomingDeallocated()
		throws MediaException
	{
		this._decoder = null;
		this._stream = null;
		this._wavData = null;
		
		// Close the input connection, if it was never read in
		InputConnection unrealizedIn = this._unrealizedIn;
		if (unrealizedIn != null)
		{
			this._unrealizedIn = null;
			AbstractPlayer.closeConnection(unrealizedIn);
		}
	}
	
	
	/**
	 * {@inheritDoc}
	 * @since 2026/01/02
	 */
	@Override
	protected void clockFastForward(long __micros)
		throws MediaException
	{
		// This doesn't actually operate in microsecond intervals, instead, we
		// set the current sample marker to the sample that's closest to the
		// specified microsecond position.
		int curSample = (int) ((__micros / 1_000_000f) *
			this._wavSampleRate * this._wavChannels);
		int wavFormat = this._wavFormat;
		int maxSample = this._wavSampleLen;

		if (curSample < 0)
			this._curSample = 0;
		else if (curSample > maxSample)
			this._curSample = maxSample / 2;

		// The only fast-forwarding format supported right now is IMA ADPCM.
		//
		// We need to pass the data array because IMA ADPCM gets predictor
		// and table indices from the samples it decodes, so we effectively
		// need to decode silently until we reach the stopping point. This will
		// probably be required for fast-forwarding other formats as well.
		else
			if (wavFormat == __WavTools__.FORMAT_IMA_ADPCM)
				this._curSample = this._decoder.reset(curSample, this._wavData,
				this._wavChannels, this._wavFrameSize);
	}

	/**
	 * {@inheritDoc}
	 * @since 2025/12/25
	 */
	@Override
	protected long clockGet()
	{
		int curSample = this._curSample;

		// For IMA ADPCM (and future ADPCM formats), each index of the byte
		// data actually holds two samples. Thus we must multiply the current
		// sample marker by 2 when returning current media time.
		if (this._wavFormat == __WavTools__.FORMAT_IMA_ADPCM)
			curSample *= 2;

		return (long) ((curSample * 1_000_000L) / this._wavBytesPerSec);
	}

	/**
	 * {@inheritDoc}
	 * @since 2025/12/25
	 */
	@Override
	protected void clockSet(long __micros)
		throws MediaException
	{
		// As in clockFastForward(long), we set time based on closest sample.
		int curSample = (int) ((__micros * this._wavSampleRate *
			this._wavChannels) / 1_000_000);
		int maxSample = this._wavSampleLen;

		if (curSample < 0)
			this._curSample = 0;
		else if (curSample > maxSample)
			this._curSample = maxSample;
 		else
 			this._curSample = curSample;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2026/01/02
	 */
	@Override
	protected boolean resetFastForward()
	{
		// IMA ADPCM is the only format that currently requires fast-forward
		return this._wavFormat == __WavTools__.FORMAT_IMA_ADPCM;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/12/25
	 */
	@Override
	protected long determineDuration()
		throws MediaException
	{
		int sampleLen = this._wavSampleLen;

		// Duration is just the amount of samples divided by the sample rate
		return (long) ((sampleLen * 1_000_000L) / this._wavBytesPerSec);
	}

	/**
	 * {@inheritDoc}
	 * @since 2025/12/25
	 */
	@Override
	protected void useVolume(int __volume)
	{
		this._volumeMult = (byte) __volume;
	}
}
