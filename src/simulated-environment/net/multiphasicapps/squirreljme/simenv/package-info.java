// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

/**
 * This package contains the simulated environment.
 *
 * The environment can only launch a single program at a time (to remain
 * simple). There is a speed penalty for the environment since classes which
 * are read must be rewritten before they are initialized. The simulated
 * environment is to be as simple as possible to allow for the main library to
 * be implemented without actually having a working JIT yet.
 *
 * @since 2016/08/13
 */

package net.multiphasicapps.squirreljme.simenv;

