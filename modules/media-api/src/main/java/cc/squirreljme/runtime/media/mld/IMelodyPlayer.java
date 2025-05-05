// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.media.mld;

import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.gcf.InputStreamConnection;
import cc.squirreljme.runtime.media.AbstractPlayer;
import com.keitaiwiki.music.MA3SamplerProvider;
import com.keitaiwiki.music.MLD;
import com.keitaiwiki.music.MLDPlayer;
import com.keitaiwiki.music.Sampler;
import com.keitaiwiki.music.SamplerProvider;
import java.io.IOException;
import java.io.InputStream;
import javax.microedition.media.Control;
import javax.microedition.media.MediaException;

/**
 * Player for i-Melody files.
 *
 * @since 2025/05/05
 */
@SquirrelJMEVendorApi
public class IMelodyPlayer
	extends AbstractPlayer
{
	/** The MA-3 instance. */
	private static MA3SamplerProvider _SAMPLER;
	
	/** The MLD data. */
	private volatile MLD _mld;
	
	/** The MLD player. */
	private volatile MLDPlayer _mldPlayer;
	
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
			SamplerProvider provider = IMelodyPlayer.__sampler();
			this._mldPlayer = new MLDPlayer(this._mld, provider,
				44100F);
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
			// Set start time
			//this._mldPlayer.setTime();
			
			throw Debugging.todo();
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
		throw Debugging.todo();
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
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/05/05
	 */
	@Override
	public long setMediaTime(long __now)
		throws MediaException
	{
		throw Debugging.todo();
	}
	
	/**
	 * Returns the MA-3 sampler.
	 *
	 * @return The sampler.
	 * @since 2025/05/05
	 */
	static final MA3SamplerProvider __sampler()
	{
		synchronized (IMelodyPlayer.class)
		{
			MA3SamplerProvider result = IMelodyPlayer._SAMPLER;
			if (result != null)
				return result;
			
			// Setup new sampler
			result = new MA3SamplerProvider();
			IMelodyPlayer._SAMPLER = result;
			
			// Use this one
			return result;
		}
	}
}
