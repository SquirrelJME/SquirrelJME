// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.aot;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.vm.VMClassLibrary;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.lang.ref.Reference;

/**
 * Contains a fake backend implementation.
 *
 * @since 2023/10/15
 */
class __FakeBackend__
	extends __FakeBase__
	implements Backend
{
	/**
	 * Initializes the fake backend.
	 *
	 * @param __name The name used.
	 * @param __ref The reference to the test framework.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/10/15
	 */
	public __FakeBackend__(String __name, Reference<TestFakeBackend> __ref)
		throws NullPointerException
	{
		super(__name, __ref);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/10/15
	 */
	@Override
	public void compileClass(CompileSettings __settings, LinkGlob __glob,
		String __name, InputStream __in)
		throws IOException, NullPointerException
	{
		this.__secondary(__glob, "compile-class-" + __name,
			true);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/10/15
	 */
	@Override
	public void compileResource(CompileSettings __settings, LinkGlob __glob,
		String __path, InputStream __in)
		throws IOException, NullPointerException
	{
		this.__secondary(__glob, "compile-resource-" + __path,
			true);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/10/15
	 */
	@Override
	public void dumpGlob(AOTSettings __inGlob, byte[] __name,
		PrintStream __out)
		throws IOException, NullPointerException
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/10/15
	 */
	@Override
	public LinkGlob linkGlob(AOTSettings __aotSettings,
		CompileSettings __compileSettings, OutputStream __out)
		throws IOException, NullPointerException
	{
		return new __FakeLinkGlob__(__aotSettings.name, this._ref, __out);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/10/15
	 */
	@Override
	public String name()
	{
		return "fake";
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/10/15
	 */
	@Override
	public void rom(AOTSettings __aotSettings, RomSettings __settings,
		OutputStream __out, VMClassLibrary... __libs)
		throws IOException, NullPointerException
	{
		this.__secondary("rom", true);
		
		// Always have something in there
		__out.write(0);
	}
}
