// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.ssjit;

/**
 * This interface is used as the output of the {@link SSJIT}, an implementation
 * handles anything .
 *
 * Note that the format is still strictly linear, although specific output
 * variants might not treat the output as such.
 *
 * It must be possible for the same output to be used across many different
 * instances of {@link SSJIT}. In this case, rather than a standalone class,
 * there is instead a large group of classes which may share data.
 *
 * @since 2016/07/02
 */
public interface SSJITOutput
{
}

