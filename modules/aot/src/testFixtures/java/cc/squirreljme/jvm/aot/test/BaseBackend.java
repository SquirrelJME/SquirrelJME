// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.aot.test;

import cc.squirreljme.jvm.aot.Backend;
import cc.squirreljme.jvm.aot.CompileSettings;
import cc.squirreljme.jvm.aot.LinkGlob;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import net.multiphasicapps.tac.TestRunnable;

/**
 * Class which acts as a base backend.
 *
 * @since 2022/08/14
 */
public abstract class BaseBackend
	extends TestRunnable
{
	/** The backend used. */
	protected final Backend backend;
	
	/** The link glob to use. */
	protected final LinkGlob linkGlob;
	
	/** The settings used for compilation. */
	protected final CompileSettings compileSettings;
	
	/** The output stream where the link glob writes to. */
	protected final ByteArrayOutputStream globOutput =
		new ByteArrayOutputStream();
	
	/**
	 * Initializes the base backend.
	 * 
	 * @param __backend The backend to use.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/08/14
	 */
	public BaseBackend(Backend __backend)
		throws NullPointerException
	{
		this.backend = __backend;
		
		CompileSettings compileSettings = this.compileSettings();
		this.compileSettings = compileSettings;
		
		// This could possibly fail
		try
		{
			this.linkGlob = __backend.linkGlob(compileSettings,
				BaseBackend.__name(this.getClass()), this.globOutput);
		}
		catch (IOException e)
		{
			throw new RuntimeException("IOIO", e);
		}
	}
	
	/**
	 * Returns the compilation settings to use, this is a default that may be
	 * overridden in a test as needed.
	 * 
	 * @return The compilation settings to use.
	 * @since 2022/08/14
	 */
	public CompileSettings compileSettings()
	{
		return new CompileSettings(false);
	}
	
	/**
	 * Determines the name of the class which is used to determine the name of
	 * the blob.
	 * 
	 * @param __class The class to get the name of.
	 * @return The base name of the class, lowercase.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/08/14
	 */
	private static String __name(Class<?> __class)
		throws NullPointerException
	{
		if (__class == null)
			throw new NullPointerException("NARG");
		
		String name = __class.getName();
		int lastSlash = name.lastIndexOf('/');
		return (lastSlash < 0 ? name : name.substring(lastSlash + 1));
	}
}
