// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.c;

import java.util.Arrays;
import java.util.List;
import net.multiphasicapps.collections.UnmodifiableList;

/**
 * The type of number something is.
 *
 * @since 2023/05/29
 */
public enum CNumberType
{
	/** Boolean. */
	JBOOLEAN(Boolean.class, "UINT8_C"),
	
	/** Signed Byte, everything can write one. */
	JBYTE(Byte.class, null),
	
	/** Signed Short. */
	JSHORT(Short.class, "INT16_C"),
	
	/** Character. */
	JCHAR(Character.class, "UINT16_C"),
	
	/** Integer. */
	JINT(Integer.class, "INT32_C"),
	
	/** Long. */
	JLONG(Long.class, "INT64_C"),
	
	/** Float. */
	JFLOAT(Float.class, "UINT32_C"),
	
	/** Double. */
	JDOUBLE(Double.class, "UINT64_C"),
	
	/* End. */
	;
	
	/** Values for this. */
	public static final List<CNumberType> VALUES =
		UnmodifiableList.of(Arrays.asList(CNumberType.values()));
	
	/** The class type used. */
	public final Class<?> classType;
	
	/** The type prefix. */
	public final String prefix;
	
	/**
	 * Initializes the number type.
	 * 
	 * @param __class The class of the type.
	 * @param __prefix The prefix used.
	 * @since 2023/05/29
	 */
	CNumberType(Class<?> __class, String __prefix)
		throws NullPointerException
	{
		if (__class == null)
			throw new NullPointerException("NARG");
		
		this.classType = __class;
		this.prefix = __prefix;
	}
}
