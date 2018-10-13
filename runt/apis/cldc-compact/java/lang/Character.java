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
import cc.squirreljme.runtime.cldc.i18n.DefaultLocale;
import cc.squirreljme.runtime.cldc.string.SingleCharacterSequence;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * This is a boxed representation of {@link char}.
 *
 * @since 2018/10/13
 */
public final class Character
	implements Comparable<Character>
{
	/** The maximum radix for digit conversions. */
	public static final int MAX_RADIX =
		36;
	
	/** The maximum value for characters. */
	public static final char MAX_VALUE =
		65535;
	
	/** The minimum radix for digit conversions. */
	public static final int MIN_RADIX =
		2;
	
	/** The minimum value for characters. */
	public static final char MIN_VALUE =
		0;
	
	/** The number of bits used to represent a character. */
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
	
	/**
	 * This compares the numerical value for characters, it does not depend
	 * on locale at all.
	 *
	 * {@inheritDoc}
	 * @since 2018/10/13
	 */
	@Override
	public int compareTo(Character __o)
		throws NullPointerException
	{
		char a = this._value,
			b = __o._value;
		
		return a - b;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/10/13
	 */
	@Override
	public boolean equals(Object __o)
	{
		if (__o == this)
			return true;
		
		if (!(__o instanceof Character))
			return false;
		
		return this._value == ((Character)__o)._value;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/10/13
	 */
	@Override
	public int hashCode()
	{
		return this._value;
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
	
	/**
	 * Returns the digit for the given character and radix.
	 *
	 * @param __c The character to decode.
	 * @param __r The radix used.
	 * @return The digit for the given radix, if the character or the radix is
	 * not valid then {@code -1} is returned.
	 * @since 2018/10/13
	 */
	public static int digit(char __c, int __r)
	{
		// Invalid radix
		if (__r < Character.MIN_RADIX || __r > Character.MAX_RADIX)
			return -1;
		
		// Decode character
		int rv;
		if (__c >= 'a' && __c <= 'z')
			rv = 10 + (__c - 'a');
		else if (__c >= 'A' && __c <= 'Z')
			rv = 10 + (__c - 'A');
		else if (__c >= '0' && __c <= '9')
			rv = __c - '0';
		
		// Not valid
		else
			return -1;
		
		// Out of bounds of the radix
		if (rv >= __r)
			return -1;
		return rv;
	}
	
	/**
	 * Returns the character for the given digit and radix.
	 *
	 * @param __dig The digit to convert to a character.
	 * @param __r The radix to use for conversion.
	 * @return The character for the digit or NUL if the digit is out of range
	 * or the radix is out of range.
	 * @since 2018/10/13
	 */
	public static char forDigit(int __dig, int __r)
	{
		if (__dig < 0 || __r < Character.MIN_RADIX ||
			__r > Character.MAX_RADIX)
			return '\0';
		
		if (__dig < 10)
			return (char)('0' + __dig);
		return (char)('a' + (__dig - 10));
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
	
	/**
	 * Converts the specified character to lower case without considering
	 * locale.
	 *
	 * @param __c The character to convert.
	 * @return The converted character.
	 * @since 2018/10/13
	 */
	@ImplementationNote("CLDC only supports Latin-1 and this method has no " +
		"locale support.")
	public static char toLowerCase(char __c)
	{
		return DefaultLocale.NO_LOCALE.toLowerCase(__c);
	}
	
	/**
	 * Returns a string representation of the given character.
	 *
	 * @param __c The character to represent as a string.
	 * @return The string representation of that character.
	 * @since 2018/10/13
	 */
	public static String toString(char __c)
	{
		return new String(new SingleCharacterSequence(__c));
	}
	
	/**
	 * Converts the specified character to lower case without considering
	 * locale.
	 *
	 * @param __c The character to convert.
	 * @return The converted character.
	 * @since 2018/10/13
	 */
	@ImplementationNote("CLDC only supports Latin-1 and this method has no " +
		"locale support.")
	public static char toUpperCase(char __c)
	{
		return DefaultLocale.NO_LOCALE.toUpperCase(__c);
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

