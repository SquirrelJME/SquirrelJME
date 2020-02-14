// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.io;

import java.io.InputStream;
import java.io.IOException;

/**
 * This interface is used to describe a compression stream which is used as
 * input.
 *
 * @since 2017/08/22
 */
public abstract class DecompressionInputStream
	extends InputStream
	implements CompressionStream
{
	/**
	 * If the decompression algorithm has a means or flag to indicate that
	 * there is no more compressed data and that the end of stream has been
	 * reached then this should return {@code true}.
	 *
	 * If it is unknown whether it can be detected or not, then {@code false}
	 * should be returned.
	 *
	 * @return {@code true} if this algorithm detects the end of file before
	 * the end of the actual input source, otherwise if only the end of the
	 * file causes EOF to be returned then this returns {@code false}. If it
	 * is not known then {@code false} must be returned.
	 * @since 2017/08/22
	 */
	public abstract boolean detectsEOF();
}

