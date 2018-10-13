// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package java.lang;

import cc.squirreljme.runtime.cldc.annotation.ImplementationNote;
import cc.squirreljme.runtime.cldc.asm.ObjectAccess;
import cc.squirreljme.runtime.cldc.string.SingleCharacterSequence;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

public final class Character
	implements Comparable<Character>
{
	public static final int MAX_RADIX =
		36;
	
	public static final char MAX_VALUE =
		65535;
	
	public static final int MIN_RADIX =
		2;
	
	public static final char MIN_VALUE =
		0;
	
	public static final int SIZE =
		16;
	
	/** The class representing the primitive type. */
	public static final Class<Character> TYPE =
		ObjectAccess.<Character>classByNameType("char");
	
	/** The character value. */
	private final char _value;
	
	/** The string representation of this value. */
	private Reference<String> _string;
	
	/**
	 * Initializes the boxed character.
	 *
	 * @param __v The character to use.
	 * @since 2018/10/11
	 */
	public Character(char __v)
	{
		this._value = __v;
	}
	
	/**
	 * Returns the character value.
	 *
	 * @return The character value.
	 * @since 2018/10/11
	 */
	public char charValue()
	{
		return this._value;
	}
	
	public int compareTo(Character __a)
	{
		throw new todo.TODO();
	}
	
	@Override
	public boolean equals(Object __a)
	{
		throw new todo.TODO();
	}
	
	public int hashCode()
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/10/12
	 */
	@Override
	public String toString()
	{
		Reference<String> ref = this._string;
		String rv;
		
		// We can represent a string for our single character as this
		// special sequence instead of just creating a new temporary string
		// just to store a single character or creating some kind of array.
		if (ref == null || null == (rv = ref.get()))
			this._string = new WeakReference<>(
				(rv = new String(new SingleCharacterSequence(this._value))));
		
		return rv;
	}
	
	public static int digit(char __a, int __b)
	{
		throw new todo.TODO();
	}
	
	public static char forDigit(int __a, int __b)
	{
		throw new todo.TODO();
	}
	
	public static boolean isDigit(char __a)
	{
		throw new todo.TODO();
	}
	
	public static boolean isISOControl(char __a)
	{
		throw new todo.TODO();
	}
	
	public static boolean isLowerCase(char __a)
	{
		throw new todo.TODO();
	}
	
	public static boolean isSpaceChar(char __a)
	{
		throw new todo.TODO();
	}
	
	public static boolean isUpperCase(char __a)
	{
		throw new todo.TODO();
	}
	
	public static boolean isWhitespace(char __a)
	{
		throw new todo.TODO();
	}
	
	public static char toLowerCase(char __a)
	{
		throw new todo.TODO();
	}
	
	public static String toString(char __a)
	{
		throw new todo.TODO();
	}
	
	public static char toUpperCase(char __a)
	{
		throw new todo.TODO();
	}
	
	/**
	 * Boxes the specified value.
	 *
	 * @param __v The value to box.
	 * @return The resulting character value.
	 * @since 2018/10/11
	 */
	@ImplementationNote("This is not cached.")
	public static Character valueOf(char __v)
	{
		return new Character(__v);
	}
}

