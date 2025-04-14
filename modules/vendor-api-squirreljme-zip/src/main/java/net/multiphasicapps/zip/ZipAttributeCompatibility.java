// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.zip;

import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;

/**
 * This is used to describe the compatibility that attributes within the ZIP
 * file are compatible with.
 *
 * @since 2016/08/07
 */
@SquirrelJMEVendorApi
public enum ZipAttributeCompatibility
{
	/** DOS. */
	@SquirrelJMEVendorApi
	DOS(0),
	
	/** Amiga. */
	@SquirrelJMEVendorApi
	AMIGA(1),
	
	/** OpenVMS. */
	@SquirrelJMEVendorApi
	OPENVMS(2),
	
	/** UNIX. */
	@SquirrelJMEVendorApi
	UNIX(3),
	
	/** VM/CMS. */
	@SquirrelJMEVendorApi
	VM_CMS(4),
	
	/** Atari ST. */
	@SquirrelJMEVendorApi
	ATARI_ST(5),
	
	/** OS/2. */
	@SquirrelJMEVendorApi
	OS_2(6),
	
	/** Mac OS (Classic). */
	@SquirrelJMEVendorApi
	MACOS(7),
	
	/** Z-System. */
	@SquirrelJMEVendorApi
	Z_SYSTEM(8),
	
	/** CP/M. */
	@SquirrelJMEVendorApi
	CP_M(9),
	
	/** NTFS. */
	@SquirrelJMEVendorApi
	NTFS(10),
	
	/** MVS. */
	@SquirrelJMEVendorApi
	MVS(11),
	
	/** VSE. */
	@SquirrelJMEVendorApi
	VSE(12),
	
	/** Acorn RISC. */
	@SquirrelJMEVendorApi
	ACORN_RISC(13),
	
	/** VFAT. */
	@SquirrelJMEVendorApi
	VFAT(14),
	
	/** MVS (alternate). */
	@SquirrelJMEVendorApi
	MVS_ALTERNATIVE(15),
	
	/** BeOS. */
	@SquirrelJMEVendorApi
	BEOS(16),
	
	/** Tandem. */
	@SquirrelJMEVendorApi
	TANDEM(17),
	
	/** OS/400. */
	@SquirrelJMEVendorApi
	OS_400(18),
	
	/** Mac OS X. */
	@SquirrelJMEVendorApi
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
	@SquirrelJMEVendorApi
	public final int identifier()
	{
		return this.id;
	}
}

