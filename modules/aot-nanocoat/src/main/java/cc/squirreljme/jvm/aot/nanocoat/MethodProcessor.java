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
import cc.squirreljme.c.CExpressionBuilder;
import cc.squirreljme.c.CFunctionType;
import cc.squirreljme.c.CUtils;
import cc.squirreljme.c.CVariable;
import cc.squirreljme.c.CFunctionBlock;
import cc.squirreljme.c.CSourceWriter;
import cc.squirreljme.c.CStructVariableBlock;
import cc.squirreljme.c.std.CStdIntNumberType;
import cc.squirreljme.jvm.aot.nanocoat.common.Constants;
import cc.squirreljme.jvm.aot.nanocoat.common.JvmTypes;
import cc.squirreljme.jvm.aot.nanocoat.linkage.ClassLinkTable;
import java.io.IOException;
import net.multiphasicapps.classfile.ByteCode;
import net.multiphasicapps.classfile.ClassFile;
import net.multiphasicapps.classfile.FieldDescriptor;
import net.multiphasicapps.classfile.Method;

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
	
	/** The output source writer. */
	protected final CSourceWriter out;
	
	/** The function for this method. */
	protected final CFunctionType function;
	
	/** The method identifier in C. */
	protected final String methodIdentifier;
	
	/** The method being processed. */
	protected final Method method;
	
	/** The link table for the class. */
	protected final ClassLinkTable linkTable;
	
	/**
	 * Initializes the method processor.
	 * 
	 * @param __glob The link glob this is under.
	 * @param __out The source output.
	 * @param __classProcessor The class file this is processing under.
	 * @param __method The method to be processed.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/05/31
	 */
	public MethodProcessor(NanoCoatLinkGlob __glob, CSourceWriter __out,
		ClassProcessor __classProcessor, Method __method)
		throws NullPointerException
	{
		if (__glob == null || __out == null || __classProcessor == null ||
			__method == null)
			throw new NullPointerException("NARG");
		
		this.glob = __glob;
		this.out = __out;
		this.classFile = __classProcessor.classFile;
		this.method = __method;
		this.linkTable = __classProcessor.linkTable;
		
		// Determine the identifier used for this class
		this.methodIdentifier = Utils.symbolMethodName(__glob,
			__method);
		
		// Build common function
		this.function = CFunctionType.of(this.methodIdentifier,
			JvmTypes.JBOOLEAN.type(),
			CVariable.of(JvmTypes.VMSTATE.type().pointerType(),
				"state"));
	}
	
	/**
	 * Processes headers for method.
	 * 
	 * @throws IOException On write errors.
	 * @since 2023/05/31
	 */
	public void processHeader()
		throws IOException
	{
		// Write out the prototype
		this.out.declare(this.function);
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
			// Method details
			struct.memberSet("name",
				CExpressionBuilder.builder()
					.string(method.name().toString())
					.build());
			struct.memberSet("type",
				CExpressionBuilder.builder()
					.string(method.type().toString())
					.build());
			struct.memberSet("flags",
				CExpressionBuilder.builder()
					.number(Constants.JINT_C, method.flags().toJavaBits())
					.build());
			
			// Slots of return value
			FieldDescriptor rVal = method.type().returnValue();
			if (rVal != null)
				struct.memberSet("rValSlots",
					CExpressionBuilder.builder()
						.number(Constants.JINT_C, rVal.stackWidth())
						.build());
			
			// Argument slots
			struct.memberSet("argSlots",
				CExpressionBuilder.builder()
					.number(Constants.JINT_C, method.argumentSlotCount())
					.build());
			
			// Code for the method?
			ByteCode code = method.byteCode();
			if (code != null)
				struct.memberSet("code",
					CExpressionBuilder.builder()
						.identifier(this.function.name)
						.build());
		}
	}
	
	/**
	 * Processes the source details for this method outside the class
	 * definition.
	 * 
	 * @throws IOException On write errors.
	 * @since 2023/05/31
	 */
	public void processSourceOutside()
		throws IOException
	{
		// Only write byte code of the method if there is actual byte code
		ByteCode code = this.method.byteCode();
		if (code == null)
			return;
		
		// Write function code
		ByteCodeProcessor processor = new ByteCodeProcessor(
			this, code);
		try (CFunctionBlock function = this.out.define(this.function))
		{
			processor.process(function);
		}
	}
}
