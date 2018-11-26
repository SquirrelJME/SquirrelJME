// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.font;

/**
 * This represents a single table within the PCF.
 *
 * @since 2018/11/25
 */
final class __PCFFontTable__
	implements Comparable<__PCFFontTable__>
{
	/** The type of table entry this is. */
	final int _type;
	
	/** The format of the entry. */
	final int _format;
	
	/** The size of the entry. */
	final int _size;
	
	/** The offset to the entry. */
	final int _offset;
	
	/**
	 * Initializes the table entry.
	 *
	 * @param __t The type.
	 * @param __f The format.
	 * @param __s The size.
	 * @param __o The offset.
	 * @since 2018/11/25
	 */
	__PCFFontTable__(int __t, int __f, int __s, int __o)
	{
		this._type = __t;
		this._format = __f;
		this._size = __s;
		this._offset = __o;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/25
	 */
	@Override
	public int compareTo(__PCFFontTable__ __o)
	{
		return this._offset - __o._offset;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/25
	 */
	@Override
	public String toString()
	{
		return String.format("(type=%d, format=%d, size=%d, offset=%d)",
			this._type, this._format, this._size, this._offset);
	}
}

