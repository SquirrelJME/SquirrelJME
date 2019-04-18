// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.summercoat;

import net.multiphasicapps.classfile.ClassName;
import net.multiphasicapps.classfile.MethodNameAndType;

/**
 * This is a handle which starts the method lookup at the specified class
 * rather than at the top, so that super calls are handled correctly.
 *
 * Lookup will naturally fail if the method is not valid.
 *
 * @since 2019/04/17
 */
public final class SuperMethodHandle
	implements MethodHandle
{
	/** The starting point for the lookup. */
	protected final ClassName classname;
	
	/** The name and type of the method, for virtual lookup. */
	protected final MethodNameAndType nameandtype;
	
	/**
	 * Initalizes the super method handle.
	 *
	 * @param __cn The class name.
	 * @param __nat The name and type.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/17
	 */
	public SuperMethodHandle(ClassName __cn, MethodNameAndType __nat)
		throws NullPointerException
	{
		if (__cn == null || __nat == null)
			throw new NullPointerException("NARG");
		
		this.classname = __cn;
		this.nameandtype = __nat;
	}
}

