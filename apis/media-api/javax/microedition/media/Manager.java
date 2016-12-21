// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package javax.microedition.media;

import java.io.IOException;
import java.io.InputStream;
import javax.microedition.media.protocol.DataSource;

public final class Manager
{
	public static final String MIDI_DEVICE_LOCATOR =
		"device://midi";
	
	public static final String TONE_DEVICE_LOCATOR =
		"device://tone";
	
	private Manager()
	{
		super();
		throw new Error("TODO");
	}
	
	public static Player createPlayer(InputStream __a, String __b)
		throws IOException, MediaException
	{
		if (false)
			throw new IOException();
		if (false)
			throw new MediaException();
		throw new Error("TODO");
	}
	
	public static Player createPlayer(String __a)
		throws IOException, MediaException
	{
		if (false)
			throw new IOException();
		if (false)
			throw new MediaException();
		throw new Error("TODO");
	}
	
	public static Player createPlayer(DataSource __a)
		throws IOException, MediaException
	{
		if (false)
			throw new IOException();
		if (false)
			throw new MediaException();
		throw new Error("TODO");
	}
	
	public static String[] getSupportedContentTypes(String __a)
	{
		throw new Error("TODO");
	}
	
	public static String[] getSupportedProtocols(String __a)
	{
		throw new Error("TODO");
	}
	
	public static TimeBase getSystemTimeBase()
	{
		throw new Error("TODO");
	}
	
	public static void playTone(int __a, int __b, int __c)
		throws MediaException
	{
		if (false)
			throw new MediaException();
		throw new Error("TODO");
	}
}


