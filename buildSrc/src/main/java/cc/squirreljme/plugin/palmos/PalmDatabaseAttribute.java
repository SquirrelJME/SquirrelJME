// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.palmos;

/**
 * This represents an attribute for the Palm Database.
 *
 * @since 2019/07/13
 */
public enum PalmDatabaseAttribute
{
	/** This is a resource database. */
	RESOURCE_DATABASE(0x0001),
	
	/** Read-only. */
	READ_ONLY(0x0002),
	
	/** Info block is dirty. */
	APP_INFO_DIRTY(0x0004),
	
	/** Database should be packed up to the PC. */
	BACKUP(0x0008),
	
	/** Is okay to install newer version? */
	OK_TO_INSTALL_NEWER(0x0010),
	
	/** Requires a reset after install. */
	RESET_AFTER_INSTALL(0x0020),
	
	/** Copy protection (prevent beaming/copy). */
	COPY_PROTECTION(0x0040),
	
	/** Used as a file stream. */
	STREAM(0x0080),
	
	/** Is hidden. */
	HIDDEN(0x0100),
	
	/** Is a data resource which can be launched. */
	LAUNCHABLE_DATA(0x0200),
	
	/** The database can be recycled. */
	RECYCLABLE(0x0400),
	
	/** Is a bundle, always beam with the same creator type. */
	BUNDLE(0x0800),
	
	/** Database is open and has not been closed properly. */
	OPEN(0x8000),
	
	/** End. */
	;
	
	/** The bit used. */
	public final int bit;
	
	/**
	 * Initializes the attribute.
	 *
	 * @param __bit The bit used.
	 * @since 2019/07/13
	 */
	PalmDatabaseAttribute(int __bit)
	{
		this.bit = __bit;
	}
}

