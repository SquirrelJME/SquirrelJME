// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.springcoat.vm;

/**
 * This class is used to setup and initialize the shaded entry point for
 * execution.
 *
 * @since 2018/11/15
 */
public class ShadedMain
{
	/**
	 * Main entry point which runs off the given class.
	 *
	 * @param __acl The class to use resources from.
	 * @param __pfx The prefix for library lookup.
	 * @param __cp The classpath for execution.
	 * @param __args Arguments to the VM.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/11/15
	 */
	public static final void main(Class<?> __acl, String __pfx,
		String[] __cp, String... __args)
		throws NullPointerException
	{
		if (__acl == null || __pfx == null || __cp == null || __args == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
	
	/**
	 * Main entry point.
	 *
	 * @param __args Program arguments.
	 * @since 2018/11/15
	 */
	public static final void main(String... __args)
	{
		throw new todo.TODO();
	}
}

