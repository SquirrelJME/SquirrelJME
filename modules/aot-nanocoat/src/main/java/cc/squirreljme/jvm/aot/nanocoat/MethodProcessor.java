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
import cc.squirreljme.c.CFunctionBlock;
import cc.squirreljme.c.CSourceWriter;
import cc.squirreljme.c.CStructVariableBlock;
import cc.squirreljme.c.CVariable;
import cc.squirreljme.jvm.aot.nanocoat.common.Constants;
import cc.squirreljme.jvm.aot.nanocoat.table.MethodTypeStaticTable;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.io.IOException;
import net.multiphasicapps.classfile.ByteCode;
import net.multiphasicapps.classfile.ClassFile;
import net.multiphasicapps.classfile.Method;
import net.multiphasicapps.classfile.MethodDescriptor;

/**
 * This processes single methods.
 *
 * @since 2023/05/31
 */
public final class MethodProcessor
{
	/** The input class file. */
	protected final ClassFile classFile;
	
	/** The glob this is being processed under. */
	protected final NanoCoatLinkGlob glob;
	
	/** The method being processed. */
	protected final Method method;
	
	/** The code fingerprint. */
	protected final CodeFingerprint codeFingerprint;
	
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
		
		// Determine the code fingerprint, which determines if this is a
		// duplicate method or not
		if (__method.byteCode() == null)
			this.codeFingerprint = null;
		else
			this.codeFingerprint = new CodeFingerprint(__method.byteCode());
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
		// Nothing needs to be done
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
		
		NanoCoatLinkGlob glob = this.glob;
		Method method = this.method;
		
		MethodTypeStaticTable methodTypes = glob.tables.methodType();
		try (CStructVariableBlock struct = __array.struct())
		{
			MethodDescriptor type = method.type();
			
			// Method details
			struct.memberSet("name",
				CBasicExpression.string(method.name().toString()));
			struct.memberSet("type",
				CBasicExpression.reference(methodTypes.put(type)));
			struct.memberSet("flags",
				CBasicExpression.number(Constants.JINT_C,
					method.flags().toJavaBits()));
			
			// If there is code, refer to it
			if (this.method.byteCode() != null)
				struct.memberSet("code",
					CBasicExpression.reference(glob.tables.code()
						.put(this.codeFingerprint, this.method.byteCode())));
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
		// Nothing needs to be done here
		
		
		/*CBasicExpression.reference(this.glob.processArgumentTypes(
							VariablePlacementMap.methodArguments(
								method.flags().isStatic(), type)))*/
		
		// Duplicate code, ignore everything following this
		/*if (duplicateOf != null)
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
				CBasicExpression.reference(glob.processVariableLimits(
					varMap.limits())));
			
			// The thrown instance variable index location
			struct.memberSet("thrownVarIndex",
				CBasicExpression.number(
					varMap.thrownVariableIndex()));
				
			// Code for the method?
			struct.memberSet("code",
				this.function.name);
		}*/
	}
}
