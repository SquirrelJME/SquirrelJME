// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.builder.support;

import java.io.InputStream;
import java.io.IOException;
import net.multiphasicapps.javac.CompilerException;
import net.multiphasicapps.javac.CompilerInput;
import net.multiphasicapps.javac.syntax.CompilationUnitSyntax;
import net.multiphasicapps.javac.token.BufferedTokenSource;

/**
 * This is used to parse classes to look for tests that exist within source
 * code.
 *
 * @since 2018/03/19
 */
@Deprecated
public final class TestParser
	implements Runnable
{
	/** The input to read from. */
	protected final CompilerInput input;
	
	/** The storage area for discovered tests. */
	protected final DefinedTests defined;
	
	/**
	 * Initializes the parser.
	 *
	 * @param __in The input file.
	 * @param __def Where discovered tests will go.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/19
	 */
	public TestParser(CompilerInput __in, DefinedTests __def)
		throws NullPointerException
	{
		if (__in == null || __def == null)
			throw new NullPointerException("NARG");
		
		this.input = __in;
		this.defined = __def;
	}
	 
	/**
	 * {@inheritDoc}
	 * @since 2018/03/19
	 */
	@Override
	public void run()
	{
		CompilerInput input = this.input;
		
		// Parse the class layout
		try (InputStream in = input.open())
		{
			// Parse compilation unit
			CompilationUnitSyntax cu = CompilationUnitSyntax.parse(
				new BufferedTokenSource(input.fileName(), in));
			
			//toplevel = TopLevelLayout.parse(input.fileName(), in);
			if (true)
				throw new todo.TODO();
		}
		
		// {@squirreljme.error AU12 Could not parse file for tests.}
		catch (IOException|CompilerException e)
		{
			throw new RuntimeException("AU12", e);
		}
		
		throw new todo.TODO();
	}
}

