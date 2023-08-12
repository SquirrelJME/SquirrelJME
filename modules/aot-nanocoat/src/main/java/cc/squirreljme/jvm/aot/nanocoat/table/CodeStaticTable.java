// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.aot.nanocoat.table;

import cc.squirreljme.c.CIdentifier;
import cc.squirreljme.c.CVariable;
import cc.squirreljme.jvm.aot.nanocoat.ArchiveOutputQueue;
import cc.squirreljme.jvm.aot.nanocoat.CodeFingerprint;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.io.IOException;
import java.lang.ref.Reference;

/**
 * Table for method code.
 *
 * @since 2023/08/12
 */
public class CodeStaticTable
	extends StaticTable<CodeFingerprint>
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
	protected CIdentifier identify(CodeFingerprint __entry)
		throws NullPointerException
	{
		if (__entry == null)
			throw new NullPointerException("NARG");
		
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/08/12
	 */
	@Override
	protected void writeEntry(ArchiveOutputQueue __archive, String __fileName,
		CVariable __variable, CodeFingerprint __entry)
		throws IOException, NullPointerException
	{
		if (__archive == null || __fileName == null || __variable == null ||
			__entry == null)
			throw new NullPointerException("NARG");
		
		throw Debugging.todo();
	}
}
