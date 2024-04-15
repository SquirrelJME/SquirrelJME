// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jdwp.host;

import cc.squirreljme.jdwp.JDWPCommand;
import cc.squirreljme.jdwp.JDWPCommandSetClassLoader;
import cc.squirreljme.jdwp.JDWPErrorType;
import cc.squirreljme.jdwp.JDWPException;
import cc.squirreljme.jdwp.JDWPPacket;
import cc.squirreljme.jdwp.host.views.JDWPViewType;
import java.util.LinkedList;
import java.util.List;

/**
 * Support for class loader information.
 *
 * @since 2021/04/20
 */
public enum JDWPHostCommandSetClassLoader
	implements JDWPCommandHandler
{
	/** Classes which are visible to the given loader. */
	VISIBLE_CLASSES(JDWPCommandSetClassLoader.VISIBLE_CLASSES)
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/04/20
		 */
		@Override
		public JDWPPacket execute(JDWPHostController __controller,
			JDWPPacket __packet)
			throws JDWPException
		{
			// Null is a valid loader, for the system in the event this is
			// ever the case
			int id = __packet.readId();
			Object loader = __controller.getState().items.get(id);
			
			// Go through all known types to find ones that use this class
			// loader
			List<Object> found = new LinkedList<>();
			JDWPViewType viewType = __controller.viewType();
			for (Object type : __controller.allTypes(false))
				if (loader == viewType.classLoader(type))
					found.add(type);
			
			JDWPPacket rv = __controller.reply(
				__packet.id(), JDWPErrorType.NO_ERROR);
			
			// Write all entries which meet the same class loader
			rv.writeInt(found.size());
			for (Object type : found)
			{
				if (type != null)
					__controller.getState().items.put(type);
				
				__controller.writeTaggedId(rv, type);
			}
			
			return rv;
		}
	},
	
	/* End. */
	;
	
	/** The base command. */
	public final JDWPCommand command;
	
	/** The ID of the packet. */
	public final int id;
	
	/**
	 * Returns the ID used.
	 * 
	 * @param __id The ID used.
	 * @since 2021/04/20
	 */
	JDWPHostCommandSetClassLoader(JDWPCommand __id)
	{
		this.command = __id;
		this.id = __id.debuggerId();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/01/23
	 */
	@Override
	public final JDWPCommand command()
	{
		return this.command;
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
