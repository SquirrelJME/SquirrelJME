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

import cc.squirreljme.runtime.lcdui.common.EditableText;
import cc.squirreljme.runtime.lcdui.ui.UIPersist;
import cc.squirreljme.runtime.lcdui.ui.UIStack;

/**
 * This represents a field of editable or read-only text which may be placed
 * within a form.
 *
 * If constraints are used within the text field then the representation of
 * the text may change according to the constraints. Note that the character
 * size limits may hinder valid values to be constrained.
 *
 * @since 2017/08/20
 */
public class TextField
	extends Item
{
	/** This is a constraint which has no limitations. */
	public static final int ANY =
		0;
	
	/**
	 * This mask is used to split the constraint between the type and any
	 * flags which may be used.
	 */
	public static final int CONSTRAINT_MASK =
		65535;
	
	/**
	 * This constraint is used to specify that the value represents currency
	 * and that when it is drawn it will be rendered as the currency for the
	 * system's current locale.
	 *
	 * Value requirements are exactly the same as {@link #DECIMAL}.
	 */
	public static final int CURRENCY =
		6;
	
	/**
	 * This constraint is used to specify that the value represents a decimal
	 * value and the field value is required to be matching.
	 *
	 * For the actual value in the decimal field, it may only contain digits,
	 * a minus sign {@code -}, and the decimal point {@code .}.
	 *
	 * When rendering the value, the number will be formatted to the current
	 * system's locale.
	 */
	public static final int DECIMAL =
		5;
	
	/**
	 * A constraint which specifies that an e-mail address is being entered.
	 */
	public static final int EMAILADDR =
		1;
	
	/**
	 * This is a constraint flag which hints that the start of a sentence
	 * should start with a capital letter.
	 */
	public static final int INITIAL_CAPS_SENTENCE =
		2097152;
	
	/**
	 * This is a constraint flag which hints that the start of every word
	 * should be capitalized.
	 */
	public static final int INITIAL_CAPS_WORD =
		1048576;
	
	/**
	 * This is a constraint flags which indicates that what is to be input
	 * will not be found in common dictionaries for the system.
	 *
	 * An example usage for this, would be user names for accounts which might
	 * not match dictionary words.
	 */
	public static final int NON_PREDICTIVE =
		524288;
	
	/**
	 * This is a constraint which requires that only an integer value is used,
	 * the integer itself matches that of {@code int}. As such only digits and
	 * the minus sign {@code -} are permitted.
	 *
	 * The value will be presented in the system's current locale.
	 */
	public static final int NUMERIC =
		2;
	
	/**
	 * This is a constriant flag which masks any input character with a
	 * special symbol to obscure them with. This should be used for passwords
	 * and PIN numbers.
	 *
	 * If this is used then {@link #NON_PREDICTIVE} and {@link #SENSITIVE} are
	 * implicitely set along with {@link #INITIAL_CAPS_SENTENCE} and
	 * {@link #INITIAL_CAPS_WORD} are impliticely cleared.
	 */
	public static final int PASSWORD =
		65536;
	
	/**
	 * This is a constraint which allows input for a phone number.
	 *
	 * If a phone book is available then access to the phonebook to input a
	 * number automatically may be used when applicable.
	 *
	 * When rendered the value may be represented in a more friendly fashion
	 * depending on the phone number.
	 */
	public static final int PHONENUMBER =
		3;
	
	/**
	 * This is a constraint flag which specifies that the given text should
	 * never be stored into a dictionary for predictive and auto-completion
	 * uses. A social-security number or credit card number for example is
	 * something which is considered sensitive.
	 *
	 * On some systems with predictive text, typing in words that are not in
	 * the dictionary will often add them to the dictionary so that they can
	 * become available to learn new words. This prevents this behavior.
	 */
	public static final int SENSITIVE =
		262144;
	
	/**
	 * This is a constraint flag which specifies that the text field cannot
	 * be edited.
	 */
	public static final int UNEDITABLE =
		131072;
	
	/** This is a consraint which specifies that a URL is being inserted. */
	public static final int URL =
		4;
	
	/** The common text editor this is associated with. */
	private final EditableText _editabletext =
		new EditableText();
	
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
		
		// Initialize text area
		this._editabletext.initialize(__t, __ms, __c);
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
		this._editabletext.setConstraints(__c);
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
		return this._editabletext.setMaxSize(__ms);
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
		this._editabletext.setString(__s);
	}
	
	public int size()
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/08
	 */
	@Override
	final void __draw(UIStack __parent, UIStack __self, Graphics __g)
	{
		__g.drawString(this.getClass().getName().toString(),
			__g.getClipX(), __g.getClipY(), 0);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/08
	 */
	@Override
	final void __updateUIStack(UIPersist __keep, UIStack __parent)
	{
		throw new todo.TODO();
	}
}

