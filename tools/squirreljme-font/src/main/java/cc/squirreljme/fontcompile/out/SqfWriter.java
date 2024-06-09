// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.fontcompile.out;

import cc.squirreljme.fontcompile.out.struct.SqfFontStruct;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.io.Closeable;
import java.io.IOException;

/**
 * Base interface for SQF writers.
 *
 * @since 2024/06/04
 */
public interface SqfWriter
	extends Closeable
{
	/**
	 * Writes the SQF output.
	 *
	 * @param __struct The struct to write.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/06/04
	 */
	void write(SqfFontStruct __struct)
		throws IOException, NullPointerException;
	
	/**
	 * Writes the SQF output.
	 *
	 * @param __structs The structs to write.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/06/09
	 */
	void write(SqfFontStruct... __structs)
		throws IOException, NullPointerException;
}
