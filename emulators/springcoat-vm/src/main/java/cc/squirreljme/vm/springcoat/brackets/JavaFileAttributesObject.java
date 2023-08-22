// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.springcoat.brackets;

import cc.squirreljme.driver.nio.java.JavaBasicFileAttributes;
import cc.squirreljme.vm.springcoat.AbstractGhostObject;
import cc.squirreljme.vm.springcoat.SpringMachine;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * Wraps {@link BasicFileAttributes}.
 *
 * @since 2023/08/21
 */
public final class JavaFileAttributesObject
	extends AbstractGhostObject
{
	/** The attributes to wrap. */
	private final BasicFileAttributes _attrib;
	
	/**
	 * Initializes the wrapper.
	 *
	 * @param __machine The machine used.
	 * @param __attrib The attribute to wrap.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/08/21
	 */
	public JavaFileAttributesObject(SpringMachine __machine,
		BasicFileAttributes __attrib)
		throws NullPointerException
	{
		super(__machine, JavaBasicFileAttributes.class);
		
		if (__attrib == null)
			throw new NullPointerException("NARG");
		
		this._attrib = __attrib;
	}
}
