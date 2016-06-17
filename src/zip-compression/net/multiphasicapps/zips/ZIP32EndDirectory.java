// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.zips;

/**
 * This provides public fields which represent the structure of the central
 * directory.
 *
 * @since 2016/03/08
 */
@Deprecated
public class ZIP32EndDirectory
	extends AbstractZIPStructureElement<ZIP32EndDirectory>
{
	/** Magic number. */
	public static final ZIP32EndDirectory MAGIC_NUMBER =
		new ZIP32EndDirectory(null, Type.UINT32, null);
	
	/** Disk number that this is. */
	public static final ZIP32EndDirectory DISK_NUMBER =
		new ZIP32EndDirectory(MAGIC_NUMBER, Type.UINT16, null);
	
	/** The starting disk. */
	public static final ZIP32EndDirectory DISK_START =
		new ZIP32EndDirectory(DISK_NUMBER, Type.UINT16, null);
	
	/** The number of entries in this current disk. */
	public static final ZIP32EndDirectory DISK_ENTRY_COUNT =
		new ZIP32EndDirectory(DISK_START, Type.UINT16, null);
	
	/** The number of entries in entire archive. */
	public static final ZIP32EndDirectory ALL_ENTRY_COUNT =
		new ZIP32EndDirectory(DISK_ENTRY_COUNT, Type.UINT16, null);
	
	/** The size of the central directory. */
	public static final ZIP32EndDirectory CENTRAL_DIR_SIZE =
		new ZIP32EndDirectory(ALL_ENTRY_COUNT, Type.UINT32, null);
	
	/** Offset from the start of the ZIP to the central directory. */
	public static final ZIP32EndDirectory CENTRAL_DIR_OFFSET =
		new ZIP32EndDirectory(CENTRAL_DIR_SIZE, Type.UINT32, null);
	
	/** The length of the ZIP comment. */
	public static final ZIP32EndDirectory COMMENT_LENGTH =
		new ZIP32EndDirectory(CENTRAL_DIR_OFFSET, Type.UINT16, null);
	
	/** ZIP comment. */
	public static final ZIP32EndDirectory COMMENT =
		new ZIP32EndDirectory(COMMENT_LENGTH, Type.UINT8,
			COMMENT_LENGTH);
	
	/** The magic number of the end directory. */
	protected static final int MAGIC_NUMBER_VALUE =
		0x06054B50;
	
	/** Base size of the end directory. */
	protected static final long BASE_SIZE =
		COMMENT.offset();
	
	/** Maximum size of the end directory. */
	protected static final long MAX_SIZE =
		BASE_SIZE + 65535L;
	
	/**
	 * Initializes the structured ZIP element.
	 *
	 * @param __bef The element before this one.
	 * @param __t The type of value stored at this position.
	 * @param __var The variable field length specifier.
	 * @throws NullPointerException If no type was specified.
	 * @since 2016/03/08
	 */
	protected ZIP32EndDirectory(ZIP32EndDirectory __bef,
		Type __t, ZIP32EndDirectory __var)
	{
		super(__bef, __t, __var);
	}
}

