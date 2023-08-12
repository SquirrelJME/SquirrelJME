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
import cc.squirreljme.c.CFileName;
import cc.squirreljme.c.CIdentifier;
import cc.squirreljme.c.CPPBlock;
import cc.squirreljme.c.CVariable;
import cc.squirreljme.jvm.aot.nanocoat.ArchiveOutputQueue;
import java.io.IOException;
import java.lang.ref.Reference;
import net.multiphasicapps.io.CRC32Calculator;

/**
 * Static table for strings.
 *
 * @since 2023/08/12
 */
public class StringStaticTable
	extends StaticTable<String>
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
	 * @since 2023/08/12
	 */
	@Override
	protected CIdentifier identify(String __entry)
		throws NullPointerException
	{
		if (__entry == null)
			throw new NullPointerException("NARG");
		
		// Java's normal hashCode for String can have many collisions, so
		// calculate the CRC of the String's UTF-8 data to attempt to obtain
		// a longer identifier to use for the string
		int crc;
		try
		{
			crc = CRC32Calculator.calculateZip(
				__entry.getBytes("utf-8"));
		}
		catch (IOException __e)
		{
			throw new IllegalArgumentException(__e);
		}
		
		// Build string identity
		return CIdentifier.of(String.format("str_%d_%s_%s",
			__entry.length(),
			Long.toString(__entry.hashCode() & 0xFFFFFFFFL, 36),
			Long.toString(crc & 0xFFFFFFFFL, 36)));
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/08/12
	 */
	@Override
	protected void writeEntry(ArchiveOutputQueue __archive, String __fileName,
		CVariable __variable, String __entry)
		throws IOException, NullPointerException
	{
		if (__archive == null || __fileName == null ||
			__variable == null || __entry == null)
			throw new NullPointerException("NARG");
		
		// Header to declare extern to the string
		CFileName headerName = CFileName.of(__fileName + ".h");
		try (CFile header = __archive.nextCFile(headerName.toString()))
		{
			try (CPPBlock ignored = header.headerGuard(headerName))
			{
				header.declare(__variable.extern());
			}
		}
		
		// Source which actually defines the string
		try (CFile source = __archive.nextCFile(__fileName + ".c"))
		{
			// Include header
			source.preprocessorInclude(headerName);
			
			// Define the actual variable
			source.define(__variable,
				CBasicExpression.string(__entry));
		}
	}
}
