// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.terp.rr;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import net.multiphasicapps.util.unmodifiable.UnmodifiableList;

/**
 * This represents the data type which is used to store the type of information
 * which is contained within a packet.
 *
 * @since 2016/06/01
 */
public enum RRDataType
{
	/** No data stored here. */
	NULL(null)
	{
		/**
		 * {@inheritDoc}
		 * @since 2016/06/03
		 */
		@Override
		public void write(DataOutputStream __dos, Object __v)
			throws ClassCastException, IOException, NullPointerException
		{
			// Even though this does nothing, a null stream is still invalid.
			if (__dos == null)
				throw new NullPointerException("NARG");
			
			// Null has no data, so nothing is written
		}
	},
	
	/** String. */
	STRING(String.class)
	{
		/**
		 * {@inheritDoc}
		 * @since 2016/06/03
		 */
		@Override
		public void write(DataOutputStream __dos, Object __v)
			throws ClassCastException, IOException, NullPointerException
		{
			__dos.writeUTF((String)__v);
		}
	},
	
	/** Boolean. */
	BOOLEAN(Boolean.class)
	{
		/**
		 * {@inheritDoc}
		 * @since 2016/06/03
		 */
		@Override
		public void write(DataOutputStream __dos, Object __v)
			throws ClassCastException, IOException, NullPointerException
		{
			__dos.writeByte((((Boolean)__v) ? 1 : 0));
		}
	},
	
	/** Byte. */
	BYTE(Byte.class)
	{
		/**
		 * {@inheritDoc}
		 * @since 2016/06/03
		 */
		@Override
		public void write(DataOutputStream __dos, Object __v)
			throws ClassCastException, IOException, NullPointerException
		{
			__dos.writeByte((Byte)__v);
		}
	},
	
	/** Short. */
	SHORT(Short.class)
	{
		/**
		 * {@inheritDoc}
		 * @since 2016/06/03
		 */
		@Override
		public void write(DataOutputStream __dos, Object __v)
			throws ClassCastException, IOException, NullPointerException
		{
			__dos.writeShort((Short)__v);
		}
	},
	
	/** Character. */
	CHARACTER(Character.class)
	{
		/**
		 * {@inheritDoc}
		 * @since 2016/06/03
		 */
		@Override
		public void write(DataOutputStream __dos, Object __v)
			throws ClassCastException, IOException, NullPointerException
		{
			__dos.writeChar((Character)__v);
		}
	},
	
	/** Integer. */
	INTEGER(Integer.class)
	{
		/**
		 * {@inheritDoc}
		 * @since 2016/06/03
		 */
		@Override
		public void write(DataOutputStream __dos, Object __v)
			throws ClassCastException, IOException, NullPointerException
		{
			__dos.writeInt((Integer)__v);
		}
	},
	
	/** Long. */
	LONG(Long.class)
	{
		/**
		 * {@inheritDoc}
		 * @since 2016/06/03
		 */
		@Override
		public void write(DataOutputStream __dos, Object __v)
			throws ClassCastException, IOException, NullPointerException
		{
			__dos.writeLong((Long)__v);
		}
	},
	
	/** Float. */
	FLOAT(Float.class)
	{
		/**
		 * {@inheritDoc}
		 * @since 2016/06/03
		 */
		@Override
		public void write(DataOutputStream __dos, Object __v)
			throws ClassCastException, IOException, NullPointerException
		{
			__dos.writeFloat((Float)__v);
		}
	},
	
	/** Double. */
	DOUBLE(Double.class)
	{
		/**
		 * {@inheritDoc}
		 * @since 2016/06/03
		 */
		@Override
		public void write(DataOutputStream __dos, Object __v)
			throws ClassCastException, IOException, NullPointerException
		{
			__dos.writeDouble((Double)__v);
		}
	},
	
	/** String Array. */
	STRING_ARRAY(String[].class)
	{
		/**
		 * {@inheritDoc}
		 * @since 2016/06/03
		 */
		@Override
		public void write(DataOutputStream __dos, Object __v)
			throws ClassCastException, IOException, NullPointerException
		{
			throw new Error("TODO");
		}
	},
	
	/** Boolean Array. */
	BOOLEAN_ARRAY(boolean[].class)
	{
		/**
		 * {@inheritDoc}
		 * @since 2016/06/03
		 */
		@Override
		public void write(DataOutputStream __dos, Object __v)
			throws ClassCastException, IOException, NullPointerException
		{
			throw new Error("TODO");
		}
	},
	
	/** Byte Array. */
	BYTE_ARRAY(byte[].class)
	{
		/**
		 * {@inheritDoc}
		 * @since 2016/06/03
		 */
		@Override
		public void write(DataOutputStream __dos, Object __v)
			throws ClassCastException, IOException, NullPointerException
		{
			throw new Error("TODO");
		}
	},
	
