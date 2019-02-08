// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.scrf;

import net.multiphasicapps.classfile.ClassName;
import net.multiphasicapps.classfile.MemberName;
import net.multiphasicapps.classfile.MethodDescriptor;

/**
 * This represents a reference to another method.
 *
 * @since 2019/02/07
 */
public final class VTableMethodReference
	extends VTableMemberReference
{
	/** The invocation type. */
	protected final InvokeType invoketype;
	
	/**
	 * Initializes the reference.
	 *
	 * @param __it Invocation type.
	 * @param __cl The class name.
	 * @param __mn The member name.
	 * @param __mt The member type.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/02/07
	 */
	public VTableMethodReference(InvokeType __it, ClassName __cl,
		MemberName __mn, MethodDescriptor __mt)
		throws NullPointerException
	{
		super((__it == InvokeType.STATIC), __cl, __mn, __mt);
		
		if (__it == null)
			throw new NullPointerException("NARG");
		
		this.invoketype = __it;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/02/07
	 */
	@Override
	public boolean equals(Object __o)
	{
		if (!super.equals(__o))
			return false;
		
		if (!(__o instanceof VTableMethodReference))
			return false;
		
		return this.invoketype == ((VTableMethodReference)__o).invoketype;
	}
}

