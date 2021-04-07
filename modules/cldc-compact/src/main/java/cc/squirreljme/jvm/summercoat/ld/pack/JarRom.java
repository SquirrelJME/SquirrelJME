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
import cc.squirreljme.jvm.summercoat.ld.mem.ReadableMemory;
import cc.squirreljme.runtime.cldc.debug.Debugging;
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
	 * @param __rc The resource to load.
	 * @return The input stream over the resource.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/04/07
	 */
	public final InputStream openResource(String __rc)
		throws NullPointerException
	{
		if (__rc == null)
			throw new NullPointerException("NARG");
		
		TableOfContents toc = this.__toc();
		throw Debugging.todo();
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
		
		throw Debugging.todo();
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
		
		throw Debugging.todo();
	}
}