	/** Short Array. */
	SHORT_ARRAY(short[].class)
	{
		/**
		 * {@inheritDoc}
		 * @since 2016/06/03
		 */
		@Override
		public void write(DataOutputStream __dos, Object __v)
			throws ClassCastException, IOException, NullPointerException
		{
			throw new Error("TODO");
		}
	},
	
	/** Character Array. */
	CHARACTER_ARRAY(char[].class)
	{
		/**
		 * {@inheritDoc}
		 * @since 2016/06/03
		 */
		@Override
		public void write(DataOutputStream __dos, Object __v)
			throws ClassCastException, IOException, NullPointerException
		{
			throw new Error("TODO");
		}
	},
	
	/** Integer Array. */
	INTEGER_ARRAY(int[].class)
	{
		/**
		 * {@inheritDoc}
		 * @since 2016/06/03
		 */
		@Override
		public void write(DataOutputStream __dos, Object __v)
			throws ClassCastException, IOException, NullPointerException
		{
			throw new Error("TODO");
		}
	},
	
	/** Long Array. */
	LONG_ARRAY(long[].class)
	{
		/**
		 * {@inheritDoc}
		 * @since 2016/06/03
		 */
		@Override
		public void write(DataOutputStream __dos, Object __v)
			throws ClassCastException, IOException, NullPointerException
		{
			throw new Error("TODO");
		}
	},
	
	/** Float Array. */
	FLOAT_ARRAY(float[].class)
	{
		/**
		 * {@inheritDoc}
		 * @since 2016/06/03
		 */
		@Override
		public void write(DataOutputStream __dos, Object __v)
			throws ClassCastException, IOException, NullPointerException
		{
			throw new Error("TODO");
		}
	},
	
	/** Double Array. */
	DOUBLE_ARRAY(double[].class)
	{
		/**
		 * {@inheritDoc}
		 * @since 2016/06/03
		 */
		@Override
		public void write(DataOutputStream __dos, Object __v)
			throws ClassCastException, IOException, NullPointerException
		{
			throw new Error("TODO");
		}
	},
	
	/** End. */
	;
	
	/** The types which are available for quick lookup. */
	public static final List<RRDataType> TYPES =
		UnmodifiableList.<RRDataType>of(
			Arrays.<RRDataType>asList(values()));
	
	/** The class type for the data here. */
	protected final Class<?> type;
	
	/**
	 * Initializes the data type information.
	 *
	 * @param __cl The class type the data uses.
	 * @since 2016/06/01
	 */
	private RRDataType(Class<?> __cl)
	{
		// Set
		this.type = __cl;
		
		// {@squirreljme.error BC0f Only 255 type codes are supported.}
		if (ordinal() > 255)
			throw new RuntimeException("BC0f");
	}
	
	/**
	 * Writes the given value to the data stream.
	 *
	 * @param __dos The stream to write to.
	 * @param __v The value to write.
	 * @throws ClassCastException If the value is of the wrong type.
	 * @throws IOException On write errors.
	 * @throws NullPointerException If the output stream is null or the
	 * value is null and it cannot be null.
	 * @since 2016/06/03
	 */
	public abstract void write(DataOutputStream __dos, Object __v)
		throws ClassCastException, IOException, NullPointerException;
	
	/**
	 * Checks whether the given class can be used as data in a packet.
	 *
	 * @param __cl The class to check.
	 * @return {@code true} if it can be used in a packet.
	 * @since 2016/06/01
	 */
	public static boolean isValidClass(Class<?> __cl)
	{
		// Null is always valid
		if (__cl == null)
			return true;
		
		// Find one that can be assigned
		for (RRDataType dt : TYPES)
			if (dt.type != null && __cl.isAssignableFrom(dt.type))
				return true;
		
		// Not found
		return false;
	}
	
	/**
	 * Checks whether the given object can be used as data in a packet.
	 *
	 * @param __o The object to check.
	 * @return {@code true} if it can be used in a packet.
	 * @since 2016/06/01
	 */
	public static boolean isValidObject(Object __o)
	{
		// Null is always valid
		if (__o == null)
			return true;
		
		// Use class of this class
		return isValidClass(__o.getClass());
	}
	
	/**
	 * Locates the data type for the given object based on its class.
	 *
	 * @param __v The value to get the type for.
	 * @return The data type this is associated with.
	 * @since 2016/06/03
	 */
	public static RRDataType of(Object __v)
	{
		// Null is null
		if (__v == null)
			return NULL;
		
		// Find it
		Class<?> cl = __v.getClass();
		for (RRDataType dt : TYPES)
			if (dt.type != null && cl.isAssignableFrom(dt.type))
				return dt;
		
		// Not found
		return null;
	}
}

