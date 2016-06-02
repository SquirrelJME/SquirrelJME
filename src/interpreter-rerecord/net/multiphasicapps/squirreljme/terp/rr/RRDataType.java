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
	NULL(null),
	
	/** String. */
	STRING(String.class),
	
	/** Boolean. */
	BOOLEAN(Boolean.class),
	
	/** Byte. */
	BYTE(Byte.class),
	
	/** Short. */
	SHORT(Short.class),
	
	/** Character. */
	CHARACTER(Character.class),
	
	/** Integer. */
	INTEGER(Integer.class),
	
	/** Long. */
	LONG(Long.class),
	
	/** Float. */
	FLOAT(Float.class),
	
	/** Double. */
	DOUBLE(Double.class),
	
	/** String Array. */
	STRING_ARRAY(String[].class),
	
	/** Boolean Array. */
	BOOLEAN_ARRAY(boolean[].class),
	
	/** Byte Array. */
	BYTE_ARRAY(byte[].class),
	
	/** Short Array. */
	SHORT_ARRAY(short[].class),
	
	/** Character Array. */
	CHARACTER_ARRAY(char[].class),
	
	/** Integer Array. */
	INTEGER_ARRAY(int[].class),
	
	/** Long Array. */
	LONG_ARRAY(long[].class),
	
	/** Float Array. */
	FLOAT_ARRAY(float[].class),
	
	/** Double Array. */
	DOUBLE_ARRAY(double[].class),
	
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
	}
	
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
}

