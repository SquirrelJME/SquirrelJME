// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jdwp;

import cc.squirreljme.runtime.cldc.debug.Debugging;

/**
 * Virtual machine command set.
 *
 * @since 2021/03/12
 */
public enum VirtualMachineCommandSet
	implements JDWPCommand
{
	/* Returns the size of variable data. */
	ID_SIZES(7)
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/03/12
		 */
		@Override
		public JDWPPacket execute(JDWPController __controller,
			JDWPPacket __packet)
			throws JDWPException
		{
			JDWPPacket rv = __controller.__reply(
				__packet.id(), JDWPErrorType.NO_ERROR);
			
			// field, method, object, reference, frame
			for (int i = 0; i < 5; i++)
				__packet.writeInt(JDWPConstants.ID_SIZE);
			
			return rv;
		}
	},
		
	/* End. */
	;
	
	/** The ID of the packet. */
	public final int id;
	
	/**
	 * Returns the ID used.
	 * 
	 * @param __id The ID used.
	 * @since 2021/03/12
	 */
	VirtualMachineCommandSet(int __id)
	{
		this.id = __id;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/03/12
	 */
	@Override
	public final int id()
	{
		return this.id;
	}
}
