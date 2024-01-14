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
import cc.squirreljme.c.CStructType;
import cc.squirreljme.c.CStructVariableBlock;
import cc.squirreljme.c.CVariable;
import cc.squirreljme.jvm.aot.nanocoat.ClassInterfaces;
import cc.squirreljme.jvm.aot.nanocoat.VariableLimits;
import cc.squirreljme.jvm.aot.nanocoat.common.JvmPrimitiveType;
import cc.squirreljme.jvm.aot.nanocoat.common.JvmTypes;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.io.IOException;
import java.lang.ref.Reference;

/**
 * Class interfaces list table.
 *
 * @since 2023/08/13
 */
public class VariableLimitsStaticTable
	extends StaticTable<VariableLimits, VariableLimits>
{
	/**
	 * Initializes the interfaces table.
	 *
	 * @param __group The group this is under.
	 * @since 2023/08/13
	 */
	public VariableLimitsStaticTable(Reference<StaticTableManager> __group)
	{
		super(__group, StaticTableType.VARIABLE_LIMITS);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/08/13
	 */
	@Override
	protected String buildIdentity(VariableLimits __key)
		throws IOException, NullPointerException
	{
		if (__key == null)
			throw new NullPointerException("NARG");
		
		// Just build the type counts in
		StringBuilder sb = new StringBuilder();
		for (JvmPrimitiveType type : JvmPrimitiveType.JAVA_TYPES)
		{
			if (sb.length() > 0)
				sb.append("_");
			sb.append(type.name().charAt(0));
			sb.append(__key.count(type));
		}
		
		return sb.toString();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/08/12
	 */
	@Override
	protected void writeSource(CFile __sourceFile, String __fileName,
		CVariable __variable, VariableLimits __key, VariableLimits __value)
		throws IOException, NullPointerException
	{
		if (__sourceFile == null || __fileName == null || __variable == null ||
			__key == null)
			throw new NullPointerException("NARG");
		
		try (CStructVariableBlock struct = __sourceFile.define(
			CStructVariableBlock.class, __variable))
		{
			try (CArrayBlock array = struct.memberArraySet(
				"maxVariables"))
			{
				for (JvmPrimitiveType type : JvmPrimitiveType.JAVA_TYPES)
					array.value(CBasicExpression.number(__value.count(type)));
			}
		}
	}
}

