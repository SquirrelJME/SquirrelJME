// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc.io;

/**
 * This is used to get the name of the encoder or decoder that was used.
 *
 * @since 2018/10/13
 */
public interface NamedCodec
{
	/**
	 * The average sequence length used for characters, used to estimate how
	 * big of an array to allocate for characters.
	 *
	 * @return The average sequence length.
	 * @since 2018/11/06
	 */
	double averageSequenceLength();
	
	/**
	 * Returns the name of the encoding.
	 *
	 * @return The encoding name.
	 * @since 2018/10/13
	 */
	String encodingName();
	
	/**
	 * Returns a hint which specifies the maximum length of a byte sequence
	 * for decoding.
	 *
	 * @return The maximum sequence length for decoding.
	 * @since 2018/10/13
	 */
	int maximumSequenceLength();
}

