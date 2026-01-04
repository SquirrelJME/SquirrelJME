// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc.full.attrib;

import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * Extra file attributes supported by SquirrelJME.
 *
 * @since 2025/12/30
 */
@SquirrelJMEVendorApi
public interface ExtraFileAttributes
	extends BasicFileAttributes
{
	/**
	 * Returns the POXIX group ID.
	 *
	 * @return The POSIX group ID.
	 * @since 2025/12/30
	 */
	@SquirrelJMEVendorApi
	int getPosixGroupId();
	
	/**
	 * Returns the POSIX user ID.
	 *
	 * @return The POSIX user ID.
	 * @since 2025/12/30
	 */
	@SquirrelJMEVendorApi
	int getPosixUserId();
	
	/**
	 * Is this archivable in DOS?
	 *
	 * @return If this is archivable in DOS.
	 * @since 2025/12/30
	 */
	@SquirrelJMEVendorApi
	boolean isDosArchivable();
	
	/**
	 * Is this hidden in DOS?
	 *
	 * @return If this is hidden.
	 * @since 2025/12/30
	 */
	@SquirrelJMEVendorApi
	boolean isDosHidden();
	
	/**
	 * Is this read-only in DOS?
	 *
	 * @return If this is read-only in DOS.
	 * @since 2025/12/30
	 */
	@SquirrelJMEVendorApi
	boolean isDosReadOnly();
	
	/**
	 * Is this a system file in DOS?
	 *
	 * @return If this is a system file in DOS.
	 * @since 2025/12/30
	 */
	@SquirrelJMEVendorApi
	boolean isDosSystem();
	
	/**
	 * Is this group executable in POSIX?
	 *
	 * @return If this is group executable in POSIX.
	 * @since 2025/12/30
	 */
	@SquirrelJMEVendorApi
	boolean isPosixGroupExecute();
	
	/**
	 * Is this group readable in POSIX?
	 *
	 * @return If this is group readable in POSIX.
	 * @since 2025/12/30
	 */
	@SquirrelJMEVendorApi
	boolean isPosixGroupRead();
	
	/**
	 * Is this set group ID in POSIX?
	 *
	 * @return If this is set group ID in POSIX.
	 * @since 2025/12/30
	 */
	@SquirrelJMEVendorApi
	boolean isPosixGroupSUID();
	
	/**
	 * Is this group writable in POSIX?
	 *
	 * @return If this is group writable in POSIX.
	 * @since 2025/12/30
	 */
	@SquirrelJMEVendorApi
	boolean isPosixGroupWrite();
	
	/**
	 * Is this other executable in POSIX?
	 *
	 * @return If this is other executable in POSIX.
	 * @since 2025/12/30
	 */
	@SquirrelJMEVendorApi
	boolean isPosixOtherExecute();
	
	/**
	 * Is this other readable in POSIX?
	 *
	 * @return If this is other readable in POSIX.
	 * @since 2025/12/30
	 */
	@SquirrelJMEVendorApi
	boolean isPosixOtherRead();
	
	/**
	 * Is this other writable in POSIX?
	 *
	 * @return If this is other writable in POSIX.
	 * @since 2025/12/30
	 */
	@SquirrelJMEVendorApi
	boolean isPosixOtherWrite();
	
	/**
	 * Is the restricted deletion (sticky) bit set for this file?
	 *
	 * @return If the restricted deletion (sticky) bit is set.
	 * @since 2025/12/30
	 */
	@SquirrelJMEVendorApi
	boolean isPosixRestrictedDelete();
	
	/**
	 * Is this user executable in POSIX?
	 *
	 * @return If this is user executable in POSIX.
	 * @since 2025/12/30
	 */
	@SquirrelJMEVendorApi
	boolean isPosixUserExecute();
	
	/**
	 * Is this user readable in POSIX?
	 *
	 * @return If this is user readable in POSIX.
	 * @since 2025/12/30
	 */
	@SquirrelJMEVendorApi
	boolean isPosixUserRead();
	
	/**
	 * Is this set user ID in POSIX?
	 *
	 * @return If this is set user ID in POSIX.
	 * @since 2025/12/30
	 */
	@SquirrelJMEVendorApi
	boolean isPosixUserSUID();
	
	/**
	 * Is this user writable in POSIX?
	 *
	 * @return If this is user writable in POSIX.
	 * @since 2025/12/30
	 */
	@SquirrelJMEVendorApi
	boolean isPosixUserWrite();
}
