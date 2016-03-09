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

import java.nio.ByteBuffer;

/**
 * This interface is used to provide an interface to accessing the structured
 * ZIP data in a safer and more abstract way. This interface is ended to be
 * used with enumerations and has the primary purpose of remove a large number
 * of {@code static final} fields containing offsets.
 *
 * @since 2016/03/08
 */
public interface ZIPStructureElement
{
	/**
	 * Returns the offset to the data.
	 *
	 * @return The offset.
	 * @since 2016/03/08
	 */
	public abstract long offset();
	
	/**
	 * The type of data stored here.
	 *
	 * @return The type of data contained here.
	 * @since 2016/03/08
	 */
	public abstract Type type();
	
	/**
	 * Returns the other element which specifies the size of part of the
	 * structure.
	 *
	 * @return The other element specifying the length of this field, or
	 * {@code null} if this field has none or is not volatile.
	 * @since 2016/03/08
	 */
	public abstract ZIPStructureElement variableField();
	
	/**
	 * This is the type of data which is stored in this element.
	 *
	 * @since 2016/03/08
	 */
	public static enum Type
	{
		/** Signed 8-bit Integer. */
		INT8(1)
		{
			/**
			 * {@inheritDoc}
			 * @since 2016/03/08
			 */
			@Override
			public long read(ByteBuffer __bb)
				throws NullPointerException
			{
				return __bb.get();
			}
		},
		
		/** Signed 16-bit Integer. */
		INT16(2)
		{
			/**
			 * {@inheritDoc}
			 * @since 2016/03/08
			 */
			@Override
			public long read(ByteBuffer __bb)
				throws NullPointerException
			{
				return __bb.getShort();
			}
		},
		
		/** Signed 32-bit Integer. */
		INT32(4)
		{
			/**
			 * {@inheritDoc}
			 * @since 2016/03/08
			 */
			@Override
			public long read(ByteBuffer __bb)
				throws NullPointerException
			{
				return __bb.getInt();
			}
		},
		
		/** Signed 64-bit Integer. */
		INT64(8)
		{
			/**
			 * {@inheritDoc}
			 * @since 2016/03/08
			 */
			@Override
			public long read(ByteBuffer __bb)
				throws NullPointerException
			{
				// ByteBuffer in CLDC 8 does not have {@code getLong()} but
				// that is not a huge problem at all.
				int lo = __bb.getInt();
				return ((long)lo) | (((long)__bb.getInt()) << 32L);
			}
		},
		
		/** Unsigned 8-bit Integer. */
		UINT8(1)
		{
			/**
			 * {@inheritDoc}
			 * @since 2016/03/08
			 */
			@Override
			public long read(ByteBuffer __bb)
				throws NullPointerException
			{
				return ((int)__bb.get()) & 0xFF;
			}
		},
		
		/** Unsigned 16-bit Integer. */
		UINT16(2)
		{
			/**
			 * {@inheritDoc}
			 * @since 2016/03/08
			 */
			@Override
			public long read(ByteBuffer __bb)
				throws NullPointerException
			{
				return ((int)__bb.getShort()) & 0xFFFF;
			}
		},
		
		/** Unsigned 32-bit Integer. */
		UINT32(4)
		{
			/**
			 * {@inheritDoc}
			 * @since 2016/03/08
			 */
			@Override
			public long read(ByteBuffer __bb)
				throws NullPointerException
			{
				return ((long)__bb.getInt()) & 0xFFFF_FFFFL;
			}
		},
		
		/** End. */
		;
		
		/** The number of bytes this field consumes. */
		protected final int bytes;
		
		/**
		 * Initializes the type information.
		 *
		 * @param __b Byte size of this field.
		 * @since 2016/03/08
		 */
		private Type(int __b)
		{
			bytes = __b;
		}
		
		/**
		 * If given a byte buffer, this reads the given value from it and
		 * returns it as a {@code long} value to represent all values.
		 *
		 * The data in the byte buffer is assumed to be in little endian
		 * format.
		 *
		 * @param __bb The buffer to read bytes from.
		 * @throws NullPointerException On null arguments.
		 */
		public abstract long read(ByteBuffer __bb)
			throws NullPointerException;
		
		/**
		 * Returns the number of bytes this type takes up.
		 *
		 * @return The number of bytes used, if this is negative then it is
		 * undefined or variable.
		 * @since 2016/03/08
		 */
		public final int size()
		{
			return bytes;
		}
	}
}

