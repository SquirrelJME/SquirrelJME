// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.kernel;

/**
 * This interface provides access to the class path that the kernel uses
 * itself and for processes. This essentially allows lookup of classes that
 * are built into SquirrelJME itself.
 *
 * This interface also handles APIs which are defined by the system and
 * included.
 *
 * Implementations of this interface must also handle primitive types and
 * array types.
 *
 * @since 2016/12/14
 */
public interface KernelSystemClassPath
{
}

