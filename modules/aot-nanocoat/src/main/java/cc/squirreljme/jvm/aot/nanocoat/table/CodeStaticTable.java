// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.aot.nanocoat.table;

import cc.squirreljme.c.CArrayBlock;
import cc.squirreljme.c.CBasicExpression;
import cc.squirreljme.c.CFile;
import cc.squirreljme.c.CFunctionBlock;
import cc.squirreljme.c.CIdentifier;
import cc.squirreljme.c.CStructVariableBlock;
import cc.squirreljme.c.CVariable;
import cc.squirreljme.jvm.aot.nanocoat.ByteCodeProcessor;
import cc.squirreljme.jvm.aot.nanocoat.CodeFingerprint;
import cc.squirreljme.jvm.aot.nanocoat.VariablePlacementMap;
import cc.squirreljme.jvm.aot.nanocoat.common.JvmFunctions;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.io.IOException;
import java.lang.ref.Reference;
import net.multiphasicapps.classfile.ByteCode;

/**
 * Table for method code.
 *
 * @since 2023/08/12
 */
public class CodeStaticTable
	extends StaticTable<CodeFingerprint, ByteCode>
{
	/**
	 * Initializes the code table.
	 *
	 * @param __group The owning group.
	 * @since 2023/08/12
	 */
	public CodeStaticTable(Reference<StaticTableManager> __group)
	{
		super(__group, StaticTableType.CODE);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/08/12
	 */
	@Override
	protected String buildIdentity(CodeFingerprint __key)
		throws IOException, NullPointerException
	{
		if (__key == null)
			throw new NullPointerException("NARG");
		
		// Use a format similar to string to hopefully make it unique enough
		return String.format("%d_%s_%s",
			__key.length(),
			Long.toString(__key.hashCode() & 0xFFFFFFFFL, 36),
			Long.toString(__key.checkSum() & 0xFFFFFFFFL, 36));
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/08/12
	 */
	@Override
	protected void writeSource(CFile __sourceFile, String __fileName,
		CVariable __variable, CodeFingerprint __key, ByteCode __value)
		throws IOException, NullPointerException
	{
		if (__sourceFile == null || __fileName == null || __variable == null ||
			__key == null)
			throw new NullPointerException("NARG");
		
		// Function code name
		CIdentifier functionName = CIdentifier.of(
			__variable.name + "__code");
		
		// Write source code of the method byte code
		ByteCodeProcessor processor = new ByteCodeProcessor(
			this.__manager(), __value);
		try (CFunctionBlock function = __sourceFile.define(
			JvmFunctions.METHOD_CODE.function().rename(functionName)))
		{
			processor.process(function);
		}
		
		// Write struct that defines the information on the code
		VariableLimitsStaticTable limitsTable =
			this.__manager().variableLimits();
		try (CStructVariableBlock struct = __sourceFile.define(
			CStructVariableBlock.class, __variable))
		{
			// Code function
			struct.memberSet("code",
				functionName);
			
			// Limits for the variables
			VariablePlacementMap placements =
				processor.variablePlacements();
			struct.memberSet("limits",
				CBasicExpression.reference(
					limitsTable.put(placements.limits())));
			
			// Thrown variable index
			struct.memberSet("thrownVarIndex",
				CBasicExpression.number(placements.thrownVariableIndex()));
		}
	}
}
