// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.aot.nanocoat;

import cc.squirreljme.c.CArrayBlock;
import cc.squirreljme.c.CBasicExpression;
import cc.squirreljme.c.CExpressionBuilder;
import cc.squirreljme.c.CFunctionType;
import cc.squirreljme.c.CFunctionBlock;
import cc.squirreljme.c.CSourceWriter;
import cc.squirreljme.c.CStructVariableBlock;
import cc.squirreljme.c.CVariable;
import cc.squirreljme.jvm.aot.nanocoat.common.Constants;
import cc.squirreljme.jvm.aot.nanocoat.common.JvmFunctions;
import cc.squirreljme.jvm.aot.nanocoat.common.JvmPrimitiveType;
import cc.squirreljme.jvm.aot.nanocoat.common.JvmTypes;
import cc.squirreljme.jvm.aot.nanocoat.linkage.ClassLinkTable;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.io.IOException;
import net.multiphasicapps.classfile.ByteCode;
import net.multiphasicapps.classfile.ClassFile;
import net.multiphasicapps.classfile.FieldDescriptor;
import net.multiphasicapps.classfile.Method;
import net.multiphasicapps.classfile.MethodDescriptor;

/**
 * This processes methods.
 *
 * @since 2023/05/31
 */
public final class MethodProcessor
{
	/** The input class file. */
	protected final ClassFile classFile;
	
	/** The glob this is being processed under. */
	protected final NanoCoatLinkGlob glob;
	
	/** The function for this method. */
	protected final CFunctionType function;
	
	/** The method identifier in C. */
	protected final String methodIdentifier;
	
	/** The identifier used for the method code. */
	protected final CVariable codeInfoVar;
	
	/** The method being processed. */
	protected final Method method;
	
	/** The link table for the class. */
	protected final ClassLinkTable linkTable;
	
	/**
	 * Initializes the method processor.
	 *
	 * @param __glob The link glob this is under.
	 * @param __classProcessor The class file this is processing under.
	 * @param __method The method to be processed.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/05/31
	 */
	public MethodProcessor(NanoCoatLinkGlob __glob,
		ClassProcessor __classProcessor, Method __method)
		throws NullPointerException
	{
		if (__glob == null || __classProcessor == null ||
			__method == null)
			throw new NullPointerException("NARG");
		
		this.glob = __glob;
		this.classFile = __classProcessor.classFile;
		this.method = __method;
		this.linkTable = __classProcessor.linkTable;
		
		// Determine the identifier used for this class
		this.methodIdentifier = Utils.symbolMethodName(__glob,
			__method);
		
		// Determine the identifier for the code information
		this.codeInfoVar = CVariable.of(JvmTypes.STATIC_CLASS_CODE,
			this.methodIdentifier + "__code");
		
		// Build common function
		this.function = JvmFunctions.METHOD_CODE.function()
			.rename(this.methodIdentifier);
	}
	
	/**
	 * Processes headers for method.
	 * 
	 * @throws IOException On write errors.
	 * @since 2023/05/31
	 */
	public void processHeader(CSourceWriter __out)
		throws IOException
	{
		// Write out the prototype
		__out.declare(this.function);
	}
	
	/**
	 * Processes the methods in the method structure.
	 * 
	 * @param __array The array to write into.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/05/31
	 */
	public void processInMethodsStruct(CArrayBlock __array)
		throws IOException, NullPointerException
	{
		if (__array == null)
			throw new NullPointerException("NARG");
		
		Method method = this.method;
		try (CStructVariableBlock struct = __array.struct())
		{
			MethodDescriptor type = method.type();
			
			// Method details
			struct.memberSet("name",
				CExpressionBuilder.builder()
					.string(method.name().toString())
					.build());
			struct.memberSet("type",
				CExpressionBuilder.builder()
					.string(type.toString())
					.build());
			struct.memberSet("flags",
				CExpressionBuilder.builder()
					.number(Constants.JINT_C, method.flags().toJavaBits())
					.build());
			
			// Write argument type mapping, since many methods in a library
			// will use the same set of arguments, this is reduced accordingly
			// by combining and sharing them
			struct.memberSet("argTypes",
				CBasicExpression.reference(this.glob.processArgumentTypes(
					VariablePlacementMap.methodTypes(
						method.flags().isStatic(), type))));
			
			// Return value type
			FieldDescriptor rVal = type.returnValue();
			struct.memberSet("rValType",
				CBasicExpression.number((rVal == null ? -1 :
					JvmPrimitiveType.of(rVal).javaType().ordinal())));
			
			// If there is code, refer to it
			if (this.method.byteCode() != null)
				struct.memberSet("code",
					CBasicExpression.reference(this.codeInfoVar));
		}
	}
	
	/**
	 * Processes the source details for this method outside the class
	 * definition.
	 * 
	 * @throws IOException On write errors.
	 * @since 2023/05/31
	 */
	public void processSourceOutside(CSourceWriter __out)
		throws IOException
	{
		// Only write byte code of the method if there is actual byte code
		ByteCode code = this.method.byteCode();
		if (code == null)
			return;
		
		// Write function code
		ByteCodeProcessor processor = new ByteCodeProcessor(
			this, code);
		try (CFunctionBlock function = __out.define(this.function))
		{
			processor.process(function);
		}
		
		// Write code information details
		try (CStructVariableBlock struct = __out.define(
			CStructVariableBlock.class, this.codeInfoVar))
		{
			// Max variable counts for each type
			VariablePlacementMap varMap = processor.variablePlacements;
			
			// Cached limits to share where possible
			struct.memberSet("limits",
				CBasicExpression.reference(this.glob.processVariableLimits(
					varMap.limits())));
			
			// The thrown instance variable index location
			struct.memberSet("thrownVarIndex",
				CBasicExpression.number(
					varMap.thrownVariableIndex()));
				
			// Code for the method?
			struct.memberSet("code",
				this.function.name);
		}
	}
}
