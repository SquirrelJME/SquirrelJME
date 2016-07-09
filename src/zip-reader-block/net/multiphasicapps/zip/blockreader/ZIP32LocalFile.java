// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.zip.blockreader;

/**
 * This represents a local file header.
 *
 * @since 2016/03/08
 */
@Deprecated
public class ZIP32LocalFile
	extends AbstractZIPStructureElement<ZIP32LocalFile>
{
	/** Magic number. */
	public static final ZIP32LocalFile MAGIC_NUMBER =
		new ZIP32LocalFile(null, Type.UINT32, null);
	
	/** Version needed to extract. */
	public static final ZIP32LocalFile EXTRACT_VERSION =
		new ZIP32LocalFile(MAGIC_NUMBER, Type.UINT16, null);
	
	/** General purpose flags. */
	public static final ZIP32LocalFile GENERAL_PURPOSE_FLAGS =
		new ZIP32LocalFile(EXTRACT_VERSION, Type.UINT16, null);
	
	/** Compression method. */
	public static final ZIP32LocalFile COMPRESSION_METHOD =
		new ZIP32LocalFile(GENERAL_PURPOSE_FLAGS, Type.UINT16, null);
	
	/** Last modification time. */
	public static final ZIP32LocalFile LAST_MODIFIED_TIME =
		new ZIP32LocalFile(COMPRESSION_METHOD, Type.UINT16, null);
	
	/** Last modification date. */
	public static final ZIP32LocalFile LAST_MODIFIED_DATE =
		new ZIP32LocalFile(LAST_MODIFIED_TIME, Type.UINT16, null);
	
	/** CRC32. */
	public static final ZIP32LocalFile CRC =
		new ZIP32LocalFile(LAST_MODIFIED_DATE, Type.UINT32, null);
	
	/** Compressed size. */
	public static final ZIP32LocalFile COMPRESSED_SIZE =
		new ZIP32LocalFile(CRC, Type.UINT32, null);
	
	/** Uncompressed size. */
	public static final ZIP32LocalFile UNCOMPRESSED_SIZE =
		new ZIP32LocalFile(COMPRESSED_SIZE, Type.UINT32, null);
	
	/** File name length. */
	public static final ZIP32LocalFile FILE_NAME_LENGTH =
		new ZIP32LocalFile(UNCOMPRESSED_SIZE, Type.UINT16, null);
	
	/** Extra data length. */
	public static final ZIP32LocalFile EXTRA_DATA_LENGTH =
		new ZIP32LocalFile(FILE_NAME_LENGTH, Type.UINT16, null);
	
	/** File name. */
	public static final ZIP32LocalFile FILE_NAME =
		new ZIP32LocalFile(EXTRA_DATA_LENGTH, Type.UINT8,
		ZIP32LocalFile.FILE_NAME_LENGTH);
	
	/** Extra data. */
	public static final ZIP32LocalFile EXTRA_DATA =
		new ZIP32LocalFile(FILE_NAME, Type.UINT8,
		ZIP32LocalFile.EXTRA_DATA_LENGTH);
	
	/** Size of the file header. */
	public static final long BASE_SIZE =
		EXTRA_DATA.offset();
	
	/** File header magic number. */
	public static final int MAGIC_NUMBER_VALUE =
		0x04034B50;
	
	/**
	 * Initializes the structured ZIP element.
	 *
	 * @param __bef The element before this one.
	 * @param __t The type of value stored at this position.
	 * @param __var The variable field length specifier.
	 * @throws NullPointerException If no type was specified.
	 * @since 2016/03/08
	 */
	protected ZIP32LocalFile(ZIP32LocalFile __bef,
		Type __t, ZIP32LocalFile __var)
	{
		super(__bef, __t, __var);
	}
}

