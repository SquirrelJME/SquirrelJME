// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.tool.rms;

import cc.squirreljme.runtime.cldc.debug.Debugging;

/**
 * Commands that can be used on RMS storage.
 *
 * @since 2025/04/14
 */
public enum RMSCommand
{
	/** Delete record. */
	DELETE("delete", "remove", "rm")
	{
	},
	
	/** Export to disk. */
	EXPORT("export", "save", "backup")
	{
	},
	
	/** Import from disk. */
	IMPORT("import", "load", "restore")
	{
	},
	
	/** List records. */
	LIST("list", "ls")
	{
	},
	
	/** Calculate the prefix used for suites. */
	PREFIX("prefix", "basename")
	{
	},
	
	/** Set ID. */
	SET_ID("setid", "changeid")
	{
	},
	
	/** Set MIDlet. */
	SET_MIDLET("setmidlet", "changemidlet")
	{
	},
	
	/** Set modification time. */
	SET_MODIFICATION_TIME("setmodtime", "setmodificationtime",
		"settime")
	{
	},
	
	/** Set modification count. */
	SET_MODIFICATION_COUNT("setmodcount", "setmodificationcount",
		"setcount")
	{
	},
	
	/** Set suite. */
	SET_SUITE("setsuite")
	{
	},
	
	/** Set vendor. */
	SET_VENDOR("setvendor")
	{
	},
	
	/** Swap two different IDs. */
	SWAP("swap", "swapid", "swapids")
	{
	},
	
	/* End. */
	;
	
	RMSCommand(String... __commands)
	{
		Debugging.todo();
	}
}
