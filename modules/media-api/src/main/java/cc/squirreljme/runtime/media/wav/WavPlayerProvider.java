// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.media.wav;

import cc.squirreljme.runtime.cldc.annotation.KeepWhenCompacting;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.gcf.InputStreamConnection;
import cc.squirreljme.runtime.media.PlayerProvider;
import java.io.IOException;
import javax.microedition.io.InputConnection;
import javax.microedition.media.MediaException;
import javax.microedition.media.Player;

/**
 * Provides {@link WavPlayer}.
 *
 * @since 2026/06/27
 */
@KeepWhenCompacting
public class WavPlayerProvider
	implements PlayerProvider
{
	/**
	 * {@inheritDoc}
	 * @since 2026/06/27
	 */
	@Override
	public boolean acceptsContentType(String __contentType)
		throws NullPointerException
	{
		if (__contentType == null)
			throw new NullPointerException("NARG");
		
		switch (__contentType)
		{
			case "audio/vnd.wave":
			case "audio/wav":
			case "audio/wave":
			case "audio/x-wav":
				return true;
		}
		
		return false;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2026/06/27
	 */
	@Override
	public String[] acceptsContentTypes()
	{
		return new String[]
		{
			"audio/vnd.wave",
			"audio/wav",
			"audio/wave",
			"audio/x-wav",
		};
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2026/06/27
	 */
	@Override
	public boolean acceptsInputConnection()
	{
		return true;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2026/06/27
	 */
	@Override
	public Player viaInputConnection(InputConnection __in,
		String __contentType)
		throws IOException, MediaException, NullPointerException
	{
		if (__in == null || __contentType == null)
			throw new NullPointerException();
		
		return new WavPlayer(__in);
	}
}
