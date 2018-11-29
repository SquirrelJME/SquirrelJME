// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.pcftosqf.pcf;

/**
 * This represents a single table within the PCF.
 *
 * @since 2018/11/25
 */
public final class PCFTableEntry
	implements Comparable<PCFTableEntry>
{
	/** The type of table entry this is. */
	public final int type;
	
	/** The format of the entry. */
	public final int format;
	
	/** The size of the entry. */
	public final int size;
	
	/** The offset to the entry. */
	public final int offset;
	
	/**
	 * Initializes the table entry.
	 *
	 * @param __t The type.
	 * @param __f The format.
	 * @param __s The size.
	 * @param __o The offset.
	 * @since 2018/11/25
	 */
	public PCFTableEntry(int __t, int __f, int __s, int __o)
	{
		this.type = __t;
		this.format = __f;
		this.size = __s;
		this.offset = __o;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/25
	 */
	@Override
	public int compareTo(PCFTableEntry __o)
	{
		return this.offset - __o.offset;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/25
	 */
	@Override
	public String toString()
	{
		return String.format("(type=%d, format=%d, size=%d, offset=%d)",
			this.type, this.format, this.size, this.offset);
	}
}

