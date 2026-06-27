// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.media.mld;

import cc.squirreljme.runtime.cldc.annotation.KeepWhenCompacting;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.media.PlayerProvider;
import javax.microedition.io.InputConnection;
import javax.microedition.media.MediaException;
import javax.microedition.media.Player;

/**
 * Provides {@link IMelodyPlayer}.
 *
 * @since 2026/06/27
 */
@KeepWhenCompacting
public class IMelodyPlayerProvider
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
			case "application/x-mld":
			case "application/x-mld-music":
			case "audio/x-mld":
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
			"application/x-mld",
			"application/x-mld-music",
			"audio/x-mld",
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
		throws MediaException, NullPointerException
	{
		if (__in == null || __contentType == null)
			throw new NullPointerException("NARG");
		
		return new IMelodyPlayer(__in);
	}
}
