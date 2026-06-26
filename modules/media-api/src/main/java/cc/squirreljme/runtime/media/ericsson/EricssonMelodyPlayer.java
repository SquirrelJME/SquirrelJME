// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.media.ericsson;

import cc.squirreljme.jvm.mle.AudioStreamShelf;
import cc.squirreljme.jvm.mle.brackets.AudioConnectionBracket;
import cc.squirreljme.jvm.mle.brackets.AudioStreamBracket;
import cc.squirreljme.jvm.mle.constants.AudioStreamChannels;
import cc.squirreljme.jvm.mle.constants.AudioStreamFormat;
import cc.squirreljme.jvm.mle.callbacks.AudioStreamRenderer;
import cc.squirreljme.jvm.mle.constants.AudioStreamRate;
import cc.squirreljme.jvm.mle.exceptions.MLECallError;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.cldc.util.StreamUtils;
import cc.squirreljme.runtime.gcf.ContentTypeUtil;
import cc.squirreljme.runtime.gcf.InputStreamConnection;
import cc.squirreljme.runtime.media.AbstractPlayer;
import cc.squirreljme.runtime.media.AbstractVolumeControl;
import cc.squirreljme.runtime.media.control.AbstractMetaDataControl;
import cc.squirreljme.runtime.media.control.AbstractDeviceFeedbackControl;
import cc.squirreljme.runtime.media.control.DeviceFeedbackControl;
import cc.squirreljme.runtime.media.control.MetaDataValues;
import net.multiphasicapps.io.DataEndianess;
import net.multiphasicapps.io.ExtendedDataInputStream;
import org.jetbrains.annotations.NotNull;
import java.io.InputStream;
import java.io.IOException;
import javax.microedition.media.MediaException;
import javax.microedition.media.Player;

/**
 * Player that supports Ericsson's iMelody and eMelody media formats.
 * 
 * @since 2026/06/01
 */
@SquirrelJMEVendorApi
public class EricssonMelodyPlayer 
	extends AbstractPlayer
	implements AudioStreamRenderer
{
	/** The audio connection. */
	@SquirrelJMEVendorApi
	private volatile AudioConnectionBracket _connection;

	/** The un-realized input stream. */
	@SquirrelJMEVendorApi
	private volatile InputStreamConnection _unrealizedIn;

	/** The audio stream used. */
	@SquirrelJMEVendorApi
	private volatile AudioStreamBracket _stream;

	/** The decoder instance for compressed PCM wav data */
	@SquirrelJMEVendorApi
	private EricssonMelodyDecoder _decoder;

	/** Holds the Melody's metadata. */
	@SquirrelJMEVendorApi
	private MetaDataValues _metadata;

	/**
	 * Creates a new EricssonMelodyPlayer instance from the received
	 * {@link InputStream}.
	 * 
	 * @param __in The wav input data.
	 * @throws NullPointerException If {@code __in} is null.
	 * @since 2026/05/26
	 */
	@SquirrelJMEVendorApi
	public EricssonMelodyPlayer(@NotNull InputStreamConnection __in,
		String __type)
		throws NullPointerException
	{
		super(__type);

		if (__in == null)
			throw new NullPointerException("NARG");

		// For later realization
		this._unrealizedIn = __in;

		if (Debugging.VERBOSE)
			Debugging.debugNote("EricssonMelodyPlayer: init(%s)", __in);

		// Register volume control so we can properly set the gain.
		this.registerControl(new AbstractVolumeControl(this));

		// Ericsson Melody can access the device LEDs, Backlight and Vibrator.
		this.registerControl(new AbstractDeviceFeedbackControl());

		// Melodies also contain metadata.
		this._metadata = new MetaDataValues();
		this.registerControl(new AbstractMetaDataControl(this._metadata));
	}

	/**
	 * {@inheritDoc}
	 * @since 2026/05/26
	 */
	@Override
	public void render(int __format, int __rate, int __channels, Object __buf,
		int __off, int __len)
	{
		try
		{
			this._decoder.parseMelody(__format, __rate, __channels, __buf,
				__off, __len);

			if (!this._decoder.hasFinished())
				return;

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

	/**
	 * {@inheritDoc}
	 * @since 2026/05/26
	 */
	@Override
	public void becomingDeallocated()
		throws MediaException
	{
		this._stream = null;
		
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
	 * @since 2026/05/26
	 */
	@Override
	protected void becomingPrefetched()
		throws MediaException
	{
		synchronized (this)
		{
			// Data is already destroyed?
			if (this._unrealizedIn == null)
				throw new MediaException("GONE");

			try (InputStream in = this._unrealizedIn.openInputStream())
			{
				AbstractDeviceFeedbackControl ctrl =
				(AbstractDeviceFeedbackControl) this.getControl(
					DeviceFeedbackControl.class.getName());

				this._decoder = new EricssonMelodyDecoder(
					StreamUtils.readAll(in), ctrl, this._metadata);
			}
			catch (IOException __e)
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
	 * @since 2026/05/26
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
				AudioStreamRate.AUTOMATIC, AudioStreamChannels.AUTOMATIC);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2026/05/26
	 */
	@Override
	protected void becomingRealized()
		throws MediaException
	{
		// Do nothing, Ericsson Melody doesn't need this.
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2026/05/26
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
	 * @since 2026/05/26
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
				this._connection =
					AudioStreamShelf.attach(this._stream, this,
						AudioStreamFormat.AUTOMATIC, AudioStreamRate.AUTOMATIC,
						AudioStreamChannels.AUTOMATIC);
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
	 * @since 2026/05/26
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

				MediaException toss = new MediaException(__e.getMessage());
				toss.initCause(__e);
				throw toss;
			}
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2026/05/26
	 */
	@Override
	protected void clockFastForward(long __micros)
		throws MediaException
	{
		this.clockSet(__micros);
	}

	/**
	 * {@inheritDoc}
	 * @since 2026/05/26
	 */
	@Override
	protected long clockGet()
	{
		// Return start time, Ericsson Melody has no getMediaTime() equivalent
		return Player.TIME_UNKNOWN;
	}

	/**
	 * {@inheritDoc}
	 * @since 2026/05/26
	 */
	@Override
	protected void clockSet(long __micros)
		throws MediaException
	{
		// Always reset to start, Ericsson Melody has no setMediaTime()
		// equivalent and no concept of fast-forwarding to a specific point.
		this._decoder.reset();
	}

	/**
	 * {@inheritDoc}
	 * @since 2026/05/26
	 */
	@Override
	protected long determineDuration()
		throws MediaException
	{
		// Return no duration, Ericsson Melody has no getDuration() equivalent
		return Player.TIME_UNKNOWN;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2026/05/26
	 */
	@Override
	protected boolean resetFastForward()
	{
		// This is a tracker based format
		return true;
	}

	/**
	 * {@inheritDoc}
	 * @since 2026/05/26
	 */
	@Override
	protected void useVolume(int __volume)
	{
		this._decoder.setMasterVolume(__volume);
	}
}
