// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.aot.pack;

import cc.squirreljme.jvm.pack.TableOfContents;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.multiphasicapps.collections.UnmodifiableList;
import net.multiphasicapps.io.ChunkFuture;

/**
 * Writer for {@link TableOfContents}.
 *
 * @since 2021/09/03
 */
public final class TableOfContentsWriter
{
	/** The number of entries per table of contents entry. */ 
	protected final int spanLength;
	
	/** Entries within the table of contents. */
	final List<TableOfContentsEntry> _entries =
		new ArrayList<>();
	
	/**
	 * Initializes the table of contents writer.
	 * 
	 * @param __numTocProperties The number of properties per individual
	 * table of content entries.
	 * @throws IllegalArgumentException If the specified amount is zero or
	 * negative.
	 * @since 2021/09/05
	 */
	public TableOfContentsWriter(int __numTocProperties)
		throws IllegalArgumentException
	{
		/* {@squirreljme.error AJ01 Zero or negative table of contents
		entries specified.} */
		if (__numTocProperties <= 0)
			throw new IllegalArgumentException("AJ01");
		
		this.spanLength = __numTocProperties;
	}
	
	/**
	 * Adds a single entry to the table of contents.
	 * 
	 * @return The added entry.
	 * @since 2021/09/05
	 */
	public TableOfContentsEntry add()
	{
		TableOfContentsEntry rv = new TableOfContentsEntry(this.spanLength);
		
		// Make sure this is done in a thread safe manner
		synchronized (this)
		{
			this._entries.add(rv);
		}
		
		return rv;
	}
	
	/**
	 * Returns the number of items in the table of contents.
	 * 
	 * @return The number of items in the table of contents.
	 * @since 2021/09/06
	 */
	public int currentCount()
	{
		synchronized (this)
		{
			return this._entries.size();
		}
	}
	
	/**
	 * Returns the table of contents entries.
	 * 
	 * @return The entries in the table of contents.
	 * @since 2021/09/06
	 */
	public List<TableOfContentsEntry> entries()
	{
		synchronized (this)
		{
			List<TableOfContentsEntry> entries = this._entries;
			return UnmodifiableList.of(Arrays.asList(
				entries.toArray(new TableOfContentsEntry[entries.size()])));
		}
	}
	
	/**
	 * Returns the number of future items in the table of contents.
	 * 
	 * @return The number of future items in the table of contents.
	 * @since 2021/09/06
	 */
	public ChunkFuture futureCount()
	{
		return new __TocFutureCount__(this);
	}
}
