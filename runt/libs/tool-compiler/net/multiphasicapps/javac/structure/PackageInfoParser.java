// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.javac.structure;

import java.util.ArrayList;
import java.util.List;
import net.multiphasicapps.javac.syntax.AnnotationSyntax;
import net.multiphasicapps.javac.syntax.CompilationUnitSyntax;
import net.multiphasicapps.javac.syntax.ModifiersSyntax;
import net.multiphasicapps.javac.syntax.ModifierSyntax;

/**
 * This parses the package information source file and for the most part is
 * used to handle the annotations accordingly if there are any.
 *
 * @since 2018/05/07
 */
@Deprecated
public final class PackageInfoParser
	implements Runnable
{
	/** The compilation unit being parsed. */
	protected final CompilationUnitSyntax input;
	
	/** The runtime information. */
	protected final RuntimeInput runtime;
	
	/** The output where it is to be stored. */
	protected final Structures output;
	
	/**
	 * Initializes the parser.
	 *
	 * @param __in The compilation unit input.
	 * @param __ri The run-time input.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/05/07
	 */
	public PackageInfoParser(CompilationUnitSyntax __in, RuntimeInput __ri)
		throws NullPointerException
	{
		if (__in == null || __ri == null)
			throw new NullPointerException("NARG");
		
		this.input = __in;
		this.runtime = __ri;
		this.output = __ri.structures();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/05/07
	 */
	@Override
	public final void run()
	{
		CompilationUnitSyntax input = this.input;
		Structures output = this.output;
		
		// Parse package annotations, if any
		List<AnnotationModifier> annotations = new ArrayList<>();
		for (ModifierSyntax modifier : input.packageModifiers().modifiers())
		{
			// Only annotation are valid for packages
			AnnotationSyntax as = (AnnotationSyntax)modifier;
			
			throw new todo.TODO();
		}
		
		// Store it
		output.put(new PackageStructure(new PackageSymbol(input.inPackage()),
			annotations));
	}
}

