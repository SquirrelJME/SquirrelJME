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
	 * This is thrown when an attempt is made to read from a variable field
	 * with an array element that is not within the range of the specified
	 * length.
	 *
	 * @since 2016/03/08
	 */
	public static class ArrayOutOfBounds
		extends ZIPFormatException
	{
		/**
		 * Initializes the exception with the given range.
		 *
		 * @param __dx The element being read.
		 * @param __end The end range of the array.
		 * @since 2016/03/08
		 */
		public ArrayOutOfBounds(long __dx, long __end)
		{
			super("Read of element " + __dx + " not within bounds of [0, " +
				__end + "].");
		}
	}
	
	/**
	 * This is thrown when the number of entries in the index has been
	 * miscounted.
	 *
	 * @since 2016/03/06
	 */
	public static class EntryMiscount
		extends ZIPFormatException
	{
		/**
		 * Initializes the exception with the given and desired entry amounts.
		 *
		 * @param __was The number of entries read.
		 * @param __want The number of desired entries.
		 * @since 2016/03/06
		 */
		public EntryMiscount(int __was, int __want)
		{
			super("Expected " + __want + " entries, however only " + __was +
				" were read.");
		}
	}
	
	/**
	 * This is thrown when a magic number in the ZIP is not valid.
	 *
	 * @since 2016/03/06
	 */
	public static class IllegalMagic
		extends ZIPFormatException
	{
		/**
		 * Initializes the exception for the given magic values.
		 *
		 * @param __was What the magic number was.
		 * @param __want What the magic number was supposed to be.
		 * @since 2016/03/08
		 */
		public IllegalMagic(int __was, int __want)
		{
			super(String.format("Expected magic number %08x, however it was " +
				"%08x", __want, __was));
		}
	}
	
	/**
	 * This is thrown when an attempt is made to open a directory as an input
	 * stream.
	 *
	 * @since 2016/03/09
	 */
	public static class IsADirectory
		extends ZIPFormatException
	{
		/**
		 * Initializes the exception with the given directory path.
		 *
		 * @param __n The directory which was attempted to be opened as a
		 * stream.
		 * @since 2016/03/09
		 */
		public IsADirectory(String __n)
		{
			super("The entry '" + __n + "' is a directory.");
		}
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
	 * This is thrown when the requested element to read is not zero and the
	 * structured type is not variable.
	 *
	 * @since 2016/02/08
	 */
	public static class NonZeroArrayRead
		extends ZIPFormatException
	{
	}
	
	/**
	 * An entry in the ZIP file had no offset specified.
	 *
	 * @since 2016/03/06
	 */
	public static class NoOffsetSpecified
		extends ZIPFormatException
	{
		/**
		 * Initializes the exception with the given index.
		 *
		 * @param __dx The index that is missing the offset.
		 * @since 2016/03/06
		 */
		public NoOffsetSpecified(int __dx)
		{
			super("The entry " + __dx + " has no offset.");
		}
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
	
	/**
	 * This is thrown when a file inside of the ZIP is using a too new of a
	 * version.
	 *
	 * @since 2016/03/07
	 */
	public static class TooNew
		extends ZIPFormatException
	{
		/**
		 * Initializes the exception with the given version numbers.
		 *
		 * @param __a Version A.
		 * @param __b Version B.
		 * @since 2016/03/07
		 */
		public TooNew(int __a, int __b)
		{
			super("ZIP version " + __stringy(true, __a, __b) + " is newer " +
				"than " + __stringy(false, __a, __b) + ".");
		}
		
		/**
		 * Converts version values to a string.
		 *
		 * @param __max If {@code true} then the max version is used, otherwise
		 * the minimum will be used.
		 * @param __a Version A.
		 * @param __b Version B.
		 * @return A string representing the version.
		 * @since 2016/03/07
		 */
		private static String __stringy(boolean __max, int __a, int __b)
		{
			// Use the highest or smallest
			int use = (__max ? Math.max(__a, __b) : Math.min(__a, __b));
			
			// Perform simple division
			return (use / 10) + "." + (use % 10);
		}
	}
	
	/**
	 * This is thrown when an entry is compressed with an unknown method.
	 *
	 * @since 2016/03/09
	 */
	public static class UnknownCompressionMethod
		extends ZIPFormatException
	{
		/**
		 * Initializes the exception with the given name and the method id.
		 *
		 * @param __n The entry where a read was attempted.
		 * @param __meth The compression method used.
		 * @since 2016/03/09
		 */
		public UnknownCompressionMethod(String __n, int __meth)
		{
			super("The entry '" + __n + "' is compressed with an unknown " +
				"compression method with identifier " + __meth + ".");
		}
	}
}

