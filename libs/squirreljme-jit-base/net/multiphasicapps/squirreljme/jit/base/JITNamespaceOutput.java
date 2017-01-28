// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.base;

/**
 * This is used as a base interface which signifies that something outputs to
 * either a single namespace binary or a single executable which contains
 * multiple namespaces. The desired output type depends completely on the JIT
 * and the target.
 *
 * @since 2016/09/28
 */
@Deprecated
public interface JITNamespaceOutput
{
}

