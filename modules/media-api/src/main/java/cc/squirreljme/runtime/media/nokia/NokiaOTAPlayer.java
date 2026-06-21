// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.media.nokia;

import cc.squirreljme.jvm.mle.AudioStreamShelf;
import cc.squirreljme.jvm.mle.brackets.AudioConnectionBracket;
import cc.squirreljme.jvm.mle.brackets.AudioStreamBracket;
import cc.squirreljme.jvm.mle.callbacks.AudioStreamRenderer;
import cc.squirreljme.jvm.mle.constants.AudioStreamChannels;
import cc.squirreljme.jvm.mle.constants.AudioStreamFormat;
import cc.squirreljme.jvm.mle.constants.AudioStreamRate;
import cc.squirreljme.jvm.mle.exceptions.MLECallError;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.cldc.util.StreamUtils;
import cc.squirreljme.runtime.gcf.InputStreamConnection;
import cc.squirreljme.runtime.media.AbstractPlayer;
import cc.squirreljme.runtime.media.AbstractVolumeControl;
import java.io.IOException;
import java.io.InputStream;
import javax.microedition.media.MediaException;
import javax.microedition.media.Player;
import org.jetbrains.annotations.NotNull;

public class NokiaOTAPlayer
	extends AbstractPlayer
	implements AudioStreamRenderer
{
	/** Underlying data containing realized OTA media */
	@SquirrelJMEVendorApi
	private byte[] _data;

	/** The audio connection. */
	@SquirrelJMEVendorApi
	private volatile AudioConnectionBracket _connection;

	/** The un-realized input stream. */
	@SquirrelJMEVendorApi
	private volatile InputStreamConnection _unrealizedIn;

	/** The audio stream used. */
	@SquirrelJMEVendorApi
	private volatile AudioStreamBracket _stream;

	/** The decoder instance for Nokia OTA Notes/Events */
	@SquirrelJMEVendorApi
	private final NokiaOTADecoder _decoder;

	/**
	 * Creates a new {@link NokiaOTAPlayer} instance from the received
	 * {@link InputStreamConnection}.
	 * 
	 * @param __in The data stream to prepare for playback
	 * @throws MediaException If the data could not be prepared for playback.
	 * @throws NullPointerException If {@code __in} is null.
	 * @since 2025/12/24
	 */
	@SquirrelJMEVendorApi
	public NokiaOTAPlayer(@NotNull InputStreamConnection __in)
		throws MediaException, NullPointerException
	{
		super("application/vnd.nokia.ota");

		if (__in == null)
			throw new NullPointerException("NARG");

		this._decoder = new NokiaOTADecoder();

		// For later realization
		this._unrealizedIn = __in;

		if (Debugging.VERBOSE)
			Debugging.debugNote("NokiaOTAPlayer: init(%s)", __in);

		// Register volume control so we can properly set the gain
		this.registerControl(new AbstractVolumeControl(this));
	}

	/**
	 * {@inheritDoc}
	 * @since 2025/12/24
	 */
	@Override
	public void render(int __format, int __rate, int __channels, Object __buf,
		int __off, int __len)
	{
		this._decoder.parseOTA(__format, __rate, __channels, __buf, __off,
			__len, this._data);

		boolean finished = this._decoder.hasFinished();
		if (finished)
		{
			try
			{
				if (super.decrementLoop())
					this.stopViaMedia();
				
				// loopViaMedia will emit a STOPPED followed by a STARTED event
				// despite Nokia Sound not specifying how loop events should
				// behave in that scenario.
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
	 * @since 2025/12/28
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
	 * @since 2025/12/24
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

			try(InputStream in = this._unrealizedIn.openInputStream())
			{
				this._data = StreamUtils.readAll(in);
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
	 * @since 2025/12/24
	 */
	@Override
	protected void becomingRealized()
		throws MediaException
	{
		// Do nothing, Nokia OTA has no realize() equivalent
	}
	
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
	 * @since 2025/12/24
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
	 * @since 2025/12/24
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
	 * @since 2026/01/02
	 */
	@Override
	protected void clockFastForward(long __micros)
		throws MediaException
	{
		this.clockSet(__micros);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/12/24
	 */
	@Override
	protected long clockGet()
	{
		// Return start time, Nokia OTA has no getMediaTime() equivalent
		return 0;
	}

	/**
	 * {@inheritDoc}
	 * @since 2025/12/24
	 */
	@Override
	protected void clockSet(long __micros)
		throws MediaException
	{
		// Always reset to start, Nokia OTA has no setMediaTime() equivalent
		// and no concept of fast-forwarding to a specific point.
		this._decoder.reset();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/12/24
	 */
	@Override
	protected long determineDuration()
		throws MediaException
	{
		// Return no duration, Nokia OTA has no getDuration() equivalent
		return Player.TIME_UNKNOWN;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2026/01/02
	 */
	@Override
	protected boolean resetFastForward()
	{
		// This is a tracker based format
		return true;
	}

	/**
	 * {@inheritDoc}
	 * @since 2025/12/24
	 */
	@Override
	protected void useVolume(int __volume)
	{
		this._decoder.setMasterVolume(__volume);
	}
}
