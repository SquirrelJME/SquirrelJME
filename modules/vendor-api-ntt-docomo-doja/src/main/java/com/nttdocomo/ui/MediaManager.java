// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package com.nttdocomo.ui;

import cc.squirreljme.runtime.cldc.DubiousImplementationError;
import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.cldc.debug.ErrorCode;
import cc.squirreljme.runtime.nttdocomo.ui.NullMediaSound;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@Api
public class MediaManager
{
	@SuppressWarnings("FinalStaticMethod")
	@Api
	public static final MediaData getData(String __uri)
	{
		throw Debugging.todo();
	}
	
	@SuppressWarnings("FinalStaticMethod")
	@Api
	public static final MediaImage getImage(String __uri)
		throws NullPointerException
	{
		if (__uri == null)
			throw new NullPointerException("NARG");
		
		return new __MIDPImage__(__uri);
	}
	
	/**
	 * Loads a sound from the given byte array.
	 *
	 * @param __buf The buffer to load a sound from.
	 * @return The resultant sound.
	 * @throws NullPointerException On null arguments.
	 * @throws UIException If there are no resources to load the given sound.
	 * @since 2024/01/14
	 */
	@Api
	@SuppressWarnings("FinalStaticMethod")
	public static final MediaSound getSound(byte[] __buf)
		throws NullPointerException, UIException
	{
		if (__buf == null)
			throw new NullPointerException("NARG");
		
		try (InputStream in = new ByteArrayInputStream(__buf))
		{
			return MediaManager.getSound(in);
		}
		catch (IOException __e)
		{
			/* {@squirreljme.error AH1c It is unspecified what happens if
			a sound fails to load from a byte array.} */
			throw new DubiousImplementationError(
				ErrorCode.__error__("AH1c"), __e);
		}
	}
	
	@Api
	@SuppressWarnings("FinalStaticMethod")
	public static final MediaSound getSound(InputStream __in)
		throws NullPointerException, UIException
	{
		if (__in == null)
			throw new NullPointerException("NARG");
		
		Debugging.todoNote("Implement getSound()...");
		return new NullMediaSound();
	}
	
	@Api
	@SuppressWarnings("FinalStaticMethod")
	public static final MediaSound getSound(String __uri)
	{
		throw Debugging.todo();
	}
}
