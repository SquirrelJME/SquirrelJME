// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.summercoat.ld.pack;

import cc.squirreljme.jvm.summercoat.ld.mem.ReadableMemory;
import cc.squirreljme.runtime.cldc.debug.Debugging;

/**
 * Represents a general table of contents.
 *
 * @since 2021/02/22
 */
public final class TableOfContents
{
	/** The table of contents data. */
	private final ReadableMemory data;
	
	/** The number of entries in the table. */
	private int _count =
		-1;
	
	/** The number of ints per entry. */
	private int _span =
		-1;
	
	/**
	 * Initializes the table of contents.
	 * 
	 * @param __data The data for the table of contents.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/04/03
	 */
	public TableOfContents(ReadableMemory __data)
		throws NullPointerException
	{
		if (__data == null)
			throw new NullPointerException("NARG");
		
		this.data = __data;
	}
	
	/**
	 * Returns the number of entries in the table.
	 * 
	 * @return The number of entries in the table.
	 * @since 2021/04/03
	 */
	public final int count()
	{
		if (this._count < 0)
			this.__init();
		
		return this._count;
	}
	
	/**
	 * Initializes the table of contents fields.
	 * 
	 * @throws InvalidRomException If the table of contents is not valid.
	 * @since 2021/04/03
	 */
	private void __init()
		throws InvalidRomException
	{
		// Debug
		Debugging.debugNote("Will read.");
		
		ReadableMemory data = this.data;
		int count = data.memReadShort(0) & 0xFFFF;
		int span = data.memReadShort(2) & 0xFFFF;
		
		// Debug
		Debugging.debugNote("TOC %d %d!", count, span);
		
		// {@squirreljme.error ZZ4v ROM has no span.}
		if (span == 0)
			throw new InvalidRomException("ZZ4v");
		
		// Store for later use;
		this._count = count;
		this._span = span;
	}
}
