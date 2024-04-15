// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.util;

/**
 * This is used to allow for unassisted launches when building ROMs and
 * otherwise.
 *
 * @since 2021/08/22
 */
public final class UnassistedLaunchEntry
{
	/** MIDlet starting class. */
	public static final String MIDLET_MAIN_CLASS =
		"javax.microedition.midlet.__MainHandler__";
	
	/** The main entry class. */
	public final String mainClass;
	
	/** The arguments to the call. */
	private final String[] _args;
	
	/**
	 * Initializes the unassisted launch entry.
	 * 
	 * @param __mainClass The main class.
	 * @param __args The arguments to the call.
	 * @since 2021/08/22
	 */
	public UnassistedLaunchEntry(String __mainClass, String... __args)
	{
		this.mainClass = __mainClass;
		this._args = __args;
	}
	
	/**
	 * Returns the launch arguments.
	 * 
	 * @return The launch arguments.
	 * @since 2021/08/22
	 */
	public final String[] args()
	{
		return this._args.clone();
	}
}
