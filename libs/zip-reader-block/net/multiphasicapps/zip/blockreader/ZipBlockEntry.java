// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
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

/**
 * This represents a single entry within a ZIP file which may be opened.
 *
 * @since 2016/12/30
 */
public final class ZipBlockEntry
{
	/** The offset of the general purpose flags. */
	private static final int _CENTRAL_DIRECTORY_FLAG_OFFSET =
		8;
	
	/** The offset to the file name length. */
	private static final int _CENTRAL_DIRECTORY_NAME_LENGTH_OFFSET =
		28;
	
	/** The offset to the extra data length. */
	private static final int _CENTRAL_DIRECTORY_EXTRA_LENGTH_OFFSET =
		_CENTRAL_DIRECTORY_NAME_LENGTH_OFFSET + 2;
	
	/** The offset to the comment length. */
	private static final int _CENTRAL_DIRECTORY_COMMENT_LENGTH_OFFSET =
		_CENTRAL_DIRECTORY_EXTRA_LENGTH_OFFSET + 2;
	
	/** The minimum length of the central directory entry. */
	private static final int _CENTRAL_DIRECTORY_MIN_LENGTH =
		46;
	
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
	 * @since 2017/01/03
	 */
	public boolean isDirectory()
		throws IOException
	{
		return __internalToString().endsWith("/");
	}
	
	/**
	 * Opens the input stream for this entry's data.
	 *
	 * @return The entry data.
	 * @throws IOException If it could not be opened.
	 * @since 2016/12/30
	 */
	public InputStream open()
		throws IOException
	{
		// {@squirreljme.error CJ0g Cannot open the entry because it is a
		// directory. (The name of the entry)}
		String s;
		if (isDirectory())
			throw new IOException(String.format("CJ0g %s", toString()));
		
		throw new Error("TODO");
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
			
		// {@squirreljme.error CJ0b Could not read the name of the
		// entry.}
		catch (IOException e)
		{
			throw new RuntimeException("CJ0b", e);
		}
	}
	
	/**
	 * Returns the internal representation of the string entry.
	 *
	 * @return The name of this entry.
	 * @throws IOException On read errors.
	 * @since 2017/01/03
	 */
	private final String __internalToString()
		throws IOException
	{
		Reference<String> ref = this._name;
		String rv;
		
		// Need to load it?
		if (ref == null || null == (rv = ref.get()))
		{
			BlockAccessor accessor = this.accessor;
			long position = this.position;
			
			// {@squirreljme.error CJ0d Could not read the central
			// directory data.}
			byte[] data = new byte[_CENTRAL_DIRECTORY_MIN_LENGTH];
			if (_CENTRAL_DIRECTORY_MIN_LENGTH != accessor.read(position,
				data, 0, _CENTRAL_DIRECTORY_MIN_LENGTH))
				throw new IOException("CJ0d");
			
			// Read file name length
			int fnl = __ArrayData__.readUnsignedShort(
				_CENTRAL_DIRECTORY_NAME_LENGTH_OFFSET, data);
			
			// {@squirreljme.error CJ0f Could not read the file name.}
			byte[] rawname = new byte[fnl];
			if (fnl != accessor.read(
				position + _CENTRAL_DIRECTORY_MIN_LENGTH, rawname, 0, fnl))
				throw new IOException("CJ0f");
			
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

