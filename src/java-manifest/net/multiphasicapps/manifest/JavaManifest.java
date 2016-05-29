// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.manifest;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.Reader;
import java.util.AbstractMap;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * This contains decoders for the standard Java manifest format.
 *
 * This class is immutable.
 *
 * @since 2016/05/20
 */
public final class JavaManifest
	extends AbstractMap<String, JavaManifestAttributes>
{
	/** Read a key. */
	private static final int _STAGE_KEY =
		0;
	
	/** Read a value (start). */
	private static final int _STAGE_VALUE_START =
		1;
	
	/** Read a value (padding). */
	private static final int _STAGE_VALUE_PADDING =
		2;
	
	/** Read a value (length). */
	private static final int _STAGE_VALUE_LINE =
		3;
	
	/** Potentially may be a continuation of a line. */
	private static final int _STAGE_VALUE_MIGHT_CONTINUE =
		4;
	
	/**
	 * Decodes the manifest from the given input stream, it is treated as
	 * UTF-8 as per the JAR specification.
	 *
	 * @param __is The input stream for the manifest data.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/05/20
	 */
	public JavaManifest(InputStream __is)
		throws IOException, NullPointerException
	{
		this(new InputStreamReader(__is, "utf-8"));
	}
	
	/**
	 * Decodes the manifest from the given reader.
	 *
	 * @param __r The characters which make up the manifest.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/05/20
	 */
	public JavaManifest(Reader __r)
		throws IOException, NullPointerException
	{
		// Check
		if (__r == null)
			throw new NullPointerException("NARG");
		
		// Map which stores read attributes
		String curname = "";
		StringBuilder curkey = new StringBuilder();
		StringBuilder curval = new StringBuilder();
		Map<String, String> working = new HashMap<>();
		
		// Read all input characters
		int stage = _STAGE_KEY;
		for (;;)
		{
			// Read single character
			int ci = __r.read();
			char c = (char)ci;
			
			// EOF?
			if (ci < 0)
				break;
			
			// Line might continue?
			if (stage == _STAGE_VALUE_MIGHT_CONTINUE)
			{
				// Ignore newlines
				if (__isNewline(c))
					continue;
				
				// If a space, eat it and enter padding mode next
				if (__isSpace(c))
				{
					stage = _STAGE_VALUE_PADDING;
					continue;
				}
				
				// Otherwise it is a key
				else
				{
					// Build strings for key and value pair
					JavaManifestKey sk = new JavaManifestKey(
						curkey.toString());
					String sv = curval.toString();
					
					// Clear old key/value
					curkey.setLength(0);
					curval.setLength(0);
					
					if (true)
						throw new Error("TODO");
					
					// Set as key and read it
					stage = _STAGE_KEY;
				}
			}
			
			// Depends on the stage
			switch (stage)
			{
					// Reading key
				case _STAGE_KEY:
					{
						// Just started reading the key?
						boolean js = (curkey.length() <= 0);
						
						// Read nothing and just newline, skip otherwise the
						// parser will fail on blank lines
						if (js && __isNewline(c))
							continue;
						
						// End of key and reading value?
						if (c == ':')
						{
							stage = _STAGE_VALUE_START;
							continue;
						}
						
						// {@squirreljme.error BB02 The specified character is
						// not a valid key character. (The character)}
						if ((!js && !__isKeyChar(c)) ||
							(js && !__isAlphaNum(c)))
							throw new IOException(String.format("BB02 %c",
								c));
						
						// Add to key
						curkey.append(c);
					}
					break;
					
					// Read spaces following :
				case _STAGE_VALUE_START:
				case _STAGE_VALUE_PADDING:
					{
						// Skip spaces, but change the stage
						if (__isSpace(c))
						{
							stage = _STAGE_VALUE_PADDING;
							continue;
						}
						
						// {@squirreljme.error BB03 Expected a space to follow
						// the colon following the start of the key.}
						if (stage == _STAGE_VALUE_START)
							throw new IOException("BB03");
						
						// Otherwise start reading line data
						else
							stage = _STAGE_VALUE_LINE;
					}
				
					// Lots of spaces
					if (stage != _STAGE_VALUE_LINE &&
						stage != _STAGE_VALUE_PADDING)
						break;
					
					// Read of actual value
				case _STAGE_VALUE_LINE:
					{
						// End of line data
						if (__isNewline(c))
						{
							// {@squirreljme.error BB04 The value in a key
							// value pair consists of no characters.}
							if (curval.length() <= 0)
								throw new IOException("BB04");
							
							// Possibly may continue on
							stage = _STAGE_VALUE_MIGHT_CONTINUE;
							continue;
						}
						
						// Add it to the line data
						curval.append(c);
					}
					break;
				
					// {@squirreljme.error BB01 Unknown manifest parse stage.
					// (The stage identifier)}
				default:
					throw new IOException(String.format("BB01 %d", stage));
			}
		}
		
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/20
	 */
	@Override
	public Set<Map.Entry<String, JavaManifestAttributes>> entrySet()
	{
		throw new Error("TODO");
	}
	
	/**
	 * Returns {@code true} if the specified character is an alpha-numeric
	 * character.
	 *
	 * @param __c The character to check.
	 * @return {@code true} if an alpha-numeric character.
	 * @since 2016/05/29
	 */
	private static boolean __isAlphaNum(char __c)
	{
		return (__c >= 'A' && __c <= 'Z') ||
			(__c >= 'a' && __c <= 'z') ||
			(__c >= '0' && __c <= '9');
	}
	
	/**
	 * Returns {@code true} if the character is part of a character which would
	 * be used for key values.
	 *
	 * @param __c The character to check.
	 * @return {@code true} if a key character.
	 * @since 2016/05/29
	 */
	private static boolean __isKeyChar(char __c)
	{
		return __isAlphaNum(__c) || __c == '_' || __c == '-';
	}
	
	/**
	 * Returns {@code true} if the character is specified to be a newline
	 * character.
	 *
	 * @param __c The character to check.
	 * @return {@code true} if the character specifies the next line.
	 * @since 2016/05/29
	 */
	private static boolean __isNewline(char __c)
	{
		return __c == '\r' || __c == '\n';
	}
	
	/**
	 * Is the specified character a space character?
	 *
	 * @param __c The character to check.
	 * @return {@code true} if it is a space character.
	 * @since 2016/05/29
	 */
	private static boolean __isSpace(char __c)
	{
		return __c == ' ';
	}
}

