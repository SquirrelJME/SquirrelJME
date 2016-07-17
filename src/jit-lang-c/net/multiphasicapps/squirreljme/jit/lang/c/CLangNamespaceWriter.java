// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.lang.c;

import java.io.PrintStream;
import net.multiphasicapps.squirreljme.jit.JITOutputConfig;
import net.multiphasicapps.squirreljme.jit.lang.LangNamespaceWriter;
import net.multiphasicapps.squirreljme.jit.lang.ResourceOutputStream;

/**
 * This is a namespace writer which targets the C programming language.
 *
 * @since 2016/07/09
 */
public class CLangNamespaceWriter
	extends LangNamespaceWriter
{
	/** The prefix to identifiers. */
	protected final String idprefix;
	
	/**
	 * The namespace writer for C code.
	 *
	 * @param __ns The namespace being written.
	 * @param __config The configuration used.
	 * @since 2016/07/09
	 */
	public CLangNamespaceWriter(String __ns,
		JITOutputConfig.Immutable __config)
	{
		super(__ns, __config);
		
		// Prefix for identifiers
		this.idprefix = escapeToCIdentifier(__ns) + "___";
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/17
	 */
	@Override
	protected ResourceOutputStream createResourceOutputStream(String __rname,
		PrintStream __ps)
		throws NullPointerException
	{
		// Check
		if (__rname == null || __ps == null)
			throw new NullPointerException("NARG");
		
		// Create
		return new CResourceOutputStream(this, __rname, __ps);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/09
	 */
	@Override
	public String nameResource(String __name)
		throws NullPointerException
	{
		return this.idprefix + escapeToCIdentifier(__name);
	}
	
	/**
	 * Escapes the given string so that it becomes a valid C identifier.
	 *
	 * @param __s The string to escape.
	 * @return An identifier safe string.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/17
	 */
	public static String escapeToCIdentifier(String __s)
		throws NullPointerException
	{
		// Check
		if (__s == null)
			throw new NullPointerException("NARG");
		
		throw new Error("TODO");
	}
}


