// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.debugger;

import cc.squirreljme.jdwp.JDWPCommandSetVirtualMachine;
import cc.squirreljme.jdwp.JDWPCapability;
import cc.squirreljme.jdwp.JDWPCommandSet;
import cc.squirreljme.jdwp.JDWPPacket;
import java.util.HashSet;
import java.util.Set;

/**
 * This contains the status of capabilities of the connected virtual machine.
 *
 * @since 2024/01/19
 */
public class CapabilityStatus
	implements ReplyHandler
{
	/** The currently set capabilities. */
	private final Set<JDWPCapability> _capabilities =
		new HashSet<>()/*new EnumSet<>(JDWPCapability.class)*/;
	
	/**
	 * {@inheritDoc}
	 * @since 2024/01/19
	 */
	@Override
	public void handlePacket(DebuggerState __debuggerState,
		JDWPPacket __packet)
		throws NullPointerException
	{
		if (__debuggerState == null || __packet == null)
			throw new NullPointerException("NARG");
		
		Set<JDWPCapability> capabilities = this._capabilities;
		synchronized (this)
		{
			// Go through all capabilities listed in the packet to update
			for (int id = 0, n = __packet.length(); id < n; id++)
			{
				// Only accept valid capabilities
				JDWPCapability capability = JDWPCapability.of(id);
				if (capability == null)
					continue;
				
				// Either set or clear the capability
				if (__packet.readBoolean())
					capabilities.add(capability);
				else
					capabilities.remove(capability);
			}
		}
	}
	
	/**
	 * Is the given capability set?
	 *
	 * @param __cap The capability to check.
	 * @return If the capability is set.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/19
	 */
	public boolean has(JDWPCapability __cap)
		throws NullPointerException
	{
		if (__cap == null)
			throw new NullPointerException("NARG");
		
		synchronized (this)
		{
			return this._capabilities.contains(__cap);
		}
	}
	
	/**
	 * Requests an update of capabilities of the remote virtual machine.
	 *
	 * @param __state The current debugging state.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/19
	 */
	public void update(DebuggerState __state)
		throws NullPointerException
	{
		if (__state == null)
			throw new NullPointerException("NARG");
		
		// Ask for normal capabilities (older VM)
		try (JDWPPacket out = __state.commLink.request(
			JDWPCommandSet.VIRTUAL_MACHINE,
			JDWPCommandSetVirtualMachine.CAPABILITIES))
		{
			__state.send(out, this, ReplyHandler.IGNORED);
		}
		
		// Ask for newer capabilities (newer VM)
		try (JDWPPacket out = __state.commLink.request(
			JDWPCommandSet.VIRTUAL_MACHINE,
			JDWPCommandSetVirtualMachine.CAPABILITIES_NEW))
		{
			__state.send(out, this, ReplyHandler.IGNORED);
		}
	}
}
