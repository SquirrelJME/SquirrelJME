// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.gc;

/**
 * This exception is thrown by the garbage collector if there is a problem with
 * it.
 *
 * @since 2016/06/04
 */
public class GCException
	extends RuntimeException
{
	public GCException()
	{
	}
	
	public GCException(String __m)
	{
		super(__m);
	}
	
	public GCException(String __m, Throwable __c)
	{
		super(__m, __c);
	}
	
	public GCException(Throwable __c)
	{
		super(__c);
	}
}

