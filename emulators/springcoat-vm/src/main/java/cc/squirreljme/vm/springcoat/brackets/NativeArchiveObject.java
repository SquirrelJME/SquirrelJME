// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.springcoat.brackets;

import cc.squirreljme.jvm.mle.brackets.NativeArchiveBracket;
import cc.squirreljme.vm.springcoat.AbstractGhostObject;
import cc.squirreljme.vm.springcoat.SpringMachine;
import org.jetbrains.annotations.NotNull;

/**
 * Native archive.
 *
 * @since 2024/03/05
 */
public class NativeArchiveObject
	extends AbstractGhostObject
{
	/** The wrapped object. */
	public final NativeArchiveBracket wrapped;
	
	/**
	 * Initializes the object.
	 *
	 * @param __machine The machine used.
	 * @param __wrapped The wrapped archive.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/04/05
	 */
	public NativeArchiveObject(SpringMachine __machine,
		NativeArchiveBracket __wrapped)
		throws NullPointerException
	{
		super(__machine, NativeArchiveBracket.class);
		
		if (__wrapped == null)
			throw new NullPointerException("NARG");
			
		this.wrapped = __wrapped;
	}
}
