// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.doclet.markdown;

import java.io.Flushable;
import java.io.IOException;

/**
  * Appends text to stdout and the wrapped {@link Appendable}.
 *
 * @since 2016/09/13
 */
class __AppendToStdOut__
	implements Appendable, Flushable
{
	/** Where to append to additionally. */
	protected final Appendable append;
	
	/**
	 * Initializes the appender.
	 *
	 * @param __a Where to append to.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/10/02
	 */
	__AppendToStdOut__(Appendable __a)
		throws NullPointerException
	{
		// Check
		if (__a == null)
			throw new NullPointerException("NARG");
		
		this.append = __a;
		
		// Add a nice banner
		System.out.println("\n----------------------------------------------");
	}
	
	/**
	 * {@inheritDoc}
	 * @sicne 2016/09/13
	 */
	@Override
	public __AppendToStdOut__ append(char __c)
		throws IOException
	{
		this.append.append(__c);
		System.out.append(__c);
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 * @sicne 2016/09/13
	 */
	@Override
	public __AppendToStdOut__ append(CharSequence __cs)
		throws IOException
	{
		// Get the string first
		String s = __cs.toString();
		
		// Append
		this.append.append(s);
		System.out.append(s);
		
		// Self
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 * @sicne 2016/09/13
	 */
	@Override
	public __AppendToStdOut__ append(CharSequence __cs, int __s, int __e)
		throws IOException
	{
		// Get string to use
		String s = __cs.subSequence(__s, __e).toString();
		
		// Append
		this.append.append(s);
		System.out.append(s);
		
		// Self
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 * @sicne 2016/09/13
	 */
	@Override
	public void flush()
		throws IOException
	{
		Appendable append = this.append;
		if (append instanceof Flushable)
			((Flushable)append).flush();
		System.out.flush();
	}
}

