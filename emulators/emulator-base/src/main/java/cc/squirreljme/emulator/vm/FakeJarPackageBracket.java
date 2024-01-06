// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.emulator.vm;

import cc.squirreljme.jvm.mle.brackets.JarPackageBracket;
import cc.squirreljme.vm.VMClassLibrary;

/**
 * A fake {@link JarPackageBracket}.
 *
 * @since 2024/01/06
 */
public final class FakeJarPackageBracket
	implements JarPackageBracket
{
	/** The library this wraps. */
	public final VMClassLibrary library;
	
	/**
	 * Initializes the fake bracket.
	 * 
	 * @param __library The library to wrap.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/06
	 */
	public FakeJarPackageBracket(VMClassLibrary __library)
		throws NullPointerException
	{
		this.library = __library;
	}
}
