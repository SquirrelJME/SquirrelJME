// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

/**
 * This package provides internal virtual machine logic. When the compiler
 * detects that one of these methods are performed it replaces the method call
 * with special logic to perform the needed operation.
 *
 * All of the classes here are specific to this virtual machine and are not
 * portable. Calling magical methods will always result in an exception.
 *
 * @since 2016/04/12
 */

@Deprecated
package net.multiphasicapps.squirreljme.magic;

