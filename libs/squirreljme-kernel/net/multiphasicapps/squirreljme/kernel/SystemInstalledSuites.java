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
 * This is the base class for access to suites which are installed on the
 * system. System suites cannot be changed because they are built into the
 * executable and are as such read-only. When a class is requested, the system
 * suites have priority. Each class still however only sees its own
 * resources.
 *
 * @since 2016/12/16
 */
public abstract class SystemInstalledSuites
{
}

