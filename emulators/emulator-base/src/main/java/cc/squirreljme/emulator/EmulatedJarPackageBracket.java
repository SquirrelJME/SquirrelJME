// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.emulator;

import cc.squirreljme.jvm.mle.brackets.JarPackageBracket;
import cc.squirreljme.jvm.mle.exceptions.MLECallError;
import cc.squirreljme.vm.VMClassLibrary;
import java.io.IOException;
import java.io.InputStream;

/**
 * This is an emulation of {@link JarPackageBracket} so that it can access
 * the path and such accordingly.
 *
 * @since 2020/10/31
 */
public class EmulatedJarPackageBracket
	implements JarPackageBracket
{
	/** The virtual machine library to wrap around. */
	protected final VMClassLibrary vmLib;
	
	/**
	 * Initializes the emulated bracket.
	 * 
	 * @param __vmLib The virtual machine library.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/10/31
	 */
	public EmulatedJarPackageBracket(VMClassLibrary __vmLib)
		throws NullPointerException
	{
		if (__vmLib == null)
			throw new NullPointerException("NARG");
		
		this.vmLib = __vmLib;
	}
	
	/**
	 * Opens the specified resource.
	 * 
	 * @param __rc The resource to open.
	 * @return The stream to the resource data.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/10/31
	 */
	public InputStream openResource(String __rc)
		throws NullPointerException
	{
		try
		{
			return this.vmLib.resourceAsStream(__rc);
		}
		catch (IOException e)
		{
			throw new MLECallError("I/O Exception.", e);
		}
	}
}
