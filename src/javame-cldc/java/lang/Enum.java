// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package java.lang;

public abstract class Enum<E extends Enum<E>>
	implements Comparable<E>
{
	protected Enum(String __a, int __b)
	{
		throw new Error("TODO");
	}
	
	@Override
	protected final Object clone()
		throws CloneNotSupportedException
	{
		throw new Error("TODO");
	}
	
	public final int compareTo(E __a)
	{
		throw new Error("TODO");
	}
	
	@Override
	public final boolean equals(Object __a)
	{
		throw new Error("TODO");
	}
	
	public final Class<E> getDeclaringClass()
	{
		throw new Error("TODO");
	}
	
	@Override
	public final int hashCode()
	{
		throw new Error("TODO");
	}
	
	public final String name()
	{
		throw new Error("TODO");
	}
	
	public final int ordinal()
	{
		throw new Error("TODO");
	}
	
	@Override
	public String toString()
	{
		throw new Error("TODO");
	}
	
	public static <T extends Enum<T>> T valueOf(Class<T> __a, 
		String __b)
	{
		throw new Error("TODO");
	}
}


