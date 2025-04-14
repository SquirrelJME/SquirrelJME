// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
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
	ARRAY('[', false, true, null),
	
	/** Byte. */
	BYTE('B', true, false, (byte)0),
	
	/** Character. */
	CHARACTER('C', true, false, '\0'),
	
	/** Object. */
	OBJECT('L', false, true, null),
	
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
	STRING('s', false, true, null),
	
	/** Thread. */
	THREAD('t', false, true, null),
	
	/** Thread Group. */
	THREAD_GROUP('g', false, true, null),
	
	/** Class Loader. */
	CLASS_LOADER('l', false, true, null),
	
	/** Class object. */
	CLASS_OBJECT('c', false, true, null),
	
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
	 * Determines the value tag from the given tag.
	 * 
	 * @param __tag The tag to parse. 
	 * @return The value tag or {@code null} if not valid.
	 * @since 2021/04/11
	 */
	public static JDWPValueTag fromTag(int __tag)
	{
		switch (__tag)
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
			case 'L':	return JDWPValueTag.OBJECT;
			case 'V':	return JDWPValueTag.VOID;
			case 's':	return JDWPValueTag.STRING;
			case 't':	return JDWPValueTag.THREAD;
			case 'g':	return JDWPValueTag.THREAD_GROUP;
			case 'l':	return JDWPValueTag.CLASS_LOADER;
			case 'c':	return JDWPValueTag.CLASS_OBJECT;
		}
		
		return null;
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
			case 'V':	return JDWPValueTag.VOID;
			case 's':	return JDWPValueTag.STRING;
			case 't':	return JDWPValueTag.THREAD;
			case 'g':	return JDWPValueTag.THREAD_GROUP;
			case 'l':	return JDWPValueTag.CLASS_LOADER;
			case 'c':	return JDWPValueTag.CLASS_OBJECT;
			
				// Dependant class types
			default:
				switch (__signature)
				{
					case "Ljava/lang/String;":
						return JDWPValueTag.STRING;
						
					case "Ljava/lang/Thread;":
					case "Lcc/squirreljme/jvm/mle/brackets/VMThreadBracket;":
						return JDWPValueTag.THREAD;
						
					case "Ljava/lang/Class;":
					case "Lcc/squirreljme/jvm/mle/brackets/TypeBracket;":
						return JDWPValueTag.CLASS_OBJECT;
				}
				
				return JDWPValueTag.OBJECT; 
		}
	}
}
