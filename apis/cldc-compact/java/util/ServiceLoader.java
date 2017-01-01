// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package java.util;

public final class ServiceLoader<S>
	implements Iterable<S>
{
	private ServiceLoader()
	{
		super();
		throw new Error("TODO");
	}
	
	public Iterator<S> iterator()
	{
		throw new Error("TODO");
	}
	
	public void reload()
	{
		throw new Error("TODO");
	}
	
	@Override
	public String toString()
	{
		throw new Error("TODO");
	}
	
	public static <S> ServiceLoader<S> load(Class<S> __a)
	{
		throw new Error("TODO");
	}
}

