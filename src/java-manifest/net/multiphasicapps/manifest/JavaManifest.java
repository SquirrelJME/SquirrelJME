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
		StringBuilder curname = new StringBuilder("");
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
			
			// Depends on the stage
			switch (stage)
			{
					// Reading key
				case _STAGE_KEY:
					{
						// Just started reading the key?
						boolean js = (curkey.length() <= 0);
						
						// {@squirreljme.error BB02 The specified character is
						// not a valid key character. (The character)}
						if ((!js && !__isKeyChar(c)) ||
							(js && !__isAlphaNum(c)))
							throw new IOException(String.format("BB02 %c",
								c));
						
						if (true)
							throw new Error("TODO");
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
}

