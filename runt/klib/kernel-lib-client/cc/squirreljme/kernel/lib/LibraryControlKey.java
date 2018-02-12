// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.kernel.lib;

/**
 * System defined control keys.
 *
 * @since 2017/12/31
 */
public interface LibraryControlKey
{
	/** Download URL. */
	public static final String DOWNLOAD_URL =
		"X-SquirrelJME-Download-URL";
	
	/** Is the program installed? */
	public static final String IS_INSTALLED =
		"X-SquirrelJME-Is-Installed";
	
	/** Is the program trusted? */
	public static final String IS_TRUSTED =
		"X-SquirrelJME-Is-Trusted";
	
	/** The trust group for this library. */
	public static final String TRUST_GROUP =
		"X-SquirrelJME-Trust-Group";
	
	/** The type of library this is. */
	public static final String TYPE =
		"X-SquirrelJME-Trust-Type";
	
	/** Prefix for state flags. */
	public static final String STATE_FLAG_PREFIX =
		"X-SquirrelJME-State-Flag-";
	
	/** Is the program available? */
	public static final String STATE_FLAG_AVAILABLE =
		"X-SquirrelJME-State-Flag-AVAILABLE";
	
	/** Is the program enabled? */
	public static final String STATE_FLAG_ENABLED =
		"X-SquirrelJME-State-Flag-ENABLED";
	
	/** Dependency prefix. */
	public static final String DEPENDENCY_PREFIX =
		"X-SquirrelJME-Dependency-";
}

