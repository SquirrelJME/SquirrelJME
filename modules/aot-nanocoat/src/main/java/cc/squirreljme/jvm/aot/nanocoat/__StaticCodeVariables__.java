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
import cc.squirreljme.c.CVariable;
import cc.squirreljme.jvm.aot.nanocoat.common.JvmFunctions;
import cc.squirreljme.jvm.aot.nanocoat.common.JvmTypes;
import java.io.IOException;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * Code variables which never change and are static.
 *
 * @since 2023/07/15
 */
public class __StaticCodeVariables__
{
	/** The class object reference. */
	private static volatile Reference<CExpression> _classObjectRef;
	
	/** Current frame cache. */
	private static volatile Reference<CVariable> _currentFrame;
	
	/** Current state cache. */
	private static volatile Reference<CVariable> _currentState;
	
	/** Current thread cache. */
	private static volatile Reference<CVariable> _currentThread;
	
	/** The current this reference. */
	private static volatile Reference<CExpression> _thisRef;
	
	/** Waiting to be thrown. */
	private static volatile Reference<CExpression> _waitingThrown;
	
	/** Return value storage. */
	private static volatile Reference<CExpression> _returnValue;
	
	/** The current linkage. */
	private static volatile Reference<CVariable> _linkage;
	
	/**
	 * Returns the reference to current class being executed.
	 *
	 * @return The current class reference.
	 * @throws IOException On write errors.
	 * @since 2023/07/19
	 */
	public static CExpression classObjectRef()
		throws IOException
	{
		Reference<CExpression> ref = __StaticCodeVariables__._classObjectRef;
		CExpression rv;
		
		if (ref == null || (rv = ref.get()) == null)
		{
			rv = CExpressionBuilder.builder()
				.identifier(__StaticCodeVariables__.currentFrame())
				.dereferenceStruct()
				.identifier(JvmTypes.VMFRAME.type(CStructType.class)
					.member("classObjectRef"))
				.build();
			__StaticCodeVariables__._classObjectRef = new WeakReference<>(rv);
		}
		
		return rv;
	}
	
	/**
	 * Returns the variable that represents the current stack frame.
	 * 
	 * @return The variable that represents the current stack frame.
	 * @since 2023/06/19
	 */
	public static CVariable currentFrame()
	{
		Reference<CVariable> ref = __StaticCodeVariables__._currentFrame;
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
			__StaticCodeVariables__._currentFrame = new WeakReference<>(rv);
		}
		
		return rv;
	}
	
	/**
	 * Returns the current state variable.
	 * 
	 * @return The current state variable.
	 * @since 2023/06/19
	 */
	public static CVariable currentState()
	{
		Reference<CVariable> ref = __StaticCodeVariables__._currentState;
		CVariable rv;
		if (ref == null || (rv = ref.get()) == null)
		{
			rv = JvmFunctions.METHOD_CODE.function()
				.argument("currentState");
			__StaticCodeVariables__._currentState = new WeakReference<>(rv);
		}
		
		return rv;
	}
	
	/**
	 * Returns the current thread.
	 * 
	 * @return The current thread.
	 * @since 2023/06/25
	 */
	public static CVariable currentThread()
	{
		Reference<CVariable> ref = __StaticCodeVariables__._currentThread;
		CVariable rv;
		if (ref == null || (rv = ref.get()) == null)
		{
			rv = JvmFunctions.METHOD_CODE.function()
				.argument("currentThread");
			__StaticCodeVariables__._currentThread = new WeakReference<>(rv);
		}
		
		return rv;
	}
	
	/**
	 * Returns the current linkage.
	 *
	 * @return The current linkage.
	 * @since 2023/07/25
	 */
	public static CVariable linkage()
	{
		Reference<CVariable> ref = __StaticCodeVariables__._linkage;
		CVariable rv;
		if (ref == null || (rv = ref.get()) == null)
		{
			rv = CVariable.of(JvmTypes.DYNAMIC_LINKAGE.pointerType(),
				"linkage");
			__StaticCodeVariables__._linkage = new WeakReference<>(rv);
		}
		
		return rv;
	}

	/**
	 * The return value expression.
	 * 
	 * @return The return value expression.
	 * @throws IOException On write errors.
	 * @since 2023/07/04
	 */
	public static CExpression returnValue()
		throws IOException
	{
		Reference<CExpression> ref = __StaticCodeVariables__._returnValue;
		CExpression rv;
		
		if (ref == null || (rv = ref.get()) == null)
		{
			rv = CExpressionBuilder.builder()
				.identifier(__StaticCodeVariables__.currentFrame())
				.dereferenceStruct()
				.identifier(JvmTypes.VMFRAME.type(CStructType.class)
					.member("returnValue"))
				.build();
			__StaticCodeVariables__._returnValue = new WeakReference<>(rv);
		}
		
		return rv;
	}
	
	/**
	 * Returns the reference to {@code this}.
	 *
	 * @return The current this reference.
	 * @throws IOException On write errors.
	 * @since 2023/07/19
	 */
	public static CExpression thisRef()
		throws IOException
	{
		Reference<CExpression> ref = __StaticCodeVariables__._thisRef;
		CExpression rv;
		
		if (ref == null || (rv = ref.get()) == null)
		{
			rv = CExpressionBuilder.builder()
				.identifier(__StaticCodeVariables__.currentFrame())
				.dereferenceStruct()
				.identifier(JvmTypes.VMFRAME.type(CStructType.class)
					.member("thisRef"))
				.build();
			__StaticCodeVariables__._thisRef = new WeakReference<>(rv);
		}
		
		return rv;
	}
	
	/**
	 * Returns the waiting to be thrown.
	 * 
	 * @return The expression for waiting thrown.
	 * @throws IOException On write errors.
	 * @since 2023/07/04
	 */
	public static CExpression waitingThrown()
		throws IOException
	{
		Reference<CExpression> ref = __StaticCodeVariables__._waitingThrown;
		CExpression rv;
		
		if (ref == null || (rv = ref.get()) == null)
		{
			rv = CExpressionBuilder.builder()
				.identifier(__StaticCodeVariables__.currentFrame())
				.dereferenceStruct()
				.identifier(JvmTypes.VMFRAME.type(CStructType.class)
					.member("waitingThrown"))
				.build();
			__StaticCodeVariables__._waitingThrown = new WeakReference<>(rv);
		}
		
		return rv;
	}
}
