// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.multivm;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.gradle.api.Task;

/**
 * Base interface for the execution of library based functions.
 *
 * @since 2021/05/16
 */
@FunctionalInterface
public interface VMLibraryExecuteFunction
{
	/**
	 * Performs the function as required.
	 * 
	 * @param __task The task used.
	 * @param __isTest Is this a test?
	 * @param __in The input stream.
	 * @param __out The output stream.
	 * @throws IOException Any read/write errors.
	 * @since 2021/05/16
	 */
	void function(VMBaseTask __task, boolean __isTest, InputStream __in,
		OutputStream __out)
		throws IOException;
}
