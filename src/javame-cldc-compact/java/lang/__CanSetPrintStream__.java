// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package java.lang;

import java.io.PrintStream;

/**
 * This is an internal {@link PrintStream} which can have the target stream
 * it writes to changed. This is to prevent abuse of using reflection to
 * change final values. Due to the potential to have extremely aggressive
 * optimization and romization, having it not do this would be dangerous
 * because in fully optimized code, the finals will NEVER change regardless
 * of how many stream setting is called.
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
			throw new NullPointerException();
		
		// Set
		_target = __def;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/17
	 */
	@Override
	public PrintStream append(CharSequence __a)
	{
		// Lock
		synchronized (lock)
		{
			return _target.append(__a);
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
		synchronized (lock)
		{
			return _target.append(__a, __b, __c);
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
		synchronized (lock)
		{
			return _target.append(__a);
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
		synchronized (lock)
		{
			return _target.checkError();
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
		synchronized (lock)
		{
			_target.close();
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
		synchronized (lock)
		{
			_target.flush();
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
		synchronized (lock)
		{
			return _target.format(__a, __b);
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
		synchronized (lock)
		{
			_target.print(__a);
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
		synchronized (lock)
		{
			_target.print(__a);
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
		synchronized (lock)
		{
			_target.print(__a);
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
		synchronized (lock)
		{
			_target.print(__a);
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
		synchronized (lock)
		{
			_target.print(__a);
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
		synchronized (lock)
		{
			_target.print(__a);
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
		synchronized (lock)
		{
			_target.print(__a);
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
		synchronized (lock)
		{
			_target.print(__a);
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
		synchronized (lock)
		{
			_target.print(__a);
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
		synchronized (lock)
		{
			return _target.printf(__a, __b);
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
		synchronized (lock)
		{
			_target.println();
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
		synchronized (lock)
		{
			_target.println(__a);
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
		synchronized (lock)
		{
			_target.println(__a);
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
		synchronized (lock)
		{
			_target.println(__a);
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
		synchronized (lock)
		{
			_target.println(__a);
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
		synchronized (lock)
		{
			_target.println(__a);
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
		synchronized (lock)
		{
			_target.println(__a);
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
		synchronized (lock)
		{
			_target.println(__a);
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
		synchronized (lock)
		{
			_target.println(__a);
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
		synchronized (lock)
		{
			_target.println(__a);
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
		synchronized (lock)
		{
			_target.write(__a);
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
		synchronized (lock)
		{
			_target.write(__a, __b, __c);
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
			throw new NullPointerException();
		
		// Lock
		synchronized (lock)
		{
			// Flush the current stream so any written bytes are written to
			// the output, so that they are not lost forever. However some
			// trickery could be done which causes flush to fail.
			try
			{
				flush();
			}
			
			// Completely ignore, also do not try printing the stack trace
			// because an infinite loop could occur.
			catch (Throwable t)
			{
			}
		
			// Change it
			_target = __ps;
		}
	}
}

