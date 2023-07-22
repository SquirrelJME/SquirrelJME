// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.launch;

/**
 * This is a listener which specifies when a suite has been scanned in.
 * 
 * Implementations must be thread safe as this may use multiple threads
 * accordingly to speed up scanning on slower multi-CPU systems.
 *
 * @since 2020/12/29
 */
public interface SuiteScanListener
{
	/**
	 * Indicates that the given application was scanned.
	 * 
	 * @param __app The application that has been scanned.
	 * @param __dx The JAR index, used to indicate progress.
	 * @param __total The total number of JARs scanned.
	 * @since 2020/12/29
	 */
	void scanned(Application __app, int __dx, int __total);
}
