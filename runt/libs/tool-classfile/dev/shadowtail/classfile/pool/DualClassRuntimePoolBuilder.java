// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package dev.shadowtail.classfile.pool;

/**
 * This is used as a builder for both class and run-time pools.
 *
 * @since 2019/07/17
 */
public final class DualClassRuntimePoolBuilder
{
	/** The class pool. */
	protected final BasicPoolBuilder classpool =
		new BasicPoolBuilder();
	
	/** The run-time pool. */
	protected final BasicPoolBuilder runpool =
		new BasicPoolBuilder();
}

