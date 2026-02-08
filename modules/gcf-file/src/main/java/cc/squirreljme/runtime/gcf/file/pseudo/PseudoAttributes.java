// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.gcf.file.pseudo;

import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import cc.squirreljme.runtime.cldc.full.attrib.AbstractFileAttributes;
import cc.squirreljme.runtime.cldc.full.attrib.StaticFileAttributes;

/**
 * Attributes used by pseudo end-points.
 *
 * @since 2026/01/03
 */
@SquirrelJMEVendorApi
public interface PseudoAttributes
{
	/** Attributes for any directory. */
	@SquirrelJMEVendorApi
	StaticFileAttributes DIRECTORY =
		new StaticFileAttributes(
			AbstractFileAttributes.IS_DIRECTORY |
			AbstractFileAttributes.IS_DOS_READ_ONLY |
			AbstractFileAttributes.IS_POSIX_USER_READ |
			AbstractFileAttributes.IS_POSIX_USER_EXECUTE |
			AbstractFileAttributes.IS_POSIX_GROUP_READ |
			AbstractFileAttributes.IS_POSIX_GROUP_EXECUTE |
			AbstractFileAttributes.IS_POSIX_OTHER_READ |
			AbstractFileAttributes.IS_POSIX_OTHER_EXECUTE, 0);
	
	/** Attributes for any file. */
	@SquirrelJMEVendorApi
	StaticFileAttributes FILE =
		new StaticFileAttributes(
			AbstractFileAttributes.IS_DOS_READ_ONLY |
			AbstractFileAttributes.IS_POSIX_USER_READ |
			AbstractFileAttributes.IS_POSIX_GROUP_READ |
			AbstractFileAttributes.IS_POSIX_OTHER_READ, 0);
}
