// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.exe.elf;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.IOException;

/**
 * This stores temporary namespace information within the ELF.
 *
 * @since 2016/08/16
 */
final class __Namespace__
{
	/** The ELF output. */
	protected final ELFOutput output;
	
	/** The name of this namespace. */
	final String _name;
	
	/** The namespace data. */
	final byte[] _data;
	
	/** The namespace length. */
	final int _length;
	
	/**
	 * Initializes the namespace informaiton and copies data from the given
	 * input stream.
	 *
	 * @param __eo The owning ELF.
	 * @param __n The name of the namespace.
	 * @param __is Is input stream of the namespace data.
	 */
	__Namespace__(ELFOutput __eo, String __n, InputStream __is)
		throws IOException, NullPointerException
	{
		// Check
		if (__n == null || __is == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.output = __eo;
		this._name = __n;
		
		// Copy the data
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream())
		{
			// Copy all of the data
			byte[] buf = new byte[4096];
			for (;;)
			{
				int rc = __is.read(buf);
				
				// EOF?
				if (rc < 0)
					break;
				
				// Copy
				baos.write(buf, 0, rc);
			}
			
			// Setup buffer
			byte[] dest = baos.toByteArray();
			this._data = dest;
			this._length = dest.length;
		}
	}
}

