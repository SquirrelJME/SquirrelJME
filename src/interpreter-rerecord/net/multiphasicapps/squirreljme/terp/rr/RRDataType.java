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
			// Array could be empty
			if (__dos == null)
				throw new NullPointerException("NARG");
			
			// Write array elements
			String[] v = (String[])__v;
			int n = v.length;
			__dos.writeInt(n);
			for (int i = 0; i < n; i++)
			{
				String q = v[i];
				
				// Strings may be null
				if (q == null)
					__dos.writeByte(0);
				
				// Otherwise write it
				else
				{
					__dos.writeByte(1);
					__dos.writeUTF(q);
				}
			}
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
			// Array could be empty
			if (__dos == null)
				throw new NullPointerException("NARG");
			
			// Write array elements
			boolean[] v = (boolean[])__v;
			int n = v.length;
			__dos.writeInt(n);
			for (int i = 0; i < n; i++)
				__dos.writeByte((v[i] ? 1 : 0));
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
			// Array could be empty
			if (__dos == null)
				throw new NullPointerException("NARG");
			
			// Write array elements
			byte[] v = (byte[])__v;
			int n = v.length;
			__dos.writeInt(n);
			for (int i = 0; i < n; i++)
				__dos.writeByte(v[i]);
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
			// Array could be empty
			if (__dos == null)
				throw new NullPointerException("NARG");
			
			// Write array elements
			short[] v = (short[])__v;
			int n = v.length;
			__dos.writeInt(n);
			for (int i = 0; i < n; i++)
				__dos.writeShort(v[i]);
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
			// Array could be empty
			if (__dos == null)
				throw new NullPointerException("NARG");
			
			// Write array elements
			char[] v = (char[])__v;
			int n = v.length;
			__dos.writeInt(n);
			for (int i = 0; i < n; i++)
				__dos.writeChar(v[i]);
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
			// Array could be empty
			if (__dos == null)
				throw new NullPointerException("NARG");
			
			// Write array elements
			int[] v = (int[])__v;
			int n = v.length;
			__dos.writeInt(n);
			for (int i = 0; i < n; i++)
				__dos.writeInt(v[i]);
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
			// Array could be empty
			if (__dos == null)
				throw new NullPointerException("NARG");
			
			// Write array elements
			long[] v = (long[])__v;
			int n = v.length;
			__dos.writeInt(n);
			for (int i = 0; i < n; i++)
				__dos.writeLong(v[i]);
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
			// Array could be empty
			if (__dos == null)
				throw new NullPointerException("NARG");
			
			// Write array elements
			float[] v = (float[])__v;
			int n = v.length;
			__dos.writeInt(n);
			for (int i = 0; i < n; i++)
				__dos.writeFloat(v[i]);
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
			// Array could be empty
			if (__dos == null)
				throw new NullPointerException("NARG");
			
			// Write array elements
			double[] v = (double[])__v;
			int n = v.length;
			__dos.writeInt(n);
			for (int i = 0; i < n; i++)
				__dos.writeDouble(v[i]);
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

