// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

/**
 * This package contains the user-space implements of interfaces, the classes
 * here are always the same implementation and will for the most part will
 * always have the same set of common code regardless of the SquirrelJME
 * implementation in the kernel.
 *
 * This purpose of this class is so that classes which need to rely on
 * system interfaces do not need to be rewritten for every single port of
 * SquirrelJME for each and every system.
 *
 * The classes in this package can rely on classes which implement native
 * behavior but the classes here cannot contain such native code.
 *
 * @since 2017/12/07
 */

package net.multiphasicapps.squirreljme.runtime.cldc.user;

