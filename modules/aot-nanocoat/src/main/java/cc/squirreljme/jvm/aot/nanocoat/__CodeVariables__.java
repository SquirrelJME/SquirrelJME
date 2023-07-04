// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.aot.nanocoat;

import cc.squirreljme.c.CExpression;
import cc.squirreljme.c.CExpressionBuilder;
import cc.squirreljme.c.CPointerType;
import cc.squirreljme.c.CStructType;
import cc.squirreljme.c.CType;
import cc.squirreljme.c.CVariable;
import cc.squirreljme.jvm.aot.nanocoat.common.Constants;
import cc.squirreljme.jvm.aot.nanocoat.common.JvmFunctions;
import cc.squirreljme.jvm.aot.nanocoat.common.JvmTypes;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.io.IOException;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * Utility class for making code variables.
 *
 * @since 2023/06/19
 */
public final class __CodeVariables__
{
	/** Instance of this class. */
	private static volatile __CodeVariables__ _INSTANCE;
	
	/** Current frame cache. */
	private volatile Reference<CVariable> _currentFrame;
	
	/** Current state cache. */
	private volatile Reference<CVariable> _currentState;
	
	/** Current thread cache. */
	private volatile Reference<CVariable> _currentThread;
	
	/**
	 * Not used.
	 * 
	 * @since 2023/06/19
	 */
	private __CodeVariables__()
	{
	}
	
	/**
	 * Returns the variable that represents the current stack frame.
	 * 
	 * @return The variable that represents the current stack frame.
	 * @since 2023/06/19
	 */
	public CVariable currentFrame()
	{
		Reference<CVariable> ref = this._currentFrame;
		CVariable rv;
		if (ref == null || (rv = ref.get()) == null)
		{
			rv = JvmFunctions.METHOD_CODE.function()
				.argument("currentThread")
				.type(CPointerType.class)
				.dereferenceType()
				.cast(CStructType.class)
				.member("top")
				.rename("currentFrame");
			this._currentFrame = new WeakReference<>(rv);
		}
		
		return rv;
	}
	
	/**
	 * Returns the current state variable.
	 * 
	 * @return The current state variable.
	 * @since 2023/06/19
	 */
	public CVariable currentState()
	{
		Reference<CVariable> ref = this._currentState;
		CVariable rv;
		if (ref == null || (rv = ref.get()) == null)
		{
			rv = JvmFunctions.METHOD_CODE.function()
				.argument("currentState");
			this._currentState = new WeakReference<>(rv);
		}
		
		return rv;
	}
	
	/**
	 * Returns the current thread.
	 * 
	 * @return The current thread.
	 * @since 2023/06/25
	 */
	public CVariable currentThread()
	{
		Reference<CVariable> ref = this._currentState;
		CVariable rv;
		if (ref == null || (rv = ref.get()) == null)
		{
			rv = JvmFunctions.METHOD_CODE.function()
				.argument("currentThread");
			this._currentState = new WeakReference<>(rv);
		}
		
		return rv;
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
			.expression(this.temporary(__index))
			.structAccess()
			.identifier("data")
			.structAccess()
			.identifier(member)
			.build();
	}
	
	/**
	 * Returns the instance of the code variables.
	 * 
	 * @return The instance of this.
	 * @since 2023/06/19
	 */
	public static final __CodeVariables__ instance()
	{
		__CodeVariables__ rv = __CodeVariables__._INSTANCE;
		if (rv != null)
			return rv;
		
		rv = new __CodeVariables__();
		__CodeVariables__._INSTANCE = rv;
		return rv;
	}
}
