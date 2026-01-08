// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package com.nttdocomo.ui;

import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.cldc.debug.ErrorCode;
import cc.squirreljme.runtime.gcf.InputStreamConnection;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.microedition.io.Connection;
import javax.microedition.io.Connector;
import javax.microedition.io.InputConnection;
import org.intellij.lang.annotations.Language;

import static cc.squirreljme.runtime.cldc.debug.ErrorCode.__error__;

@Api
public class MediaManager
{
	@SuppressWarnings("FinalStaticMethod")
	@Api
	public static final MediaData getData(
		@Language("http-url-reference") String __uri)
	{
		throw Debugging.todo();
	}
	
	@SuppressWarnings("FinalStaticMethod")
	@Api
	public static final MediaImage getImage(
		@Language("http-url-reference") String __uri)
		throws NullPointerException
	{
		if (__uri == null)
			throw new NullPointerException("NARG");
		
		try (InputStream in = MediaManager.__fromUri(__uri))
		{
			return new __MIDPImage__(new InputStreamConnection(in));
		}
		catch (IOException __e)
		{
			throw new IllegalArgumentException(__e.getMessage(), __e);
		}
	}
	
	@Api
	@SuppressWarnings("FinalStaticMethod")
	public static final MediaImage getImage(InputStream __in)
		throws NullPointerException
	{
		if (__in == null)
			throw new NullPointerException("NARG");
		
		return new __MIDPImage__(new InputStreamConnection(__in));
	}
	
	@Api
	@SuppressWarnings("FinalStaticMethod")
	public static final MediaImage getImage(byte[] __buf)
		throws NullPointerException
	{
		if (__buf == null)
			throw new NullPointerException("NARG");
		
		try (InputStream in = new ByteArrayInputStream(__buf))
		{
			return new __MIDPImage__(new InputStreamConnection(in));
		}
		catch (IOException __e)
		{
			throw new IllegalArgumentException(__e.getMessage() , __e);
		}
	}
	
	/**
	 * Loads a sound from the given byte array.
	 *
	 * @param __buf The buffer to load a sound from.
	 * @return The resultant sound.
	 * @throws IllegalArgumentException If the given audio file is not
	 * supported.
	 * @throws NullPointerException On null arguments.
	 * @throws UIException If there are no resources to load the given sound.
	 * @since 2024/01/14
	 */
	@Api
	@SuppressWarnings("FinalStaticMethod")
	public static final MediaSound getSound(byte[] __buf)
		throws IllegalArgumentException, NullPointerException, UIException
	{
		if (__buf == null)
			throw new NullPointerException("NARG");
		
		try (InputStream in = new ByteArrayInputStream(__buf))
		{
			return MediaManager.getSound(in);
		}
		catch (IOException __e)
		{
			throw new IllegalArgumentException(__e.getMessage() , __e);
		}
	}
	
	/**
	 * Describe this. 
	 *
	 * @param __in The input sound data.
	 * @return The resultant sound.
	 * @throws NullPointerException On null arguments.
	 * @throws UIException If there are not enough resources to load the sound
	 * or if the format is not supported.
	 * @since 2025/05/05
	 */
	@Api
	@SuppressWarnings("FinalStaticMethod")
	public static final MediaSound getSound(InputStream __in)
		throws NullPointerException, UIException
	{
		if (__in == null)
			throw new NullPointerException("NARG");
		
		return new __MIDPPlayer__(new InputStreamConnection(__in));
	}
	
	/**
	 * Opens a sound from the given URI resource.
	 *
	 * @param __uri The URI to read from.
	 * @return The resultant sound.
	 * @throws IllegalArgumentException If the sound could not be read from
	 * the given resource.
	 * @throws NullPointerException On null arguments.
	 * @throws SecurityException If access to the URI is not permitted.
	 * @throws UIException If there is not enough system resources available
	 * to load the sound.
	 * @since 2025/05/05
	 */
	@Api
	@SuppressWarnings("FinalStaticMethod")
	public static final MediaSound getSound(
		@Language("http-url-reference") String __uri)
		throws IllegalArgumentException, NullPointerException,
			SecurityException, UIException
	{
		if (__uri == null)
			throw new NullPointerException("NARG");
		
		try (InputStream in = MediaManager.__fromUri(__uri))
		{
			return MediaManager.getSound(in);
		}
		catch (IOException __e)
		{
			throw new IllegalArgumentException(__e.getMessage(), __e);
		}
	}
	
	/**
	 * Opens a stream to the given URI.
	 *
	 * @param __uri The URI to read from.
	 * @return The resultant stream.
	 * @throws IllegalArgumentException If the data could not be read from
	 * the given resource.
	 * @throws NullPointerException On null arguments.
	 * @throws SecurityException If access to the URI is not permitted.
	 * @throws UIException If there is not enough system resources available
	 * to load the data.
	 * @since 2025/05/05
	 */
	private static final InputStream __fromUri(
		@Language("http-url-reference") String __uri)
	{
		if (__uri == null)
			throw new NullPointerException("NARG");
		
		/* {@squirreljme.error AH0w Invalid protocol specified. (The URI)} */
		if (__uri.startsWith("comm:") || __uri.startsWith("obex:"))
			throw new IllegalArgumentException(
				__error__("AH0w %s", __uri));
		
		// Open connection to the target
		try (Connection con = Connector.open(__uri))
		{
			/* {@squirreljme.error AH1g The URI cannot be
			read from. (The URI)} */
			if (!(con instanceof InputConnection))
				throw new IllegalArgumentException(
					__error__("AH1g %s", __uri));
			
			// Read in
			return ((InputConnection)con).openInputStream();
		}
		catch (IOException __e)
		{
			throw new IllegalArgumentException(__e.getMessage(), __e);
		}
	}
}
