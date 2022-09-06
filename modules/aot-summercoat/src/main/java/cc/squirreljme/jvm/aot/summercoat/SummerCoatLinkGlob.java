// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.aot.summercoat;

import cc.squirreljme.jvm.aot.CompilationStatistics;
import cc.squirreljme.jvm.aot.CompileSettings;
import cc.squirreljme.jvm.aot.LinkGlob;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * This is the link glob for SummerCoat which stores the state of the compiler
 * as it processes multiple classes into one.
 *
 * @since 2022/09/04
 */
public final class SummerCoatLinkGlob
	implements LinkGlob
{
	/** The compilation settings for this glob. */
	protected final CompileSettings settings;
	
	/** The factory for creating handlers for method compilation. */
	protected final SummerCoatHandlerFactory handlerFactory;
	
	/** The name of the glob. */
	protected final String name;
	
	/** The final output where compilation targets go. */
	protected final OutputStream finalOutput;
	
	/** Compilation statistics. */
	protected final CompilationStatistics statistics =
		new CompilationStatistics();
	
	/**
	 * Initializes the glob.
	 * 
	 * @param __settings The settings for this glob.
	 * @param __handlerFactory The factory for handling method compilations.
	 * @param __name The name of the glob.
	 * @param __finalOutput The stream to write to for final linking.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/09/05
	 */
	public SummerCoatLinkGlob(CompileSettings __settings,
		SummerCoatHandlerFactory __handlerFactory, String __name,
		OutputStream __finalOutput)
		throws NullPointerException
	{
		if (__settings == null || __handlerFactory == null ||
			__name == null || __finalOutput == null)
			throw new NullPointerException("NARG");
		
		this.settings = __settings;
		this.handlerFactory = __handlerFactory;
		this.name = __name;
		this.finalOutput = __finalOutput;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/09/05
	 */
	@Override
	public CompileSettings compileSettings()
	{
		return this.settings;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/09/04
	 */
	@Override
	public void finish()
		throws IOException
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/09/04
	 */
	@Override
	public void joinResource(String __name, InputStream __data)
		throws IOException, NullPointerException
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/09/06
	 */
	@Override
	public CompilationStatistics statistics()
	{
		return this.statistics;
	}
}
