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
import cc.squirreljme.jvm.aot.nanocoat.linkage.ClassLink;
import cc.squirreljme.jvm.aot.nanocoat.linkage.Linkage;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.io.IOException;
import java.lang.ref.Reference;

/**
 * Table for class linkages.
 *
 * @since 2023/08/29
 */
public class ClassLinkTable
	extends StaticTable<ClassLink, ClassLink>
{
	/**
	 * Initializes the static link table.
	 *
	 * @param __group The owning group.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/08/11
	 */
	ClassLinkTable(Reference<StaticTableManager> __group)
		throws NullPointerException
	{
		super(__group, StaticTableType.CLASS_LINK);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/08/29
	 */
	@Override
	protected String buildIdentity(ClassLink __key)
		throws IOException, NullPointerException
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/08/29
	 */
	@Override
	protected void writeSource(CFile __sourceFile, String __fileName,
		CVariable __variable, ClassLink __key, ClassLink __value)
		throws IOException, NullPointerException
	{
		throw Debugging.todo();
	}
}
