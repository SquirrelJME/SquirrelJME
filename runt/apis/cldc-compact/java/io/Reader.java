// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package java.io;

import cc.squirreljme.runtime.cldc.annotation.ImplementationNote;

public abstract class Reader
	implements Closeable
{
	/** The lock to use when performing read operations. */
	@ImplementationNote("This is only used with the skip() method.")
	protected Object lock;
	
	/**
	 * Initializes the base reader.
	 *
	 * @since 2018/10/13
	 */
	@ImplementationNote("The lock should be initialized to this, however " +
		"this would result in the reader itself never able to be freed " +
		"because it refers to itself.")
	protected Reader()
	{
		this.lock = null;
	}
	
	/**
	 * Initializes the reader to lock against the given object.
	 *
	 * @param __l The object to lock against.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/10/13
	 */
	protected Reader(Object __l)
		throws NullPointerException
	{
		if (__l == null)
			throw new NullPointerException("NARG");
		
		this.lock = __l;
	}
	
	public abstract void close()
		throws IOException;
	
	/**
	 * Reads multiple characters.
	 *
	 * @param __c The output characters.
	 * @param __o The offset into the output.
	 * @param __l The number of characters to read.
	 * @return The number of read characters or {@code -1} on end of file.
	 * @throws IndexOutOfBoundsException If the offset and/or length are
	 * negative or exceed the array bounds.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/10/13
	 */
	public abstract int read(char[] __c, int __o, int __l)
		throws IndexOutOfBoundsException, IOException, NullPointerException;
	
	public void mark(int __a)
		throws IOException
	{
		if (false)
			throw new IOException();
		throw new todo.TODO();
	}
	
	public boolean markSupported()
	{
		throw new todo.TODO();
	}
	
	/**
	 * Reads a single character from the input.
	 *
	 * @return The character which was read or {@code -1} on end of file.
	 * @throws IOException On read errors.
	 * @since 2018/10/13
	 */
	public int read()
		throws IOException
	{
		// Try reading the character in a loop
		char[] buf = new char[1];
		for (;;)
		{
			int rc = this.read(buf, 0, 1);
			
			// Try to read character again
			if (rc == 0)
				continue;
			
			// EOF
			else if (rc < 0)
				return rc;
			
			return buf[0];
		}
	}
	
	/**
	 * Reads in multiple characters from the stream.
	 *
	 * @param __c The characters to read.
	 * @return The number of read characters.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/10/13
	 */
	public int read(char[] __c)
		throws IOException, NullPointerException
	{
		if (__c == null)
			throw new NullPointerException("NARG");
		
		return this.read(__c, 0, __c.length);
	}
	
	public boolean ready()
		throws IOException
	{
		if (false)
			throw new IOException();
		throw new todo.TODO();
	}
	
	public void reset()
		throws IOException
	{
		if (false)
			throw new IOException();
		throw new todo.TODO();
	}
	
	public long skip(long __a)
		throws IOException
	{
		// To prevent clashes when skipping characters
		synchronized (this.__lock())
		{
			if (false)
				throw new IOException();
			throw new todo.TODO();
		}
	}
	
	/**
	 * Returns the locking object.
	 *
	 * @return The locking object.
	 * @since 2018/10/13
	 */
	final Object __lock()
	{
		Object rv = this.lock;
		return (rv == null ? this : rv);
	}
}

