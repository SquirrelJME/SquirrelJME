// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jit.objectfile;

/**
 * This contains the properties which are used when writing data to a section,
 * it determines how data is to be encoded.
 *
 * @since 2018/02/24
 */
public final class DataProperties
{
	/** The endianess. */
	protected final Endian endian;
	
	/** The pointer type. */
	protected final IntegerType pointertype;
	
	/**
	 * Initializes the data properties which determines how to write into
	 * sections.
	 *
	 * @param __e The endianess of the data.
	 * @param __t The type used for pointer data.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/02/24
	 */
	public DataProperties(Endian __e, IntegerType __p)
		throws NullPointerException
	{
		if (__e == null || __p == null)
			throw new NullPointerException("NARG");
		
		this.endian = __e;
		this.pointertype = __p;
	}
}

