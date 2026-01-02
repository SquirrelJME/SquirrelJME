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
import cc.squirreljme.jvm.mle.constants.AudioStreamFormat;
import cc.squirreljme.jvm.mle.callbacks.AudioStreamRenderer;
import cc.squirreljme.jvm.mle.exceptions.MLECallError;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.cldc.util.StreamUtils;
import cc.squirreljme.runtime.gcf.InputStreamConnection;
import cc.squirreljme.runtime.media.AbstractPlayer;
import cc.squirreljme.runtime.media.AbstractVolumeControl;
import java.io.DataInputStream;
import net.multiphasicapps.io.DataEndianess;
import net.multiphasicapps.io.ExtendedDataInputStream;
import org.jetbrains.annotations.NotNull;
import java.io.InputStream;
import java.io.IOException;
import javax.microedition.media.MediaException;

@SquirrelJMEVendorApi
public class WavPlayer 
	extends AbstractPlayer
	implements AudioStreamRenderer
{
	/** The audio connection. */
	@SquirrelJMEVendorApi
	private volatile AudioConnectionBracket _connection;

	/** The audio stream used. */
	@SquirrelJMEVendorApi
	private volatile AudioStreamBracket _stream;

	/** The decoder instance for compressed PCM wav data */
	@SquirrelJMEVendorApi
	private WavDecoder _decoder;

	/** The un-realized input stream. */
	@SquirrelJMEVendorApi
	private volatile InputStreamConnection _unrealizedIn;

	/** The sample rate of the wav data in use */
	@SquirrelJMEVendorApi
	private int _wavSampleRate;

	/** The amount of audio channels of the wav data in use */
	@SquirrelJMEVendorApi
	private byte _wavChannels;

	/** The underlying format of the wav data in use */
	@SquirrelJMEVendorApi
	private int _wavFormat;

	/** The amount samples in each frame of the wav data */
	@SquirrelJMEVendorApi
	private int _wavFrameSize;

	/** Bit-depth of samples in the wav data in use */
	@SquirrelJMEVendorApi
	private byte _wavBits;

	/** How many valid audio samples the wav data in use has */
	@SquirrelJMEVendorApi
	private int _wavSampleLen;

	/** Current sample marker for wav audio rendering. */
	@SquirrelJMEVendorApi
	private int _curSample;

	/** Volume multiplier for rendered samples (0-100%). */
	@SquirrelJMEVendorApi
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
	@SquirrelJMEVendorApi
	public WavPlayer(@NotNull InputStreamConnection __in)
		throws NullPointerException
	{
		super("audio/wav");

		if (__in == null)
			throw new NullPointerException("NARG");
				
		// For later realization
		this._unrealizedIn = __in;

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
		int curSample = this._curSample;
		int sampleLen = this._wavSampleLen;
		byte volMult = this._volumeMult;
		long wavSample;
		int format = this._wavFormat;
		byte[] wavData = this._wavData;

		if (format == __WavTools__.FORMAT_ALAW_WAV)
		{
			short[] sbuf = (short[])__buf;
			curSample += this._decoder.decodeALaw(this._wavData, curSample,
				sampleLen, sbuf,  __off, __len, this._volumeMult);
		}
		else if (format == __WavTools__.FORMAT_MULAW_WAV)
		{
			short[] sbuf = (short[])__buf;
			curSample += this._decoder.decodeULaw(this._wavData, curSample,
				sampleLen, sbuf, __off, __len, this._volumeMult);
		}
		else if (format == __WavTools__.FORMAT_IMA_ADPCM)
		{
			short[] sbuf = (short[])__buf;
			curSample += this._decoder.decodeIMAADPCM(this._wavData, sampleLen,
				curSample, this._wavChannels,this._wavFrameSize, sbuf, __off,
				__len, this._volumeMult);
		}

		// Else, it's a bog-standard PCM wav, we just need to move it to output
		else
		{
			int toCopy = Math.min(__len, sampleLen - curSample);
			switch (this._wavBits)
			{
				case 8:
					byte[] bbuf = (byte[])__buf;

					for (int i = 0; i < toCopy; i++) 
					{
						// We have each sample taking two bytes of data here
						wavSample = (wavData[curSample++] & 0xFF) * volMult /
							100;

						bbuf[__off + i] = (byte) (wavSample & 0xFF);
					}
					break;

				case 16:
					short[] sbuf = (short[])__buf;
					for (int i = 0; i < toCopy; i++) 
					{
						// We have each sample taking two bytes of data here
						wavSample = (((wavData[curSample++] & 0xFF) << 8) |
							((wavData[curSample++] & 0xFF))) * volMult / 100;

						sbuf[__off + i] = (short) wavSample;
					}
					break;

				case 32:
					if (__format == AudioStreamFormat.INT_S32)
					{
						int[] ibuf = (int[]) __buf;

						for (int i = 0; i < toCopy; i++) 
						{
							// We have each sample taking 4 bytes of data here
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
		if ((curSample == sampleLen))
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
		synchronized (this)
		{
			// Create the native audio stream
			AudioStreamBracket stream = null;
			try
			{
				stream = AudioStreamShelf.stream();
				this._stream = stream;
			}
			catch (MLECallError __e)
			{
				__e.printStackTrace();

				MediaException mex = new MediaException(__e.getMessage());
				mex.initCause(__e);
				throw mex;
			}
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

					if (this._wavFormat == __WavTools__.FORMAT_PCM_WAV)
						this._wavBits = (byte) wavProps[5];

					// These must be converted to 16-bit PCM when rendering
					else if (this._wavFormat == __WavTools__.FORMAT_ALAW_WAV ||
						this._wavFormat == __WavTools__.FORMAT_MULAW_WAV ||
						this._wavFormat == __WavTools__.FORMAT_IMA_ADPCM)
					{
						this._wavBits = (byte) 16;
						this._decoder = new WavDecoder();
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
	 *
	 * @return
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
		InputStreamConnection unrealizedIn = this._unrealizedIn;
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
		// TODO: This is for compatibility with old code, for variable width
		// TODO: formats this should be handled here just for those
		this.clockSet(__micros);
	}

	/**
	 * {@inheritDoc}
	 * @since 2025/12/25
	 */
	@Override
	protected long clockGet()
	{
		// Convert the current sample marker into its microsecond equivalent.
		return (long) (1_000_000L * ((float) this._curSample /
			this._wavSampleRate));
	}

	/**
	 * {@inheritDoc}
	 * @since 2025/12/25
	 */
	@Override
	protected void clockSet(long __micros)
		throws MediaException
	{
		// This one doesn't actually operate in microsecond intervals, instead,
		// it sets the current sample marker to the sample that's closest to
		// the specified microsecond position.
		int curSample = (int) ((__micros / 1_000_000f) *
			this._wavSampleRate);

		// Due to how IMA ADPCM works, we should reset it to the start,
		// then fast-forward it to the 'curSample' value set here.
		//
		// We need to pass the data array because IMA ADPCM gets predictor
		// and table indices from the samples it decodes, so we effectively
		// need to decode silently until we reach the stopping point.
		if (this._wavFormat == __WavTools__.FORMAT_IMA_ADPCM)
			this._curSample = this._decoder.resetADPCM(curSample,
				this._wavData, this._wavChannels, this._wavFrameSize);
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
		// Only variable width formats require fast-forward based setMediaTime
		int format = this._wavFormat;
		return format == __WavTools__.FORMAT_ALAW_WAV ||
			format == __WavTools__.FORMAT_MULAW_WAV ||
			format == __WavTools__.FORMAT_IMA_ADPCM;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/12/25
	 */
	@Override
	protected long determineDuration()
		throws MediaException
	{
		// Duration is just the amount of samples divided by the sample rate
		return (long) (1_000_000L * ((float) this._wavSampleLen /
			this._wavSampleRate));
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
