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

public interface Deque<E>
	extends Queue<E>
{
	
	void addFirst(E __a);
	
	void addLast(E __a);
	
	Iterator<E> descendingIterator();
	
	@Override
	E element();
	
	E getFirst();
	
	E getLast();
	
	boolean offerFirst(E __a);
	
	boolean offerLast(E __a);
	
	@Override
	E peek();
	
	E peekFirst();
	
	E peekLast();
	
	@Override
	E poll();
	
	E pollFirst();
	
	E pollLast();
	
	E pop();
	
	void push(E __a);
	
	@Override
	E remove();
	
	E removeFirst();
	
	boolean removeFirstOccurrence(Object __a);
	
	E removeLast();
	
	boolean removeLastOccurrence(Object __a);
	
}

