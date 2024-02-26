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
import cc.squirreljme.jvm.aot.nanocoat.common.JvmInvokeType;
import java.io.IOException;
import net.multiphasicapps.classfile.MethodReference;

/**
 * Normal method invocation linkage.
 *
 * @since 2023/07/04
 */
public final class InvokeLinkage
	implements Linkage
{
	/** The type of invocation. */
	protected final JvmInvokeType type;
	
	/** The target method. */
	protected final MethodReference target;
	
	/**
	 * Initializes the invocation linkage.
	 *
	 * @param __type The type of invocation.
	 * @param __target The target method.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/07/04
	 */
	public InvokeLinkage(JvmInvokeType __type, MethodReference __target)
		throws NullPointerException
	{
		if (__type == null || __target == null)
			throw new NullPointerException("NARG");
		
		this.type = __type;
		this.target = __target;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/07/04
	 */
	@Override
	public boolean equals(Object __o)
	{
		if (__o == this)
			return true;
		if (!(__o instanceof InvokeLinkage))
			return false;
		
		InvokeLinkage o = (InvokeLinkage)__o;
		return this.target.equals(o.target) &&
			this.type == o.type;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/07/04
	 */
	@Override
	public int hashCode()
	{
		return this.type.hashCode() ^ this.target.hashCode();
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
			 __output.memberStructSet("invokeNormal"))
		{
			struct.memberSet("type",
				CBasicExpression.number(this.type.ordinal()));
			
			struct.memberSet("targetClass",
				CBasicExpression.string(this.target.className().toString()));
			struct.memberSet("targetMethodName",
				CBasicExpression.string(this.target.memberName().toString()));
			struct.memberSet("targetMethodType",
				CBasicExpression.string(this.target.memberType().toString()));
		}
	}
}
