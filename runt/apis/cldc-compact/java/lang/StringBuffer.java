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

public final class StringBuffer
	implements Appendable, CharSequence
{
	/** The base buffer. */
	protected final StringBuilder builder;
	
	public StringBuffer()
	{
		this.builder = new StringBuilder();
	}
	
	public StringBuffer(int __a)
	{
		this.builder = new StringBuilder(__a);
	}
	
	public StringBuffer(String __a)
	{
		this.builder = new StringBuilder(__a);
	}
	
	public StringBuffer(CharSequence __a)
	{
		this.builder = new StringBuilder(__a);
	}
	
	public StringBuffer append(Object __a)
	{
		synchronized (this)
		{
			this.builder.append(__a);
			return this;
		}
	}
	
	public StringBuffer append(String __a)
	{
		synchronized (this)
		{
			this.builder.append(__a);
			return this;
		}
	}
	
	public StringBuffer append(StringBuffer __a)
	{
		synchronized (this)
		{
			this.builder.append(__a);
			return this;
		}
	}
	
	public StringBuffer append(CharSequence __a)
	{
		synchronized (this)
		{
			this.builder.append(__a);
			return this;
		}
	}
	
	public StringBuffer append(CharSequence __a, int __b, int __c)
	{
		synchronized (this)
		{
			this.builder.append(__a, __b, __c);
			return this;
		}
	}
	
	public StringBuffer append(char[] __a)
	{
		synchronized (this)
		{
			this.builder.append(__a);
			return this;
		}
	}
	
	public StringBuffer append(char[] __a, int __b, int __c)
	{
		synchronized (this)
		{
			this.builder.append(__a, __b, __c);
			return this;
		}
	}
	
	public StringBuffer append(boolean __a)
	{
		synchronized (this)
		{
			this.builder.append(__a);
			return this;
		}
	}
	
	public StringBuffer append(char __a)
	{
		synchronized (this)
		{
			this.builder.append(__a);
			return this;
		}
	}
	
	public StringBuffer append(int __a)
	{
		synchronized (this)
		{
			this.builder.append(__a);
			return this;
		}
	}
	
	public StringBuffer append(long __a)
	{
		synchronized (this)
		{
			this.builder.append(__a);
			return this;
		}
	}
	
	public StringBuffer append(float __a)
	{
		synchronized (this)
		{
			this.builder.append(__a);
			return this;
		}
	}
	
	public StringBuffer append(double __a)
	{
		synchronized (this)
		{
			this.builder.append(__a);
			return this;
		}
	}
	
	public int capacity()
	{
		synchronized (this)
		{
			return this.builder.capacity();
		}
	}
	
	public char charAt(int __a)
	{
		synchronized (this)
		{
			return this.builder.charAt(__a);
		}
	}
	
	public StringBuffer delete(int __a, int __b)
	{
		synchronized (this)
		{
			this.builder.delete(__a, __b);
			return this;
		}
	}
	
	public StringBuffer deleteCharAt(int __a)
	{
		synchronized (this)
		{
			this.builder.deleteCharAt(__a);
			return this;
		}
	}
	
	public void ensureCapacity(int __a)
	{
		synchronized (this)
		{
			this.builder.ensureCapacity(__a);
		}
	}
	
	public void getChars(int __a, int __b, char[] __c, int __d)
	{
		synchronized (this)
		{
			this.builder.getChars(__a, __b, __c, __d);
		}
	}
	
	public int indexOf(String __a)
	{
		synchronized (this)
		{
			return this.builder.indexOf(__a);
		}
	}
	
	public int indexOf(String __a, int __b)
	{
		synchronized (this)
		{
			return this.builder.indexOf(__a, __b);
		}
	}
	
	public StringBuffer insert(int __a, char[] __b, int __c, int __d)
	{
		synchronized (this)
		{
			this.builder.insert(__a, __b, __c, __d);
			return this;
		}
	}
	
	public StringBuffer insert(int __a, Object __b)
	{
		synchronized (this)
		{
			this.builder.insert(__a, __b);
			return this;
		}
	}
	
	public StringBuffer insert(int __a, String __b)
	{
		synchronized (this)
		{
			this.builder.insert(__a, __b);
			return this;
		}
	}
	
	public StringBuffer insert(int __a, char[] __b)
	{
		synchronized (this)
		{
			this.builder.insert(__a, __b);
			return this;
		}
	}
	
	public StringBuffer insert(int __a, CharSequence __b)
	{
		synchronized (this)
		{
			this.builder.insert(__a, __b);
			return this;
		}
	}
	
	public StringBuffer insert(int __a, CharSequence __b, int __c,
		int __d)
	{
		synchronized (this)
		{
			this.builder.insert(__a, __b, __c, __d);
			return this;
		}
	}
	
	public StringBuffer insert(int __a, boolean __b)
	{
		synchronized (this)
		{
			this.builder.insert(__a, __b);
			return this;
		}
	}
	
	public StringBuffer insert(int __a, char __b)
	{
		synchronized (this)
		{
			this.builder.insert(__a, __b);
			return this;
		}
	}
	
	public StringBuffer insert(int __a, int __b)
	{
		synchronized (this)
		{
			this.builder.insert(__a, __b);
			return this;
		}
	}
	
	public StringBuffer insert(int __a, long __b)
	{
		synchronized (this)
		{
			this.builder.insert(__a, __b);
			return this;
		}
	}
	
	public StringBuffer insert(int __a, float __b)
	{
		synchronized (this)
		{
			this.builder.insert(__a, __b);
			return this;
		}
	}
	
	public StringBuffer insert(int __a, double __b)
	{
		synchronized (this)
		{
			this.builder.insert(__a, __b);
			return this;
		}
	}
	
	public int lastIndexOf(String __a)
	{
		synchronized (this)
		{
			return this.builder.lastIndexOf(__a);
		}
	}
	
	public int lastIndexOf(String __a, int __b)
	{
		synchronized (this)
		{
			return this.builder.lastIndexOf(__a, __b);
		}
	}
	
	public int length()
	{
		synchronized (this)
		{
			return this.builder.length();
		}
	}
	
	public StringBuffer replace(int __a, int __b, String __c)
	{
		synchronized (this)
		{
			this.builder.replace(__a, __b, __c);
			return this;
		}
	}
	
	public StringBuffer reverse()
	{
		synchronized (this)
		{
			this.builder.reverse();
			return this;
		}
	}
	
	public void setCharAt(int __a, char __b)
	{
		synchronized (this)
		{
			this.builder.setCharAt(__a, __b);
		}
	}
	
	public void setLength(int __a)
	{
		synchronized (this)
		{
			this.builder.setLength(__a);
		}
	}
	
	public CharSequence subSequence(int __a, int __b)
	{
		synchronized (this)
		{
			return this.builder.subSequence(__a, __b);
		}
	}
	
	public String substring(int __a)
	{
		synchronized (this)
		{
			return this.builder.substring(__a);
		}
	}
	
	public String substring(int __a, int __b)
	{
		synchronized (this)
		{
			return this.builder.substring(__a, __b);
		}
	}
	
	public String toString()
	{
		synchronized (this)
		{
			return this.builder.toString();
		}
	}
	
	public void trimToSize()
	{
		synchronized (this)
		{
			this.builder.trimToSize();
		}
	}
}

