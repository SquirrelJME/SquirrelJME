// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package java.lang.ref;

public class ReferenceQueue<T>
{
	public ReferenceQueue()
	{
		super();
		throw new Error("TODO");
	}
	
	public Reference<? extends T> poll()
	{
		throw new Error("TODO");
	}
	
	public Reference<? extends T> remove(long __a)
		throws IllegalArgumentException, InterruptedException
	{
		if (false)
			throw new IllegalArgumentException();
		if (false)
			throw new InterruptedException();
		throw new Error("TODO");
	}
	
	public Reference<? extends T> remove()
		throws InterruptedException
	{
		if (false)
			throw new InterruptedException();
		throw new Error("TODO");
	}
}

