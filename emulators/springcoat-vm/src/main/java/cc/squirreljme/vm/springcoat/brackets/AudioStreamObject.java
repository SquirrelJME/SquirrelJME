// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.springcoat.brackets;

import cc.squirreljme.jvm.mle.brackets.AudioStreamBracket;
import cc.squirreljme.jvm.mle.brackets.BucketBracket;
import cc.squirreljme.vm.springcoat.AbstractGhostObject;
import cc.squirreljme.vm.springcoat.SpringMachine;

/**
 * Represents a {@link AudioStreamBracket}.
 *
 * @since 2025/04/25
 */
public class AudioStreamObject
	extends AbstractGhostObject
{
	/** The stream this wraps. */
	public final AudioStreamBracket stream;
	
	/**
	 * Initializes the stream wrapper.
	 *
	 * @param __machine The machine used.
	 * @throws NullPointerException On null arguments.
	 * @since 2025/04/25
	 */
	public AudioStreamObject(SpringMachine __machine,
		AudioStreamBracket __wrapped)
		throws NullPointerException
	{
		super(__machine, AudioStreamBracket.class);
		
		this.stream = __wrapped;
	}
}
