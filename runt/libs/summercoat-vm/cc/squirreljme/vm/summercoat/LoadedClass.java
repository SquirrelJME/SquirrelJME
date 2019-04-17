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

import dev.shadowtail.classfile.mini.MinimizedClassFile;
import net.multiphasicapps.classfile.MethodDescriptor;
import net.multiphasicapps.classfile.MethodName;

/**
 * This represents a class which has been loaded.
 *
 * @since 2019/01/06
 */
public final class LoadedClass
{
	/** The minimized class. */
	protected final MinimizedClassFile miniclass;
	
	/** The super class. */
	protected final LoadedClass superclass;
	
	/** Interface classes. */
	final LoadedClass[] _interfaces;
	
	/**
	 * Initializes the loaded class.
	 *
	 * @param __cf The minimized class file.
	 * @param __sn The super class.
	 * @param __in The interfaces.
	 * @throws NullPointerException On null arguments, except for {@code __sn}.
	 * @since 2019/04/17
	 */
	public LoadedClass(MinimizedClassFile __cf, LoadedClass __sn,
		LoadedClass[] __in)
		throws NullPointerException
	{
		if (__cf == null || __in == null)
			throw new NullPointerException("NARG");
		
		for (LoadedClass o : (__in = __in.clone()))
			if (o == null)
				throw new NullPointerException("NARG");
		
		this.miniclass = __cf;
		this.superclass = __sn;
		this._interfaces = __in;
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

