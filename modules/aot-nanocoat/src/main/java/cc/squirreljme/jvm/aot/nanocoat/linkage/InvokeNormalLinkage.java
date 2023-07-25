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
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.io.IOException;
import net.multiphasicapps.classfile.MethodNameAndType;
import net.multiphasicapps.classfile.MethodReference;

/**
 * Normal method invocation linkage.
 *
 * @since 2023/07/04
 */
public final class InvokeNormalLinkage
	implements Linkage
{
	/** Is this static? */
	protected final boolean isStatic;
	
	/** The source method. */
	protected final MethodNameAndType source;
	
	/** The target method. */
	protected final MethodReference target;
	
	/**
	 * Initializes the normal linkage.
	 * 
	 * @param __source The source method name and type.
	 * @param __static Is this static?
	 * @param __target The target method.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/07/04
	 */
	public InvokeNormalLinkage(MethodNameAndType __source, boolean __static,
		MethodReference __target)
		throws NullPointerException
	{
		if (__source == null || __target == null)
			throw new NullPointerException("NARG");
		
		this.source = __source;
		this.isStatic = __static;
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
		if (!(__o instanceof InvokeNormalLinkage))
			return false;
		
		InvokeNormalLinkage o = (InvokeNormalLinkage)__o;
		return this.source.equals(o.source) &&
			this.target.equals(o.target) &&
			this.isStatic == o.isStatic;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/07/04
	 */
	@Override
	public int hashCode()
	{
		return this.source.hashCode() ^ this.target.hashCode() +
			(this.isStatic ? 1 : 0);
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
			struct.memberSet("isStatic",
				(this.isStatic ? Constants.TRUE : Constants.FALSE));
			
			struct.memberSet("sourceMethodName",
				CBasicExpression.string(this.source.name().toString()));
			struct.memberSet("sourceMethodType",
				CBasicExpression.string(this.source.type().toString()));
			
			struct.memberSet("targetClass",
				CBasicExpression.string(this.target.className().toString()));
			struct.memberSet("targetMethodName",
				CBasicExpression.string(this.target.memberName().toString()));
			struct.memberSet("targetMethodType",
				CBasicExpression.string(this.target.memberType().toString()));
		}
	}
}
