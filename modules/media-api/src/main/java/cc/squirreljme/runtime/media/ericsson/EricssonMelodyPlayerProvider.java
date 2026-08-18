// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.media.ericsson;

import cc.squirreljme.runtime.cldc.annotation.KeepWhenCompacting;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.media.PlayerProvider;
import java.io.IOException;
import javax.microedition.io.InputConnection;
import javax.microedition.media.MediaException;
import javax.microedition.media.Player;

/**
 * Provides {@link EricssonMelodyPlayer}.
 *
 * @since 2026/06/27
 */
@KeepWhenCompacting
public class EricssonMelodyPlayerProvider
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
		switch (__contentType)
		{
			case "audio/e-melody":
			case "audio/iMelody":
			case "text/x-eMelody":
			case "text/x-iMelody":
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
			"audio/e-melody",
			"audio/iMelody",
			"text/x-eMelody",
			"text/x-iMelody",
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
			throw new NullPointerException("NARG");
		
		return new EricssonMelodyPlayer(__in, __contentType);
	}
}
