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
 * Reference type command set.
 *
 * @since 2021/03/13
 */
public enum ReferenceTypeCommandSet
	implements JDWPCommand
{
	/** Methods with generic types. */
	METHODS_WITH_GENERIC(15)
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/03/13
		 */
		@Override
		public JDWPPacket execute(JDWPController __controller,
			JDWPPacket __packet)
			throws JDWPException
		{
			// Which class does this refer to?
			JDWPClass type = __controller.state.classes.get(
				__packet.readId());
			if (type == null)
				return __controller.__reply(
				__packet.id(), ErrorType.INVALID_CLASS);
				
			JDWPPacket rv = __controller.__reply(
				__packet.id(), ErrorType.NO_ERROR);
			
			// Write number of methods
			JDWPMethod[] methods = type.debuggerMethods();
			rv.writeInt(methods.length);
			
			// Write information on each method
			for (JDWPMethod method : methods)
			{
				// Register this method for later lookup
				__controller.state.methods.put(method);
				
				// Information about the method
				rv.writeId(method);
				rv.writeString(method.debuggerMethodName());
				rv.writeString(method.debuggerMethodType());
				
				// Generics are not used in SquirrelJME, ignore
				rv.writeString("");
				
				// Modifier flags
				rv.writeInt(method.debuggerMethodFlags());
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
	 * @since 2021/03/13
	 */
	ReferenceTypeCommandSet(int __id)
	{
		this.id = __id;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/03/13
	 */
	@Override
	public final int debuggerId()
	{
		return this.id;
	}
}
