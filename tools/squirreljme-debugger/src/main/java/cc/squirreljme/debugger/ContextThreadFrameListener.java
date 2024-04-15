// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.debugger;

/**
 * A listener that is called when the context changes.
 *
 * @since 2024/01/25
 */
public interface ContextThreadFrameListener
{
	/**
	 * This called when the context has changed for a thread.
	 *
	 * @param __oldThread The old thread.
	 * @param __oldFrame The old frame.
	 * @param __oldLocation The old location.
	 * @param __newThread The new thread.
	 * @param __newFrame The new frame.
	 * @param __newLocation The new location.
	 * @since 2024/01/25
	 */
	void contextChanged(InfoThread __oldThread, InfoFrame __oldFrame,
		FrameLocation __oldLocation, InfoThread __newThread,
		InfoFrame __newFrame, FrameLocation __newLocation);
}
