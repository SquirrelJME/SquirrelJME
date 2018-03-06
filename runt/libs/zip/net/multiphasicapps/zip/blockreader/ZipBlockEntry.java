// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.zip.blockreader;

import java.io.InputStream;
import java.io.IOException;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import net.multiphasicapps.zip.IBM437CodePage;
import net.multiphasicapps.zip.ZipCompressionType;
import net.multiphasicapps.zip.ZipException;

/**
 * This represents a single entry within a ZIP file which may be opened.
 *
 * @since 2016/12/30
 */
public final class ZipBlockEntry
{
	/** Maximum version. */
	private static final int _MAX_CENTRAL_DIR_VERSION =
		20;
	
	/** The version which made the ZIP */
	private static final int _CENTRAL_DIRECTORY_MADE_BY_VERSION_OFFSET =
		4;
	
	/** The offset to the version needed to extract. */
	private static final int _CENTRAL_DIRECTORY_EXTRACT_VERSION_OFFSET =
		6;
	
	/** The offset of the general purpose flags. */
	private static final int _CENTRAL_DIRECTORY_FLAG_OFFSET =
		8;
	
	/** The offset to the method of compression. */
	private static final int _CENTRAL_DIRECTORY_METHOD_OFFSET =
		10;
	
	/** The offset to the CRC for data integrity. */
	private static final int _CENTRAL_DIRECTORY_CRC_OFFSET =
		16;
	
	/** The offset to the compressed size. */
	private static final int _CENTRAL_DIRECTORY_COMPRESSED_OFFSET =
		20;
	
	/** The offset to the uncompressed size. */
	private static final int _CENTRAL_DIRECTORY_UNCOMPRESSED_OFFSET =
		24;
	
	/** The offset to the file name length. */
	private static final int _CENTRAL_DIRECTORY_NAME_LENGTH_OFFSET =
		28;
	
	/** The offset to the extra data length. */
	private static final int _CENTRAL_DIRECTORY_EXTRA_LENGTH_OFFSET =
		_CENTRAL_DIRECTORY_NAME_LENGTH_OFFSET + 2;
	
	/** The offset to the comment length. */
	private static final int _CENTRAL_DIRECTORY_COMMENT_LENGTH_OFFSET =
		_CENTRAL_DIRECTORY_EXTRA_LENGTH_OFFSET + 2;
	
	/** The relative offset to the local header. */
	private static final int _CENTRAL_DIRECTORY_LOCAL_HEADER_OFFSET =
		42;
	
	/** The minimum length of the central directory entry. */
	private static final int _CENTRAL_DIRECTORY_MIN_LENGTH =
		46;
	
	/** The local file header magic number. */
	private static final int _LOCAL_HEADER_MAGIC_NUMBER =
		0x04034B50;
	
	/** The offset to the file name length in the local header. */
	private static final int _LOCAL_HEADER_NAME_LENGTH_OFFSET =
		26;
	
	/** The offset to the comment length in the local header. */
	private static final int _LOCAL_HEADER_COMMENT_LENGTH_OFFSET =
		28;
	
	/** The local header minimum size. */
	private static final int _LOCAL_HEADER_MIN_LENGTH =
		30;
	
	/** General purpose flag: Is UTF-8 encoded filename/comment? */
	protected static final int GPF_ENCODING_UTF8 =
		(1 << 11);
	
	/** The owning reader. */
	protected final ZipBlockReader owner;
	
	/** The data accessor. */
	protected final BlockAccessor accessor;
	
	/** The position of this entry. */
	protected final long position;
	
	/** The name of this file. */
	private volatile Reference<String> _name;
	
