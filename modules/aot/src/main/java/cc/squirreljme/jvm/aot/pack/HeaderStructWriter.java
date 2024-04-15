// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.aot.pack;

import cc.squirreljme.jvm.pack.HeaderStruct;
import net.multiphasicapps.io.ChunkFuture;

/**
 * Writer for standard {@link HeaderStruct}.
 *
 * @since 2021/09/03
 */
public final class HeaderStructWriter
{
	/** Properties to write. */
	protected final PropertySpan properties;
	
	/**
	 * Initializes the header struct writer.
	 * 
	 * @param __numProperties The number of properties to store.
	 * @throws IllegalArgumentException If the number of properties is
	 * negative.
	 * @since 2021/09/03
	 */
	public HeaderStructWriter(int __numProperties)
		throws IllegalArgumentException
	{
		this.properties = new PropertySpan(__numProperties);
	}
	
	/**
	 * Sets the given property.
	 * 
	 * @param __property The property to set.
	 * @param __value The value to set.
	 * @throws IndexOutOfBoundsException If this property is outside of the
	 * set bounds.
	 * @since 2021/09/03
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
	 * @since 2021/09/03
	 */
	public final void set(int __property, ChunkFuture __value)
		throws IndexOutOfBoundsException
	{
		this.properties.set(__property, __value);
	}
}
