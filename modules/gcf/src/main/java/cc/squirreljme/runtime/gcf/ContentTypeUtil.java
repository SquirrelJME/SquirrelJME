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
import java.io.IOException;
import java.io.InputStream;
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
	 * Attempts to guess the content type of the stream.
	 * 
	 * @param __in The stream to guess.
	 * @return The guessed content type or {@code null} if it could not be
	 * determined.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/04/24
	 */
	@Language("mime-type-reference")
	public static String guess(InputStream __in)
		throws IOException, NullPointerException
	{
		if (__in == null)
			throw new NullPointerException("NARG");
		
		// Use 12 bytes of context
		byte[] buf = new byte[12];
		__in.mark(buf.length);
		for (int i = 0, n = buf.length; i < n; i++)
			buf[i] = (byte)__in.read();
		__in.reset();
		
		// Forward
		return ContentTypeUtil.guess(buf, 0, buf.length);
	}
	
	/**
	 * Attempts to guess the content type of the given set of bytes.
	 * 
	 * @param __b The buffer.
	 * @param __o The offset into the buffer.
	 * @param __l The length of bytes.
	 * @return The guessed content type or {@code null} if it could not be
	 * determined.
	 * @throws IndexOutOfBoundsException If the offset and/or length are
	 * out of bounds.
	 * @throws NullPointerException On null arguments.
	 * @since 2026/01/03
	 */
	@Language("mime-type-reference")
	public static String guess(byte[] __b, int __o, int __l)
		throws IndexOutOfBoundsException, NullPointerException
	{
		if (__b == null)
			throw new NullPointerException("NARG");
		if (__o < 0 || __l < 0 || (__o + __l) < 0 || (__o + __l) > __b.length)
			throw new IndexOutOfBoundsException("IOOB");
		
		// Read in header completely
		int a = (0 < __l ? __b[__o] : 0) & 0xFF;
		int b = (1 < __l ? __b[__o + 1] : 0) & 0xFF;
		int c = (2 < __l ? __b[__o + 2] : 0) & 0xFF;
		int d = (3 < __l ? __b[__o + 3] : 0) & 0xFF;
		int e = (4 < __l ? __b[__o + 4] : 0) & 0xFF;
		int f = (5 < __l ? __b[__o + 5] : 0) & 0xFF;
		int g = (6 < __l ? __b[__o + 6] : 0) & 0xFF;
		int h = (7 < __l ? __b[__o + 7] : 0) & 0xFF;
		int i = (8 < __l ? __b[__o + 8] : 0) & 0xFF;
		int j = (9 < __l ? __b[__o + 9] : 0) & 0xFF;
		int k = (10 < __l ? __b[__o + 10] : 0) & 0xFF;
		int l = (11 < __l ? __b[__o + 11] : 0) & 0xFF;
		
		// Simplified magic
		int magic = ((a) << 24) | (b << 16) | (c << 8) | d;
		
		// MIDI (MThd/MTrk)
		if ((a == 'M' && b == 'T' && c == 'h' && d == 'd') ||
			(a == 'M' && b == 'T' && c == 'r' && d == 'k'))
			return "audio/midi";
		
		// WAVE
		if (a == 'R' && b == 'I' && c == 'F' && d == 'F' &&
			i == 'W' && j == 'A' && k == 'V' && l == 'E')
			return "audio/wave";
		
		// AIFF
		if (a == 'F' && b == 'O' && c == 'R' && d == 'M' &&
			i == 'A' && j == 'I' && k == 'F' && l == 'F')
			return "audio/aiff";
		
		// Basic sound
		if (a == 0x2E && b == 0x73 && c == 0x6E && d == 0x64)
			return "audio/basic";
		
		// i-melody MLD
		if ((a == 'm' && b == 'e' && c == 'l' && d == 'o'))
			return "audio/x-mld";
		
		// SMAF
		if (a == 'M' && b == 'M' && c == 'M' && d == 'D' &&
			i == 'C' && j == 'N' && k == 'T' && l == 'I')
			return "application/x-smaf";
		
		// GIF? (GIF8)
		if (magic == 0x47494638)
			return "image/gif";
		
		// PNG?
		if (magic == 0x89504E47)
			return "image/png";
		
		// JPEG?
		if ((magic & 0xFFFFFF00) == 0xFFD8FF00)
			return "image/jpeg";
		
		// Text files (ALWAYS LOWEST PRIORITY)
		if (!Character.isISOControl((char)a) &&
			!Character.isISOControl((char)b) &&
			!Character.isISOControl((char)c) &&
			!Character.isISOControl((char)d) &&
			!Character.isISOControl((char)e) &&
			!Character.isISOControl((char)f) &&
			!Character.isISOControl((char)g) &&
			!Character.isISOControl((char)h) &&
			!Character.isISOControl((char)i) &&
			!Character.isISOControl((char)j) &&
			!Character.isISOControl((char)k) &&
			!Character.isISOControl((char)l))
		{
			// Java Manifest
			if ((a == 'M' || a == 'm') &&
				(b == 'A' || b == 'a') &&
				(c == 'N' || c == 'n') &&
				(d == 'I' || d == 'i') &&
				(e == 'F' || e == 'f') &&
				(f == 'E' || f == 'e') &&
				(g == 'S' || g == 's') &&
				(h == 'T' || h == 't') &&
				(i == '-') &&
				(j == 'V' || j == 'v') &&
				(k == 'E' || k == 'e') &&
				(l == 'R' || l == 'r'))
				return "text/vnd.sun.j2me.app-descriptor";
			
			// XPM
			if ((a == '/') &&
				(b == '*') &&
				(c == ' ') &&
				(d == 'X' || d == 'x') &&
				(e == 'P' || e == 'p') &&
				(f == 'M' || f == 'm') &&
				(g == ' ') &&
				(h == '*') &&
				(i == '/'))
				return "image/x-xpixmap";
			
			// <!DOCTYPE Family
			if ((a == '<') &&
				(b == '!') &&
				(c == 'D') &&
				(d == 'O') &&
				(e == 'C') &&
				(f == 'T') &&
				(g == 'Y') &&
				(h == 'P') &&
				(i == 'E') &&
				(j == ' '))
			{
				// HTML
				if ((k == 'h' || k == 'H') &&
					(l == 't' || l == 'T'))
					return "text/html";
				
				// SVG
				if ((k == 's' || k == 'S') &&
					(l == 'v' || l == 'V'))
					return "image/svg+xml";
				
				// XML fallback
				return "text/xml";
			}
			
			// XML
			if ((a == '<') &&
				(b == '?') &&
				(c == 'x') &&
				(d == 'm') &&
				(e == 'l'))
				return "text/xml";
			
			// Text fallback
			return "text/plain";
		}
		
		// Unknown
		return null;
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
	
	/**
	 * Returns the extension for the contne type.
	 *
	 * @param __type The content type to map to an extension.
	 * @return The extension, or {@code null} if it is unknown.
	 * @throws NullPointerException On null arguments.
	 * @since 2026/01/03
	 */
	@SquirrelJMEVendorApi
	public static String toExtension(
		@Language("mime-type-reference") String __type)
		throws NullPointerException
	{
		if (__type == null)
			throw new NullPointerException("NARG");
		
		// Try to determine it based on the extension
		switch (__type)
		{
			case "text/plain":
				return "txt";
			
			case "text/html":
				return "htm";
				
			case "application/java-archive":
				return "jar";
				
			case "text/vnd.sun.j2me.app-descriptor":
				return "jad";
				
			case "image/gif":
				return "gif";
				
			case "image/png":
				return "png";
				
			case "image/bmp":
				return "bmp";
			
			case "image/x-xpixmap":
				return "xpm";
				
			case "image/x-portable-bitmap":
				return "pbm";
				
			case "image/x-portable-graymap":
				return "pgm";
				
			case "image/x-portable-pixmap":
				return "ppm";
				
			case "image/x-portable-anymap":
				return "pnm";
				
			case "image/jpeg":
				return "jpg";
				
			case "image/svg+xml":
				return "svg";
				
			case "audio/midi":
				return "mid";
			
			case "application/x-mld-music":
				return "mld";
				
			case "audio/wave":
				return "wav";
				
			case "audio/basic":
				return "au";
				
			case "audio/aiff":
				return "aif";
			
			case "audio/mpeg":
				return "mp3";
			
			case "video/mpeg":
				return "mpg";
				
			case "application/vnd.nokia.ota":
				return "ota";
			
				// Unknown
			default:
				return null;
		}
	}
}
