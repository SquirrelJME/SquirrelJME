// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.aot.pack;

import net.multiphasicapps.io.ChunkForwardedFuture;
import net.multiphasicapps.io.ChunkFuture;
import net.multiphasicapps.io.ChunkFutureInteger;

/**
 * Represents a single set of property spans.
 *
 * @since 2021/09/05
 */
public final class PropertySpan
{
	/** Properties to write. */
	private final ChunkForwardedFuture[] _properties;
	
	/**
	 * Initializes the property span.
	 * 
	 * @param __numProperties The number of properties to store.
	 * @throws IllegalArgumentException If the number of properties is
	 * negative.
	 * @since 2021/09/03
	 */
	public PropertySpan(int __numProperties)
		throws IllegalArgumentException
	{
		/* {@squirreljme.error AJ02 Invalid number of properties.} */
		if (__numProperties <= 0)
			throw new IllegalArgumentException("AJ02 " + __numProperties);
		
		ChunkForwardedFuture[] properties =
			new ChunkForwardedFuture[__numProperties];
		for (int i = 0, n = properties.length; i < n; i++)
			properties[i] = new ChunkForwardedFuture(true);
		
		this._properties = properties;
	}
	
	/**
	 * Returns the property count.
	 * 
	 * @return The number of properties to store.
	 * @since 2021/09/06
	 */
	public int count()
	{
		return this._properties.length;
	}
	
	/**
	 * Returns the value of the given entry.
	 * 
	 * @param __property The index to get.
	 * @return The future chunk.
	 * @since 2021/09/06
	 */
	public ChunkFuture get(int __property)
	{
		synchronized (this)
		{
			return this._properties[__property];
		}
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
		this.set(__property, new ChunkFutureInteger(__value));
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
		synchronized (this)
		{
			this._properties[__property].set(__value);
		}
	}
}
