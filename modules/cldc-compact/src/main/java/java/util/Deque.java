// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package java.util;

import cc.squirreljme.runtime.cldc.annotation.Api;

@Api
public interface Deque<E>
	extends Queue<E>
{
	@Override
	boolean add(E __a);
	
	@Api
	void addFirst(E __a);
	
	@Api
	void addLast(E __a);
	
	@Override
	boolean contains(Object __a);
	
	@Api
	Iterator<E> descendingIterator();
	
	@Override
	E element();
	
	@Api
	E getFirst();
	
	@Api
	E getLast();
	
	@Override
	Iterator<E> iterator();
	
	@Override
	boolean offer(E __a);
	
	@Api
	boolean offerFirst(E __a);
	
	@Api
	boolean offerLast(E __a);
	
	@Override
	E peek();
	
	@Api
	E peekFirst();
	
	@Api
	E peekLast();
	
	@Override
	E poll();
	
	@Api
	E pollFirst();
	
	@Api
	E pollLast();
	
	@Api
	E pop();
	
	@Api
	void push(E __a);
	
	@Override
	E remove();
	
	@Override
	boolean remove(Object __a);
	
	@Api
	E removeFirst();
	
	@Api
	boolean removeFirstOccurrence(Object __a);
	
	@Api
	E removeLast();
	
	@Api
	boolean removeLastOccurrence(Object __a);
	
	@Override
	int size();
}

