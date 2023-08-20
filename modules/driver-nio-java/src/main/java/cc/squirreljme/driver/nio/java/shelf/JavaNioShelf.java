// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.driver.nio.java.shelf;

import cc.squirreljme.jvm.mle.annotation.SquirrelJMENativeShelf;
import cc.squirreljme.jvm.mle.exceptions.MLECallError;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import org.jetbrains.annotations.CheckReturnValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Native shelf interface for Java File access.
 *
 * @since 2023/08/20
 */
@SquirrelJMEVendorApi
@SquirrelJMENativeShelf
public final class JavaNioShelf
{
	/**
	 * Not used.
	 * 
	 * @since 2023/08/20
	 */
	private JavaNioShelf()
	{
	}
	
	/**
	 * Obtains the bracket for the given path.
	 *
	 * @param __path The path to obtain.
	 * @param __failSegment The failing segment, written to if it is known.
	 * @return The path.
	 * @throws MLECallError On null arguments or if the path is not valid.
	 * @since 2023/08/20
	 */
	@SquirrelJMEVendorApi
	@CheckReturnValue
	public static native JavaPathBracket getPath(@NotNull String __path,
		@Nullable int[] __failSegment)
		throws MLECallError;
	
	/**
	 * Returns the root of the given path.
	 *
	 * @param __path The path's root.
	 * @return The root or {@code null} if there is none.
	 * @throws MLECallError On null arguments.
	 * @since 2023/08/20
	 */
	@SquirrelJMEVendorApi
	@CheckReturnValue
	public static native JavaPathBracket getRoot(JavaPathBracket __path)
		throws MLECallError;
	
	/**
	 * The separator for files.
	 *
	 * @return The separator used for files.
	 * @since 2023/08/20
	 */
	@SquirrelJMEVendorApi
	@CheckReturnValue
	public static native String getSeparator();
}
