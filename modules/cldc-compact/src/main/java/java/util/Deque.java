// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package java.util;

import cc.squirreljme.runtime.cldc.annotation.Api;

public interface Deque<E>
	extends Queue<E>
{
	@Override
	boolean add(E __a);
	
	void addFirst(E __a);
	
	void addLast(E __a);
	
	@Api
	@Override
	boolean contains(Object __a);
	
	Iterator<E> descendingIterator();
	
	@Api
	@Override
	E element();
	
	E getFirst();
	
	E getLast();
	
	@Override
	Iterator<E> iterator();
	
	@Api
	@Override
	boolean offer(E __a);
	
	boolean offerFirst(E __a);
	
	boolean offerLast(E __a);
	
	@Api
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
	
	@Api
	@Override
	E remove();
	
	@Override
	boolean remove(Object __a);
	
	E removeFirst();
	
	boolean removeFirstOccurrence(Object __a);
	
	E removeLast();
	
	boolean removeLastOccurrence(Object __a);
	
	@Api
	@Override
	int size();
}

