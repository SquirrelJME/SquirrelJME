// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.media.mld;

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
import cc.squirreljme.runtime.gcf.InputStreamConnection;
import cc.squirreljme.runtime.media.AbstractPlayer;
import cc.squirreljme.runtime.media.AbstractVolumeControl;
import cc.squirreljme.runtime.midlet.DoJaRuntime;
import com.keitaiwiki.music.MA3SamplerProvider;
import com.keitaiwiki.music.MLD;
import com.keitaiwiki.music.MLDPlayer;
import com.keitaiwiki.music.MLDPlayerEvent;
import com.keitaiwiki.music.SamplerProvider;
import com.keitaiwiki.music.SineSamplerProvider;
import java.io.IOException;
import java.io.InputStream;
import javax.microedition.io.InputConnection;
import javax.microedition.media.MediaException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

/**
 * Player for i-Melody files.
 *
 * @since 2025/05/05
 */
@SquirrelJMEVendorApi
public class IMelodyPlayer
	extends AbstractPlayer
	implements AudioStreamRenderer
{
	/** The MA-3 instance. */
	private static SamplerProvider _SAMPLER;
	
	/** The audio connection. */
	private volatile AudioConnectionBracket _connection;
	
	/** The last end-type message. */
	private volatile int _lastEndType;
	
	/** The MLD data. */
	private volatile MLD _mld;
	
	/** The MLD player. */
	private volatile MLDPlayer _mldPlayer;
	
	/** The audio stream used. */
	private volatile AudioStreamBracket _stream;
	
	/** The unrealized data input. */
	private volatile InputConnection _unrealizedIn;
	
	/**
	 * Initializes the MLD player.
	 *
	 * @param __in The input MLD data.
	 * @throws NullPointerException On null arguments.
	 * @since 2025/05/05
	 */
	public IMelodyPlayer(InputConnection __in)
		throws NullPointerException
	{
		super("application/x-mld-music");
		
		if (__in == null)
			throw new NullPointerException("NARG");
		
		// For later realization
		this._unrealizedIn = __in;
		
		// Register volume control
		this.registerControl(new AbstractVolumeControl(this));
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/12/28
	 */
	@Override
	public void becomingDeallocated()
		throws MediaException
	{
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
	 * @since 2025/05/05
	 */
	@Override
	protected void becomingPrefetched()
		throws MediaException
	{
		synchronized (this)
		{
			// Which sample set is used for playback?
			SamplerProvider provider = IMelodyPlayer.__sampler();
			
			// Setup MLD player
			MLDPlayer mldPlayer = new MLDPlayer(this._mld, provider,
				48000F);
			mldPlayer.setPlaybackEventsEnabled(true);
			
			// Store it now
			this._mldPlayer = mldPlayer;
			
			// Determine the duration so something gets processed
			mldPlayer.getDuration(true);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2026/01/03
	 */
	@Override
	protected void becomingPrimed()
		throws MediaException
	{
		synchronized (this)
		{
			// Does not need to be created?
			if (this._stream != null)
				return;
			
			// Create native audio stream for playback
			this._stream = AbstractPlayer.stream(
				AudioStreamFormat.FLOAT_F32,
				AudioStreamRate.HZ_48000, 
				AudioStreamChannels.STEREO);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/05/05
	 */
	@Override
	protected void becomingRealized()
		throws MediaException
	{
		synchronized (this)
		{
			// The connection must still exist
			InputConnection con = this._unrealizedIn;
			if (con == null)
				throw new MediaException("GONE");
			
			// Load in the MLD data
			try (InputStream in = con.openInputStream())
			{
				this._mld = new MLD(in);
			}
			catch (IOException __e)
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
	 * @since 2026/01/03
	 */
	@Override
	protected void becomingSolvent()
		throws MediaException
	{
		synchronized (this)
		{
			AudioStreamBracket stream = this._stream;
			this._stream = null;
			
			if (stream != null)
				AbstractPlayer.streamDisconnect(stream, false);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/05/05
	 */
	@Override
	protected boolean becomingStarted()
		throws MediaException
	{
		synchronized (this)
		{
			// Start rendering the stream, which will cause the audio to be
			// played
			try
			{
				this._connection =
					AudioStreamShelf.attach(this._stream, this,
						AudioStreamFormat.FLOAT_F32,
						AudioStreamRate.HZ_48000,
						AudioStreamChannels.STEREO);
			}
			catch (MLECallError __e)
			{
				__e.printStackTrace();
				
				MediaException toss = new MediaException(__e.getMessage());
				toss.initCause(__e);
				throw toss;
			}
		}
		
		// Do set the new state
		return true;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/05/05
	 */
	@Override
	protected void becomingStopped()
		throws MediaException
	{
		synchronized (this)
		{
			try
			{
				// Disconnect
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
	 * @since 2026/01/03
	 */
	@Override
	protected void clockFastForward(long __micros)
		throws MediaException
	{
		this.clockSet(__micros);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/06/15
	 */
	@Override
	protected long clockGet()
	{
		synchronized (this)
		{
			// Can only get the time if the player is valid
			MLDPlayer mldPlayer = this._mldPlayer;
			if (mldPlayer == null)
				return IMelodyPlayer.TIME_UNKNOWN;
			
			// This uses double time, in microseconds
			return (long)(mldPlayer.getTime() * 1_000_000D);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/06/15
	 */
	@Override
	protected void clockSet(long __micros)
		throws MediaException
	{
		synchronized (this)
		{
			// Can only set the time if the player is valid
			MLDPlayer mldPlayer = this._mldPlayer;
			if (mldPlayer == null)
				return;
			
			// Reset synthesizer state
			mldPlayer.reset();
			
			// This uses double time
			// Media time is in microseconds
			mldPlayer.setTime((double)__micros / 1_000_000D);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/05/05
	 */
	@Override
	protected long determineDuration()
		throws MediaException
	{
		MLD mld = this._mld;
		if (mld == null)
			return IMelodyPlayer.TIME_UNKNOWN;
		
		return (long)(mld.getDuration(true) * 1_000_000D);
	}
	
	/**
	 * Returns the last ending type.
	 *
	 * @return The last ending type.
	 * @since 2025/06/03
	 */
	@SquirrelJMEVendorApi
	public final int lastEndType()
	{
		synchronized (this)
		{
			return this._lastEndType;
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/06/01
	 */
	@Override
	public void render(int __format,
		@Range(from = 0, to = Integer.MAX_VALUE) int __rate,
		@Range(from = 0, to = Integer.MAX_VALUE) int __channels,
		@NotNull Object __buf,
		@Range(from = 0, to = Integer.MAX_VALUE) int __off,
		@Range(from = 0, to = Integer.MAX_VALUE) int __len)
	{
		MLDPlayer mldPlayer = this._mldPlayer;
		if (mldPlayer == null)
			return;
		
		int rendered = -1;

		// Keep rendering frames
		float[] buf = (float[])__buf;
		for (int offset = 0, left = __len / __channels; left > 0;)
		{
			// Render the current chunk
			// offset + frames * 2 > samples.length
			rendered = mldPlayer.render(buf, offset, left,
				1.0F, 1.0F, false, false);

			// MLDPlayer reached end of playback
			if (rendered < 0)
				break;
			
			// Shift by amount of frames rendered
			left -= rendered;
			offset += rendered * 2;
			
			// Consume all events
			MLDPlayerEvent[] events = mldPlayer.getEvents();
			if (events != null)
				for (MLDPlayerEvent event : events)
				{
					// Skip blank events
					if (event == null)
						continue;
					
					// Handle events
					try
					{
						this.__handleEvent(mldPlayer, event);
					}
					
					// Drop these
					catch (MediaException __e)
					{
						__e.printStackTrace();
					}
				}
		}

		// Don't check for End-Of-Media below, we have processed some data.
		if (rendered >= 0)
			return;

		try
		{
			if (super.decrementLoop())
			{
				this._lastEndType = MLDPlayer.EVENT_END;
				this.stopViaMedia();
			}

			// Only DoJa >= 5.0 supports loops in AudioPresenter
			else
			{
				if (DoJaRuntime.versionLeast(5, 0))
				{
					this._lastEndType = MLDPlayer.EVENT_LOOP;
					this.loopViaMedia();
				}

				// This shouldn't ever happen, throw an exception if it does
				else
					throw Debugging.oops();
			}
		}
		catch (MediaException __e)
		{
			__e.printStackTrace();
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2026/01/02
	 */
	@Override
	protected boolean resetFastForward()
	{
		// This is capable of setting the position without any issues
		return false;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/06/03
	 */
	@Override
	protected void useVolume(int __volume)
	{
		synchronized (this)
		{
			// Ignore volume set if there is no player
			MLDPlayer mldPlayer = this._mldPlayer;
			if (mldPlayer == null)
				return;
			
			// Forward the volume
			mldPlayer.sampler.masterVolume(__volume / 100.0F);
		}
	}
	
	/**
	 * Handles an event.
	 *
	 * @param __mldPlayer The player handling events for.
	 * @param __event The event to handle.
	 * @throws MediaException On any errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2025/06/01
	 */
	private void __handleEvent(MLDPlayer __mldPlayer, MLDPlayerEvent __event)
		throws MediaException, NullPointerException
	{
		if (__mldPlayer == null || __event == null)
			throw new NullPointerException("NARG");
		
		// DoJa 5.0+ supports looped MLDs, so in this event just go back to
		// the start
		if (DoJaRuntime.versionLeast(5, 0) &&
			__event.type == MLDPlayer.EVENT_LOOP)
		{
			this.setMediaTime(0);
			return;
		}
		
		// Treat media loops and event ends the same for MIDP
		if (__event.type == MLDPlayer.EVENT_LOOP ||
			__event.type == MLDPlayer.EVENT_END)
		{
			// Record the last type, used for DoJa handling
			this._lastEndType = __event.type;
			
			// Media is stopping, stop playing
			if (super.decrementLoop())
				this.stopViaMedia();
			
			// Media is looping, go back to the start
			else
				this.loopViaMedia();
		}
	}
	
	/**
	 * Returns the sampler to use.
	 *
	 * @return The sampler.
	 * @since 2025/05/05
	 */
	static final SamplerProvider __sampler()
	{
		synchronized (IMelodyPlayer.class)
		{
			SamplerProvider result = IMelodyPlayer._SAMPLER;
			if (result != null)
				return result;
			
			// Setup new sampler
			if (false)
				result = new SineSamplerProvider();
			else
				result = new MA3SamplerProvider();
			IMelodyPlayer._SAMPLER = result;
			
			// Use this one
			return result;
		}
	}
}
