// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.test;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This is used to wrap an exception and record its family so that profiling
 * can be used for comparison. Stack traces are not recorded. This just places
 * the class family tree of the input exception to record compability between
 * exceptions. So if one system throws {@link ArrayIndexOutOfBoundsException}
 * it will be legal for {@link IndexOutOfBoundsException} but not the other way
 * around.
 *
 * @since 2017/03/28
 */
public final class TestException
{
	/** The family of classes used in the throwable. */
	private final String[] _classes;
	
	/** String representation. */
	private volatile Reference<String> _string;
	
	/**
	 * Initializes the exception holder sourced from the given Throwable.
	 *
	 * @param __t The throwable to source from.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/03/28
	 */
	public TestException(Throwable __t)
		throws NullPointerException
	{
		// Check
		if (__t == null)
			throw new NullPointerException("NARG");
		
		// Debug
		__t.printStackTrace();
		
		// Record all classes
		List<String> classes = new ArrayList<>();
		for (Class<?> rover = __t.getClass(); rover != null;
			rover = rover.getSuperclass())
			classes.add(rover.getName());
		this._classes = classes.<String>toArray(new String[classes.size()]);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/03/29
	 */
	@Override
	public boolean equals(Object __o)
	{
		// Check
		if (!(__o instanceof TestException))
			return false;
		
		return Arrays.equals(this._classes, ((TestException)__o)._classes);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/03/29
	 */
	@Override
	public int hashCode()
	{
		int rv = 0;
		for (String s : this._classes)
			rv ^= s.hashCode();
		return rv;
	}
	
	/**
	 * Checks if this wrapped exception is compatible with the specified
	 * wrapped exception.
	 *
	 * @param __o The wrapped exception to check.
	 * @return {@code true} if the exceptions are compatible.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/03/28
	 */
	public boolean isCompatible(TestException __o)
		throws NullPointerException
	{
		// Check
		if (__o == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/03/28
	 */
	@Override
	public String toString()
	{
		Reference<String> ref = this._string;
		String rv;
		
		// Cache?
		if (ref == null || null == (rv = ref.get()))
		{
			// Escape all strings
			StringBuilder sb = new StringBuilder();
			for (String v : this._classes)
			{
				if (sb.length() > 0)
					sb.append('|');
				sb.append(TestResult.__escapeString(v));
			}
			
			// Store
			this._string = new WeakReference<>((rv = sb.toString()));
		}
		
		return rv;
	}
}

