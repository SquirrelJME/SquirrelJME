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
import cc.squirreljme.c.CStructVariableBlock;
import cc.squirreljme.c.CVariable;
import java.io.IOException;
import java.lang.ref.Reference;
import net.multiphasicapps.classfile.FieldDescriptor;
import net.multiphasicapps.classfile.MethodDescriptor;

/**
 * Contains method type information.
 *
 * @since 2023/08/13
 */
public class MethodTypeStaticTable
	extends StaticTable<MethodDescriptor, MethodDescriptor>
{
	/**
	 * Initializes the method type table.
	 *
	 * @param __group The group this is under.
	 * @since 2023/08/12
	 */
	public MethodTypeStaticTable(Reference<StaticTableManager> __group)
	{
		super(__group, StaticTableType.METHOD_TYPE);
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @since 2023/08/12
	 */
	@Override
	protected String buildIdentity(MethodDescriptor __key)
		throws IOException, NullPointerException
	{
		if (__key == null)
			throw new NullPointerException("NARG");
		
		// Make method descriptors similar to strings
		StringStaticTable string = this.__manager().strings();
		return String.format("%d_%s",
			__key.argumentCount(),
			string.identify(__key.toString()));
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/08/12
	 */
	@Override
	protected void writeSource(CFile __sourceFile, String __fileName,
		CVariable __variable, MethodDescriptor __key,
		MethodDescriptor __value)
		throws IOException, NullPointerException
	{
		if (__sourceFile == null || __fileName == null || __variable == null ||
			__key == null)
			throw new NullPointerException("NARG");
		
		// Define the type information
		StringStaticTable strings = this.__manager().strings();
		FieldTypeStaticTable fields = this.__manager().fieldType();
		try (CStructVariableBlock struct = __sourceFile.define(
			CStructVariableBlock.class, __variable))
		{
			struct.memberSet("hashCode",
				CBasicExpression.number(__key.hashCode()));
			
			struct.memberSet("descriptor",
				CBasicExpression.reference(strings.put(__key.toString())));
			
			// Return value
			FieldDescriptor rVal = __key.returnValue();
			if (rVal == null)
				struct.memberSet("returnType",
					CVariable.NULL);
			else
				struct.memberSet("returnType",
					CBasicExpression.reference(fields.put(rVal)));
			
			// Arguments
			int n = __key.argumentCount();
			struct.memberSet("argCount",
				CBasicExpression.number(n));
			try (CArrayBlock array = struct.memberArraySet(
				"argTypes"))
			{
				for (int i = 0; i < n; i++)
					array.value(CBasicExpression.reference(
						fields.put(__key.argument(i))));
			}
		}
	}
}
