// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.kernel.execfinder;

/**
 * This interface describes executable finders, which are used to locate and
 * provide access to executable data. Since executables may be in many forms
 * such as being in the ROM of a cart, on the filesystem, or some other
 * virtual means (such as downloading over the network).
 *
 * The executables which are available are dependent on the finder itself.
 *
 * The service loader is used to locate services for execution.
 *
 * @since 2016/05/16
 */
public interface ExecutableFinder
{
}

