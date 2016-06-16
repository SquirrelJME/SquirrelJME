// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.mascot;

import java.io.InputStream;
import net.multiphasicapps.io.hex.HexInputStream;

/**
 * This is the encoding of the data image.
 *
 * @since 2016/06/16
 */
public enum DataEncoding
{
	/** Plain encoding. */
	PLAIN("")
	{
		/**
		 * {@inheritDoc}
		 * @Since 2016/06/16
		 */
		@Override
		public InputStream decode(InputStream __is)
			throws NullPointerException
		{
			// Check
			if (__is == null)
				throw new NullPointerException("NARG");
			
			return __is;
		}
	},
	
	/** Hexadecimal. */
	HEXADECIMAL(".hex")
	{
		/**
		 * {@inheritDoc}
		 * @Since 2016/06/16
		 */
		@Override
		public InputStream decode(InputStream __is)
			throws NullPointerException
		{
			// Check
			if (__is == null)
				throw new NullPointerException("NARG");
			
			return new HexInputStream(__is);
		}
	},
	
	/** Base 64. */
	BASE64(".b64")
	{
		/**
		 * {@inheritDoc}
		 * @Since 2016/06/16
		 */
		@Override
		public InputStream decode(InputStream __is)
			throws NullPointerException
		{
			// Check
			if (__is == null)
				throw new NullPointerException("NARG");
			
			throw new Error("TODO");
		}
	},
	
	/** End. */
	;
	
	/** The extension which is used. */
	protected final String extension;
	
	/**
	 * Initializes the data encoding.
	 *
	 * @param __x The extension to add to.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/06/16
	 */
	private DataEncoding(String __x)
		throws NullPointerException
	{
		// Check
		if (__x == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.extension = __x;
	}
	
	/**
	 * Decodes the given input stream.
	 *
	 * @param __is The input stream to decode.
	 * @return The stream which decodes the given stream.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/06/16
	 */
	public abstract InputStream decode(InputStream __is)
		throws NullPointerException;
	
	/**
	 * {@inheritDoc}
	 * @since 2016/06/16
	 */
	@Override
	public String toString()
	{
		return this.extension;
	}
}

