// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.summercoat;

/**
 * This is an instance which provides execution access to statically execute
 * a method handle.
 *
 * @since 2019/04/17
 */
@Deprecated
public final class StaticMethodInstance
	extends PlainObject
{
	/** The handle to execute. */
	protected final MethodHandle handle;
	
	/**
	 * Initializes the static method instance.
	 *
	 * @param __mh The method handle to use.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/17
	 */
	public StaticMethodInstance(MethodHandle __mh)
		throws NullPointerException
	{
		if (__mh == null)
			throw new NullPointerException("NARG");
		
		this.handle = __mh;
	}
}

