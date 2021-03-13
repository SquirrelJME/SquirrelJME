// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jdwp;

import java.util.ArrayList;
import java.util.List;

/**
 * Command set for JDWP Packets.
 *
 * @since 2021/03/12
 */
public enum JDWPCommandSet
{
	/** Unknown command set. */
	UNKNOWN(-1),
	
	/** Virtual machine command set. */
	VIRTUAL_MACHINE(1, VirtualMachineCommandSet.values()),
	
	/* End. */
	;
	
	/** Quick lookup for command sets. */
	private static final JDWPCommandSet[] _QUICK;
	
	/** The Id of the set. */
	public final int id;
	
	/** Command set commands. */
	private final JDWPCommand[] _commands; 
	
	static
	{
		// Fill in ID maps
		List<JDWPCommandSet> quick = new ArrayList<>();
		for (JDWPCommandSet set : JDWPCommandSet.values())
		{
			// Ignore unknown
			if (set == JDWPCommandSet.UNKNOWN)
				continue;
			
			// Add null fillers
			int id = set.id;
			while (quick.size() <= id)
				quick.add(null);
			
			// Set specific position
			quick.set(id, set);
		}
		
		_QUICK = quick.<JDWPCommandSet>toArray(
			new JDWPCommandSet[quick.size()]);
	}
	
	/**
	 * Initializes the command set enum.
	 * 
	 * @param __id The ID number.
	 * @param __cmds Available commands.
	 * @since 2021/03/12
	 */
	JDWPCommandSet(int __id, JDWPCommand... __cmds)
	{
		this.id = __id;
		
		// Build quick command lookup
		List<JDWPCommand> commands = new ArrayList<>(__cmds.length);
		for (JDWPCommand cmd : __cmds)
		{
			// Add null fillers
			int cmdId = cmd.id();
			while (commands.size() <= cmdId)
				commands.add(null);
			
			// Set in slot
			commands.set(cmdId, cmd);
		}
		
		this._commands = commands.<JDWPCommand>toArray(
			new JDWPCommand[commands.size()]);
	}
	
	/**
	 * Returns the command.
	 * 
	 * @param __id The ID of the command.
	 * @return The command used, {@code null} if unknown.
	 * @since 2021/03/12
	 */
	public final JDWPCommand command(int __id)
	{
		JDWPCommand[] commands = this._commands;
		if (__id < 0 || __id >= commands.length)
			return null;
		return commands[__id];
	}
	
	/**
	 * Returns the command set of the given ID.
	 * 
	 * @param __id The id of the command set.
	 * @return The command set used or {@link #UNKNOWN} if not known.
	 * @since 2021/03/12
	 */
	public static JDWPCommandSet of(int __id)
	{
		JDWPCommandSet[] quick = JDWPCommandSet._QUICK;
		if (__id < 0 || __id >= quick.length)
			return JDWPCommandSet.UNKNOWN;
		
		JDWPCommandSet rv = quick[__id];
		return (rv == null ? JDWPCommandSet.UNKNOWN : rv);
	}
}
