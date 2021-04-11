// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jdwp;

/**
 * The value tag for given values.
 *
 * @since 2021/03/15
 */
public enum JDWPValueTag
{
	/** Array. */
	ARRAY('[', false, false, null),
	
	/** Byte. */
	BYTE('B', true, false, (byte)0),
	
	/** Character. */
	CHARACTER('C', true, false, '\0'),
	
	/** Object. */
	OBJECT('L', false, false, null),
	
	/** Float. */
	FLOAT('F', true, false, 0F),
	
	/** Double. */
	DOUBLE('D', true, false, 0D),
	
	/** Integer. */
	INTEGER('I', true, false, 0),
	
	/** Long. */
	LONG('J', true, false, 0L),
	
	/** Short. */
	SHORT('S', true, false, (short)0),
	
	/** Void. */
	VOID('V', false, false, null),
	
	/** Boolean. */
	BOOLEAN('Z', true, false, false),
	
	/** String. */
	STRING('s', false, false, null),
	
	/** Thread. */
	THREAD('t', false, false, null),
	
	/** Thread Group. */
	THREAD_GROUP('g', false, false, null),
	
	/** Class Loader. */
	CLASS_LOADER('l', false, false, null),
	
	/** Class object. */
	CLASS_OBJECT('c', false, false, null),
	
	/* End. */
	;
	
	/** The tag value used. */
	public final char tag;
	
	/** Is this a number type? If not this is an object type. */
	public final boolean isNumber;
	
	/** Is this an object type? */
	public final boolean isObject;
	
	/** Default value representation. */
	public final Object defaultValue;
	
	/**
	 * Initializes the value tag.
	 * 
	 * @param __tag The tag used.
	 * @param __isNumber Is this a number type?
	 * @param __isObject Is this an object?
	 * @param __defaultValue The default value for fillers.
	 * @since 2021/03/17
	 */
	JDWPValueTag(char __tag, boolean __isNumber, boolean __isObject,
		Object __defaultValue)
	{
		this.tag = __tag;
		this.isNumber = __isNumber;
		this.isObject = __isObject;
		this.defaultValue = __defaultValue;
	}
	
	/**
	 * Determines the value tag from the given signature.
	 * 
	 * @param __signature The signature to read from. 
	 * @return The value tag.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/04/11
	 */
	public static JDWPValueTag fromSignature(String __signature)
		throws NullPointerException
	{
		if (__signature == null)
			throw new NullPointerException("NARG");
		
		// Depends on the first character mostly
		char first = (!__signature.isEmpty() ? __signature.charAt(0) : '\0');
		switch (first)
		{
			case '[':	return JDWPValueTag.ARRAY;
			case 'Z':	return JDWPValueTag.BOOLEAN;
			case 'B':	return JDWPValueTag.BYTE;
			case 'S':	return JDWPValueTag.SHORT;
			case 'C':	return JDWPValueTag.CHARACTER;
			case 'I':	return JDWPValueTag.INTEGER;
			case 'F':	return JDWPValueTag.FLOAT;
			case 'J':	return JDWPValueTag.LONG;
			case 'D':	return JDWPValueTag.DOUBLE;
			
				// Dependant class types
			default:
				switch (__signature)
				{
					case "java/lang/String":
						return JDWPValueTag.STRING;
						
					case "java/lang/Thread":
					case "cc/squirreljme/jvm/debug/SyntheticThread":
						return JDWPValueTag.THREAD;
						
					case "java/lang/Class":
					case "cc/squirreljme/jvm/debug/SyntheticClass":
						return JDWPValueTag.CLASS_LOADER;
						
					case "cc/squirreljme/jvm/debug/SyntheticThreadGroup":
						return JDWPValueTag.THREAD_GROUP;
				}
				
				return JDWPValueTag.OBJECT; 
		}
	}
}
