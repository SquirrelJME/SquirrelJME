// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.aot.nanocoat.csv;

import cc.squirreljme.c.CIdentifier;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import net.multiphasicapps.classfile.ClassName;

/**
 * A single entry within the class CSV.
 *
 * @since 2023/09/12
 */
public class ClassCsvEntry
	implements Comparable<ClassCsvEntry>
{
	/** The name of this class. */
	public final ClassName thisName;
	
	/** The name of the identifier. */
	public final CIdentifier identifier;
	
	/** The header path. */
	public final String headerPath;
	
	/** The source path. */
	public final String sourcePath;
	
	/**
	 * Registers the CSV entry.
	 *
	 * @param __thisName The name of this class.
	 * @param __identifier The identifier to the class.
	 * @param __headerPath The class header file.
	 * @param __sourcePath The class source file.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/09/12
	 */
	public ClassCsvEntry(ClassName __thisName, CIdentifier __identifier,
		String __headerPath, String __sourcePath)
		throws NullPointerException
	{
		if (__thisName == null || __identifier == null ||
			__headerPath == null || __sourcePath == null)
			throw new NullPointerException("NARG");
		
		this.thisName = __thisName;
		this.identifier = __identifier;
		this.headerPath = __headerPath;
		this.sourcePath = __sourcePath;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/09/12
	 */
	@Override
	public int compareTo(ClassCsvEntry __b)
	{
		int rv = this.thisName.compareTo(__b.thisName);
		if (rv != 0)
			return rv;
		
		rv = this.identifier.compareTo(__b.identifier);
		if (rv != 0)
			return rv;
		
		rv = this.headerPath.compareTo(__b.headerPath);
		if (rv != 0)
			return rv;
		
		return this.sourcePath.compareTo(__b.sourcePath);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/09/12
	 */
	@Override
	public boolean equals(Object __o)
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/09/12
	 */
	@Override
	public int hashCode()
	{
		throw Debugging.todo();
	}
}
