// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.debugger;

import cc.squirreljme.jdwp.JDWPId;
import net.multiphasicapps.classfile.ClassName;
import net.multiphasicapps.classfile.MethodDescriptor;
import net.multiphasicapps.classfile.MethodFlags;
import net.multiphasicapps.classfile.MethodName;

/**
 * Not Described.
 *
 * @since 2024/01/22
 */
public class InfoMethod
	extends Info
{
	/** The method flags. */
	protected final MethodFlags flags;
	
	/** The method name. */
	protected final MethodName name;
	
	/** The method type. */
	protected final MethodDescriptor type;
	
	/** The class this is in. */
	protected final ClassName inClass;
	
	/**
	 * Initializes the method information.
	 *
	 * @param __state The debugger state.
	 * @param __id The ID number of this info.
	 * @param __inClass The class this is in.
	 * @param __name The method name.
	 * @param __type The method type.
	 * @param __flags The method flags.
	 * @since 2024/01/22
	 */
	public InfoMethod(DebuggerState __state, JDWPId __id, ClassName __inClass,
		MethodName __name, MethodDescriptor __type, MethodFlags __flags)
		throws NullPointerException
	{
		super(__state, __id, InfoKind.METHOD);
		
		this.inClass = __inClass;
		this.name = __name;
		this.type = __type;
		this.flags = __flags;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/01/22
	 */
	@Override
	protected boolean internalUpdate(DebuggerState __state)
		throws NullPointerException
	{
		return true;
	}
}
