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
 * Appends text to stdout.
 *
 * @since 2016/09/13
 */
class __AppendToStdOut__
	implements Appendable, Flushable
{
	/**
	 * {@inheritDoc}
	 * @sicne 2016/09/13
	 */
	@Override
	public __AppendToStdOut__ append(char __c)
		throws IOException
	{
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
		System.out.append(__cs);
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
		System.out.append(__cs, __s, __e);
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
		System.out.flush();
	}
}

