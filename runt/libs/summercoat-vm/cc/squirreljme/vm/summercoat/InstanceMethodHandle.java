// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.summercoat;

import net.multiphasicapps.classfile.ClassName;
import net.multiphasicapps.classfile.MethodNameAndType;

/**
 * This is a method handle which when called, it will look up the appropriate
 * method to execute for the given object.
 *
 * Lookup will naturally fail if the method is not valid.
 *
 * @since 2019/01/10
 */
public final class InstanceMethodHandle
	implements MethodHandle
{
	/** The class we hope to be looking in. */
	protected final ClassName classname;
	
	/** The name and type of the method, for virtual lookup. */
	protected final MethodNameAndType nameandtype;
	
	/**
	 * Initalizes the instance method handle.
	 *
	 * @param __cn The class name.
	 * @param __nat The name and type.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/17
	 */
	public InstanceMethodHandle(ClassName __cn, MethodNameAndType __nat)
		throws NullPointerException
	{
		if (__cn == null || __nat == null)
			throw new NullPointerException("NARG");
		
		this.classname = __cn;
		this.nameandtype = __nat;
	}
}

