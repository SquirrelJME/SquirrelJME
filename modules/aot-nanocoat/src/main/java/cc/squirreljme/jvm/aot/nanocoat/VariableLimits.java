// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.aot.nanocoat;

import cc.squirreljme.jvm.aot.nanocoat.common.JvmPrimitiveType;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.cldc.util.IntegerIntegerArray;
import java.util.Arrays;
import java.util.Comparator;

/**
 * Contains basic information on the limit of variables.
 *
 * @since 2023/08/09
 */
public class VariableLimits
	implements Comparable<VariableLimits>
{
	/** The type counts used. */
	private final int[] _typeCounts;
	
	/**
	 * Initializes the variable limits.
	 *
	 * @param __typeCounts The type counts.
	 * @throws IllegalArgumentException If the counts are not valid or does
	 * not match the primitive type count.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/08/13
	 */
	public VariableLimits(int[] __typeCounts)
		throws IllegalArgumentException, NullPointerException
	{
		if (__typeCounts == null)
			throw new NullPointerException("NARG");
		
		/* {@squirreljme.error NC06 Needs to be Java primitive type count.} */
		if (__typeCounts.length != JvmPrimitiveType.NUM_JAVA_TYPES)
			throw new IllegalArgumentException("NC06");
		
		/* {@squirreljme.error NC07 Negative type count.} */
		__typeCounts = __typeCounts.clone();
		for (int count : __typeCounts)
			if (count < 0)
				throw new IllegalArgumentException("NC07");
		
		// Store
		this._typeCounts = __typeCounts;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/09/02
	 */
	@Override
	public int compareTo(VariableLimits __b)
	{
		int[] a = this._typeCounts;
		int[] b = __b._typeCounts;
		
		// Compare lengths first
		int len = a.length;
		int result = len - b.length;
		if (result != 0)
			return result;
		
		// Then individual values
		for (int i = 0; i < len; i++)
		{
			result = a[i] - b[i];
			if (result != 0)
				return result;
		}
		
		// Is the same
		return 0;
	}
	
	/**
	 * Returns the count for the given Java type.
	 *
	 * @param __type The Java type to use.
	 * @return The count of the variables.
	 * @throws IllegalArgumentException If the type is not a valid Java type.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/09/02
	 */
	public int count(JvmPrimitiveType __type)
		throws IllegalArgumentException, NullPointerException
	{
		if (__type == null)
			throw new NullPointerException("NARG");
		
		/* {@squirreljme.error NC08 Invalid Java type.} */
		if (__type.ordinal() >= JvmPrimitiveType.NUM_JAVA_TYPES)
			throw new IllegalArgumentException("NC08");
		
		return this._typeCounts[__type.ordinal()];
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/08/13
	 */
	@Override
	public boolean equals(Object __o)
	{
		if (this == __o)
			return true;
		if (!(__o instanceof VariableLimits))
			return false;
		
		VariableLimits o = (VariableLimits)__o;
		return Arrays.equals(this._typeCounts, o._typeCounts);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/08/13
	 */
	@Override
	public int hashCode()
	{
		return new IntegerIntegerArray(this._typeCounts).hashCode();
	}
}
