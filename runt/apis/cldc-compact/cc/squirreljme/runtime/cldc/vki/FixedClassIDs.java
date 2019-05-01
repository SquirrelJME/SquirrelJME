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
public final class FixedClassIDs
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
	
	/** Array of byte arrays. */
	public static final int PRIMITIVE_BYTE_ARRAY_ARRAY =
		27;
	
	/** Boolean. */
	public static final int BOXED_BOOLEAN =
		28;
	
	/** Byte. */
	public static final int BOXED_BYTE =
		29;
	
	/** Short. */
	public static final int BOXED_SHORT =
		30;
	
	/** Character. */
	public static final int BOXED_CHARACTER =
		31;
	
	/** Integer. */
	public static final int BOXED_INTEGER =
		32;
	
	/** Long. */
	public static final int BOXED_LONG =
		33;
	
	/** Float. */
	public static final int BOXED_FLOAT =
		34;
	
	/** Double. */
	public static final int BOXED_DOUBLE =
		35;
	
	/** Memory UTF Sequence. */
	public static final int MEMORY_UTF_SEQUENCE =
		36;
	
	/** Number of fixed IDs. */
	public static final int MAX_FIXED =
		48;
	
	/**
	 * Not used.
	 *
	 * @since 2019/04/27
	 */
	private FixedClassIDs()
	{
	}
	
	/**
	 * Returns the fixed ID of the given class or {@code -1} if not one.
	 *
	 * @return The fixed ID or {@code -1} if it is not one.
	 * @since 2019/04/27
	 */
	public static final int of(String __cl)
	{
		switch (__cl)
		{
			case "boolean":
				return PRIMITIVE_BOOLEAN;
			case "byte":
				return PRIMITIVE_BYTE;
			case "short":
				return PRIMITIVE_SHORT;
			case "char":
				return PRIMITIVE_CHARACTER;
			case "int":
				return PRIMITIVE_INTEGER;
			case "long":
				return PRIMITIVE_LONG;
			case "float":
				return PRIMITIVE_FLOAT;
			case "double":
				return PRIMITIVE_DOUBLE;
			case "[Z":
				return PRIMITIVE_BOOLEAN_ARRAY;
			case "[B":
				return PRIMITIVE_BYTE_ARRAY;
			case "[[B":
				return PRIMITIVE_BYTE_ARRAY_ARRAY;
			case "[S":
				return PRIMITIVE_SHORT_ARRAY;
			case "[C":
				return PRIMITIVE_CHARACTER_ARRAY;
			case "[I":
				return PRIMITIVE_INTEGER_ARRAY;
			case "[J":
				return PRIMITIVE_LONG_ARRAY;
			case "[F":
				return PRIMITIVE_FLOAT_ARRAY;
			case "[D":
				return PRIMITIVE_DOUBLE_ARRAY;
			case "java/lang/Object":
				return OBJECT;
			case "java/lang/Throwable":
				return THROWABLE;
			case "java/lang/Class":
				return CLASS;
			case "java/lang/String":
				return STRING;
			case "Ljava/lang/Thread;":
				return THREAD;
				
			case "Ljava/lang/Boolean;":
				return BOXED_BOOLEAN;
			case "Ljava/lang/Byte;":
				return BOXED_BYTE;
			case "Ljava/lang/Short;":
				return BOXED_SHORT;
			case "Ljava/lang/Character;":
				return BOXED_CHARACTER;
			case "Ljava/lang/Integer;":
				return BOXED_INTEGER;
			case "Ljava/lang/Long;":
				return BOXED_LONG;
			case "Ljava/lang/Float;":
				return BOXED_FLOAT;
			case "Ljava/lang/Double;":
				return BOXED_DOUBLE;
				
			case "[Ljava/lang/Object;":
				return OBJECT_ARRAY;
			case "[Ljava/lang/String;":
				return STRING_ARRAY;
				
			case "cc/squirreljme/runtime/cldc/vki/Kernel":
				return KERNEL;
			case "cc/squirreljme/runtime/cldc/lang/ClassData":
				return CLASSDATA;
			case "cc/squirreljme/runtime/cldc/lang/ClassDataV2":
				return CLASSDATAV2;
			
			case "cc/squirreljme/runtime/cldc/string/MemoryUTFSequence":
				return MEMORY_UTF_SEQUENCE;
		}
		
		// Not a fixed type
		return -1;
	}
}

