// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.palmos;

/**
 * This represents a single record within a database.
 *
 * @since 2019/07/13
 */
public final class PalmRecord
{
	/** The type. */
	public final String type;
	
	/** The record ID. */
	public final int id;
	
	/** The record length. */
	public final int length;
	
	/** The data. */
	final byte[] _data;
	
	/**
	 * Initializes the Palm record.
	 *
	 * @param __type The type.
	 * @param __id The ID.
	 * @param __b The buffer.
	 * @param __o The offset.
	 * @param __l The length.
	 * @throws IndexOutOfBoundsException If the offset and/or length are
	 * negative or exceed the array bounds.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/07/13
	 */
	public PalmRecord(String __type, int __id, byte[] __b, int __o, int __l)
		throws IndexOutOfBoundsException, NullPointerException
	{
		if (__type == null || __b == null)
			throw new NullPointerException("NARG");
		if (__o < 0 || __l < 0 || (__o + __l) < 0 || (__o + __l) > __b.length)
			throw new IndexOutOfBoundsException("IOOB");
		
		this.type = __type;
		this.id = __id;
		this.length = __l;
		
		// Copy and store bytes
		byte[] dest = new byte[__l];
		System.arraycopy(__b, __o, dest, 0, __l);
		this._data = dest;
	}
}

