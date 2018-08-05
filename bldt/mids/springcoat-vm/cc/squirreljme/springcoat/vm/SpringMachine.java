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

import cc.squirreljme.builder.support.Binary;
import net.multiphasicapps.classfile.ClassName;

/**
 * This class contains the instance of the SpringCoat virtual machine and has
 * a classpath along with all the needed storage for variables and such.
 *
 * @since 2018/07/29
 */
public final class SpringMachine
{
	/** The class path for the machine. */
	protected final Binary[] classpath;
	
	/**
	 * Initializes the machine.
	 *
	 * @param __classpath The classpath.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/08/04
	 */
	public SpringMachine(Binary... __classpath)
		throws NullPointerException
	{
		for (Binary b : __classpath = (__classpath == null ? new Binary[0] :
			__classpath.clone()))
			if (b == null)
				throw new NullPointerException("NARG");
		this.classpath = __classpath;
	}
	
	/**
	 * Locates the specified class in the machine.
	 *
	 * @param __cn The class to load.
	 * @return 
	 * @throws NullPointerException On null arguments.
	 */
	public final SpringClass locateClass(ClassName __cn)
		throws NullPointerException
	{
		if (__cn == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
	
	public final SpringClass locateClass(String __cn)
		throws NullPointerException
	{
		if (__cn == null)
			throw new NullPointerException("NARG");
		
		return this.locateClass(new ClassName(__cn));
	}
}

