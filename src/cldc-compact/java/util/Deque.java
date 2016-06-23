// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package java.util;

public interface Deque<E>
	extends Queue<E>
{
	public abstract boolean add(E __a);
	
	public abstract void addFirst(E __a);
	
	public abstract void addLast(E __a);
	
	public abstract boolean contains(Object __a);
	
	public abstract Iterator<E> descendingIterator();
	
	public abstract E element();
	
	public abstract E getFirst();
	
	public abstract E getLast();
	
	public abstract Iterator<E> iterator();
	
	public abstract boolean offer(E __a);
	
	public abstract boolean offerFirst(E __a);
	
	public abstract boolean offerLast(E __a);
	
	public abstract E peek();
	
	public abstract E peekFirst();
	
	public abstract E peekLast();
	
	public abstract E poll();
	
	public abstract E pollFirst();
	
	public abstract E pollLast();
	
	public abstract E pop();
	
	public abstract void push(E __a);
	
	public abstract E remove();
	
	public abstract boolean remove(Object __a);
	
	public abstract E removeFirst();
	
	public abstract boolean removeFirstOccurrence(Object __a);
	
	public abstract E removeLast();
	
	public abstract boolean removeLastOccurrence(Object __a);
	
	public abstract int size();
}

