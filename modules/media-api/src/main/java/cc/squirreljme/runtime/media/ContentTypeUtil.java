// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.media;

import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import org.intellij.lang.annotations.Language;

/**
 * Content type utilities.
 *
 * @since 2025/12/27
 */
@SquirrelJMEVendorApi
public final class ContentTypeUtil
{
	/**
	 * Not used.
	 *
	 * @since 2025/12/27
	 */
	private ContentTypeUtil()
	{
	}
	
	/**
	 * Guesses the content type based on the file path.
	 *
	 * @param __path The path to guess the content type for.
	 * @return The content type, or {@code null} if it is unknown.
	 * @throws NullPointerException On null arguments.
	 * @since 2025/12/27
	 */
	@SquirrelJMEVendorApi
	@Language("mime-type-reference")
	public static String guessByPath(String __path)
		throws NullPointerException
	{
		if (__path == null)
			throw new NullPointerException("NARG");
		
		// Reduce to the last path element
		int ls = __path.lastIndexOf('/');
		if (ls >= 0)
			__path = __path.substring(ls + 1);
		
		// Find the last dot, if there is none then this is unknown
		int ld = __path.lastIndexOf(".");
		if (ld < 0)
			return null;
		
		// Try to determine it based on the extension
		switch (__path.substring(ld + 1).toLowerCase())
		{
			case "mid":
			case "midi":
			case "rmi":
			case "smf":
				return "audio/midi";
			
			case "mld":
				return "application/x-mld-music";
				
			case "wav":
			case "wave":
				return "audio/wave";
				
			case "au":
			case "snd":
				return "audio/basic";
				
			case "aif":
			case "aiff":
			case "aifc":
				return "audio/aiff";
			
			case "mp3":
			case "mpga":
			case "bit":
				return "audio/mpeg";
			
			case "mpg":
			case "mpeg":
			case "m2p":
			case "ps":
				return "video/mpeg";
			
				// Unknown
			default:
				return null;
		}
	}
}
