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

import net.multiphasicapps.classfile.MethodDescriptor;
import net.multiphasicapps.classfile.MethodName;

/**
 * This represents a class which has been loaded.
 *
 * @since 2019/01/06
 */
public final class LoadedClass
{
	/**
	 * Looks up the given method.
	 *
	 * @param __lut The type of lookup to perform.
	 * @param __static Is the specified method static?
	 * @param __name The name of the method.
	 * @param __desc The method descriptor.
	 * @return The handle to the method.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/01/10
	 */
	public final MethodHandle lookupMethod(MethodLookupType __lut,
		boolean __static, String __name, String __desc)
		throws NullPointerException
	{
		return this.lookupMethod(__lut, __static, new MethodName(__name),
			new MethodDescriptor(__desc));
	}
	
	/**
	 * Looks up the given method.
	 *
	 * @param __lut The type of lookup to perform.
	 * @param __static Is the specified method static?
	 * @param __name The name of the method.
	 * @param __desc The method descriptor.
	 * @return The handle to the method.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/01/10
	 */
	public final MethodHandle lookupMethod(MethodLookupType __lut, 
		boolean __static, MethodName __name, MethodDescriptor __desc)
		throws NullPointerException
	{
		throw new todo.TODO();
	}
}

