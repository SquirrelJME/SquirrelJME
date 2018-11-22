// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.tool.manifest;

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
import net.multiphasicapps.collections.UnmodifiableMap;

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
	
	/** The attributes defined in this manifest file. */
	protected final Map<String, JavaManifestAttributes> attributes;
	
	/**
	 * Initializes a blank manifest.
	 *
	 * @since 2018/02/10
	 */
	public JavaManifest()
	{
		// Initialize a blank set of main attributes
		Map<String, JavaManifestAttributes> backing =
			new HashMap<>();
		backing.put("", new JavaManifestAttributes());
		
		// Lock in the backing map
		this.attributes = UnmodifiableMap.<String, JavaManifestAttributes>
			of(backing);
	}
	
	/**
	 * Decodes the manifest from the given input stream, it is treated as
	 * UTF-8 as per the JAR specification.
	 *
	 * @param __is The input stream for the manifest data.
	 * @throws IOException On read errors.
	 * @throws JavaManifestException If the manifest is malformed.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/05/20
	 */
	public JavaManifest(InputStream __is)
		throws IOException, JavaManifestException, NullPointerException
	{
		this(new BufferedReader(new InputStreamReader(__is, "utf-8")));
	}
	
	/**
	 * Decodes the manifest from the given reader.
	 *
	 * @param __r The characters which make up the manifest.
	 * @throws IOException On read errors.
	 * @throws JavaManifestException If the manifest is malformed.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/05/20
	 */
	public JavaManifest(Reader __r)
		throws IOException, JavaManifestException, NullPointerException
	{
		// Check
		if (__r == null)
			throw new NullPointerException("NARG");
		
		// Map which stores read attributes
		String curname = "";
		StringBuilder curkey = new StringBuilder(64);
		StringBuilder curval = new StringBuilder(256);
		Map<JavaManifestKey, String> working = new HashMap<>();
		
		// The target backing map
		Map<String, JavaManifestAttributes> backing =
			new HashMap<>();
		
		// Read all input characters
		int stage = _STAGE_KEY;
		boolean firstchar = true;
		for (;;)
		{
			// Read single character
			int ci = __r.read();
			char c = (char)ci;
			
			// EOF?
			if (ci < 0)
				break;
			
			// It is possible for a manifest to have a BOM at the start, if it
			// does just remove it
			if (firstchar)
			{
				firstchar = false;
				
				// Ignore the BOM
				if (c == 0xFEFF)
					continue;
			}
			
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
					
					// If this is the name key, then there is a new entire
					// grouping that is used
					if (sk.equals("name"))
					{
						// Setup a copy of the working map
						JavaManifestAttributes back =
							new JavaManifestAttributes(working);
						
						// Clear it
						working.clear();
						
						// Add it to the backing map
						backing.put(curname, back);
						
						// Set new name to the value
						curname = sv;
					}
					
					// Otherwise it is just added to the map
					else
						working.put(sk, sv);
					
					// Start reading keys again
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
						
						// {@squirreljme.error BB01 The specified character is
						// not a valid key character. (The character; The
						// character code)}
						if ((!js && !__isKeyChar(c)) ||
							(js && !__isAlphaNum(c)))
							throw new JavaManifestException(
								String.format("BB01 %c %04x", c, (int)c));
						
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
						
						// {@squirreljme.error BB02 Expected a space to follow
						// the colon following the start of the key.}
						if (stage == _STAGE_VALUE_START)
							throw new JavaManifestException("BB02");
						
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
							// Possibly may continue on
							stage = _STAGE_VALUE_MIGHT_CONTINUE;
							continue;
						}
						
						// Add it to the line data
						if (stage != _STAGE_VALUE_PADDING)
							curval.append(c);
					}
					break;
				
					// {@squirreljme.error BB03 Unknown manifest parse stage.
					// (The stage identifier)}
				default:
					throw new JavaManifestException(
						String.format("BB03 %d", stage));
			}
		}
		
		// {@squirreljme.error BB04 End of file reached while reading a
		// partial key value.}
		if (stage == _STAGE_KEY && curkey.length() != 0)
			throw new JavaManifestException("BB04");
		
		// Key and value waiting to be added to the working map?
		if (curkey.length() != 0)
		{
			// Generate values
			JavaManifestKey sk = new JavaManifestKey(
				curkey.toString());
			String sv = curval.toString();
			
			// Add to working set
			working.put(sk, sv);
		}
		
		// Add the current key to the target map
		backing.put(curname, new JavaManifestAttributes(working));
		
		// Lock in the backing map
		this.attributes = UnmodifiableMap.<String, JavaManifestAttributes>
			of(backing);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/20
	 */
	@Override
	public boolean containsKey(Object __k)
	{
		return this.attributes.containsKey(__k);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/20
	 */
	@Override
	public Set<Map.Entry<String, JavaManifestAttributes>> entrySet()
	{
		return this.attributes.entrySet();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/20
	 */
	@Override
	public JavaManifestAttributes get(Object __k)
	{
		return this.attributes.get(__k);
	}
	
	/**
	 * Returns the mapping of main attributes.
	 *
	 * @return The main attribute mapping.
	 * @since 2016/05/29
	 */
	public JavaManifestAttributes getMainAttributes()
	{
		return this.attributes.get("");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/20
	 */
	@Override
	public int size()
	{
		return this.attributes.size();
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

