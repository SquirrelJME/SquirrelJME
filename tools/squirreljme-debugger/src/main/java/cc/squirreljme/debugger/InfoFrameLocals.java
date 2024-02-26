// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.debugger;

import cc.squirreljme.jdwp.JDWPValue;

/**
 * Frame local variable information storage.
 *
 * @since 2024/01/26
 */
public class InfoFrameLocals
{
	/** The maximum number of locals. */
	public static final int MAX_LOCALS =
		127;
	
	/** The number of locals to look ahead at most when nothing was found. */
	static final int _LOCAL_BUMP =
		8;
	
	/** Frame values. */
	private final JDWPValue[] _values =
		new JDWPValue[InfoFrameLocals.MAX_LOCALS];
	
	/**
	 * Gets the value at the given index. 
	 *
	 * @param __dx The index to get.
	 * @return The resultant value.
	 * @throws IndexOutOfBoundsException If the index is not valid.
	 * @since 2024/01/27
	 */
	public JDWPValue get(int __dx)
		throws IndexOutOfBoundsException
	{
		if (__dx < 0 || __dx >= InfoFrameLocals.MAX_LOCALS)
			throw new IndexOutOfBoundsException("IOOB");
		
		// Get accordingly
		synchronized (this)
		{
			return this._values[__dx];
		}
	}
	
	/**
	 * Sets the value at the given index.
	 *
	 * @param __dx The index to set.
	 * @param __value The value to use.
	 * @throws IndexOutOfBoundsException If the index is out of bounds.
	 * @since 2024/01/26
	 */
	public final void set(int __dx, JDWPValue __value)
		throws IndexOutOfBoundsException
	{
		if (__dx < 0 || __dx >= InfoFrameLocals.MAX_LOCALS)
			throw new IndexOutOfBoundsException("IOOB");
		
		// Set accordingly
		synchronized (this)
		{
			this._values[__dx] = __value;
		}
	}
}
