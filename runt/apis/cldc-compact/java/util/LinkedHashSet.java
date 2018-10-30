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

public class LinkedHashSet<E>
	extends HashSet<E>
	implements Set<E>, Cloneable
{
	public LinkedHashSet(int __initcap, float __load)
	{
		super(__initcap, __load);
	}
	
	public LinkedHashSet(int __initcap)
	{
		super(__initcap);
	}
	
	public LinkedHashSet()
	{
		super();
	}
	
	public LinkedHashSet(Collection<? extends E> __c)
	{
		super(__c);
	}
}

