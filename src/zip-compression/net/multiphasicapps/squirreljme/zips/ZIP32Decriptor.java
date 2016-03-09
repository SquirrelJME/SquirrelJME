// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.zips;

/**
 * This represents the descriptor (without the magic number).
 *
 * @since 2016/03/09
 */
public class ZIP32Decriptor
	extends AbstractZIPStructureElement<ZIP32Decriptor>
{
	/** The CRC. */
	public static final ZIP32Decriptor CRC =
		new ZIP32Decriptor(null, Type.UINT32, null);
	
	/** Compressed size. */
	public static final ZIP32Decriptor COMPRESSED_SIZE =
		new ZIP32Decriptor(CRC, Type.UINT32, null);
	
	/** Uncompressed size. */
	public static final ZIP32Decriptor UNCOMPRESSED_SIZE =
		new ZIP32Decriptor(COMPRESSED_SIZE, Type.UINT32, null);
	
	/** The size of this structure. */
	public static final long BASE_SIZE =
		UNCOMPRESSED_SIZE.offset() + UNCOMPRESSED_SIZE.type().size();
	
	/** The value for the magic number. */
	public static final int MAGIC_NUMBER_VALUE =
		0x08074B50;
	
	/**
	 * Initializes the structured ZIP element.
	 *
	 * @param __bef The element before this one.
	 * @param __t The type of value stored at this position.
	 * @param __var The variable field length specifier.
	 * @throws NullPointerException If no type was specified.
	 * @since 2016/03/09
	 */
	protected ZIP32Decriptor(ZIP32Decriptor __bef,
		Type __t, ZIP32Decriptor __var)
	{
		super(__bef, __t, __var);
	}
}

