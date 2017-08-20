// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.lcdui;


public class TextField
	extends Item
{
	public static final int ANY =
		0;
	
	public static final int CONSTRAINT_MASK =
		65535;
	
	public static final int CURRENCY =
		6;
	
	public static final int DECIMAL =
		5;
	
	public static final int EMAILADDR =
		1;
	
	public static final int INITIAL_CAPS_SENTENCE =
		2097152;
	
	public static final int INITIAL_CAPS_WORD =
		1048576;
	
	public static final int NON_PREDICTIVE =
		524288;
	
	public static final int NUMERIC =
		2;
	
	public static final int PASSWORD =
		65536;
	
	public static final int PHONENUMBER =
		3;
	
	public static final int SENSITIVE =
		262144;
	
	public static final int UNEDITABLE =
		131072;
	
	public static final int URL =
		4;
	
	/** The text contained within the field. */
	private final StringBuilder _value =
		new StringBuilder();
	
	/** The maximum length of the text field. */
	private volatile int _maxlength =
		Integer.MAX_VALUE;
	
	/**
	 * Initializes the text field.
	 *
	 * If the implementation does not support the specified number of
	 * characters then the input string will be truncated and no exception
	 * will be thrown provided the input is still valid.
	 *
	 * @param __l The label of the field.
	 * @param __t The text to initially set the field to,
	 * see {@link #setString(String)}.
	 * @param __ms The maximum amount of characters in the field,
	 * see {@link #setMaxSize(int)}.
	 * @param __c The constraints of the field,
	 * see {@link #setConstraints(int)}.
	 * @throws IllegalArgumentException If the maximum size is zero or less;
	 * the constraints is not valid; the text is not valid; or the text
	 * exceeds the maximum specified characters.
	 * @since 2017/08/19
	 */
	public TextField(String __l, String __t, int __ms, int __c)
		throws IllegalArgumentException
	{
		// Standard item properties
		setLabel(__l);
		
		// Size and constraints are set first to make sure the string is
		// valid
		int n = setMaxSize(__ms);
		setConstraints(__c);
		
		// Set the string which validates the input
		// However if the constraints are smaller for the implementation then
		// no exception is thrown (just give the truncated string)
		if (__t != null)
			setString((n < __c ? __t.substring(0, n) : __t));
	}
	
	public void delete(int __a, int __b)
	{
		throw new todo.TODO();
	}
	
	public int getCaretPosition()
	{
		throw new todo.TODO();
	}
	
	public int getChars(char[] __a)
	{
		throw new todo.TODO();
	}
	
	public int getConstraints()
	{
		throw new todo.TODO();
	}
	
	public int getMaxSize()
	{
		throw new todo.TODO();
	}
	
	public String getString()
	{
		throw new todo.TODO();
	}
	
	public void insert(char[] __a, int __b, int __c, int __d)
	{
		throw new todo.TODO();
	}
	
	public void insert(String __a, int __b)
	{
		throw new todo.TODO();
	}
	
	public void setCaret(int __i)
	{
		throw new todo.TODO();
	}
	
	public void setChars(char[] __a, int __b, int __c)
	{
		throw new todo.TODO();
	}
	
	public void setConstraints(int __a)
	{
		throw new todo.TODO();
	}
	
	public void setHighlight(int __i, int __l)
	{
		throw new todo.TODO();
	}
	
	public void setInitialInputMode(String __a)
	{
		throw new todo.TODO();
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
		// {@squirreljme.error EB1d The maximum characters in the text field
		// cannot be zero or negative. (The maximum characters)}
		if (__ms <= 0)
			throw new IllegalArgumentException(String.format("EB1d %d", __ms));
		
		// Make sure the input is valid during truncation
		if (true)
			throw new todo.TODO();
		
		// Set, SquirrelJME does not have a fixed limit on the size of text
		// fields
		this._maxlength = __ms;
		return __ms;
	}
	
	public void setString(String __a)
	{
		throw new todo.TODO();
	}
	
	public int size()
	{
		throw new todo.TODO();
	}
}


