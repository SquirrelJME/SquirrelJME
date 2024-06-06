// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.fontcompile.util;

import cc.squirreljme.fontcompile.InvalidFontException;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.cldc.util.SortedTreeMap;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Map;
import java.util.NoSuchElementException;
import net.multiphasicapps.collections.UnmodifiableMap;

/**
 * Represents a single Glyph along with its ID.
 *
 * @since 2024/05/18
 */
public final class GlyphId
	implements Comparable<GlyphId>
{
	/** Glyph cache table. */
	private static final Map<Integer, GlyphId> _CACHE =
		new SortedTreeMap<>();
	
	/** Adobe Glyph List. */
	private static final Map<String, Integer> _AGL =
		GlyphId.__loadAgl();
	
	/** The codepoint used. */
	public final int codepoint;
	
	/**
	 * Initializes the glyph.
	 *
	 * @param __codepoint The codepoint used.
	 * @since 2024/05/18
	 */
	private GlyphId(int __codepoint)
	{
		this.codepoint = __codepoint;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/05/18
	 */
	@Override
	public int compareTo(GlyphId __b)
	{
		return this.codepoint - __b.codepoint;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/05/18
	 */
	@Override
	public boolean equals(Object __o)
	{
		if (__o == this)
			return true;
		else if (!(__o instanceof GlyphId))
			return false;
		
		GlyphId o = (GlyphId)__o;
		return this.codepoint == o.codepoint;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/05/18
	 */
	@Override
	public int hashCode()
	{
		return this.codepoint;
	}
	
	/**
	 * Returns the first glyph that is the start of this glyph's page.
	 *
	 * @return The glyph page.
	 * @since 2024/06/05
	 */
	public GlyphId page()
	{
		int page = this.codepoint & (~0xFF);
		if (page == this.codepoint)
			return this;
		return GlyphId.of(page);
	}
	
	/**
	 * Returns the offset of this ID in the page.
	 *
	 * @return The page offset.
	 * @since 2024/06/05
	 */
	public int pageOffset()
	{
		return this.codepoint & 0xFF;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/06/03
	 */
	@Override
	public String toString()
	{
		return Integer.toString(this.codepoint); 
	}
	
	/**
	 * Returns the glyph for the given codepoint.
	 *
	 * @param __codepoint The codepoint to use.
	 * @return The resultant glyph.
	 * @since 2024/05/18
	 */
	public static GlyphId of(int __codepoint)
	{
		Integer box = __codepoint;
		
		Map<Integer, GlyphId> cache = GlyphId._CACHE;
		synchronized (GlyphId.class)
		{
			// Check cache
			GlyphId rv = cache.get(box);
			if (rv != null)
				return rv;
			
			// Need to create it?
			rv = new GlyphId(__codepoint);
			cache.put(box, rv);
			return rv;
		}
	}
	
	/**
	 * Parses the given glyph string for a glyph ID.
	 *
	 * @param __in The input string.
	 * @return The resultant glyph id.
	 * @throws InvalidFontException If the ID is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/05/18
	 */
	public static GlyphId parse(String __in)
		throws InvalidFontException, NullPointerException
	{
		if (__in == null)
			throw new NullPointerException("NARG");
		
		// Unicode formats?
		if (__in.startsWith("U+"))
			return GlyphId.of(Integer.parseInt(
				__in.substring("U+".length()), 16));
		else if (__in.startsWith("uni") && __in.length() == 3 + 4)
			return GlyphId.of(Integer.parseInt(
				__in.substring("uni".length()), 16));
		
		// Identity character?
		else if (__in.startsWith("Identity."))
			return GlyphId.of(Integer.parseInt(__in.substring(
				"Identity.".length()), 10));
		
		// Otherwise will be a character in the AGL
		Integer agl = GlyphId._AGL.get(__in);
		if (agl == null)
			throw new InvalidFontException("Unknown glyph: " + __in);
		return GlyphId.of(agl);
	}
	
	/**
	 * Loads the AGL.
	 *
	 * @return The loaded AGL.
	 * @since 2024/05/18
	 */
	private static Map<String, Integer> __loadAgl()
	{
		Map<String, Integer> result = new SortedTreeMap<>();
		
		try (InputStream rc = GlyphId.class.getResourceAsStream(
			"/cc/squirreljme/fontcompile/agl.txt"))
		{
			if (rc == null)
				throw new RuntimeException("No AGL list?");
			
			try (BufferedReader br = new BufferedReader(
				new InputStreamReader(rc, "utf-8")))
			{
				for (;;)
				{
					String ln = br.readLine();
					
					// EOF?
					if (ln == null)
						break;
					
					// Trim to clean up
					ln = ln.trim();
					
					// Comment or blank line?
					if (ln.startsWith("#") || ln.isEmpty())
						continue;
					
					// Find semicolon
					int semi = ln.indexOf(';');
					if (semi < 0)
						throw new RuntimeException("Invalid AGL: " + ln);
					
					// Multi-character hex, we have no idea how to handle this
					// currently
					String hex = ln.substring(semi + 1).trim();
					if (hex.indexOf(' ') >= 0)
						continue;
					
					// Parse and store
					result.put(ln.substring(0, semi).trim(), Integer.parseInt(
						hex, 16));
				}
			}
		}
		catch (IOException __e)
		{
			throw new RuntimeException("Could not parse AGL.", __e);
		}
		
		// Use this
		return UnmodifiableMap.<String, Integer>of(result);
	}
}
