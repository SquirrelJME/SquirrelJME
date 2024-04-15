// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.springcoat.brackets;

import cc.squirreljme.emulator.terminal.TerminalPipe;
import cc.squirreljme.jvm.mle.brackets.PipeBracket;
import cc.squirreljme.vm.springcoat.AbstractGhostObject;
import cc.squirreljme.vm.springcoat.SpringMachine;

/**
 * This wraps a {@link PipeBracket}.
 *
 * @since 2022/03/19
 */
public class PipeObject
	extends AbstractGhostObject
{
	/** The pipe this points to. */
	public final TerminalPipe pipe;
	
	/**
	 * Wraps the virtual pipe with an object for usage within the VM.
	 * 
	 * @param __machine The machine being used to initialize this.
	 * @param __pipe The actual pipe to wrap.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/03/19
	 */
	public PipeObject(SpringMachine __machine, TerminalPipe __pipe)
		throws NullPointerException
	{
		super(__machine, PipeBracket.class);
				
		if (__pipe == null)
			throw new NullPointerException("NARG");
		
		this.pipe = __pipe;
	}
}
