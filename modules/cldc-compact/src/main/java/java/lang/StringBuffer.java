// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package java.lang;

import cc.squirreljme.runtime.cldc.annotation.Api;

/**
 * This is a thread safe version of {@link StringBuilder}, to reduce the need
 * to document everything please see that documentation instead.
 *
 * The instance itself is used as the monitor.
 *
 * @see StringBuilder
 * @since 2018/12/09
 */
@Api
public final class StringBuffer
	implements Appendable, CharSequence
{
	/** The base buffer. */
	private final StringBuilder _builder;
	
	/**
	 * Initializes a blank string buffer.
	 *
	 * @since 2018/12/08
	 */
	@Api
	public StringBuffer()
	{
		this._builder = new StringBuilder();
	}
	
	/**
	 * Initializes the string buffer using the given value.
	 *
	 * @param __a The value.
	 * @since 2018/12/08
	 */
	@Api
	public StringBuffer(int __a)
	{
		this._builder = new StringBuilder(__a);
	}
	
	/**
	 * Initializes the string buffer using the given value.
	 *
	 * @param __a The value.
	 * @since 2018/12/08
	 */
	@Api
	public StringBuffer(String __a)
	{
		this._builder = new StringBuilder(__a);
	}
	
	/**
	 * Initializes the string buffer using the given value.
	 *
	 * @param __a The value.
	 * @since 2018/12/08
	 */
	@Api
	public StringBuffer(CharSequence __a)
	{
		this._builder = new StringBuilder(__a);
	}
	
	/**
	 * See {@link StringBuilder#append(Object)}.
	 *
	 * @param __a Same as linked documentation.
	 * @return Same as linked documentation.
	 * @since 2018/12/08
	 */
	@Api
	public StringBuffer append(Object __a)
	{
		synchronized (this)
		{
			this._builder.append(__a);
			return this;
		}
	}
	
	/**
	 * See {@link StringBuilder#append(String)}.
	 *
	 * @param __a Same as linked documentation.
	 * @return Same as linked documentation.
	 * @since 2018/12/08
	 */
	@Api
	public StringBuffer append(String __a)
	{
		synchronized (this)
		{
			this._builder.append(__a);
			return this;
		}
	}
	
	/**
	 * See {@link StringBuilder#append(StringBuffer)}.
	 *
	 * @param __a Same as linked documentation.
	 * @return Same as linked documentation.
	 * @since 2018/12/08
	 */
	@Api
	public StringBuffer append(StringBuffer __a)
	{
		synchronized (this)
		{
			this._builder.append(__a);
			return this;
		}
	}
	
	/**
	 * See {@link StringBuilder#append(CharSequence)}.
	 *
	 * @param __a Same as linked documentation.
	 * @return Same as linked documentation.
	 * @since 2018/12/08
	 */
	@Api
	@Override
	public StringBuffer append(CharSequence __a)
	{
		synchronized (this)
		{
			this._builder.append(__a);
			return this;
		}
	}
	
	/**
	 * See {@link StringBuilder#append(CharSequence, int, int)}.
	 *
	 * @param __a Same as linked documentation.
	 * @param __b Same as linked documentation.
	 * @param __c Same as linked documentation.
	 * @return Same as linked documentation.
	 * @since 2018/12/08
	 */
	@Api
	@Override
	public StringBuffer append(CharSequence __a, int __b, int __c)
	{
		synchronized (this)
		{
			this._builder.append(__a, __b, __c);
			return this;
		}
	}
	
	/**
	 * See {@link StringBuilder#append(char[])}.
	 *
	 * @param __a Same as linked documentation.
	 * @return Same as linked documentation.
	 * @since 2018/12/08
	 */
	@Api
	public StringBuffer append(char[] __a)
	{
		synchronized (this)
		{
			this._builder.append(__a);
			return this;
		}
	}
	
	/**
	 * See {@link StringBuilder#append(char[], int, int)}.
	 *
	 * @param __a Same as linked documentation.
	 * @param __b Same as linked documentation.
	 * @param __c Same as linked documentation.
	 * @return Same as linked documentation.
	 * @since 2018/12/08
	 */
	@Api
	public StringBuffer append(char[] __a, int __b, int __c)
	{
		synchronized (this)
		{
			this._builder.append(__a, __b, __c);
			return this;
		}
	}
	
	/**
	 * See {@link StringBuilder#append(boolean)}.
	 *
	 * @param __a Same as linked documentation.
	 * @return Same as linked documentation.
	 * @since 2018/12/08
	 */
	@Api
	public StringBuffer append(boolean __a)
	{
		synchronized (this)
		{
			this._builder.append(__a);
			return this;
		}
	}
	
	/**
	 * See {@link StringBuilder#append(char)}.
	 *
	 * @param __a Same as linked documentation.
	 * @return Same as linked documentation.
	 * @since 2018/12/08
	 */
	@Override
	public StringBuffer append(char __a)
	{
		synchronized (this)
		{
			this._builder.append(__a);
			return this;
		}
	}
	
	/**
	 * See {@link StringBuilder#append(int)}.
	 *
	 * @param __a Same as linked documentation.
	 * @return Same as linked documentation.
	 * @since 2018/12/08
	 */
	@Api
	public StringBuffer append(int __a)
	{
		synchronized (this)
		{
			this._builder.append(__a);
			return this;
		}
	}
	
	/**
	 * See {@link StringBuilder#append(long)}.
	 *
	 * @param __a Same as linked documentation.
	 * @return Same as linked documentation.
	 * @since 2018/12/08
	 */
	@Api
	public StringBuffer append(long __a)
	{
		synchronized (this)
		{
			this._builder.append(__a);
			return this;
		}
	}
	
	/**
	 * See {@link StringBuilder#append(float)}.
	 *
	 * @param __a Same as linked documentation.
	 * @return Same as linked documentation.
	 * @since 2018/12/08
	 */
	@Api
	public StringBuffer append(float __a)
	{
		synchronized (this)
		{
			this._builder.append(__a);
			return this;
		}
	}
	
	/**
	 * See {@link StringBuilder#append(double)}.
	 *
	 * @param __a Same as linked documentation.
	 * @return Same as linked documentation.
	 * @since 2018/12/08
	 */
	@Api
	public StringBuffer append(double __a)
	{
		synchronized (this)
		{
			this._builder.append(__a);
			return this;
		}
	}
	
	/**
	 * See {@link StringBuilder#capacity()}.
	 *
	 * @return Same as linked documentation.
	 * @since 2018/12/08
	 */
	@Api
	public int capacity()
	{
		synchronized (this)
		{
			return this._builder.capacity();
		}
	}
	
	/**
	 * See {@link StringBuilder#charAt(int)}.
	 *
	 * @param __a Same as linked documentation.
	 * @return Same as linked documentation.
	 * @since 2018/12/08
	 */
	@Override
	public char charAt(int __a)
	{
		synchronized (this)
		{
			return this._builder.charAt(__a);
		}
	}
	
	/**
	 * See {@link StringBuilder#delete(int, int)}.
	 *
	 * @param __a Same as linked documentation.
	 * @param __b Same as linked documentation.
	 * @return Same as linked documentation.
	 * @since 2018/12/08
	 */
	@Api
	public StringBuffer delete(int __a, int __b)
	{
		synchronized (this)
		{
			this._builder.delete(__a, __b);
			return this;
		}
	}
	
	/**
	 * See {@link StringBuilder#deleteCharAt(int)}.
	 *
	 * @param __a Same as linked documentation.
	 * @return Same as linked documentation.
	 * @since 2018/12/08
	 */
	@Api
	public StringBuffer deleteCharAt(int __a)
	{
		synchronized (this)
		{
			this._builder.deleteCharAt(__a);
			return this;
		}
	}
	
	/**
	 * See {@link StringBuilder#ensureCapacity(int)}.
	 *
	 * @param __a Same as linked documentation.
	 * @since 2018/12/08
	 */
	@Api
	public void ensureCapacity(int __a)
	{
		synchronized (this)
		{
			this._builder.ensureCapacity(__a);
		}
	}
	
	/**
	 * See {@link StringBuilder#getChars(int, int, char[], int)}.
	 *
	 * @param __a Same as linked documentation.
	 * @param __b Same as linked documentation.
	 * @param __c Same as linked documentation.
	 * @param __d Same as linked documentation.
	 * @since 2018/12/08
	 */
	@Api
	public void getChars(int __a, int __b, char[] __c, int __d)
	{
		synchronized (this)
		{
			this._builder.getChars(__a, __b, __c, __d);
		}
	}
	
	/**
	 * See {@link StringBuilder#indexOf(String)}.
	 *
	 * @param __a Same as linked documentation.
	 * @return Same as linked documentation.
	 * @since 2018/12/08
	 */
	@Api
	public int indexOf(String __a)
	{
		synchronized (this)
		{
			return this._builder.indexOf(__a);
		}
	}
	
	/**
	 * See {@link StringBuilder#indexOf(String, int)}.
	 *
	 * @param __a Same as linked documentation.
	 * @param __b Same as linked documentation.
	 * @return Same as linked documentation.
	 * @since 2018/12/08
	 */
	@Api
	public int indexOf(String __a, int __b)
	{
		synchronized (this)
		{
			return this._builder.indexOf(__a, __b);
		}
	}
	
	/**
	 * See {@link StringBuilder#insert(int, char[], int, int)}.
	 *
	 * @param __a Same as linked documentation.
	 * @param __b Same as linked documentation.
	 * @param __c Same as linked documentation.
	 * @param __d Same as linked documentation.
	 * @return Same as linked documentation.
	 * @since 2018/12/08
	 */
	@Api
	public StringBuffer insert(int __a, char[] __b, int __c, int __d)
	{
		synchronized (this)
		{
			this._builder.insert(__a, __b, __c, __d);
			return this;
		}
	}
	
	/**
	 * See {@link StringBuilder#insert(int, Object)}.
	 *
	 * @param __a Same as linked documentation.
	 * @param __b Same as linked documentation.
	 * @return Same as linked documentation.
	 * @since 2018/12/08
	 */
	@Api
	public StringBuffer insert(int __a, Object __b)
	{
		synchronized (this)
		{
			this._builder.insert(__a, __b);
			return this;
		}
	}
	
	/**
	 * See {@link StringBuilder#insert(int, String)}.
	 *
	 * @param __a Same as linked documentation.
	 * @param __b Same as linked documentation.
	 * @return Same as linked documentation.
	 * @since 2018/12/08
	 */
	@Api
	public StringBuffer insert(int __a, String __b)
	{
		synchronized (this)
		{
			this._builder.insert(__a, __b);
			return this;
		}
	}
	
	/**
	 * See {@link StringBuilder#insert(int, char[])}.
	 *
	 * @param __a Same as linked documentation.
	 * @param __b Same as linked documentation.
	 * @return Same as linked documentation.
	 * @since 2018/12/08
	 */
	@Api
	public StringBuffer insert(int __a, char[] __b)
	{
		synchronized (this)
		{
			this._builder.insert(__a, __b);
			return this;
		}
	}
	
	/**
	 * See {@link StringBuilder#insert(int, CharSequence)}.
	 *
	 * @param __a Same as linked documentation.
	 * @param __b Same as linked documentation.
	 * @return Same as linked documentation.
	 * @since 2018/12/08
	 */
	@Api
	public StringBuffer insert(int __a, CharSequence __b)
	{
		synchronized (this)
		{
			this._builder.insert(__a, __b);
			return this;
		}
	}
	
	/**
	 * See {@link StringBuilder#insert(int, CharSequence, int, int)}.
	 *
	 * @param __a Same as linked documentation.
	 * @param __b Same as linked documentation.
	 * @param __c Same as linked documentation.
	 * @param __d Same as linked documentation.
	 * @return Same as linked documentation.
	 * @since 2018/12/08
	 */
	@Api
	public StringBuffer insert(int __a, CharSequence __b, int __c,
		int __d)
	{
		synchronized (this)
		{
			this._builder.insert(__a, __b, __c, __d);
			return this;
		}
	}
	
	/**
	 * See {@link StringBuilder#insert(int, boolean)}.
	 *
	 * @param __a Same as linked documentation.
	 * @param __b Same as linked documentation.
	 * @return Same as linked documentation.
	 * @since 2018/12/08
	 */
	@Api
	public StringBuffer insert(int __a, boolean __b)
	{
		synchronized (this)
		{
			this._builder.insert(__a, __b);
			return this;
		}
	}
	
	/**
	 * See {@link StringBuilder#insert(int, char)}.
	 *
	 * @param __a Same as linked documentation.
	 * @param __b Same as linked documentation.
	 * @return Same as linked documentation.
	 * @since 2018/12/08
	 */
	@Api
	public StringBuffer insert(int __a, char __b)
	{
		synchronized (this)
		{
			this._builder.insert(__a, __b);
			return this;
		}
	}
	
	/**
	 * See {@link StringBuilder#insert(int, int)}.
	 *
	 * @param __a Same as linked documentation.
	 * @param __b Same as linked documentation.
	 * @return Same as linked documentation.
	 * @since 2018/12/08
	 */
	@Api
	public StringBuffer insert(int __a, int __b)
	{
		synchronized (this)
		{
			this._builder.insert(__a, __b);
			return this;
		}
	}
	
	/**
	 * See {@link StringBuilder#insert(int, long)}.
	 *
	 * @param __a Same as linked documentation.
	 * @param __b Same as linked documentation.
	 * @return Same as linked documentation.
	 * @since 2018/12/08
	 */
	@Api
	public StringBuffer insert(int __a, long __b)
	{
		synchronized (this)
		{
			this._builder.insert(__a, __b);
			return this;
		}
	}
	
	/**
	 * See {@link StringBuilder#insert(int, float)}.
	 *
	 * @param __a Same as linked documentation.
	 * @param __b Same as linked documentation.
	 * @return Same as linked documentation.
	 * @since 2018/12/08
	 */
	@Api
	public StringBuffer insert(int __a, float __b)
	{
		synchronized (this)
		{
			this._builder.insert(__a, __b);
			return this;
		}
	}
	
	/**
	 * See {@link StringBuilder#insert(int, double)}.
	 *
	 * @param __a Same as linked documentation.
	 * @param __b Same as linked documentation.
	 * @return Same as linked documentation.
	 * @since 2018/12/08
	 */
	@Api
	public StringBuffer insert(int __a, double __b)
	{
		synchronized (this)
		{
			this._builder.insert(__a, __b);
			return this;
		}
	}
	
	/**
	 * See {@link StringBuilder#lastIndexOf(String)}.
	 *
	 * @param __a Same as linked documentation.
	 * @return Same as linked documentation.
	 * @since 2018/12/08
	 */
	@Api
	public int lastIndexOf(String __a)
	{
		synchronized (this)
		{
			return this._builder.lastIndexOf(__a);
		}
	}
	
	/**
	 * See {@link StringBuilder#lastIndexOf(String, int)}.
	 *
	 * @param __a Same as linked documentation.
	 * @param __b Same as linked documentation.
	 * @return Same as linked documentation.
	 * @since 2018/12/08
	 */
	@Api
	public int lastIndexOf(String __a, int __b)
	{
		synchronized (this)
		{
			return this._builder.lastIndexOf(__a, __b);
		}
	}
	
	/**
	 * See {@link StringBuilder#length()}.
	 *
	 * @return Same as linked documentation.
	 * @since 2018/12/08
	 */
	@Override
	public int length()
	{
		synchronized (this)
		{
			return this._builder.length();
		}
	}
	
	/**
	 * See {@link StringBuilder#replace(int, int, String)}.
	 *
	 * @param __a Same as linked documentation.
	 * @param __b Same as linked documentation.
	 * @param __c Same as linked documentation.
	 * @return Same as linked documentation.
	 * @since 2018/12/08
	 */
	@Api
	public StringBuffer replace(int __a, int __b, String __c)
	{
		synchronized (this)
		{
			this._builder.replace(__a, __b, __c);
			return this;
		}
	}
	
	/**
	 * See {@link StringBuilder#reverse()}.
	 *
	 * @return Same as linked documentation.
	 * @since 2018/12/08
	 */
	@Api
	public StringBuffer reverse()
	{
		synchronized (this)
		{
			this._builder.reverse();
			return this;
		}
	}
	
	/**
	 * See {@link StringBuilder#setCharAt(int, char)}.
	 *
	 * @param __a Same as linked documentation.
	 * @param __b Same as linked documentation.
	 * @since 2018/12/08
	 */
	@Api
	public void setCharAt(int __a, char __b)
	{
		synchronized (this)
		{
			this._builder.setCharAt(__a, __b);
		}
	}
	
	/**
	 * See {@link StringBuilder#setLength(int)}.
	 *
	 * @param __a Same as linked documentation.
	 * @since 2018/12/08
	 */
	@Api
	public void setLength(int __a)
	{
		synchronized (this)
		{
			this._builder.setLength(__a);
		}
	}
	
	/**
	 * See {@link StringBuilder#subSequence(int, int)}.
	 *
	 * @param __a Same as linked documentation.
	 * @param __b Same as linked documentation.
	 * @return Same as linked documentation.
	 * @since 2018/12/08
	 */
	@Override
	public CharSequence subSequence(int __a, int __b)
	{
		synchronized (this)
		{
			return this._builder.subSequence(__a, __b);
		}
	}
	
	/**
	 * See {@link StringBuilder#substring(int)}.
	 *
	 * @param __a Same as linked documentation.
	 * @return Same as linked documentation.
	 * @since 2018/12/08
	 */
	@Api
	public String substring(int __a)
	{
		synchronized (this)
		{
			return this._builder.substring(__a);
		}
	}
	
	/**
	 * See {@link StringBuilder#substring(int, int)}.
	 *
	 * @param __a Same as linked documentation.
	 * @param __b Same as linked documentation.
	 * @return Same as linked documentation.
	 * @since 2018/12/08
	 */
	@Api
	public String substring(int __a, int __b)
	{
		synchronized (this)
		{
			return this._builder.substring(__a, __b);
		}
	}
	
	/**
	 * See {@link StringBuilder#toString()}.
	 *
	 * @return Same as linked documentation.
	 * @since 2018/12/08
	 */
	@Override
	public String toString()
	{
		synchronized (this)
		{
			return this._builder.toString();
		}
	}
	
	/**
	 * See {@link StringBuilder#trimToSize()}.
	 *
	 * @since 2018/12/08
	 */
	@Api
	public void trimToSize()
	{
		synchronized (this)
		{
			this._builder.trimToSize();
		}
	}
}

