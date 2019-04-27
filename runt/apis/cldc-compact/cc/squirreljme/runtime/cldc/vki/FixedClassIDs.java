// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc.vki;

/**
 * These are classes which always have a fixed identifier in the class table
 * so that they always refer to the same classes at all times.
 *
 * @since 2019/04/21
 */
public interface FixedClassIDs
{
	/** No Class. */
	public static final int NONE =
		0;
	
	/** Primitive boolean. */
	public static final int PRIMITIVE_BOOLEAN =
		1;
	
	/** Primitive byte. */
	public static final int PRIMITIVE_BYTE =
		2;
	
	/** Primitive short. */
	public static final int PRIMITIVE_SHORT =
		3;
	
	/** Primitive char. */
	public static final int PRIMITIVE_CHARACTER =
		4;
	
	/** Primitive int. */
	public static final int PRIMITIVE_INTEGER =
		5;
	
	/** Primitive long. */
	public static final int PRIMITIVE_LONG =
		6;
	
	/** Primitive float. */
	public static final int PRIMITIVE_FLOAT =
		7;
	
	/** Primitive double. */
	public static final int PRIMITIVE_DOUBLE =
		8;
	
	/** Primitive boolean array. */
	public static final int PRIMITIVE_BOOLEAN_ARRAY =
		9;
	
	/** Primitive byte array. */
	public static final int PRIMITIVE_BYTE_ARRAY =
		10;
	
	/** Primitive short array. */
	public static final int PRIMITIVE_SHORT_ARRAY =
		11;
	
	/** Primitive char array. */
	public static final int PRIMITIVE_CHARACTER_ARRAY =
		12;
	
	/** Primitive int array. */
	public static final int PRIMITIVE_INTEGER_ARRAY =
		13;
	
	/** Primitive long array. */
	public static final int PRIMITIVE_LONG_ARRAY =
		14;
	
	/** Primitive float array. */
	public static final int PRIMITIVE_FLOAT_ARRAY =
		15;
	
	/** Primitive double array. */
	public static final int PRIMITIVE_DOUBLE_ARRAY =
		16;
	
	/** Object. */
	public static final int OBJECT =
		17;
	
	/** Throwable. */
	public static final int THROWABLE =
		18;
	
	/** Class. */
	public static final int CLASS =
		19;
	
	/** String. */
	public static final int STRING =
		20;
	
	/** Kernel. */
	public static final int KERNEL =
		21;
	
	/** ClassData. */
	public static final int CLASSDATA =
		22;
	
	/** ClassDataV2. */
	public static final int CLASSDATAV2 =
		23;
	
	/** Thread. */
	public static final int THREAD =
		24;
	
	/** Object array. */
	public static final int OBJECT_ARRAY =
		25;
	
	/** String array. */
	public static final int STRING_ARRAY =
		26;
	
	/** Number of fixed IDs. */
	public static final int MAX_FIXED =
		27;
}

