// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jdwp.host;

import cc.squirreljme.jdwp.JDWPErrorType;
import cc.squirreljme.jdwp.JDWPException;
import cc.squirreljme.jdwp.JDWPHostController;
import cc.squirreljme.jdwp.JDWPPacket;

/**
 * Class type commands.
 *
 * @since 2021/03/14
 */
public enum JDWPHostCommandSetClassType
	implements JDWPCommandHandler
{
	/** The super class of the given class. */
	SUPERCLASS(1)
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/03/14
		 */
		@Override
		public JDWPPacket execute(JDWPHostController __controller,
			JDWPPacket __packet)
			throws JDWPException
		{
			// Which class does this refer to?
			Object type = __controller.readType(__packet, false); 
			
			// Respond with the class ID
			JDWPPacket rv = __controller.reply(
				__packet.id(), JDWPErrorType.NO_ERROR);
			
			Object superClass = __controller.viewType().superType(type);
			__controller.writeObject(rv, superClass);
			
			// Register the super class so it can be known
			if (superClass != null)
				__controller.getState().items.put(superClass);
			
			return rv;
		}
	}
	
	/* End. */
	;
	
	/** The ID of the packet. */
	public final int id;
	
	/**
	 * Returns the ID used.
	 * 
	 * @param __id The ID used.
	 * @since 2021/03/14
	 */
	JDWPHostCommandSetClassType(int __id)
	{
		this.id = __id;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/03/14
	 */
	@Override
	public final int debuggerId()
	{
		return this.id;
	}
}
