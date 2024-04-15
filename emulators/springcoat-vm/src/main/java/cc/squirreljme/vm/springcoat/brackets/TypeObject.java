// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.springcoat.brackets;

import cc.squirreljme.jvm.mle.brackets.TypeBracket;
import cc.squirreljme.vm.springcoat.AbstractGhostObject;
import cc.squirreljme.vm.springcoat.SpringClass;
import cc.squirreljme.vm.springcoat.SpringMachine;

/**
 * Represents a type from within the VM.
 *
 * @since 2020/06/02
 */
public class TypeObject
	extends AbstractGhostObject
{
	/** The class this provides. */
	protected final SpringClass classy;
	
	/**
	 * Initializes the type object.
	 *
	 * @param __machine The machine used.
	 * @param __cl The class to use.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/06/04
	 */
	public TypeObject(SpringMachine __machine, SpringClass __cl)
		throws NullPointerException
	{
		super(__machine, TypeBracket.class);
		
		if (__cl == null)
			throw new NullPointerException("NARG");
		
		this.classy = __cl;
	}
	
	/**
	 * Returns the class this refers to.
	 *
	 * @return The class this refers to.
	 * @since 2020/06/13
	 */
	public final SpringClass getSpringClass()
	{
		return this.classy;
	}
}
