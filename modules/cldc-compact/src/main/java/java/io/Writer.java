// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package java.io;

import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.annotation.ImplementationNote;
import java.lang.ref.WeakReference;

/**
 * This is a base class for outputs which write with characters instead
 * of bytes.
 * 
 * @since 2022/07/12
 */
@Api
public abstract class Writer
	implements Appendable, Closeable
{
	/** The object to lock onto for streams. */
	@Api
	@ImplementationNote("This may cause a never-free.")
	protected Object lock;
	
	/**
	 * Initializes the writer.
	 * 
	 * @since 2022/07/12
	 */
	@Api
	@ImplementationNote("The lock should be initialized to this, however " +
		"this would result in the reader itself never able to be freed " +
		"because it refers to itself.")
	protected Writer()
	{
		this.lock = new WeakReference<>(this);
	}
	
	/**
	 * Initializes the writer.
	 * 
	 * @param __lock The lock to use.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/07/12
	 */
	@Api
	protected Writer(Object __lock)
		throws NullPointerException
	{
		if (__lock == null)
			throw new NullPointerException("NARG");
		
		this.lock = __lock;
	}
	
	/**
	 * Flushes the output.
	 * 
	 * @throws IOException If this could not be flushed.
	 * @since 2022/07/12
	 */
	@Api
	public abstract void flush()
		throws IOException;
	
	/**
	 * Writes the specified characters to the output.
	 * 
	 * @param __c The characters to write.
	 * @param __o The offset into the buffer.
	 * @param __l The number of characters to write.
	 * @throws IndexOutOfBoundsException If the offset and/or length are
	 * negative or exceeds the array bounds.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/07/12
	 */
	@Api
	public abstract void write(char[] __c, int __o, int __l)
		throws IndexOutOfBoundsException, IOException, NullPointerException;
	
	/**
	 * {@inheritDoc}
	 * @since 2022/07/12
	 */
	@Override
	public Writer append(CharSequence __c)
		throws IOException
	{
		this.__write((__c == null ? "null" : __c), 0,
			(__c == null ? 4 : __c.length()));
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/07/12
	 */
	@Override
	public Writer append(CharSequence __c, int __s, int __e)
		throws IOException
	{
		this.__write((__c == null ? "null" : __c), __s, __e - __s);
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/07/12
	 */
	@Override
	public Writer append(char __c)
		throws IOException
	{
		this.__write(__c);
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/07/12
	 */
	@Api
	public void write(int __c)
		throws IOException
	{
		this.__write((char)__c);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/07/12
	 */
	@Api
	public void write(char[] __c)
		throws IOException
	{
		this.write(__c, 0, __c.length);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/07/12
	 */
	@Api
	public void write(String __str)
		throws IOException
	{
		this.__write(__str, 0, __str.length());
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/07/12
	 */
	@Api
	public void write(String __str, int __off, int __len)
		throws IndexOutOfBoundsException, IOException, NullPointerException
	{
		this.__write(__str, __off, __len);
	}
	
	/**
	 * Writes a single character.
	 * 
	 * @param __c The single character to write.
	 * @throws IOException On write errors.
	 * @since 2022/07/12
	 */
	private void __write(char __c)
		throws IOException
	{
		char[] single = new char[1];
		single[0] = (char)__c;
		
		this.write(single, 0, 1);
	}
	
	/**
	 * Writes the character sequence to the output.
	 * 
	 * @param __str The string to write.
	 * @param __off The offset into the string.
	 * @param __len The length of the string.
	 * @throws IndexOutOfBoundsException If the offset and/or length are
	 * negative or exceed the array bounds.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/07/12
	 */
	private void __write(CharSequence __str, int __off, int __len)
		throws IndexOutOfBoundsException, IOException, NullPointerException
	{
		if (__str == null)
			throw new NullPointerException("NARG");
		if (__off < 0 || __len < 0 || (__off + __len) < 0 ||
			(__off + __len) > __str.length())
			throw new IndexOutOfBoundsException("IOOB");
		
		// Read in characters
		char[] buf = new char[__len];
		for (int i = 0; i < __len; i++)
			buf[i] = __str.charAt(__off + i);
		
		// Forward all of it
		this.write(buf, 0, buf.length);
	}
}

