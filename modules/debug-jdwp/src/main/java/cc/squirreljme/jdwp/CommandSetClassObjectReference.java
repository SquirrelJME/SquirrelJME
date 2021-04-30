// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jdwp;

import cc.squirreljme.jdwp.views.JDWPViewType;
import cc.squirreljme.runtime.cldc.debug.Debugging;

/**
 * Handles class object references.
 *
 * @since 2021/04/30
 */
public enum CommandSetClassObjectReference
	implements JDWPCommand
{
	/** The actual type of the class object. */
	REFLECTED_TYPE(1)
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/04/30
		 */
		@Override
		public JDWPPacket execute(JDWPController __controller,
			JDWPPacket __packet)
			throws JDWPException
		{
			Object object = __packet.readObject(__controller, false);
			
			// Try to find the matching instance type
			Object found = null;
			JDWPViewType viewType = __controller.viewType();
			for (Object type : __controller.__allTypes(false))
			{
				Object maybe = viewType.instance(type);
				if (maybe != null && viewType.instance(type) == object)
				{
					found = type;
					break; 
				}
			}
			
			// Not found?
			if (found == null)
				throw ErrorType.INVALID_OBJECT.toss(object,
					System.identityHashCode(object), null);
					
			// Make sure it is known
			__controller.state.items.put(found);
			
			JDWPPacket rv = __controller.__reply(
				__packet.id(), ErrorType.NO_ERROR);
			
			// Write the class type and reference to that type
			rv.writeByte(JDWPUtils.classType(__controller, found).id);
			rv.writeId(System.identityHashCode(found));
				
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
	 * @since 2021/04/30
	 */
	CommandSetClassObjectReference(int __id)
	{
		this.id = __id;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/04/30
	 */
	@Override
	public final int debuggerId()
	{
		return this.id;
	}
}
