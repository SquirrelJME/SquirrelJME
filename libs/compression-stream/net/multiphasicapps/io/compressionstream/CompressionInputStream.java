// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.io.compressionstream;

import java.io.InputStream;
import java.io.IOException;

/**
 * This interface is used to describe a compression stream which is used as
 * input.
 *
 * @since 2017/08/22
 */
public abstract class CompressionInputStream
	extends InputStream
	implements CompressionStream
{
}

