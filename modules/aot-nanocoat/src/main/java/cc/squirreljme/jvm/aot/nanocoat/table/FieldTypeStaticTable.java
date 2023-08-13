// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.aot.nanocoat.table;

import cc.squirreljme.c.CBasicExpression;
import cc.squirreljme.c.CFile;
import cc.squirreljme.c.CStructVariableBlock;
import cc.squirreljme.c.CVariable;
import cc.squirreljme.jvm.aot.nanocoat.common.JvmPrimitiveType;
import java.io.IOException;
import java.lang.ref.Reference;
import net.multiphasicapps.classfile.FieldDescriptor;

/**
 * Field type static table.
 *
 * @since 2023/08/13
 */
public class FieldTypeStaticTable
	extends StaticTable<FieldDescriptor, FieldDescriptor>
{
	/**
	 * Initializes the code table.
	 *
	 * @param __group The owning group.
	 * @since 2023/08/13
	 */
	public FieldTypeStaticTable(Reference<StaticTableManager> __group)
	{
		super(__group, StaticTableType.FIELD_TYPE);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/08/13
	 */
	@Override
	protected String buildIdentity(FieldDescriptor __key)
		throws IOException, NullPointerException
	{
		if (__key == null)
			throw new NullPointerException("NARG");
		
		// Make field descriptors similar to strings
		StringStaticTable string = this.__manager().strings();
		return String.format("%s",
			string.identify(__key.toString()));
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/08/13
	 */
	@Override
	protected void writeSource(CFile __sourceFile, String __fileName,
		CVariable __variable, FieldDescriptor __key, FieldDescriptor __value)
		throws IOException, NullPointerException
	{
		if (__sourceFile == null || __fileName == null || __variable == null ||
			__key == null)
			throw new NullPointerException("NARG");
		
		StringStaticTable strings = this.__manager().strings();
		try (CStructVariableBlock struct = __sourceFile.define(
			CStructVariableBlock.class, __variable))
		{
			struct.memberSet("hashCode",
				CBasicExpression.number(__key.hashCode()));
			
			struct.memberSet("descriptor",
				CBasicExpression.reference(strings.put(__key.toString())));
			
			struct.memberSet("basicType",
				CBasicExpression.number(JvmPrimitiveType.of(__key).ordinal()));
		}
	}
}
