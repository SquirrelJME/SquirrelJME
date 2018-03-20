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
import net.multiphasicapps.javac.basic.BasicStructure;
import net.multiphasicapps.javac.basic.BasicStructureParser;
import net.multiphasicapps.javac.CompilerInput;

/**
 * This is used to parse classes to look for tests that exist within source
 * code.
 *
 * @since 2018/03/19
 */
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
		// Parse the structure for the file
		CompilerInput input = this.input;
		BasicStructure bs;
		try (InputStream in = input.open();
			BasicStructureParser bsp = new BasicStructureParser(
				input.fileName(), in))
		{
			bs = bsp.parse();
		}
		
		// {@squirreljme.error AU20 Could not parse file for tests.}
		catch (IOException e)
		{
			throw new RuntimeException("AU20", e);
		}
		
		throw new todo.TODO();
	}
}

