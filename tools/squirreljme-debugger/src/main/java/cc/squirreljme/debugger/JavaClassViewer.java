// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.debugger;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import net.multiphasicapps.classfile.ClassFile;
import net.multiphasicapps.classfile.ClassName;
import net.multiphasicapps.classfile.Method;

/**
 * Viewer for Class files using SquirrelJME's {@link ClassFile}.
 *
 * @since 2024/01/22
 */
public class JavaClassViewer
	implements ClassViewer
{
	/** The class to view. */
	protected final ClassFile classFile;
	
	/** Known methods. */
	private volatile MethodViewer[] _methods;
	
	/**
	 * Initializes the class viewer.
	 *
	 * @param __class The class to view.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/22
	 */
	public JavaClassViewer(ClassFile __class)
		throws NullPointerException
	{
		if (__class == null)
			throw new NullPointerException("NARG");
		
		this.classFile = __class;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/01/22
	 */
	@Override
	public MethodViewer[] methods()
	{
		// Need to wrap each method?
		MethodViewer[] result = this._methods;
		if (result == null)
		{
			// Get class methods
			Method[] classMethods = this.classFile.methods();
			int count = classMethods.length;
			
			// Setup result and wrap all methods
			result = new MethodViewer[count];
			for (int i = 0; i < count; i++)
				result[i] = new JavaMethodViewer(classMethods[i]);
			
			// Cache for later
			this._methods = result;
		}
		
		// Return cached value
		return result;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/01/22
	 */
	@Override
	public ClassName thisName()
	{
		return this.classFile.thisName();
	}
}
