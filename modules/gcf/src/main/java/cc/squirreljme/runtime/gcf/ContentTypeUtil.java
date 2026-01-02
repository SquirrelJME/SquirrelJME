// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.gcf;

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
		int ld = __path.lastIndexOf('.');
		if (ld < 0)
			return null;
		
		// Try to determine it based on the extension
		switch (__path.substring(ld + 1).toLowerCase())
		{
			case "txt":
				return "text/plain";
			
			case "htm":
			case "html":
				return "text/html";
				
			case "jar":
				return "application/java-archive";
				
			case "jad":
				return "text/vnd.sun.j2me.app-descriptor";
				
			case "gif":
				return "image/gif";
				
			case "png":
				return "image/png";
				
			case "bmp":
			case "dib":
				return "image/bmp";
			
			case "xpm":
				return "image/x-xpixmap";
				
			case "pbm":
				return "image/x-portable-bitmap";
				
			case "pgm":
				return "image/x-portable-graymap";
				
			case "ppm":
				return "image/x-portable-pixmap";
				
			case "pnm":
				return "image/x-portable-anymap";
				
			case "jpg":
			case "jpeg":
			case "jpe":
			case "jif":
			case "jfif":
			case "jfi":
				return "image/jpeg";
				
			case "svg":
			case "svgz":
				return "image/svg+xml";
				
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
				
			case "ota":
				return "application/vnd.nokia.ota";
			
				// Unknown
			default:
				return null;
		}
	}
	
	/**
	 * Is this a media type?
	 *
	 * @param __type The input type.
	 * @return If this is a media type.
	 * @throws NullPointerException On null arguments.
	 * @since 2025/12/30
	 */
	@SquirrelJMEVendorApi
	public static boolean isMedia(
		@Language("mime-type-reference") String __type)
		throws NullPointerException
	{
		if (__type == null)
			throw new NullPointerException("NARG");
		
		return ContentTypeUtil.isMediaAudio(__type) ||
			ContentTypeUtil.isMediaImage(__type) ||
			ContentTypeUtil.isMediaVideo(__type);
	}
	
	/**
	 * Is this an audio type?
	 *
	 * @param __type The input type.
	 * @return If this is an audio type.
	 * @throws NullPointerException On null arguments.
	 * @since 2025/12/30
	 */
	@SquirrelJMEVendorApi
	public static boolean isMediaAudio(
		@Language("mime-type-reference") String __type)
		throws NullPointerException
	{
		if (__type == null)
			throw new NullPointerException("NARG");
		
		// Generic?
		if (__type.startsWith("audio/"))
			return true;
		
		switch (__type)
		{
			case "audio/midi":
			case "application/x-mld-music":
			case "audio/wave":
			case "audio/basic":
			case "audio/aiff":
			case "audio/mpeg":
			case "application/vnd.nokia.ota":
				return true;
		}
		
		return false;
		
	}
	
	/**
	 * Is this an image type?
	 *
	 * @param __type The input type.
	 * @return If this is an image type.
	 * @throws NullPointerException On null arguments.
	 * @since 2025/12/30
	 */
	@SquirrelJMEVendorApi
	public static boolean isMediaImage(
		@Language("mime-type-reference") String __type)
		throws NullPointerException
	{
		if (__type == null)
			throw new NullPointerException("NARG");
		
		// Generic?
		if (__type.startsWith("image/"))
			return true;
		
		switch (__type)
		{
			case "image/png":
			case "image/bmp":
			case "image/x-xpixmap":
			case "image/x-portable-bitmap":
			case "image/x-portable-graymap":
			case "image/x-portable-pixmap":
			case "image/x-portable-anymap":
			case "image/jpeg":
			case "image/svg+xml":
				return true;
		}
		
		return false;
	}
	
	/**
	 * Is this a video type?
	 *
	 * @param __type The input type.
	 * @return If this is a video type.
	 * @throws NullPointerException On null arguments.
	 * @since 2025/12/30
	 */
	@SquirrelJMEVendorApi
	public static boolean isMediaVideo(
		@Language("mime-type-reference") String __type)
		throws NullPointerException
	{
		if (__type == null)
			throw new NullPointerException("NARG");
		
		// Generic?
		if (__type.startsWith("video/"))
			return true;
		
		switch (__type)
		{
			case "image/gif":
			case "video/mpeg":
				return true;
		}
		
		return false;
	}
}
