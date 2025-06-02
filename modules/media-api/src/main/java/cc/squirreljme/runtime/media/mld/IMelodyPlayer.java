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
import cc.squirreljme.jvm.mle.brackets.AudioStreamBracket;
import cc.squirreljme.jvm.mle.brackets.AudioConnectionBracket;
import cc.squirreljme.jvm.mle.callbacks.AudioStreamRenderer;
import cc.squirreljme.jvm.mle.exceptions.MLECallError;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.gcf.InputStreamConnection;
import cc.squirreljme.runtime.media.AbstractPlayer;
import com.keitaiwiki.music.MA3Sampler;
import com.keitaiwiki.music.MA3SamplerProvider;
import com.keitaiwiki.music.MLD;
import com.keitaiwiki.music.MLDPlayer;
import com.keitaiwiki.music.Sampler;
import com.keitaiwiki.music.SamplerProvider;
import com.keitaiwiki.music.SineSampler;
import com.keitaiwiki.music.SineSamplerProvider;
import java.io.IOException;
import java.io.InputStream;
import javax.microedition.media.Control;
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
	private static Sampler _SAMPLER;
	
	/** The belayed media time. */
	private volatile long _belayTime =
		-1;
	
	/** The audio connection. */
	private volatile AudioConnectionBracket _connection;
	
	/** The MLD data. */
	private volatile MLD _mld;
	
	/** The MLD player. */
	private volatile MLDPlayer _mldPlayer;
	
	/** The audio stream used. */
	private volatile AudioStreamBracket _stream;
	
	/** The unrealized data input. */
	private volatile InputStreamConnection _unrealizedIn;
	
	/**
	 * Initializes the MLD player.
	 *
	 * @param __in The input MLD data.
	 * @throws NullPointerException On null arguments.
	 * @since 2025/05/05
	 */
	public IMelodyPlayer(InputStreamConnection __in)
		throws NullPointerException
	{
		super("audio/x-mld");
		
		if (__in == null)
			throw new NullPointerException("NARG");
		
		// For later realization
		this._unrealizedIn = __in;
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
			// Setup MLD player
			Sampler provider = IMelodyPlayer.__sampler();
			MLDPlayer mldPlayer = new MLDPlayer(this._mld, provider,
				48000F);
			
			// Initialize it
			mldPlayer.reset();
			mldPlayer.setPlaybackEventsEnabled(true);
			
			// Store it now
			this._mldPlayer = mldPlayer;
			
			// Set the correct time
			long belayTime = this._belayTime;
			if (belayTime >= 0)
			{
				this._belayTime = -1;
				this.setMediaTime(belayTime);
			}
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
			InputStreamConnection con = this._unrealizedIn;
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
	 * @since 2025/05/05
	 */
	@Override
	protected void becomingStarted()
		throws MediaException
	{
		synchronized (this)
		{
			// Create native audio stream for playback
			AudioStreamBracket stream;
			try
			{
				stream = AudioStreamShelf.stream();
			}
			catch (MLECallError __e)
			{
				__e.printStackTrace();
				
				MediaException toss = new MediaException(__e.getMessage());
				toss.initCause(__e);
				throw toss;
			}
			
			// Set the stream
			this._stream = stream;
			
			// Start rendering the stream, which will cause the audio to be
			// played
			try
			{
				this._connection =
					AudioStreamShelf.attach(stream, this);
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
	 * @since 2025/05/05
	 */
	@Override
	public void close()
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/05/05
	 */
	@Override
	public void deallocate()
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/05/05
	 */
	@Override
	protected long determineDuration()
		throws MediaException
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/05/05
	 */
	@Override
	public Control getControl(String __control)
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/05/05
	 */
	@Override
	public Control[] getControls()
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/05/05
	 */
	@Override
	public long getMediaTime()
		throws IllegalStateException
	{
		synchronized (this)
		{
			// Can only get the time if the player is valid
			MLDPlayer mldPlayer = this._mldPlayer;
			if (mldPlayer == null)
				throw new IllegalStateException("GONE");
			
			// This uses double time, in microseconds
			return (long)(mldPlayer.getTime() * 1_000_000D);
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
		
		// Keep rendering frames
		float[] buf = (float[])__buf;
		for (int offset = 0, left = __len / __channels; left > 0;)
		{
			// Render the current chunk
			// offset + frames * 2 > samples.length
			int rendered = mldPlayer.render(buf, offset, left,
				1.0F, 1.0F, true, true);
			if (rendered < 0)
				rendered = left;
			
			// Shift by amount of frames rendered
			left -= rendered;
			offset += rendered * 2;
			
			// Consume all events
			mldPlayer.getEvents();
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/05/05
	 */
	@Override
	public long setMediaTime(long __micros)
		throws MediaException
	{
		synchronized (this)
		{
			// Can only set the time if the player is valid
			MLDPlayer mldPlayer = this._mldPlayer;
			if (mldPlayer == null)
			{
				this._belayTime = __micros;
				return __micros;
			}
			
			// Clear the belay time
			this._belayTime = -1;
			
			// This uses double time
			// Media time is in microseconds
			mldPlayer.setTime((double)__micros / 1_000_000D);
			
			// Use the actually set time
			return this.getMediaTime();
		}
	}
	
	/**
	 * Returns the sampler to use.
	 *
	 * @return The sampler.
	 * @since 2025/05/05
	 */
	static final Sampler __sampler()
	{
		synchronized (IMelodyPlayer.class)
		{
			Sampler result = IMelodyPlayer._SAMPLER;
			if (result != null)
				return result;
			
			// Setup new sampler
			if (false)
				result = new SineSampler();
			else
				result = new MA3Sampler();
			IMelodyPlayer._SAMPLER = result;
			
			// Use this one
			return result;
		}
	}
}
