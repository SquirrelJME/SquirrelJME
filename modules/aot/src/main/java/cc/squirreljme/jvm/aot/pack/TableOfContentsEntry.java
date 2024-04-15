// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.aot.pack;

import net.multiphasicapps.io.ChunkFuture;

/**
 * A single entry within the {@link TableOfContentsWriter}.
 *
 * @since 2021/09/05
 */
public final class TableOfContentsEntry
{
	/** Properties to write. */
	protected final PropertySpan properties;
	
	/**
	 * Initializes the table of contents entry writer.
	 * 
	 * @param __numProperties The number of properties to store.
	 * @throws IllegalArgumentException If the number of properties is
	 * negative.
	 * @since 2021/09/05
	 */
	TableOfContentsEntry(int __numProperties)
		throws IllegalArgumentException
	{
		this.properties = new PropertySpan(__numProperties);
	}
	
	/**
	 * Returns the value of the given entry.
	 * 
	 * @param __i The index to get.
	 * @return The future chunk.
	 * @since 2021/09/06
	 */
	public ChunkFuture get(int __i)
	{
		return this.properties.get(__i);
	}
	
	/**
	 * Sets the given property.
	 * 
	 * @param __property The property to set.
	 * @param __value The value to set.
	 * @throws IndexOutOfBoundsException If this property is outside of the
	 * set bounds.
	 * @since 2021/09/05
	 */
	public final void set(int __property, int __value)
		throws IndexOutOfBoundsException
	{
		this.properties.set(__property, __value);
	}
	
	/**
	 * Sets the given property.
	 * 
	 * @param __property The property to set.
	 * @param __value The value to set.
	 * @throws IndexOutOfBoundsException If this property is outside of the
	 * set bounds.
	 * @since 2021/09/05
	 */
	public final void set(int __property, ChunkFuture __value)
		throws IndexOutOfBoundsException
	{
		this.properties.set(__property, __value);
	}
}
