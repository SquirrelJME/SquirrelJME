// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.classfile;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * This represents a range of instruction addresses, the start is inclusive
 * and the end is exclusive.
 *
 * @since 2019/03/21
 */
public final class InstructionAddressRange
	implements Comparable<InstructionAddressRange>
{
	/** The start address. */
	protected final int start;
	
	/** The end address. */
	protected final int end;
	
	/** String representation. */
	private Reference<String> _string;
	
	/**
	 * Initializes the address range.
	 *
	 * @param __s The start address.
	 * @param __e The end address.
	 * @since 2019/03/21
	 */
	public InstructionAddressRange(int __s, int __e)
	{
		this.start = __s;
		this.end = __e;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/03/21
	 */
	@Override
	public final int compareTo(InstructionAddressRange __b)
	{
		int rv = this.start - __b.start;
		if (rv != 0)
			return rv;
		
		return this.end - __b.end;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/03/21
	 */
	@Override
	public final boolean equals(Object __o)
	{
		if (__o == this)
			return true;
		
		if (!(__o instanceof InstructionAddressRange))
			return false;
		
		InstructionAddressRange o = (InstructionAddressRange)__o;
		return this.start == o.start &&
			this.end == o.end;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/03/21
	 */
	@Override
	public final int hashCode()
	{
		return this.start ^ (~this.end);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/03/21
	 */
	@Override
	public final String toString()
	{
		Reference<String> ref = this._string;
		String rv;
		
		if (ref == null || null == (rv = ref.get()))
			this._string = new WeakReference<>((rv = "@[" + this.start +
				"-" + this.end + ")"));
		
		return rv;
	}
}

