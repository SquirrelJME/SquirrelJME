// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.debugger;

import cc.squirreljme.jdwp.JDWPId;
import cc.squirreljme.jdwp.JDWPPacket;

/**
 * Modifier for a location.
 *
 * @since 2024/07/27
 */
public class EventModifierLocationOnly
	implements EventModifier
{
	/** The class id. */
	protected final JDWPId classId;
	
	/** The method id. */
	protected final JDWPId methodId;
	
	/** Index into the byte code. */
	protected final long index;
	
	/**
	 * Initializes the location modifier.
	 *
	 * @param __classId The class ID.
	 * @param __methodId The method ID.
	 * @param __index The index.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/07/27
	 */
	public EventModifierLocationOnly(JDWPId __classId, JDWPId __methodId,
		long __index)
		throws NullPointerException
	{
		if (__classId == null || __methodId == null)
			throw new NullPointerException("NARG");
		
		this.classId = __classId;
		this.methodId = __methodId;
		this.index = __index;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/07/27
	 */
	@Override
	public void write(DebuggerState __debuggerState, JDWPPacket __packet)
		throws NullPointerException
	{
		if (__debuggerState == null || __packet == null)
			throw new NullPointerException("NARG");
		
		__packet.writeByte('c');
		__packet.writeId(this.classId);
		__packet.writeId(this.methodId);
		__packet.writeLong(this.index);
	}
}
