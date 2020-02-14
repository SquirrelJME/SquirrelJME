// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
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

