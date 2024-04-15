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
import cc.squirreljme.jdwp.JDWPCommandSetMethod;
import cc.squirreljme.jdwp.JDWPErrorType;
import cc.squirreljme.jdwp.JDWPException;
import cc.squirreljme.jdwp.JDWPLocalVariable;
import cc.squirreljme.jdwp.JDWPPacket;
import cc.squirreljme.jdwp.host.views.JDWPViewType;
import net.multiphasicapps.classfile.MethodDescriptor;

/**
 * Method command set.
 *
 * @since 2021/03/14
 */
public enum JDWPHostCommandSetMethod
	implements JDWPCommandHandler
{
	/** Line number table. */
	LINE_TABLE(JDWPCommandSetMethod.LINE_TABLE)
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
			// Read class and the method it is in
			Object classy = __controller.readType(__packet, false);
			int methodId = __packet.readId();
			
			// Not a valid method?
			JDWPViewType viewType = __controller.viewType();
			if (!viewType.isValidMethod(classy, methodId))
				throw JDWPErrorType.INVALID_METHOD_ID.toss(classy, methodId);
			
			JDWPPacket rv = __controller.reply(
				__packet.id(), JDWPErrorType.NO_ERROR);
				
			// Put down all the valid indexes in the method, even if there
			// are no possible lines we may still want to break at specific
			// byte code addresses even without the lines?
			long addrCount = viewType.methodLocationCount(classy, methodId);
			rv.writeLong(0);
			rv.writeLong(Math.max(-1, addrCount));
			
			// Obtain the line table to record, ensure that it exists and is
			// valid. If this method has no byte code (abstract/native?),
			// then do nothing.
			int[] lineTable = viewType.methodLineTable(classy, methodId);
			if (addrCount <= 0 || lineTable == null ||
				lineTable.length == 0 || lineTable[0] < 0)
			{
				// No information, so there are no lines
				rv.writeInt(0);
			}
			
			// Otherwise record the information
			else
			{
				// Shrink the table so less information is given
				int countedLines = 0;
				int[] paddedPCs = new int[lineTable.length];
				int[] paddedLines = new int[lineTable.length];
				for (int i = 0, n = lineTable.length, lastLine = -1;
					i < n; i++)
				{
					// If the line has changed, record it
					int at = lineTable[i];
					if (i == 0 || at != lastLine)
					{
						paddedPCs[countedLines] = i;
						paddedLines[countedLines++] = at;
						
						// Do not repeat this line
						lastLine = at;
					}
				}
				
				// Write out the condensed line table
				rv.writeInt(countedLines);
				for (int i = 0, n = countedLines; i < n; i++)
				{
					rv.writeLong(paddedPCs[i]);
					rv.writeInt(Math.max(0, paddedLines[i]));
				}
			}
			
			return rv;
		}
	},
	
	/** Variable table without generics. */
	VARIABLE_TABLE(JDWPCommandSetMethod.VARIABLE_TABLE)
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/04/30
		 */
		@Override
		public JDWPPacket execute(JDWPHostController __controller,
			JDWPPacket __packet)
			throws JDWPException
		{
			return this.__variables(false, __controller, __packet);
		}
	},
	
	/** Method byte code. */
	BYTE_CODES(JDWPCommandSetMethod.BYTE_CODES)
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/03/21
		 */
		@Override
		public JDWPPacket execute(JDWPHostController __controller,
			JDWPPacket __packet)
			throws JDWPException
		{
			// Read class and the method it is in
			Object classy = __controller.readType(__packet, false);
			int methodId = __packet.readId();
			
			// Not a valid method?
			JDWPViewType viewType = __controller.viewType();
			if (!viewType.isValidMethod(classy, methodId))
				throw JDWPErrorType.INVALID_METHOD_ID.toss(classy, methodId);
			
			// Absent information is not returned normally, but in this case
			// return it
			byte[] byteCode = viewType.methodByteCode(classy, methodId);
			if (byteCode == null)
				throw JDWPErrorType.ABSENT_INFORMATION.toss(classy, methodId);
			
			JDWPPacket rv = __controller.reply(
				__packet.id(), JDWPErrorType.NO_ERROR);
			
			// Dump the byte code
			rv.writeInt(byteCode.length);
			rv.write(byteCode, 0, byteCode.length);
			
			return rv;
		}
	},
	
	/** Variable table with generics. */
	VARIABLE_TABLE_WITH_GENERIC(JDWPCommandSetMethod.VARIABLE_TABLE_WITH_GENERIC)
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/03/15
		 */
		@Override
		public JDWPPacket execute(JDWPHostController __controller,
			JDWPPacket __packet)
			throws JDWPException
		{
			return this.__variables(true, __controller, __packet);
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
	 * @since 2021/03/14
	 */
	JDWPHostCommandSetMethod(JDWPCommand __id)
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
	 * @since 2021/03/14
	 */
	@Override
	public final int debuggerId()
	{
		return this.id;
	}
	
	/**
	 * Handles standard or generic variable table information.
	 * 
	 * @param __generic Write generic variable tables?
	 * @param __controller The controller used.
	 * @param __packet The packet to read from.
	 * @return The resultant packet.
	 * @throws JDWPException If this is not valid.
	 * @since 2021/04/30
	 */
	JDWPPacket __variables(boolean __generic, JDWPHostController __controller,
		JDWPPacket __packet)
		throws JDWPException
	{
		// Read class and the method it is in
		Object classy = __controller.readType(__packet, false);
		int methodId = __packet.readId();
		
		// Not a valid method?
		JDWPViewType viewType = __controller.viewType();
		if (!viewType.isValidMethod(classy, methodId))
			throw JDWPErrorType.INVALID_METHOD_ID.toss(classy, methodId);
		
		// Get the variable table, if missing then ignore it
		JDWPLocalVariable[] variables = viewType.methodVariableTable(classy,
			methodId);
		if (variables == null || variables.length <= 0)
			return __controller.reply(
				__packet.id(), JDWPErrorType.ABSENT_INFORMATION);
		
		// Setup packet
		JDWPPacket rv = __controller.reply(
			__packet.id(), JDWPErrorType.NO_ERROR);
		
		// Write down the number of argument slots
		MethodDescriptor desc = new MethodDescriptor(
			viewType.methodSignature(classy, methodId));
		rv.writeInt(desc.argumentSlotCount());
		
		// Write down everything we know
		int count = variables.length;
		rv.writeInt(count);
		for (int i = 0; i < count; i++)
		{
			JDWPLocalVariable variable = variables[i];
			
			rv.writeLong(variable.startPc);
			rv.writeString(variable.variableName);
			rv.writeString(variable.fieldDescriptor);
			
			// No generics are used
			if (__generic)
				rv.writeString("");
			
			rv.writeInt(variable.length);
			rv.writeInt(variable.localSlot);
		}
		
		return rv;
	}
}
