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
 * This package contains an implementation of the garbage collector and
 * provides a standard default. This garbage collector is stop the world, and
 * uses the mark and sweep algorithm to find objects which are not referenced.
 *
 * @since 2016/06/04
 */

package net.multiphasicapps.squirreljme.gc.std;

