// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jdwp;

import cc.squirreljme.jdwp.host.views.JDWPViewType;
import java.util.LinkedList;
import java.util.List;

/**
 * Support for class loader information.
 *
 * @since 2021/04/20
 */
public enum CommandSetClassLoader
	implements JDWPCommand
{
	/** Classes which are visible to the given loader. */
	VISIBLE_CLASSES(1)
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/04/20
		 */
		@Override
		public JDWPPacket execute(JDWPController __controller,
			JDWPPacket __packet)
			throws JDWPException
		{
			// Null is a valid loader, for the system in the event this is
			// ever the case
			int id = __packet.readId();
			Object loader = __controller.state.items.get(id);
			
			// Go through all known types to find ones that use this class
			// loader
			List<Object> found = new LinkedList<>();
			JDWPViewType viewType = __controller.viewType();
			for (Object type : __controller.__allTypes(false))
				if (loader == viewType.classLoader(type))
					found.add(type);
			
			JDWPPacket rv = __controller.__reply(
				__packet.id(), ErrorType.NO_ERROR);
			
			// Write all entries which meet the same class loader
			rv.writeInt(found.size());
			for (Object type : found)
			{
				if (type != null)
					__controller.state.items.put(type);
				
				rv.writeTaggedId(__controller, type);
			}
			
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
	 * @since 2021/04/20
	 */
	CommandSetClassLoader(int __id)
	{
		this.id = __id;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/04/20
	 */
	@Override
	public final int debuggerId()
	{
		return this.id;
	}
}
