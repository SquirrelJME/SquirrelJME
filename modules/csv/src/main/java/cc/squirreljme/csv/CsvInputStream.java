// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.csv;

import java.io.Closeable;
import java.io.IOException;

/**
 * Interface for reading from a CSV file via an input stream.
 *
 * @since 2023/09/12
 */
public interface CsvInputStream
	extends Closeable
{
	/**
	 * Returns the next line.
	 *
	 * @param __line The next line read, written to.
	 * @return Will be {@code false} if EOF is reached.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/09/12
	 */
	boolean next(StringBuilder __line)
		throws IOException, NullPointerException;
}
