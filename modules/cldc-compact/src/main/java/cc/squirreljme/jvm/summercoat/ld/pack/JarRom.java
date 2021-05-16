// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.summercoat.ld.pack;

import cc.squirreljme.jvm.mle.brackets.JarPackageBracket;
import cc.squirreljme.jvm.summercoat.SummerCoatUtil;
import cc.squirreljme.jvm.summercoat.constants.ClassInfoConstants;
import cc.squirreljme.jvm.summercoat.constants.JarProperty;
import cc.squirreljme.jvm.summercoat.constants.JarTocProperty;
import cc.squirreljme.jvm.summercoat.ld.mem.ReadableMemory;
import cc.squirreljme.jvm.summercoat.ld.mem.ReadableMemoryInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * This represents a Jar, or some other abstract file, within the ROM.
 *
 * @since 2021/04/04
 */
public class JarRom
	implements JarPackageBracket
{
	/** The JAR data. */
	protected final ReadableMemory data;
	
	/** The JAR flags. */
	protected final int flags;
	
	/** The JAR name. */
	protected final String name;
	
	/** Header structure, dynamically load. */
	private HeaderStruct _header;
	
	/** Table of contents, dynamically loaded. */
	private TableOfContents _toc;
	
	/**
	 * Initializes the Jar ROM.
	 * 
	 * @param __flags The flags for the JAR.
	 * @param __name The name of the JAR.
	 * @param __data The data for the JAR.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/04/04
	 */
	public JarRom(int __flags, String __name, ReadableMemory __data)
		throws NullPointerException
	{
		if (__name == null || __data == null)
			throw new NullPointerException("NARG");
		
		this.flags = __flags;
		this.name = __name;
		this.data = __data;
	}
	
	/**
	 * Opens a stream to the resource data.
	 * 
	 * @param __rc The resource to load.
	 * @return The input stream over the resource or {@code null} if not found.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/04/07
	 */
	public final InputStream openResource(String __rc)
		throws NullPointerException
	{
		if (__rc == null)
			throw new NullPointerException("NARG");
			
		ReadableMemory data = this.data;
		
		// We need the hash code to do a quicker lookup of entries
		int hash = __rc.hashCode();
		
		// Look through the TOC
		TableOfContents toc = this.__toc();
		for (int i = 0, n = toc.count(); i < n; i++)
		{
			// Not this hash code?
			if (hash != toc.get(i, JarTocProperty.INT_NAME_HASHCODE))
				continue;
			
			// This is not the entry we are looking for?
			if (0 != SummerCoatUtil.strCmp(__rc, data.absoluteAddress(
					toc.get(i, JarTocProperty.OFFSET_NAME))))
				continue;
			
			// Return a stream over the entry data
			return new ReadableMemoryInputStream(data,
				toc.get(i, JarTocProperty.OFFSET_DATA),
				toc.get(i, JarTocProperty.SIZE_DATA));
		}
		
		// Not found, so return null for it
		return null;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/04/06
	 */
	@Override
	public final String toString()
	{
		return this.name;
	}
	
	/**
	 * Potentially loads and returns the header.
	 * 
	 * @return The header.
	 * @throws InvalidRomException If the ROM is not valid.
	 * @since 2021/04/07
	 */
	private HeaderStruct __header()
		throws InvalidRomException
	{
		HeaderStruct rv = this._header;
		if (rv != null)
			return rv;
		
		// Decode the header
		try (ReadableMemoryInputStream in =
			new ReadableMemoryInputStream(this.data))
		{
			// {@squirreljme.error Invalid JAR magic number.}
			rv = HeaderStruct.decode(in, JarProperty.NUM_JAR_PROPERTIES);
			if (rv.magicNumber != ClassInfoConstants.JAR_MAGIC_NUMBER)
				throw new InvalidRomException("ZZ57");
			
			this._header = rv;
		}
		
		// {@squirreljme.error ZZ56 The JAR header is corrupt.}
		catch (IOException e)
		{
			throw new InvalidRomException("ZZ56", e);
		}
		
		return rv;
	}
	
	/**
	 * Potentially loads and returns the table of contents.
	 * 
	 * @return The table of contents.
	 * @throws InvalidRomException If the ROM is not valid.
	 * @since 2021/04/07
	 */
	private TableOfContents __toc()
	{
		TableOfContents rv = this._toc;
		if (rv != null)
			return rv;
		
		// Load the header
		HeaderStruct header = this.__header();
		
		// Load in the table of contents
		rv = new TableOfContents(this.data.subSection(
			header.getProperty(JarProperty.OFFSET_TOC),
			header.getProperty(JarProperty.SIZE_TOC)));
		
		// Cache and use it
		this._toc = rv;
		return rv;
	}
}
