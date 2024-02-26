// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.emulator.vm;

import cc.squirreljme.jvm.launch.VirtualJarPackageShelf;
import cc.squirreljme.jvm.mle.JarPackageShelf;
import cc.squirreljme.jvm.mle.brackets.JarPackageBracket;
import cc.squirreljme.jvm.mle.exceptions.MLECallError;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.vm.VMClassLibrary;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.Map;

/**
 * A fake version of {@link JarPackageShelf} for usage with -jar.
 *
 * @since 2024/01/06
 */
public class FakeJarPackageShelf
	implements VirtualJarPackageShelf
{
	/** The fake libraries. */
	private final JarPackageBracket[] _fakes;
	
	/**
	 * Wraps the given suites with a fake package shelf.
	 *
	 * @param __suites The suites to wrap.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/06
	 */
	public FakeJarPackageShelf(Map<String, VMClassLibrary> __suites)
		throws NullPointerException
	{
		if (__suites == null)
			throw new NullPointerException("NARG");
		
		// Setup base fake array
		int n = __suites.size();
		JarPackageBracket[] fakes = new JarPackageBracket[n];
		
		// Fill it in with wrapped suites
		int at = 0;
		for (VMClassLibrary lib : __suites.values())
			fakes[at++] = new FakeJarPackageBracket(lib);
		
		// Store for later usage
		this._fakes = fakes;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/01/06
	 */
	@Override
	public boolean equals(JarPackageBracket __a, JarPackageBracket __b)
		throws MLECallError
	{
		if ((__a == null) != (__b == null))
			return false;
		else if (__a == null)
			return true;
		
		return ((FakeJarPackageBracket)__a).library ==
			((FakeJarPackageBracket)__b).library;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/01/06
	 */
	@Override
	public JarPackageBracket[] libraries()
	{
		return this._fakes.clone();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/01/06
	 */
	@Override
	public String libraryPath(JarPackageBracket __jar)
		throws MLECallError
	{
		if (__jar == null)
			throw new MLECallError("NULL");
		
		VMClassLibrary lib = ((FakeJarPackageBracket)__jar).library;
		
		Path path = lib.path();
		if (path != null)
			return path.toString();
		
		return lib.name();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/01/06
	 */
	@Override
	public InputStream openResource(JarPackageBracket __jar, String __rc)
		throws MLECallError
	{
		if (__jar == null || __rc == null)
			throw new MLECallError("NARG");
		
		try
		{
			return ((FakeJarPackageBracket)__jar).library
				.resourceAsStream(__rc);
		}
		catch (IOException __e)
		{
			throw new MLECallError(__e);
		}
	}
}
