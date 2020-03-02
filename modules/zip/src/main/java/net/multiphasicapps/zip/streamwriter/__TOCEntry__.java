// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.zip.streamwriter;

import net.multiphasicapps.zip.ZipCompressionType;

/**
 * This is used to store the temporary information which is used to later
 * write the central directory.
 *
 * @since 2016/07/15
 */
class __TOCEntry__
{
	/** The local file position. */
	final long _localposition;
	
	/** The name of the entry. */
	final byte[] _name;
	
	/** The compression type. */
	final ZipCompressionType _compression;
	
	/** The uncompressed size. */
	volatile long _uncompressed;
	
	/** The compressed size. */
	volatile long _compressed;
	
	/** The CRC32 of the entry. */
	volatile int _crc;
	
	/**
	 * Initializes the table of contents entry.
	 *
	 * @param __lfp The local file position.
	 * @param __name The file name.
	 * @param __comp The compression used.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/15
	 */
	__TOCEntry__(long __lfp, byte[] __name, ZipCompressionType __comp)
		throws NullPointerException
	{
		// Check
		if (__name == null || __comp == null)
			throw new NullPointerException("NARG");
		
		// Set
		this._localposition = __lfp;
		this._name = __name;
		this._compression = __comp;
	}
}

