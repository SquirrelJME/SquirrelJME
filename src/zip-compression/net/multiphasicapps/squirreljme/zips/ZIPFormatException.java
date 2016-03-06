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

import java.io.IOException;

/**
 * This exception is thrown when the input ZIP file is not valid or is
 * malformed.
 *
 * @since 2016/03/02
 */
public class ZIPFormatException
	extends IOException
{
	/**
	 * Initializes exception with no message.
	 *
	 * @since 2016/03/02
	 */
	public ZIPFormatException()
	{
		super();
	}
	
	/**
	 * Initializes exception with the given message.
	 *
	 * @param __msg The exception message.
	 * @since 2016/03/02
	 */
	public ZIPFormatException(String __msg)
	{
		super(__msg);
	}
	
	/**
	 * Initializes exception with the given message and cause.
	 *
	 * @param __msg The exception message.
	 * @param __c The cause.
	 * @since 2016/03/02
	 */
	public ZIPFormatException(String __msg, Throwable __c)
	{
		super(__msg, __c);
	}
	
	/**
	 * This is thrown when the ZIP contains a negative entry count.
	 *
	 * @since 2016/03/05
	 */
	public static class NegativeEntryCount
		extends ZIPFormatException
	{
		/**
		 * Initializes a message for negative entries.
		 *
		 * @param __ec The entry count.
		 * @since 2016/03/05
		 */
		public NegativeEntryCount(int __ec)
		{
			super(__ec + " entries.");
		}
	}
	
	/**
	 * This is thrown when the end of the ZIP index (the central directory)
	 * could not be found.
	 *
	 * @since 2016/02/05
	 */
	public static class NoCentralDirectory
		extends ZIPFormatException
	{
	}
	
	/**
	 * This is thrown when the ZIP channel was not fully read.
	 *
	 * @since 2016/02/05
	 */
	public static class ShortRead
		extends ZIPFormatException
	{
		/**
		 * Initializes the short read message, the higher value is the expected
		 * count while the lower is the actual count.
		 *
		 * @param __a Byte count.
		 * @param __b Byte count.
		 * @since 2016/03/05
		 */
		public ShortRead(int __a, int __b)
		{
			super("Expected to read " + Math.max(__a, __b) + ", read " +
				Math.min(__a, __b) + ".");
		}
	}
}

