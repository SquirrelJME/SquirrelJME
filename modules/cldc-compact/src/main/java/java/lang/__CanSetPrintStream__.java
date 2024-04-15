// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package java.lang;

import java.io.PrintStream;

/**
 * This is an internal {@link PrintStream} which can have the target stream
 * it writes to changed. This is to prevent abuse of using reflection to
 * change final values. Due to the potential to have extremely aggressive
 * optimization and romization, having it not do this would be dangerous
 * because in fully optimized code, the finals will NEVER change regardless
 * of how many times stream setting is called.
 *
 * @since 2016/03/17
 */
final class __CanSetPrintStream__
	extends PrintStream
{
	/** Internal lock. */
	protected final Object lock =
		new Object();
	
	/** Target output stream. */
	private volatile PrintStream _target;
	
	/**
	 * Initializes the settable proxy for the given stream as the default
	 * value.
	 *
	 * @param __def The initial stream to target.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/17
	 */
	__CanSetPrintStream__(PrintStream __def)
		throws NullPointerException
	{
		super(__def, true);
		
		// Check
		if (__def == null)
			throw new NullPointerException("NARG");
		
		// Set
		this._target = __def;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/17
	 */
	@Override
	public PrintStream append(CharSequence __a)
	{
		// Lock
		synchronized (this.lock)
		{
			return this._target.append(__a);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/17
	 */
	@Override
	public PrintStream append(CharSequence __a, int __b, int __c)
	{
		// Lock
		synchronized (this.lock)
		{
			return this._target.append(__a, __b, __c);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/17
	 */
	@Override
	public PrintStream append(char __a)
	{
		// Lock
		synchronized (this.lock)
		{
			return this._target.append(__a);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/17
	 */
	@Override
	public boolean checkError()
	{
		// Lock
		synchronized (this.lock)
		{
			return this._target.checkError();
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/17
	 */
	@Override
	public void close()
	{
		// Lock
		synchronized (this.lock)
		{
			this._target.close();
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/17
	 */
	@Override
	public void flush()
	{
		// Lock
		synchronized (this.lock)
		{
			this._target.flush();
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/17
	 */
	@Override
	public PrintStream format(String __a, Object... __b)
	{
		// Lock
		synchronized (this.lock)
		{
			return this._target.format(__a, __b);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/17
	 */
	@Override
	public void print(boolean __a)
	{
		// Lock
		synchronized (this.lock)
		{
			this._target.print(__a);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/17
	 */
	@Override
	public void print(char __a)
	{
		// Lock
		synchronized (this.lock)
		{
			this._target.print(__a);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/17
	 */
	@Override
	public void print(int __a)
	{
		// Lock
		synchronized (this.lock)
		{
			this._target.print(__a);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/17
	 */
	@Override
	public void print(long __a)
	{
		// Lock
		synchronized (this.lock)
		{
			this._target.print(__a);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/17
	 */
	@Override
	public void print(float __a)
	{
		// Lock
		synchronized (this.lock)
		{
			this._target.print(__a);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/17
	 */
	@Override
	public void print(double __a)
	{
		// Lock
		synchronized (this.lock)
		{
			this._target.print(__a);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/17
	 */
	@Override
	public void print(char[] __a)
	{
		// Lock
		synchronized (this.lock)
		{
			this._target.print(__a);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/17
	 */
	@Override
	public void print(String __a)
	{
		// Lock
		synchronized (this.lock)
		{
			this._target.print(__a);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/17
	 */
	@Override
	public void print(Object __a)
	{
		// Lock
		synchronized (this.lock)
		{
			this._target.print(__a);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/17
	 */
	@Override
	public PrintStream printf(String __a, Object... __b)
	{
		// Lock
		synchronized (this.lock)
		{
			return this._target.printf(__a, __b);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/17
	 */
	@Override
	public void println()
	{
		// Lock
		synchronized (this.lock)
		{
			this._target.println();
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/17
	 */
	@Override
	public void println(boolean __a)
	{
		// Lock
		synchronized (this.lock)
		{
			this._target.println(__a);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/17
	 */
	@Override
	public void println(char __a)
	{
		// Lock
		synchronized (this.lock)
		{
			this._target.println(__a);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/17
	 */
	@Override
	public void println(int __a)
	{
		// Lock
		synchronized (this.lock)
		{
			this._target.println(__a);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/17
	 */
	@Override
	public void println(long __a)
	{
		// Lock
		synchronized (this.lock)
		{
			this._target.println(__a);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/17
	 */
	@Override
	public void println(float __a)
	{
		// Lock
		synchronized (this.lock)
		{
			this._target.println(__a);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/17
	 */
	@Override
	public void println(double __a)
	{
		// Lock
		synchronized (this.lock)
		{
			this._target.println(__a);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/17
	 */
	@Override
	public void println(char[] __a)
	{
		// Lock
		synchronized (this.lock)
		{
			this._target.println(__a);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/17
	 */
	@Override
	public void println(String __a)
	{
		// Lock
		synchronized (this.lock)
		{
			this._target.println(__a);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/17
	 */
	@Override
	public void println(Object __a)
	{
		// Lock
		synchronized (this.lock)
		{
			this._target.println(__a);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/17
	 */
	@Override
	public void write(int __a)
	{
		// Lock
		synchronized (this.lock)
		{
			this._target.write(__a);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/17
	 */
	@Override
	public void write(byte[] __a, int __b, int __c)
	{
		// Lock
		synchronized (this.lock)
		{
			this._target.write(__a, __b, __c);
		}
	}
	
	/**
	 * Sets the new stream to target.
	 *
	 * @param __ps The stream to target.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/17
	 */
	void __set(PrintStream __ps)
		throws NullPointerException
	{
		// Check
		if (__ps == null)
			throw new NullPointerException("NARG");
		
		// If this is a can set stream then we likely tried to restore the
		// old output stream, so if we ever print anything again this will
		// fail completely with infinite recursion.
		while (__ps instanceof __CanSetPrintStream__)
			__ps = ((__CanSetPrintStream__)__ps)._target;
		
		// Lock
		synchronized (this.lock)
		{
			// Flush the current stream so any written bytes are written to
			// the output, so that they are not lost forever. However some
			// trickery could be done which causes flush to fail.
			try
			{
				this.flush();
			}
			
			// Completely ignore, also do not try printing the stack trace
			// because an infinite loop could occur.
			catch (Throwable t)
			{
			}
		
			// Change it
			this._target = __ps;
		}
	}
}

