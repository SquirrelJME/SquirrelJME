// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.aot.nanocoat.linkage;

import net.multiphasicapps.classfile.ClassName;

/**
 * Linkage for class objects.
 *
 * @since 2023/07/16
 */
public class ClassObjectLinkage
	implements Linkage
{
	/** The class name. */
	protected final ClassName className;
	
	public ClassObjectLinkage(ClassName __className)
		throws NullPointerException
	{
		if (__className == null)
			throw new NullPointerException("NARG");
		
		this.className = __className;
	}
	
}
