// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.aot.nanocoat;

import cc.squirreljme.c.CBasicExpression;
import cc.squirreljme.c.CExpression;
import cc.squirreljme.c.CExpressionBuilder;
import cc.squirreljme.c.CRootExpressionBuilder;
import cc.squirreljme.c.CStructType;
import cc.squirreljme.c.CType;
import cc.squirreljme.c.CVariable;
import cc.squirreljme.c.std.CTypeProvider;
import cc.squirreljme.jvm.aot.nanocoat.common.Constants;
import cc.squirreljme.jvm.aot.nanocoat.common.JvmTypes;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.io.IOException;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * Represents temporary value storage within the virtual machine.
 *
 * @since 2023/07/15
 */
public class JvmTemporary
{
	/** The temporary variable index. */
	protected final int index;
	
	/** Cache for the temporary index. */
	private volatile Reference<CExpression> _tempIndex;
	
	/**
	 * Initializes the temporary.
	 * 
	 * @param __index THe index of the temporary.
	 * @throws IllegalArgumentException If the index is not valid.
	 * @since 2023/07/15
	 */
	JvmTemporary(int __index)
		throws IllegalArgumentException
	{
		if (__index < 0)
			throw new IllegalArgumentException("IOOB");
		
		this.index = __index;
	}
	
	/**
	 * Accesses the given type.
	 * 
	 * @param __type The type to access.
	 * @return The expression for the type.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/07/15
	 */
	public CExpression access(CType __type)
		throws IOException, NullPointerException
	{
		if (__type == null)
			throw new NullPointerException("NARG");
		
		return CExpressionBuilder.builder()
			.identifier(Constants.TEMPORARY)
			.arrayAccess(this.index)
			.expression(JvmTemporary.__viaType(false, __type))
			.build();
	}
	
	/**
	 * Accesses the given type.
	 * 
	 * @param __type The type to access.
	 * @return The expression for the type.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/07/15
	 */
	public CExpression access(CTypeProvider __type)
		throws IOException, NullPointerException
	{
		if (__type == null)
			throw new NullPointerException("NARG");
		
		return this.access(__type.type());
	}
	
	/**
	 * Accesses the temporary stack of the current temporary index to access
	 * a value of the given type.
	 * 
	 * @param __type The type to access.
	 * @return The expression to access the type.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/07/15
	 */
	public CExpression accessTemp(CType __type)
		throws IOException, NullPointerException
	{
		if (__type == null)
			throw new NullPointerException("NARG");
		
		return CExpressionBuilder.builder()
			.identifier(__StaticCodeVariables__.currentFrame())
			.dereferenceStruct()
			.identifier(JvmTypes.VMFRAME.type(CStructType.class)
				.member("tempStack"))
			.arrayAccess(this.tempIndex())
			.expression(JvmTemporary.__viaType(false, __type))
			.build();
	}
	
	/**
	 * Accesses the temporary stack of the current temporary index to access
	 * a value of the given type.
	 * 
	 * @param __type The type to access.
	 * @return The expression to access the type.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/07/15
	 */
	public CExpression accessTemp(CTypeProvider __type)
		throws IOException, NullPointerException
	{
		if (__type == null)
			throw new NullPointerException("NARG");
		
		return this.accessTemp(__type.type());
	}
	
	/**
	 * References the given type.
	 * 
	 * @param __type The type to access.
	 * @return The expression for the type.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/07/15
	 */
	public CExpression reference(CType __type)
		throws IOException, NullPointerException
	{
		return CBasicExpression.reference(this.access(__type));
	}
	
	/**
	 * References the given type.
	 * 
	 * @param __type The type to access.
	 * @return The expression for the type.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/07/15
	 */
	public CExpression reference(CTypeProvider __type)
		throws IOException, NullPointerException
	{
		if (__type == null)
			throw new NullPointerException("NARG");
		
		return this.reference(__type.type());
	}
	
	/**
	 * Accesses the temporary stack of the current temporary index to reference
	 * a value of the given type.
	 * 
	 * @param __type The type to reference.
	 * @return The expression to reference the type.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/07/15
	 */
	public CExpression referenceTemp(CType __type)
		throws IOException, NullPointerException
	{
		return CBasicExpression.reference(this.accessTemp(__type));
	}
	
	/**
	 * Accesses the temporary stack of the current temporary index to reference
	 * a value of the given type.
	 * 
	 * @param __type The type to reference.
	 * @return The expression to reference the type.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/07/15
	 */
	public CExpression referenceTemp(CTypeProvider __type)
		throws IOException, NullPointerException
	{
		if (__type == null)
			throw new NullPointerException("NARG");
		
		return this.referenceTemp(__type.type());
	}
	
	/**
	 * Quick access to temporary index.
	 * 
	 * @return The temporary index.
	 * @throws IOException On write errors.
	 * @since 2023/07/15
	 */
	public CExpression tempIndex()
		throws IOException
	{
		Reference<CExpression> ref = this._tempIndex;
		CExpression rv;
		
		// Cache this since it will definitely be used much
		if (ref == null || (rv = ref.get()) == null)
		{
			rv = this.access(JvmTypes.TEMP_INDEX);
			this._tempIndex = new WeakReference<>(rv);
		}
		
		return rv;
	}
	
	/**
	 * Determines the expression to use to access the given type.
	 * 
	 * @param __pointer Is this a pointer union access?
	 * @param __type The type that is desired for accessing.
	 * @return The expression to access the type.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/07/15
	 */
	private static CExpression __viaType(boolean __pointer, CType __type)
		throws IOException, NullPointerException
	{
		if (__type == null)
			throw new NullPointerException("NARG");
		
		// Referring to self is just an empty value
		if (JvmTypes.ANY.type().equals(__type))
			return CExpression.EMPTY;
		
		// Determine the type ID by looking for union members
		CVariable member = null;
		for (CVariable var : JvmTypes.ANY_DATA.type(CStructType.class)
			.members())
			if (__type.equals(var.type))
			{
				member = var;
				break;
			}
		
		// Need a member for this?
		if (member == null)
			throw Debugging.todo(__type);
		
		// Construct expression to access it
		CRootExpressionBuilder builder = CExpressionBuilder.builder();
		
		// Pointer or through struct?
		if (__pointer)
			builder.dereferenceStruct();
		else
			builder.structAccess();
			
		// Finish off
		return builder
			.identifier("data")
			.structAccess()
			.identifier(member)
			.build();
	}
}
