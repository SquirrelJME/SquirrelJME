// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.nativecode;

/**
 * This represents the type of floating point value that may be stored
 * within a register.
 *
 * @since 2016/09/02
 */
public enum NativeRegisterFloatType
	implements NativeRegisterType
{
	/** Float. */
	FLOAT(4),
	
	/** Double. */
	DOUBLE(8),
	
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
	private NativeRegisterFloatType(int __b)
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
		return true;
	}
}

