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
import cc.squirreljme.c.CFunctionBlock;
import cc.squirreljme.c.CStructType;
import cc.squirreljme.c.CType;
import cc.squirreljme.c.CTypeDefType;
import cc.squirreljme.c.CVariable;
import cc.squirreljme.jvm.aot.nanocoat.common.Constants;
import cc.squirreljme.jvm.aot.nanocoat.common.JvmTypes;
import cc.squirreljme.jvm.aot.nanocoat.linkage.Container;
import cc.squirreljme.jvm.aot.nanocoat.linkage.Linkage;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.io.IOException;

/**
 * Utility class for making code variables.
 *
 * @since 2023/06/19
 */
public final class __CodeVariables__
{
	/** Initial variables output. */
	private final CFunctionBlock _initVars;
	
	/** The current number of temporaries. */
	volatile int _numTemporaries;
	
	/** The current maximum number of temporaries. */
	volatile int _maxTemporaries;
	
	/**
	 * Initializes the code variables.
	 * 
	 * @param __initVars The initial variables to write in.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/07/15
	 */
	__CodeVariables__(CFunctionBlock __initVars)
		throws NullPointerException
	{
		if (__initVars == null)
			throw new NullPointerException("NARG");
		
		this._initVars = __initVars;
	}
	
	/**
	 * Returns the variable that represents the current stack frame.
	 * 
	 * @return The variable that represents the current stack frame.
	 * @since 2023/06/19
	 */
	public CVariable currentFrame()
	{
		return __StaticCodeVariables__.currentFrame();
	}
	
	/**
	 * Returns the current state variable.
	 * 
	 * @return The current state variable.
	 * @since 2023/06/19
	 */
	public CVariable currentState()
	{
		return __StaticCodeVariables__.currentState();
	}
	
	/**
	 * Returns the current thread.
	 * 
	 * @return The current thread.
	 * @since 2023/06/25
	 */
	public CVariable currentThread()
	{
		return __StaticCodeVariables__.currentThread();
	}
	
	/**
	 * Creates a linkage reference.
	 * 
	 * @param __linkage The linkage to refer to.
	 * @param __what What member to look for.
	 * @return The expression to refer to the linkage.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/07/04
	 */
	public CExpression linkageReference(
		Container<? extends Linkage> __linkage, String __what)
		throws IOException, NullPointerException
	{
		if (__linkage == null || __what == null)
			throw new NullPointerException("NARG");
		
		return CExpressionBuilder.builder()
			.reference(CExpressionBuilder.builder()
				.identifier(this.currentFrame())
				.dereferenceStruct()
				.identifier(JvmTypes.VMFRAME
					.type(CStructType.class).member("linkage"))
				.arrayAccess(__linkage.index())
				.dereferenceStruct()
				.identifier(JvmTypes.STATIC_LINKAGE
					.type(CStructType.class).member("data"))
				.structAccess()
				.identifier(JvmTypes.STATIC_LINKAGE
					.type(CStructType.class).member("data")
					.type(CStructType.class).member(__what))
			.build()).build();
	}
	
	/**
	 * The return value expression.
	 * 
	 * @return The return value expression.
	 * @throws IOException On write errors.
	 * @since 2023/07/04
	 */
	public CExpression returnValue()
		throws IOException
	{
		return __StaticCodeVariables__.returnValue();
	}
	
	/**
	 * Returns a temporary variable.
	 *
	 * @param __index The temporary variable index.
	 * @return The variable for the temporary.
	 * @throws IndexOutOfBoundsException If the index is not valid.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/07/03
	 */
	public CExpression temporary(int __index)
		throws IndexOutOfBoundsException, IOException, NullPointerException
	{
		if (__index < 0)
			throw new IndexOutOfBoundsException("IOOB");
		
		// Bump up count
		this._numTemporaries = Math.max(this._numTemporaries, __index);
		this._maxTemporaries = Math.max(this._maxTemporaries, __index);
		
		// Construct expression to access it
		return CExpressionBuilder.builder()
			.identifier(Constants.TEMPORARY)
			.arrayAccess(__index)
			.build();
	}
	
	/**
	 * Returns a temporary variable.
	 *
	 * @param __index The temporary variable index.
	 * @param __type The type of temporary to get.
	 * @return The variable for the temporary.
	 * @throws IndexOutOfBoundsException If the index is not valid.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/07/03
	 */
	public CExpression temporary(int __index, CType __type)
		throws IndexOutOfBoundsException, IOException, NullPointerException
	{
		if (__type == null)
			throw new NullPointerException("NARG");
		
		// Bump up count
		this._numTemporaries = Math.max(this._numTemporaries, __index);
		this._maxTemporaries = Math.max(this._maxTemporaries, __index);
		
		// Passed ANY, but we really just want the existing any
		if (JvmTypes.ANY.type().equals(__type))
			return this.temporary(__index);
		return this.temporary(this.temporary(__index), __type);
	}
	
	/**
	 * Returns a temporary with the given type expression.
	 * 
	 * @param __dx The index expression.
	 * @param __type The type used.
	 * @return The expression to refer to it by type.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/07/15
	 */
	public CExpression temporary(CExpression __dx, CType __type)
		throws IOException, NullPointerException
	{
		if (__dx == null || __type == null)
			throw new NullPointerException("NARG");
		
		// Self any type already? Just use the single expression
		if (JvmTypes.ANY.type().equals(__type))
			return __dx;
		
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
		return CExpressionBuilder.builder()
			.expression(__dx)
			.structAccess()
			.identifier("data")
			.structAccess()
			.identifier(member)
			.build();
	}
	
	/**
	 * Returns a reference to a temporary known value.
	 * 
	 * @param __dx The index expression.
	 * @param __type The type used.
	 * @return The expression to refer to it by type.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/07/15
	 */
	public CExpression temporaryReference(CExpression __dx, CType __type)
		throws IOException, NullPointerException
	{
		if (__dx == null || __type == null)
			throw new NullPointerException("NARG");
		
		return CBasicExpression.reference(
			this.temporary(__dx, __type));
	}
	
	/**
	 * Returns the waiting to be thrown.
	 * 
	 * @return The expression for waiting thrown.
	 * @throws IOException On write errors.
	 * @since 2023/07/04
	 */
	public CExpression waitingThrown()
		throws IOException
	{
		return __StaticCodeVariables__.waitingThrown();
	}
}
