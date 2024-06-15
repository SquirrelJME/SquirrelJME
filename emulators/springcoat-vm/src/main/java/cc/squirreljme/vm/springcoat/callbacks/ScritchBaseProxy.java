// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.springcoat.callbacks;

import cc.squirreljme.vm.springcoat.SpringMachine;
import cc.squirreljme.vm.springcoat.SpringProxyObject;

/**
 * Not Described.
 *
 * @param <I> The interface to proxy.
 * @since 2024/06/15
 */
public abstract class ScritchBaseProxy<I>
	extends SpringProxyObject
{
	/** The proxied interface. */
	public final I wrapped;
	
	/**
	 * Initializes the base proxy.
	 *
	 * @param __class The class being proxied.
	 * @param __machine The machine to use.
	 * @param __wrapped The wrapped interface.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/06/15
	 */
	public ScritchBaseProxy(Class<I> __class, SpringMachine __machine,
		I __wrapped)
		throws NullPointerException
	{
		super(__class, __machine);
		
		if (__wrapped == null)
			throw new NullPointerException("NARG");
		
		this.wrapped = __wrapped;
	}
}
