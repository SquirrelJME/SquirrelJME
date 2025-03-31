// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.javadoc.compat;

import java.util.Locale;
import java.util.Set;
import javax.lang.model.SourceVersion;
import jdk.javadoc.doclet.Doclet;
import jdk.javadoc.doclet.DocletEnvironment;
import jdk.javadoc.doclet.Reporter;

/**
 * Wraps an old Doclet.
 *
 * @since 2025/03/30
 */
public class DocletWrapper
	implements Doclet
{
	/** The wrapped Doclet. */
	private final com.sun.javadoc.Doclet wrapped;
	
	/**
	 * Initializes the wrapper.
	 *
	 * @param __wrapped The wrapped Doclet.
	 * @throws NullPointerException On null arguments.
	 * @since 2025/03/30
	 */
	public DocletWrapper(com.sun.javadoc.Doclet __wrapped)
		throws NullPointerException
	{
		if (__wrapped == null)
			throw new NullPointerException("NARG");
		
		this.wrapped = __wrapped;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/03/30
	 */
	@Override
	public void init(Locale __locale, Reporter __reporter)
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/03/30
	 */
	@Override
	public String getName()
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/03/30
	 */
	@Override
	public Set<? extends Option> getSupportedOptions()
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/03/30
	 */
	@Override
	public SourceVersion getSupportedSourceVersion()
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/03/30
	 */
	@Override
	public boolean run(DocletEnvironment __docletEnvironment)
	{
		throw new Error("TODO");
	}
}
