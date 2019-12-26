// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package dev.shadowtail.squirrelquarrel;

import javax.microedition.lcdui.Choice;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.List;

/**
 * This listens to commands within the game.
 *
 * @since 2019/12/25
 */
public final class CommandHandler
	implements CommandListener
{
	/** The command to exit the game. */
	public static final Command EXIT_COMMAND =
		new Command("Quit", Command.EXIT, 10);
	
	/** The game to interact with. */
	protected final Game game;
	
	/**
	 * Initializes the command handler.
	 *
	 * @param __g The game being played.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/12/25
	 */
	public CommandHandler(Game __g)
		throws NullPointerException
	{
		if (__g == null)
			throw new NullPointerException("NARG");
		
		this.game = __g;
	}
	
	/**
	 * Executes the given command.
	 *
	 * @param __c The command to execute.
	 * @since 2019/12/25
	 */
	public void commandAction(Command __c, Displayable __d)
	{
		// Exit the game?
		if (__c == EXIT_COMMAND)
			System.exit(0);
		
		// The in-game menu was requested
		else if (__c == GameInterface.STATUS_COMMAND)
			__d.getCurrentDisplay().setCurrent(
				this.__makeStatusMenu((GameInterface)__d));
		
		// Returning to the game?
		else if (__c instanceof ReturnToGameCommand)
			__d.getCurrentDisplay().setCurrent(
				((ReturnToGameCommand)__c).gameinterface);
	}
	
	/**
	 * Makes and returns the status menu.
	 *
	 * @param __gi The game interface.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/12/25
	 */
	private final Displayable __makeStatusMenu(GameInterface __gi)
		throws NullPointerException
	{
		if (__gi == null)
			throw new NullPointerException("NARG");
		
		// Make a list to show status items
		List items = new List("Status", Choice.IMPLICIT);
		
		// For now add some dummy items
		items.append("P1 Acorns: 9999", null);
		items.append("P2 Acorns: 9999", null);
		items.append("P3 Acorns: 9999", null);
		items.append("P4 Acorns: 9999", null);
		items.append("Season: Spring", null);
		
		// Set commands for this list
		items.addCommand(new ReturnToGameCommand(__gi));
		items.addCommand(EXIT_COMMAND);
		
		// Use self as the command listener
		items.setCommandListener(this);
		
		// Return the list
		return items;
	}
}

