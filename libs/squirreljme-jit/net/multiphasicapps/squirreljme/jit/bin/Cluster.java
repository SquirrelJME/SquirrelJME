// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.bin;

/**
 * This represents a group of resources which are referenced by classes, since
 * {@link Class#getResourceAsStream(String)} for Java ME requires that classes
 * only access resources from within their own JAR. This makes it so that
 * behavior is duplicated as intended.
 *
 * @since 2017/06/17
 */
public class Cluster
{
}

