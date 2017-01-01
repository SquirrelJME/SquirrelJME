// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.nativecode;

/**
 * This is the type of value that can be stored within registers.
 *
 * @since 2016/09/02
 */
public enum NativeRegisterIntegerType
	implements NativeRegisterType
{
	/** Byte. */
	BYTE(1),
	
	/** Short. */
	SHORT(2),
	
	/** Integer. */
	INTEGER(4),
	
	/** Long. */
	LONG(8),
	
	/** End. */
	;
	
	/** Bytes to store. */
	protected final int bytes;
	
	/**
	 * Initializes the register type.
	 *
	 * @param __b The bytes needed to store.
	 * @since 2016/09/09
	 */
	private NativeRegisterIntegerType(int __b)
	{
		this.bytes = __b;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/09
	 */
	@Override
	public final int bytes()
	{
		return this.bytes;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/09
	 */
	@Override
	public final boolean isFloat()
	{
		return false;
	}
}

