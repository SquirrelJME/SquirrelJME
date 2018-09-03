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

import java.io.DataInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import net.multiphasicapps.classfile.ClassName;
import net.multiphasicapps.classfile.ClassFile;
import net.multiphasicapps.classfile.Method;
import net.multiphasicapps.classfile.MethodDescriptor;
import net.multiphasicapps.classfile.MethodName;
import net.multiphasicapps.classfile.MethodNameAndType;

/**
 * This is a representation of a class file as it is seen by the virtual
 * machine, it is intended to remain simple and only refer to what is needed
 * for the machine to run.
 *
 * @since 2018/07/21
 */
public final class SpringClass
{
	/** The name of this class. */
	protected final ClassName name;
	
	/** The class file data. */
	protected final ClassFile file;
	
	/** The super class. */
	protected final SpringClass superclass;
	
	/** Interface classes. */
	private final SpringClass[] _interfaceclasses;
	
	/** Methods which exist in this class, includes statics for this only. */
	private final Map<MethodNameAndType, SpringMethod> _methods =
		new HashMap<>();
	
	/**
	 * Initializes the spring class.
	 *
	 * @param __super The super class of this class.
	 * @param __interfaces The the interfaces this class implements.
	 * @param __cf The class file for this class.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/07/21
	 */
	SpringClass(SpringClass __super, SpringClass[] __interfaces,
		ClassFile __cf)
		throws NullPointerException
	{
		if (__interfaces == null || __cf == null)
			throw new NullPointerException("NARG");
		
		ClassName name = __cf.thisName();
		this.name = name;
		
		this.file = __cf;
		this.superclass = __super;
		
		// Check
		this._interfaceclasses = (__interfaces = __interfaces.clone());
		for (SpringClass x : __interfaces)
			if (x == null)
				throw new NullPointerException("NARG");
		
		// Go through and initialize methods declared in this class
		Map<MethodNameAndType, SpringMethod> methods = this._methods;
		for (Method m : __cf.methods())
			if (null != methods.put(m.nameAndType(), new SpringMethod(m)))
			{
				// {@squirreljme.error BK06 Duplicated method in class. (The
				// method)}
				throw new SpringClassFormatException(name, String.format(
					"BK06 %s", m.nameAndType()));
			}
		
		// Go through super and interfaces and add non-static methods which
		for (int i = 0, n = __interfaces.length; i <= n; i++)
		{
			// The class to look within
			SpringClass lookin = (i == 0 ? __super : __interfaces[i - 1]);
			if (lookin == null)
				continue;
			
			// Go through class methods
			for (Map.Entry<MethodNameAndType, SpringMethod> e :
				lookin._methods.entrySet())
			{
				MethodNameAndType k = e.getKey();
				SpringMethod v = e.getValue();
				
				// Ignore static and initializer methods
				if (v.isStatic() || v.isInstanceInitializer() ||
					v.isStaticInitializer())
					continue;
				
				// If the method does not exist in the table then it gets added
				// otherwise it is effectively replaced
				if (!methods.containsKey(k))
					methods.put(k, v);
			}
		}
		
		// Debug
		todo.DEBUG.note("Class %s has %d methods.", name, methods.size());
	}
	
	/**
	 * Locates the given method in the class.
	 *
	 * @param __static Is the method static?
	 * @param __name The name of the method.
	 * @param __desc The descriptor of the method.
	 * @return The method which was found.
	 * @throws NullPointerException On null arguments.
	 * @throws SpringNoSuchMethodException If the specified method does not
	 * exist.
	 * @since 2018/09/03
	 */
	public final SpringMethod lookupMethod(boolean __static, MethodName __name,
		MethodDescriptor __desc)
		throws NullPointerException, SpringNoSuchMethodException
	{
		if (__name == null || __desc == null)
			throw new NullPointerException("NARG");
		
		return this.lookupMethod(__static, new MethodNameAndType(__name,
			__desc));
	}
	
	/**
	 * Locates the given method in the class.
	 *
	 * @param __static Is the method static?
	 * @param __nat The name and type of the method.
	 * @return The method which was found.
	 * @throws NullPointerException On null arguments.
	 * @throws SpringNoSuchMethodException If the specified method does not
	 * exist.
	 * @since 2018/09/03
	 */
	public final SpringMethod lookupMethod(boolean __static,
		MethodNameAndType __nat)
		throws NullPointerException, SpringNoSuchMethodException
	{
		if (__nat == null)
			throw new NullPointerException("NARG");
		
		// Debug
		todo.DEBUG.note("Looking up method %s (static=%b)", __nat, __static);
		
		// {@squirreljme.error BK07 The specified method does not exist.
		// (The class which was looked in; The name and type of the method)} 
		SpringMethod rv = this._methods.get(__nat);
		if (rv == null)
			throw new SpringNoSuchMethodException(String.format("BK07 %s %s",
				this.name, __nat));
		
		// {@squirreljme.error BK08 The specified method exists in the class
		// however it does not match being static. (The class the method is in;
		// The name and type of the method; If a static method was requested)}
		if (rv.isStatic() != __static)
			throw new SpringNoSuchMethodException(String.format("BK08 %s %s",
				this.name, __nat, __static));
		
		return rv;
	}
}

