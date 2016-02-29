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

public interface ListIterator<E>
	extends Iterator<E>
{
	public abstract void add(E __a);
	
	public abstract boolean hasNext();
	
	public abstract boolean hasPrevious();
	
	public abstract E next();
	
	public abstract int nextIndex();
	
	public abstract E previous();
	
	public abstract int previousIndex();
	
	public abstract void remove();
	
	public abstract void set(E __a);
}

