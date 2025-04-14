// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.emulator;

import cc.squirreljme.jvm.mle.brackets.NativeArchiveBracket;
import cc.squirreljme.jvm.mle.brackets.NativeArchiveEntryBracket;
import cc.squirreljme.jvm.mle.exceptions.MLECallError;
import java.io.Closeable;

/**
 * Not Described.
 *
 * @since 2024/03/05
 */
public abstract class EmulatedNativeArchiveBracket
	implements Closeable, NativeArchiveBracket
{
	/**
	 * Returns the given entry.
	 *
	 * @param __name The name of the entry to get.
	 * @return The resultant entry or {@code null} if not found.
	 * @throws MLECallError If the archive is not valid.
	 * @since 2024/03/05
	 */
	protected abstract EmulatedNativeArchiveEntryBracket entry(String __name)
		throws MLECallError;
}
