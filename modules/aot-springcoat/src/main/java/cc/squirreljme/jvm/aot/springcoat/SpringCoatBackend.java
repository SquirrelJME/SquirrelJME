// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.aot.springcoat;

import cc.squirreljme.jvm.aot.Backend;
import cc.squirreljme.jvm.aot.CompileSettings;
import cc.squirreljme.jvm.aot.LinkGlob;
import cc.squirreljme.jvm.aot.RomSettings;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.vm.VMClassLibrary;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;

/**
 * Backend for SpringCoat operations.
 * 
 * {@squirreljme.error AI01 This operation is not available.}
 *
 * @since 2021/08/21
 */
public class SpringCoatBackend
	implements Backend
{
	/**
	 * {@inheritDoc}
	 * @since 2021/08/21
	 */
	@Override
	public void compileClass(CompileSettings __settings, LinkGlob __glob,
		String __name, InputStream __in, OutputStream __out)
		throws IOException, NullPointerException
	{
		throw new IllegalArgumentException("AI01");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/08/21
	 */
	@Override
	public void dumpGlob(byte[] __inGlob, String __name, PrintStream __out)
		throws IOException, NullPointerException
	{
		throw new IllegalArgumentException("AI01");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/08/21
	 */
	@Override
	public LinkGlob linkGlob(CompileSettings __settings, String __name,
		OutputStream __out)
		throws IOException, NullPointerException
	{
		throw new IllegalArgumentException("AI01");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/08/21
	 */
	@Override
	public String name()
	{
		return "springcoat";
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/08/21
	 */
	@Override
	public void rom(RomSettings __settings, OutputStream __out,
		VMClassLibrary... __libs)
		throws IOException, NullPointerException
	{
		if (__settings == null || __out == null || __libs == null)
			throw new NullPointerException("NARG");
		
		throw Debugging.todo();
	}
}
