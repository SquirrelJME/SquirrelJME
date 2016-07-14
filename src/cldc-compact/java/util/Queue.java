// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package java.util;

public interface Queue<E>
	extends Collection<E>
{
	public abstract boolean add(E __a);
	
	public abstract E element();
	
	public abstract boolean offer(E __a);
	
	public abstract E peek();
	
	public abstract E poll();
	
	public abstract E remove();
}

