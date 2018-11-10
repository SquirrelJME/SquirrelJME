// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.common;

import javax.microedition.lcdui.TextField;

/**
 * This class is used to manage text, it manages the caret position.
 *
 * The maximum size for a text field may never be zero or less than that.
 *
 * All locks are performed on the current object.
 *
 * @see javax.microedition.lcdui.TextBox
 * @see javax.microedition.lcdui.TextEditor
 * @see javax.microedition.lcdui.TextField
 * @since 2017/10/20
 */
public final class EditableText
{
	/** The maximum constraint number. */
	private static final int _MAX_CONSTRAINT =
		TextField.CURRENCY;
	
	/** The valid constraint flag bits. */
	private static final int _VALID_CONSTRAINT_FLAG_BITS =
		TextField.INITIAL_CAPS_SENTENCE | TextField.INITIAL_CAPS_WORD |
		TextField.NON_PREDICTIVE | TextField.PASSWORD |
		TextField.SENSITIVE | TextField.UNEDITABLE;
	
	/** Internal lock for text thread safety. */
	protected final Object lock =
		new Object();
	
	/** The text contained within the field. */
	private final StringBuilder _value =
		new StringBuilder();
	
	/** The maximum length of the text field. */
	private volatile int _maxlength =
		Integer.MAX_VALUE;
	
	/** The current constraints. */
	private volatile int _constraints;
	
	/**
	 * Initializes the text field by setting every parameter at once.
	 *
	 * If the implementation does not support the specified number of
	 * characters then the input string will be truncated and no exception
	 * will be thrown provided the input is still valid.
	 *
	 * @param __t The text to initially set the field to,
	 * see {@link #setString(String)}.
	 * @param __ms The maximum amount of characters in the field,
	 * see {@link #setMaxSize(int)}.
	 * @param __c The constraints of the field,
	 * see {@link #setConstraints(int)}.
	 * @throws IllegalArgumentException If the maximum size is zero or less;
	 * the constraints is not valid; the text is not valid; or the text
	 * exceeds the maximum specified characters.
	 * @since 2017/10/20
	 */
	public void initialize(String __t, int __ms, int __c)
		throws IllegalArgumentException
	{
		// Lock
		synchronized (this.lock)
		{
			// Size and constraints are set first to make sure the string is
			// valid
			int n = setMaxSize(__ms);
			setConstraints(__c);
		
			// Set the string which validates the input
			// However if the constraints are smaller for the implementation
			// then no exception is thrown (just give the truncated string)
			if (__t != null)
				setString((n < __c ? __t.substring(0, n) : __t));
		}
	}
	
	/**
	 * Sets the constraints for this field according to the constraints which
	 * are available. If the current value is not valid under the constraints
	 * to be set then it is cleared.
	 *
	 * @param __c The constraints to set.
	 * @throws IllegalArgumentException If the input constraints are not
	 * valid.
	 * @since 2017/08/20
	 */
	public void setConstraints(int __c)
		throws IllegalArgumentException
	{
		// {@squirreljme.error EB01 The specified constraint type is not
		// valid. (The type)}
		int type = (__c & TextField.CONSTRAINT_MASK);
		if (type < 0 || type > _MAX_CONSTRAINT)
			throw new IllegalArgumentException(String.format("EB01 %d", type));
		
		// {@squirreljme.error EB02 The specified constraint flags are not
		// valid. (The constraint flags)}
		if (((__c ^ type) & ~_VALID_CONSTRAINT_FLAG_BITS) != 0)
			throw new IllegalArgumentException(String.format("EB02 %04x",
				__c >>> 16));
		
		// Lock
		synchronized (this.lock)
		{
			// If the new constraints are not valid then any previous text
			// is cleared
			StringBuilder value = this._value;
			if (!__check(value, this._maxlength, __c))
			{
				value.setLength(0);
				return;
			}
			
			// Set
			this._constraints = __c;
		}
	}
	
	/**
	 * Sets the maximum size of the text field, if the value is larger than
	 * this value then it will be truncated.
	 *
	 * @param __ms The number of characters that can be placed in the field.
	 * @return The actual maximum capacity, this may be smaller than the
	 * input value.
	 * @throws IllegalArgumentException If the maximum size is not a non-zero
	 * positive value or if the value would not be valid after truncation if
	 * constraints are used.
	 * @since 2017/08/19
	 */
	public int setMaxSize(int __ms)
		throws IllegalArgumentException
	{
		// {@squirreljme.error EB03 The maximum characters in the text field
		// cannot be zero or negative. (The maximum characters)}
		if (__ms <= 0)
			throw new IllegalArgumentException(String.format("EB03 %d", __ms));
		
		// Lock
		synchronized (this.lock)
		{
			// {@squirreljme.error EB04 Cannot set the maximum size because the
			// input text field would have an invalid value.}
			if (!__check(this._value, __ms, this._constraints))
				throw new IllegalArgumentException("EB04");
		
			// Set, SquirrelJME does not have a fixed limit on the size of text
			// fields
			this._maxlength = __ms;
			return __ms;
		}
	}
	
	/**
	 * Sets the value of the text field.
	 *
	 * @param __s The value to set, if {@code null} then an empty string is
	 * used.
	 * @throws IllegalArgumentException If the input string is not within
	 * the constraints or character count limitations.
	 * @since 2017/08/20
	 */
	public void setString(String __s)
		throws IllegalArgumentException
	{
		// Null becomes empty
		if (__s == null)
			__s = "";
		
		// Lock
		synchronized (this.lock)
		{
			// {@squirreljme.error EB05 Cannot set the specified string
			// because it is not valid within the constraints.}
			if (!__check(__s, this._maxlength, this._constraints))
				throw new IllegalArgumentException("EB05");
			
			// Set value
			StringBuilder value = this._value;
			value.setLength(0);
			value.append(__s);
		}
	}
	
	/**
	 * This checks the given sequence to make sure it is within the
	 * specified constraints.
	 *
	 * @param __cs The sequence to check.
	 * @param __ms The maximum number of characters to check.
	 * @param __c The constraints specifications.
	 * @return Whether or not the data is valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/08/20
	 */
	private static boolean __check(CharSequence __cs, int __ms, int __c)
		throws NullPointerException
	{
		// Check
		if (__cs == null)
			throw new NullPointerException("NARG");
		
		// No constraints used or the input string is empty
		// Empty strings are always valid because otherwise constructing
		// objects would always fail with illegal values
		int type = (__c & TextField.CONSTRAINT_MASK),
			n = __cs.length();
		if (type == TextField.ANY || n == 0)
			return true;
		
		throw new todo.TODO();
	}
}

