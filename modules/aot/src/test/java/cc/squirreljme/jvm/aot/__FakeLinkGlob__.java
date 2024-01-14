// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.aot;

import cc.squirreljme.jvm.manifest.JavaManifest;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.ref.Reference;
import java.util.List;

/**
 * Fake LinkGlob implementation.
 *
 * @since 2023/10/15
 */
class __FakeLinkGlob__
	extends __FakeBase__
	implements LinkGlob
{
	/** Where this writes to. */
	public final OutputStream out;
	
	/**
	 * Initializes the fake link glob.
	 *
	 * @param __name The name used.
	 * @param __ref The reference to point to.
	 * @param __out Where the output is written to.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/10/15
	 */
	public __FakeLinkGlob__(String __name, Reference<TestFakeBackend> __ref,
		OutputStream __out)
		throws NullPointerException
	{
		super(__name, __ref);
		
		if (__out == null)
			throw new NullPointerException("NARG");
		
		this.out = __out;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/10/15
	 */
	@Override
	public void close()
		throws IOException
	{
		this.__secondary("close", true);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/10/15
	 */
	@Override
	public void finish()
		throws IOException
	{
		this.__secondary("finish", true);
		
		// Always have something in there
		this.out.write(1);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/10/15
	 */
	@Override
	public void initialize()
		throws IOException
	{
		this.__secondary("initialize", true);
		
		// Always have something in there
		this.out.write(2);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/10/15
	 */
	@Override
	public void rememberManifest(JavaManifest __manifest)
	{
		this.__secondary("remember-manifest", true);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/10/15
	 */
	@Override
	public void rememberTests(List<String> __tests)
	{
		this.__secondary("remember-tests-count",
			__tests.size());
		this.__secondary("remember-tests-values",
			__tests.toArray(new String[0]));
	}
}
