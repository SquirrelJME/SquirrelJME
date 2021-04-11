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
 * Method command set.
 *
 * @since 2021/03/14
 */
public enum CommandSetMethod
	implements JDWPCommand
{
	/** Line number table. */
	LINE_TABLE(1)
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
			// Ignore class it is not needed
			__packet.readId();
			
			// Find the method
			JDWPMethod method = __controller.state.oldMethods.get(
				__packet.readId());
			if (method == null)
				return __controller.__reply(
				__packet.id(), ErrorType.INVALID_METHOD_ID);
			
			JDWPPacket rv = __controller.__reply(
				__packet.id(), ErrorType.NO_ERROR);
				
			// Put down all the valid indexes in the method, even if there
			// are no possible lines we may still want to break at specific
			// byte code addresses even without the lines?
			long addrCount = method.debuggerLocationCount();
			rv.writeLong(0);
			rv.writeLong(addrCount);
			
			// Obtain the line table to record, ensure that it exists and is
			// valid. If this method has no byte code (abstract/native?),
			// then do nothing.
			int[] lineTable = method.debuggerLineTable();
			if (addrCount <= 0 || lineTable == null ||
				lineTable.length == 0 || lineTable[0] < 0)
			{
				// No information, so there are no lines
				rv.writeInt(0);
			}
			
			// Otherwise record the information
			else
			{
				// Write out the line table
				rv.writeInt(lineTable.length);
				for (int i = 0, n = lineTable.length; i < n; i++)
				{
					rv.writeLong(i);
					rv.writeInt(Math.max(0, lineTable[i]));
				}
			}
			
			return rv;
		}
	},
	
	/** Method byte code. */
	BYTE_CODES(3)
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/03/21
		 */
		@Override
		public JDWPPacket execute(JDWPController __controller,
			JDWPPacket __packet)
			throws JDWPException
		{
			// Ignore class it is not needed
			__packet.readId();
			
			// Find the method
			JDWPMethod method = __controller.state.oldMethods.get(
				__packet.readId());
			if (method == null)
				return __controller.__reply(
					__packet.id(), ErrorType.INVALID_METHOD_ID);
			
			// Absent information is not returned normally, but in this case
			// return it
			byte[] byteCode = method.debuggerByteCode();
			if (byteCode == null)
				return __controller.__reply(
					__packet.id(), ErrorType.ABSENT_INFORMATION);
			
			JDWPPacket rv = __controller.__reply(
				__packet.id(), ErrorType.NO_ERROR);
			
			// Dump the byte code
			rv.writeInt(byteCode.length);
			rv.write(byteCode, 0, byteCode.length);
			
			return rv;
		}
	},
	
	/** Variable table with generics. */
	VARIABLE_TABLE_WITH_GENERIC(5)
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/03/15
		 */
		@Override
		public JDWPPacket execute(JDWPController __controller,
			JDWPPacket __packet)
			throws JDWPException
		{
			// Ignore class it is not needed
			__packet.readId();
			
			// Find the method
			JDWPMethod method = __controller.state.oldMethods.get(
				__packet.readId());
			if (method == null)
				return __controller.__reply(
				__packet.id(), ErrorType.INVALID_METHOD_ID);
			
			// TODO: Implement
			Debugging.todoNote("Implement VariableTableWithGeneric.");
			return __controller.__reply(
				__packet.id(), ErrorType.ABSENT_INFORMATION);
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
	 * @since 2021/03/14
	 */
	CommandSetMethod(int __id)
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
