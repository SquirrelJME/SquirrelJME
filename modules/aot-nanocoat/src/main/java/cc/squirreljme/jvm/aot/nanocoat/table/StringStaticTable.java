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
import cc.squirreljme.c.CVariable;
import java.io.IOException;
import java.lang.ref.Reference;
import net.multiphasicapps.io.CRC32Calculator;

/**
 * Static table for strings.
 *
 * @since 2023/08/12
 */
public class StringStaticTable
	extends StaticTable<String, String>
{
	/**
	 * Initializes the string table.
	 *
	 * @param __group The owning group.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/08/11
	 */
	StringStaticTable(Reference<StaticTableManager> __group)
		throws NullPointerException
	{
		super(__group, StaticTableType.STRING);
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @since 2023/08/12
	 */
	@Override
	protected String buildIdentity(String __key)
		throws IOException, NullPointerException
	{
		if (__key == null)
			throw new NullPointerException("NARG");
		
		// Java's normal hashCode for String can have many collisions, so
		// calculate the CRC of the String's UTF-8 data to attempt to obtain
		// a longer identifier to use for the string
		int crc;
		try
		{
			crc = CRC32Calculator.calculateZip(
				__key.getBytes("utf-8"));
		}
		catch (IOException __e)
		{
			throw new IllegalArgumentException(__e);
		}
		
		// Build string identity
		return String.format("%d_%s_%s",
			__key.length(),
			Long.toString(__key.hashCode() & 0xFFFFFFFFL, 36),
			Long.toString(crc & 0xFFFFFFFFL, 36));
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/08/12
	 */
	@Override
	protected void writeSource(CFile __sourceFile, String __fileName,
		CVariable __variable, String __key, String __value)
		throws IOException, NullPointerException
	{
		if (__sourceFile == null || __fileName == null ||
			__variable == null || __key == null)
			throw new NullPointerException("NARG");
		
		// Define the actual variable
		__sourceFile.define(__variable,
			CBasicExpression.string(__key));
	}
}
