// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.mle.brackets;

import cc.squirreljme.jvm.mle.annotation.GhostObject;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;

/**
 * Represents a socket connection which uses datagrams and not a singular
 * stream of bytes for communication.
 *
 * @since 2026/05/17
 */
@SquirrelJMEVendorApi
@GhostObject
public interface DatagramBracket
{
}
