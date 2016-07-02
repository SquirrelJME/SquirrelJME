// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit;

import java.util.ServiceLoader;

/**
 * This is used to modify the the JIT for a given operating system.
 *
 * If a modifier for a given operating system and architecture (with variant)
 * exists then it will be given the JIT after it is constructed.
 *
 * This is used with the service loader.
 *
 * @since 2016/07/02
 */
public abstract class JITOSModifier
{
}

