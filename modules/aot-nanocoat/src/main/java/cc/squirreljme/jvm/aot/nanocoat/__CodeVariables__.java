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
import cc.squirreljme.c.CVariable;
import cc.squirreljme.jvm.aot.nanocoat.common.JvmFunctions;
import cc.squirreljme.jvm.aot.nanocoat.common.JvmTypes;
import cc.squirreljme.jvm.aot.nanocoat.linkage.Container;
import cc.squirreljme.jvm.aot.nanocoat.linkage.Linkage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for making code variables.
 *
 * @since 2023/06/19
 */
public final class __CodeVariables__
{
	/** Initial variables output. */
	private final CFunctionBlock _initVars;
	
	/** Temporary cache. */
	private final List<JvmTemporary> _temporaries =
		new ArrayList<>();
	
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
	 * Returns the reference to current class being executed.
	 *
	 * @return The current class reference.
	 * @throws IOException On write errors.
	 * @since 2023/07/19
	 */
	public CExpression classObjectRef()
		throws IOException
	{
		return __StaticCodeVariables__.classObjectRef();
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
	 * Returns the current linkage.
	 *
	 * @return The current linkage.
	 * @since 2023/07/25
	 */
	public CVariable linkage()
	{
		return __StaticCodeVariables__.linkage();
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
			.functionCall(JvmFunctions.NVM_LOOKUP_REFERENCE,
				this.currentFrame(),
				CBasicExpression.number(__linkage.index()))
			.build();
	}
	
	/**
	 * Returns the number of maximum temporaries used.
	 * 
	 * @return The maximum number of temporaries.
	 * @since 2023/07/15
	 */
	public int maxTemporaries()
	{
		return this._temporaries.size();
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
	public JvmTemporary temporary(int __index)
		throws IndexOutOfBoundsException, IOException, NullPointerException
	{
		if (__index < 0)
			throw new IndexOutOfBoundsException("IOOB");
		
		// Bump up count for current "instruction"
		this._numTemporaries = Math.max(this._numTemporaries, __index);
		
		// Build a cache of temporaries since these are always kept around
		List<JvmTemporary> temps = this._temporaries;
		while (temps.size() <= __index)
			temps.add(new JvmTemporary(temps.size()));
		
		return temps.get(__index);
	}
	
	/**
	 * Returns the reference to {@code this}.
	 *
	 * @return The current this reference.
	 * @throws IOException On write errors.
	 * @since 2023/07/19
	 */
	public CExpression thisRef()
		throws IOException
	{
		return __StaticCodeVariables__.thisRef();
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
