// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.symbols;

import net.multiphasicapps.squirreljme.jit.java.ClassFlags;
import net.multiphasicapps.squirreljme.jit.java.ClassName;
import net.multiphasicapps.squirreljme.jit.JITException;

/**
 * The contains information on the structure of the class.
 *
 * @since 2017/08/24
 */
public class ClassStructure
{
	/** The name of this class. */
	protected final ClassName thisname;
	
	/**
	 * Initializes the class structure.
	 *
	 * @param __tn The name of this class.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/08/25
	 */
	public ClassStructure(ClassName __tn)
		throws NullPointerException
	{
		// Check
		if (__tn == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.thisname = __tn;
	}
	
	/**
	 * Sets the flags of the class.
	 *
	 * @param __f The class flags.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/08/25
	 */
	public final void setFlags(ClassFlags __f)
		throws NullPointerException
	{
		// Check
		if (__f == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
	
	/**
	 * Sets the interfaces that this class implements.
	 *
	 * @param __i The interfaces to implement.
	 * @throws JITException If an interface is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/08/25
	 */
	public final void setInterfaces(ClassName... __i)
		throws JITException, NullPointerException
	{
		// Check
		if (__i == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
	
	/**
	 * Sets the super class.
	 *
	 * @param __n The name of the super class.
	 * @throws JITException If the super class is not valid.
	 * @since 2017/08/25
	 */
	public final void setSuperClass(ClassName __n)
		throws JITException
	{
		// {@squirreljme.error JI0s Either Object has a superclass which it
		// cannot extend any class or any other class does not have a super
		// class. (The current class name; The super class name)}
		ClassName thisname = this.thisname;
		if (thisname.equals(new ClassName("java/lang/Object")) !=
			(__n == null))
			throw new JITException(String.format("JI0s %s %s", thisname, __n));
		
		throw new todo.TODO();
	}
}

