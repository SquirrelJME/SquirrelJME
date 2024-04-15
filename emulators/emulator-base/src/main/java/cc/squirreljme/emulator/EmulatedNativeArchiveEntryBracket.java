// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.emulator;

import cc.squirreljme.jvm.mle.brackets.NativeArchiveEntryBracket;
import cc.squirreljme.jvm.mle.exceptions.MLECallError;
import java.io.InputStream;

/**
 * Base class for emulated archive entries.
 *
 * @since 2024/03/05
 */
public abstract class EmulatedNativeArchiveEntryBracket
	implements NativeArchiveEntryBracket
{
	/**
	 * Is this a directory?
	 *
	 * @return If this is a directory.
	 * @since 2024/03/05
	 */
	public abstract boolean isDirectory();
	
	/**
	 * Opens the given entry.
	 *
	 * @return The stream to the entry data.
	 * @throws MLECallError If the entry cannot be opened.
	 * @since 2024/03/05
	 */
	public abstract InputStream open()
		throws MLECallError;
	
	/**
	 * Returns the uncompressed size of the entry.
	 *
	 * @return The uncompressed entry size.
	 * @since 2024/03/05
	 */
	public abstract long uncompressedSize();
}
