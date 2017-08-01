// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.java;

/**
 * This class represents a basic block which exists to wrap a small portion of
 * the byte code to fix it within a set of requirements. Basic blocks start at
 * addresses which are a target of a jump (normal or exceptional), this means
 * that it becomes known where all non-normal execution flow instructions
 * land. This removes the need to have an overly complex execution graph.
 * Basic blocks end where entry points exist or the method terminates via
 * throwing an exception or returning.
 *
 * @see BasicBlocks
 * @since 2017/08/01
 */
public class BasicBlock
{
}