	/**
	 * Initializes the block entry.
	 *
	 * @param __br The owning block reader.
	 * @param __id The entry ID.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/12/30
	 */
	ZipBlockEntry(ZipBlockReader __br, int __id)
		throws NullPointerException
	{
		// Check
		if (__br == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.owner = __br;
		this.accessor = __br._accessor;
		
		// Get position
		this.position = __br._offsets[__id];
	}
	
	/**
	 * Returns {@code true} if the entry pertains to a directory.
	 *
	 * @return If it is a directory or not.
	 * @throws IOException On read errors.
	 * @throws ZipException If the ZIP is malformed.
	 * @since 2017/01/03
	 */
	public boolean isDirectory()
		throws IOException, ZipException
	{
		return __internalToString().endsWith("/");
	}
	
	/**
	 * Returns the time this entry was last modified.
	 *
	 * @return The last modified time or {@code Long.MIN_VALUE} if it is not
	 * valid.
	 * @since 2018/03/06
	 */
	public long lastModifiedTime()
	{
		return Long.MIN_VALUE;
	}
	
	/**
	 * Returns the name of this entry.
	 *
	 * @return The entry name.
	 * @since 2017/03/01
	 */
	public String name()
	{
		return toString();
	}
	
	/**
	 * Opens the input stream for this entry's data.
	 *
	 * @return The entry data.
	 * @throws IOException On read errors.
	 * @throws ZipException If it could not be opened.
	 * @since 2016/12/30
	 */
	public InputStream open()
		throws IOException, ZipException
	{
		// {@squirreljme.error BF0a Cannot open the entry because it is a
		// directory. (The name of the entry)}
		String s;
		if (isDirectory())
			throw new ZipException(String.format("BF0a %s", toString()));
		
		ZipBlockReader owner = this.owner;
		BlockAccessor accessor = this.accessor;
		long position = this.position;
		
		// {@squirreljme.error BF0b Could not read the central
		// directory data.}
		byte[] data = new byte[_CENTRAL_DIRECTORY_MIN_LENGTH];
		if (_CENTRAL_DIRECTORY_MIN_LENGTH != accessor.read(position,
			data, 0, _CENTRAL_DIRECTORY_MIN_LENGTH))
			throw new ZipException("BF0b");
		
		// The version needed to extract should not have the upper byte set
		// but some archive writing software sets the upper byte to match the
		// OS with the version in the made by bit.
		int ver = __ArrayData__.readUnsignedShort(
			_CENTRAL_DIRECTORY_EXTRACT_VERSION_OFFSET, data),
			made = __ArrayData__.readUnsignedShort(
			_CENTRAL_DIRECTORY_MADE_BY_VERSION_OFFSET, data);
		if ((ver & 0xFF00) != 0 && (made & 0xFF00) == (ver & 0xFF00))
			ver &= 0xFF;
		
		// {@squirreljme.error BF0c Cannot open the entry because it uses
		// too new of a version. (The version number)}
		if (_MAX_CENTRAL_DIR_VERSION < ver)
			throw new ZipException(String.format("BF0c %d", ver));
		
		// Need these later to determine how much data is available and how it
		// is stored.
		int method = __ArrayData__.readUnsignedShort(
				_CENTRAL_DIRECTORY_METHOD_OFFSET, data);
		int crc = __ArrayData__.readSignedInt(_CENTRAL_DIRECTORY_CRC_OFFSET,
			data);
		long uncompressed = __ArrayData__.readUnsignedInt(
				_CENTRAL_DIRECTORY_UNCOMPRESSED_OFFSET, data),
			compressed = __ArrayData__.readUnsignedInt(
				_CENTRAL_DIRECTORY_COMPRESSED_OFFSET, data);
			
		// Determine the offset to the local header which precedes the data
		// of the entry
		long lhoffset = owner._zipbaseaddr + __ArrayData__.readUnsignedInt(
			_CENTRAL_DIRECTORY_LOCAL_HEADER_OFFSET, data);
		
		// {@squirreljme.error BF0d Could not read the local file header from
		// the ZIP file.}
		byte[] header = new byte[_LOCAL_HEADER_MIN_LENGTH];
		if (_LOCAL_HEADER_MIN_LENGTH != accessor.read(lhoffset, header, 0,
			_LOCAL_HEADER_MIN_LENGTH))
			throw new ZipException("BF0d");
		
		// {@squirreljme.error BF0e The magic number for the local file header
		// is not valid.}
		if (__ArrayData__.readSignedInt(0, header) !=
			_LOCAL_HEADER_MAGIC_NUMBER)
			throw new ZipException("BF0e");
		
		// Need to know the file name and comment lengths, since they may
		// differ in the local header for some reason
		int lhfnl = __ArrayData__.readUnsignedShort(
				_LOCAL_HEADER_NAME_LENGTH_OFFSET, header), 
			lhcml = __ArrayData__.readUnsignedShort(
				_LOCAL_HEADER_COMMENT_LENGTH_OFFSET, header);
		
		// The base address of the data is after the local header position
		long database = lhoffset + _LOCAL_HEADER_MIN_LENGTH + lhfnl + lhcml;
		
		// Get base stream before compression
		InputStream base = new __BlockAccessorRegionInputStream__(accessor,
			database, compressed);
		
		// {@squirreljme.error BF0f Unknown compression method for entry. (The
		// method identifier)}
		ZipCompressionType ztype = ZipCompressionType.forMethod(method);
		if (ztype == null)
			throw new ZipException(String.format("BF0f %d", method));
		
		// Wrap input so it may be read
		InputStream algo = ztype.inputStream(base);
		
		// Need to calculate the CRC for the stream of data
		return new __CRCInputStream__(algo, crc);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/12/30
	 */
	@Override
	public String toString()
	{
		try
		{
			return __internalToString();
		}
			
		// {@squirreljme.error BF0g Could not read the name of the
		// entry.}
		catch (IOException e)
		{
			throw new RuntimeException("BF0g", e);
		}
	}
	
	/**
	 * Returns the internal representation of the string entry.
	 *
	 * @return The name of this entry.
	 * @throws IOException On read errors.
	 * @throws ZipException If the ZIP is malformed.
	 * @since 2017/01/03
	 */
	private final String __internalToString()
		throws IOException, ZipException
	{
		Reference<String> ref = this._name;
		String rv;
		
		// Need to load it?
		if (ref == null || null == (rv = ref.get()))
		{
			BlockAccessor accessor = this.accessor;
			long position = this.position;
			
			// {@squirreljme.error BF0h Could not read the central
			// directory data.}
			byte[] data = new byte[_CENTRAL_DIRECTORY_MIN_LENGTH];
			if (_CENTRAL_DIRECTORY_MIN_LENGTH != accessor.read(position,
				data, 0, _CENTRAL_DIRECTORY_MIN_LENGTH))
				throw new ZipException("BF0h");
			
			// Read file name length
			int fnl = __ArrayData__.readUnsignedShort(
				_CENTRAL_DIRECTORY_NAME_LENGTH_OFFSET, data);
			
			// {@squirreljme.error BF0i Could not read the file name.}
			byte[] rawname = new byte[fnl];
			if (fnl != accessor.read(
				position + _CENTRAL_DIRECTORY_MIN_LENGTH, rawname, 0, fnl))
				throw new ZipException("BF0i");
			
			// UTF-8 Encoded?
			if ((__ArrayData__.readUnsignedShort(
				_CENTRAL_DIRECTORY_FLAG_OFFSET, data) &
				GPF_ENCODING_UTF8) != 0)
				rv = new String(rawname, 0, fnl, "utf-8");
			
			// DOS codepage
			else
				rv = IBM437CodePage.toString(rawname, 0, fnl);
			
			// Store for later
			this._name = new WeakReference<>(rv);
		}
		
		return rv;
	}
}

