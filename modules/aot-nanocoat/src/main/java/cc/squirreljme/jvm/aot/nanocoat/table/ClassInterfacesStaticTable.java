// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.aot.nanocoat.table;

import cc.squirreljme.c.CFile;
import cc.squirreljme.c.CVariable;
import cc.squirreljme.jvm.aot.nanocoat.ClassInterfaces;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.io.IOException;
import java.lang.ref.Reference;

/**
 * Class interfaces list table.
 *
 * @since 2023/08/12
 */
public class ClassInterfacesStaticTable
	extends StaticTable<ClassInterfaces, ClassInterfaces>
{
	/**
	 * Initializes the interfaces table.
	 *
	 * @param __group The group this is under.
	 * @since 2023/08/12
	 */
	public ClassInterfacesStaticTable(Reference<StaticTableManager> __group)
	{
		super(__group, StaticTableType.CLASS_INTERFACES);
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @since 2023/08/12
	 */
	@Override
	protected String buildIdentity(ClassInterfaces __key)
		throws IOException, NullPointerException
	{
		if (__key == null)
			throw new NullPointerException("NARG");
		
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/08/12
	 */
	@Override
	protected void writeSource(CFile __sourceFile, String __fileName,
		CVariable __variable, ClassInterfaces __key, ClassInterfaces __value)
		throws IOException, NullPointerException
	{
		if (__sourceFile == null || __fileName == null || __variable == null ||
			__key == null)
			throw new NullPointerException("NARG");
		
		throw Debugging.todo();
	}
}
