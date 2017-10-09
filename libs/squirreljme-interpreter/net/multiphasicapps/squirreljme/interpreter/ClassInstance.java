// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.interpreter;

import java.lang.ref.Reference;
import java.util.Collection;
import java.util.Deque;
import net.multiphasicapps.squirreljme.jit.cff.ClassName;
import net.multiphasicapps.squirreljme.jit.cff.MethodDescriptor;
import net.multiphasicapps.squirreljme.jit.cff.MethodName;
import net.multiphasicapps.squirreljme.jit.VerifiedJITInput;

/**
 * This represents the instance of a class object. This is specially handled
 * by the virtual machine because classes are unique objects compared to
 * standard objects.
 *
 * @since 2017/10/06
 */
public class ClassInstance
	extends Instance
{
	/**
	 * Initializes the instance of the class object.
	 *
	 * @param __p The owning process.
	 * @since 2017/10/08
	 */
	ClassInstance(Reference<VMProcess> __p)
	{
		super(__p);
		
		throw new todo.TODO();
	}
	
	/**
	 * Returns the default constructor for the class.
	 *
	 * @return The default constructor.
	 * @since 2017/10/08
	 */
	public final ClassMethod getDefaultConstructor()
	{
		return getSpecialMethod(new MethodName("<init>"),
			new MethodDescriptor("()V"));
	}
	
	/**
	 * Returns the method which has the specified name and descriptor without
	 * using virtual lookup.
	 *
	 * @param __n The name of the method.
	 * @param __d The method descriptor.
	 * @return The method.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/10/08
	 */
	public final ClassMethod getSpecialMethod(MethodName __n,
		MethodDescriptor __d)
		throws NullPointerException
	{
		if (__n == null || __d == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
	
	/**
	 * Returns the method using virtual lookup for the given instance.
	 *
	 * @param __i The instance to get the method for.
	 * @param __n The name of the method to locate.
	 * @param __d The descriptor to the method.
	 * @return The method for the given instance.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/10/08
	 */
	public final ClassMethod getVirtualMethod(Instance __i, MethodName __n,
		MethodDescriptor __d)
		throws NullPointerException
	{
		if (__i == null || __n == null || __d == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
}

