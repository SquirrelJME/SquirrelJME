// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.springcoat.brackets;

import cc.squirreljme.jvm.mle.brackets.JarPackageBracket;
import cc.squirreljme.vm.VMClassLibrary;
import cc.squirreljme.vm.springcoat.AbstractGhostObject;
import cc.squirreljme.vm.springcoat.SpringMachine;

/**
 * Represents a JAR Package.
 *
 * @since 2020/06/17
 */
public class JarPackageObject
	extends AbstractGhostObject
{
	/** The class library. */
	private final VMClassLibrary library;
	
	/**
	 * Initializes the package object.
	 *
	 * @param __machine The machine used.
	 * @param __lib The library.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/06/17
	 */
	public JarPackageObject(SpringMachine __machine, VMClassLibrary __lib)
		throws NullPointerException
	{
		super(__machine, JarPackageBracket.class);
		
		if (__lib == null)
			throw new NullPointerException("NARG");
		
		this.library = __lib;
	}
	
	/**
	 * Returns the library.
	 *
	 * @return The library.
	 * @since 2020/06/17
	 */
	public VMClassLibrary library()
	{
		return this.library;
	}
}
