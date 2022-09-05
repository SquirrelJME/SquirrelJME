// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.aot.summercoat;

import cc.squirreljme.jvm.aot.CompileSettings;
import cc.squirreljme.jvm.aot.LinkGlob;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.io.IOException;
import java.io.InputStream;

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
	
	/**
	 * 
	 * 
	 * @param __settings The settings for this glob.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/09/05
	 */
	public SummerCoatLinkGlob(CompileSettings __settings)
		throws NullPointerException
	{
		if (__settings == null)
			throw new NullPointerException("NARG");
		
		this.settings = __settings;
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
	public void join(String __name, boolean __isRc, InputStream __data)
		throws IOException, NullPointerException
	{
		throw Debugging.todo();
	}
}
