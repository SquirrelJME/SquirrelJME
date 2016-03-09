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
 * This provides structural details for the central directory of a ZIP file.
 *
 * @since 2016/03/08
 */
public class ZIP32CentralDirectory
	extends AbstractZIPStructureElement<ZIP32CentralDirectory>
{
	/** The magic number of a directory entry. */
	public static final ZIP32CentralDirectory MAGIC_NUMBER =
		new ZIP32CentralDirectory(null, Type.UINT32, null);
	
	/** The version this was made by. */
	public static final ZIP32CentralDirectory BY_VERSION =
		new ZIP32CentralDirectory(MAGIC_NUMBER, Type.UINT16, null);
	
	/** The version needed to extract. */
	public static final ZIP32CentralDirectory EXTRACT_VERSION =
		new ZIP32CentralDirectory(BY_VERSION, Type.UINT16, null);
	
	/** General purpose flags. */
	public static final ZIP32CentralDirectory GENERAL_PURPOSE_FLAGS =
		new ZIP32CentralDirectory(EXTRACT_VERSION, Type.UINT16, null);
	
	/** The compression method. */
	public static final ZIP32CentralDirectory COMPRESSION_METHOD =
		new ZIP32CentralDirectory(GENERAL_PURPOSE_FLAGS, Type.UINT16, null);
	
	/** Last modified time. */
	public static final ZIP32CentralDirectory LAST_MODIFIED_TIME =
		new ZIP32CentralDirectory(COMPRESSION_METHOD, Type.UINT16, null);
	
	/** Last modified date. */
	public static final ZIP32CentralDirectory LAST_MODIFIED_DATE =
		new ZIP32CentralDirectory(LAST_MODIFIED_TIME, Type.UINT16, null);
	
	/** CRC-32. */
	public static final ZIP32CentralDirectory CRC =
		new ZIP32CentralDirectory(LAST_MODIFIED_DATE, Type.UINT32, null);
	
	/** Compressed Size. */
	public static final ZIP32CentralDirectory COMPRESSED_SIZE =
		new ZIP32CentralDirectory(CRC, Type.UINT32, null);
	
	/** Uncompressed Size. */
	public static final ZIP32CentralDirectory UNCOMPRESSED_SIZE =
		new ZIP32CentralDirectory(COMPRESSED_SIZE, Type.UINT32, null);
	
	/** File name length. */
	public static final ZIP32CentralDirectory FILE_NAME_LENGTH =
		new ZIP32CentralDirectory(UNCOMPRESSED_SIZE, Type.UINT16, null);
	
	/** Extra data length. */
	public static final ZIP32CentralDirectory EXTRA_DATA_LENGTH =
		new ZIP32CentralDirectory(FILE_NAME_LENGTH, Type.UINT16, null);
	
	/** Comment length. */
	public static final ZIP32CentralDirectory COMMENT_LENGTH =
		new ZIP32CentralDirectory(EXTRA_DATA_LENGTH, Type.UINT16, null);
	
	/** Starting disk number. */
	public static final ZIP32CentralDirectory DISK_NUMBER_START =
		new ZIP32CentralDirectory(COMMENT_LENGTH, Type.UINT16, null);
	
	/** Internal attributes. */
	public static final ZIP32CentralDirectory INTERNAL_ATTRIBUTES =
		new ZIP32CentralDirectory(DISK_NUMBER_START, Type.UINT16, null);
	
	/** External attributes. */
	public static final ZIP32CentralDirectory EXTERNAL_ATTRIBUTES =
		new ZIP32CentralDirectory(INTERNAL_ATTRIBUTES, Type.UINT32, null);
	
	/** Local header offset from the start of the ZIP. */
	public static final ZIP32CentralDirectory LOCAL_HEADER_OFFSET =
		new ZIP32CentralDirectory(EXTERNAL_ATTRIBUTES, Type.UINT32, null);
	
	/** File name. */
	public static final ZIP32CentralDirectory FILE_NAME =
		new ZIP32CentralDirectory(LOCAL_HEADER_OFFSET, Type.UINT8,
		FILE_NAME_LENGTH);
	
	/** Extra data field. */
	public static final ZIP32CentralDirectory EXTRA_DATA =
		new ZIP32CentralDirectory(FILE_NAME, Type.UINT8,
		EXTRA_DATA_LENGTH);
	
	/** Comment. */
	public static final ZIP32CentralDirectory COMMENT =
		new ZIP32CentralDirectory(EXTRA_DATA, Type.UINT8,
		COMMENT_LENGTH);
	
	/** Base size of the central directory. */
	protected static final long BASE_SIZE =
		COMMENT.offset();
	
	/** Central directory magic number value. */
	public static final int MAGIC_NUMBER_VALUE =
		0x02014B50;
	
	/**
	 * Initializes the structured ZIP element.
	 *
	 * @param __bef The element before this one.
	 * @param __t The type of value stored at this position.
	 * @param __var The variable field length specifier.
	 * @throws NullPointerException If no type was specified.
	 * @since 2016/03/08
	 */
	protected ZIP32CentralDirectory(ZIP32CentralDirectory __bef,
		Type __t, ZIP32CentralDirectory __var)
	{
		super(__bef, __t, __var);
	}
}

