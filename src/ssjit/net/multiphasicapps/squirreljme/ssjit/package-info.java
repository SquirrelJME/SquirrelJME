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
 * This package contains the Single Stage JIT which is meant to reduce the
 * computational and memory requirements needed for dynamic code generation.
 * A single pass is the fastest and uses the least amount of memory and any
 * binaries which are generated are written in an ad-hoc manner. This
 * sacrifices optimization of the input byte code in some areas so that a JIT
 * may be used on the most limited of systems.
 *
 * @since 2016/06/24
 */

package net.multiphasicapps.squirreljme.ssjit;

