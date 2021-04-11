// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jdwp;

/**
 * Class type commands.
 *
 * @since 2021/03/14
 */
public enum CommandSetClassType
	implements JDWPCommand
{
	/** The super class of the given class. */
	SUPERCLASS(1)
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/03/14
		 */
		@Override
		public JDWPPacket execute(JDWPController __controller,
			JDWPPacket __packet)
			throws JDWPException
		{
			// Which class does this refer to?
			JDWPClass type = __controller.state.getAnyClass(__packet.readId());
			if (type == null)
				return __controller.__reply(
				__packet.id(), ErrorType.INVALID_CLASS);
			
			// Respond with the class ID
			JDWPPacket rv = __controller.__reply(
				__packet.id(), ErrorType.NO_ERROR);
			JDWPClass superClass = type.debuggerSuperClass();
			rv.writeId(superClass);
			
			// Register the super class so it can be known
			if (superClass != null)
				__controller.state.oldClasses.put(superClass);
			
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
	CommandSetClassType(int __id)
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
