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
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import net.multiphasicapps.tac.TestConsumer;

/**
 * Class which acts as a base backend.
 *
 * @since 2022/08/14
 */
public abstract class BaseBackend
	extends TestConsumer<String>
{
	/** The backend used. */
	protected final Backend backend;
	
	/** Parameters for the compilation situation. */
	private volatile SituationParameters _parameters;
	
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
	}
	
	/**
	 * Performs the testing.
	 * 
	 * @param __parameters The parameters to test with.
	 * @throws Throwable On any throwable.
	 * @since 2022/09/05
	 */
	public abstract void test(SituationParameters __parameters)
		throws Throwable;
	
	/**
	 * Returns the compilation settings to use, this is a default that may be
	 * overridden in a test as needed.
	 * 
	 * @param __variant The variant used.
	 * @return The compilation settings to use.
	 * @since 2022/08/14
	 */
	public CompileSettings compileSettings(String __variant)
	{
		return new CompileSettings(false, __variant);
	}
	
	/**
	 * Returns the situation parameters for testing.
	 * 
	 * @return The situation parameters.
	 * @since 2022/09/05
	 */
	public final SituationParameters situationParameters()
	{
		return this._parameters;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/09/05
	 */
	@Override
	public final void test(String __variant)
		throws Throwable
	{
		Backend backend = this.backend;
		CompileSettings compileSettings = this.compileSettings(__variant);
		ByteArrayOutputStream globOutput = new ByteArrayOutputStream();
		
		// This could possibly fail
		LinkGlob linkGlob;
		try
		{
			linkGlob = this.backend.linkGlob(compileSettings,
				BaseBackend.__name(this.getClass()), globOutput);
		}
		catch (IOException e)
		{
			throw new RuntimeException("IOIO", e);
		}
		
		// Run test
		SituationParameters parameters = new SituationParameters(backend,
			linkGlob, compileSettings, globOutput);
		this._parameters = parameters;
		this.test(parameters);
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
