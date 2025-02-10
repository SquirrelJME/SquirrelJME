// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.zip;

/**
 * This is used to describe the compatibility that attributes within the ZIP
 * file are compatible with.
 *
 * @since 2016/08/07
 */
public enum ZipAttributeCompatibility
{
	/** DOS. */
	DOS(0),
	
	/** Amiga. */
	AMIGA(1),
	
	/** OpenVMS. */
	OPENVMS(2),
	
	/** UNIX. */
	UNIX(3),
	
	/** VM/CMS. */
	VM_CMS(4),
	
	/** Atari ST. */
	ATARI_ST(5),
	
	/** OS/2. */
	OS_2(6),
	
	/** Mac OS (Classic). */
	MACOS(7),
	
	/** Z-System. */
	Z_SYSTEM(8),
	
	/** CP/M. */
	CP_M(9),
	
	/** NTFS. */
	NTFS(10),
	
	/** MVS. */
	MVS(11),
	
	/** VSE. */
	VSE(12),
	
	/** Acorn RISC. */
	ACORN_RISC(13),
	
	/** VFAT. */
	VFAT(14),
	
	/** MVS (alternate). */
	MVS_ALTERNATIVE(15),
	
	/** BeOS. */
	BEOS(16),
	
	/** Tandem. */
	TANDEM(17),
	
	/** OS/400. */
	OS_400(18),
	
	/** Mac OS X. */
	MACOSX(19),
	
	/** End. */
	;
	
	/** The compatibility identifier. */
	private int id;
	
	/**
	 * Initializes the base compatibility information.
	 *
	 * @param __id The identifier used.
	 * @since 2016/08/07
	 */
	ZipAttributeCompatibility(int __id)
	{
		this.id = __id;
	}
	
	/**
	 * Returns the version identifier.
	 *
	 * @return The version identifier.
	 * @since 2016/08/07
	 */
	public final int identifier()
	{
		return this.id;
	}
}

