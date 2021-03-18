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
	ARRAY('[', false),
	
	/** Byte. */
	BYTE('B', true),
	
	/** Character. */
	CHARACTER('C', true),
	
	/** Object. */
	OBJECT('L', false),
	
	/** Float. */
	FLOAT('F', true),
	
	/** Double. */
	DOUBLE('D', true),
	
	/** Integer. */
	INTEGER('I', true),
	
	/** Long. */
	LONG('J', true),
	
	/** Short. */
	SHORT('S', true),
	
	/** Void. */
	VOID('V', false),
	
	/** Boolean. */
	BOOLEAN('Z', true),
	
	/** String. */
	STRING('s', false),
	
	/** Thread. */
	THREAD('t', false),
	
	/** Thread Group. */
	THREAD_GROUP('g', false),
	
	/** Class Loader. */
	CLASS_LOADER('l', false),
	
	/** Class object. */
	CLASS_OBJECT('c', false),
	
	/* End. */
	;
	
	/** The tag value used. */
	public final char tag;
	
	/** Is this a number type? If not this is an object type. */
	public final boolean isNumber;
	
	/**
	 * Initializes the value tag.
	 * 
	 * @param __tag The tag used.
	 * @param __isNumber Is this a number type?
	 * @since 2021/03/17
	 */
	JDWPValueTag(char __tag, boolean __isNumber)
	{
		this.tag = __tag;
		this.isNumber = __isNumber;
	}
}
