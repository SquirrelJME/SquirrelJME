// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.bin;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * This class represents a temporary binary which is used to contain the
 * compiled fragments which will eventually be placed within sections as the
 * output binary is built.
 *
 * @since 2017/08/24
 */
public class TemporaryBinary
{
	/** Fragments which exist within the binary. */
	private final Map<LinkingPoint, TemporaryFragment> _fragments =
		new LinkedHashMap<>();
}

