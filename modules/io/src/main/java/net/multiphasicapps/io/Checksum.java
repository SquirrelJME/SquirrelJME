// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.io;

/**
 * This interface is implemented by checksum calculators and may be used to
 * assist in the calculation of checksums.
 *
 * @since 2017/03/05
 */
public interface Checksum
{
	/**
	 * Returns the currently calculated checksum value.
	 *
	 * @return The current checksum value.
	 * @since 2017/03/05
	 */
	int checksum();
	
	/**
	 * Offers a single byte for checksum calcualtion.
	 *
	 * @param __b The byte to offer.
	 * @since 2017/03/05
	 */
	void offer(byte __b);
	
	/**
	 * Offers multiple byte for checksum calculation.
	 *
	 * @param __b The bytes to offer.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/03/05
	 */
	void offer(byte[] __b)
		throws NullPointerException;
	
	/**
	 * Offers multiple byte for checksum calculation.
	 *
	 * @param __b The bytes to offer.
	 * @param __o The starting offset to read bytes from.
	 * @param __l The number of bytes to buffer.
	 * @throws ArrayIndexOutOfBoundsException If the offset or length are
	 * negative or they exceed the array bounds.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/03/05
	 */
	void offer(byte[] __b, int __o, int __l)
		throws ArrayIndexOutOfBoundsException, NullPointerException;
	
	/**
	 * Resets the checksum calculator to its initial state.
	 *
	 * @since 2017/03/05
	 */
	void reset();
}

