// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.aot.nanocoat.linkage;

import cc.squirreljme.c.CBasicExpression;
import cc.squirreljme.c.CStructVariableBlock;
import cc.squirreljme.jvm.aot.nanocoat.common.Constants;
import java.io.IOException;
import net.multiphasicapps.classfile.FieldReference;
import net.multiphasicapps.classfile.MethodNameAndType;

/**
 * Represents linkage to accessing a field.
 *
 * @since 2023/07/16
 */
public final class FieldAccessLinkage
	implements Linkage
{
	/** Is this static? */
	protected final boolean isStatic;
	
	/** The target method. */
	protected final FieldReference target;
	
	/** Is this writing the field? */
	protected final boolean isStore;
	
	/**
	 * Initializes the linkage.
	 *
	 * @param __static Is the access static?
	 * @param __target The target field being access.
	 * @param __store Is the access writing the value?
	 * @throws NullPointerException On null arguments.
	 * @since 2023/07/16
	 */
	public FieldAccessLinkage(boolean __static,
		FieldReference __target, boolean __store)
		throws NullPointerException
	{
		if (__target == null)
			throw new NullPointerException("NARG");
		
		this.isStatic = __static;
		this.target = __target;
		this.isStore = __store;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/07/16
	 */
	@Override
	public boolean equals(Object __o)
	{
		if (this == __o)
			return true;
		if (!(__o instanceof FieldAccessLinkage))
			return false;
		
		FieldAccessLinkage o = (FieldAccessLinkage)__o;
		return this.isStatic == o.isStatic &&
			this.isStore == o.isStore &&
			this.target.equals(o.target);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/07/16
	 */
	@Override
	public int hashCode()
	{
		return this.target.hashCode() +
			(this.isStatic ? 1 : 0) +
			(this.isStore ? 2 : 0);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/07/25
	 */
	@Override
	public void write(CStructVariableBlock __output)
		throws IOException, NullPointerException
	{
		try (CStructVariableBlock struct =
			 __output.memberStructSet("fieldAccess"))
		{
			struct.memberSet("isStatic",
				(this.isStatic ? Constants.TRUE : Constants.FALSE));
			struct.memberSet("isStore",
				(this.isStore ? Constants.TRUE : Constants.FALSE));
			
			struct.memberSet("targetClass",
				CBasicExpression.string(this.target.className().toString()));
			struct.memberSet("targetFieldName",
				CBasicExpression.string(this.target.memberName().toString()));
			struct.memberSet("targetFieldType",
				CBasicExpression.string(this.target.memberType().toString()));
		}
	}
}
