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
import java.nio.file.Path;
import org.jetbrains.annotations.CheckReturnValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

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
	@NotNull
	public static native JavaPathBracket fsPath(@NotNull String __path,
		@Nullable int[] __failSegment)
		throws MLECallError;
	
	/**
	 * Reads the filesystem attributes at the given path.
	 *
	 * @param __path The path to get the attributes from.
	 * @param __noFollow Should symlinks not be followed?
	 * @return The attributes or {@code null} if the file does not exist.
	 * @throws MLECallError On read errors.
	 * @since 2023/08/21
	 */
	@SquirrelJMEVendorApi
	@Nullable
	public static native JavaFileAttributesBracket fsReadAttributes(
		@NotNull JavaPathBracket __path, boolean __noFollow)
		throws MLECallError;
	
	/**
	 * The separator for files.
	 *
	 * @return The separator used for files.
	 * @since 2023/08/20
	 */
	@SquirrelJMEVendorApi
	@CheckReturnValue
	@NotNull
	public static native String fsSeparator();
	
	/**
	 * Gets the path index from the given path.
	 *
	 * @param __path The path to get the name from.
	 * @param __dx The path index.
	 * @return The path for the given index from the given path.
	 * @throws MLECallError On null arguments or the index is not valid.
	 * @since 2023/08/20
	 */
	@NotNull
	public static native JavaPathBracket pathName(
		@NotNull JavaPathBracket __path,
		@Range(from = 0, to = Integer.MAX_VALUE) int __dx)
		throws MLECallError;
	
	/**
	 * Returns the name count of the given path.
	 *
	 * @param __path The path to get the name count of.
	 * @return The number of names in the path.
	 * @throws MLECallError On null arguments.
	 * @since 2023/08/20
	 */
	@SquirrelJMEVendorApi
	@Range(from = 0, to = Integer.MAX_VALUE)
	public static native int pathNameCount(@NotNull JavaPathBracket __path)
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
	@Nullable
	public static native JavaPathBracket pathRoot(
		@NotNull JavaPathBracket __path)
		throws MLECallError;
	
	/**
	 * Returns the string representation of the path.
	 *
	 * @param __path The path to get the representation of.
	 * @return The string that represents the path.
	 * @throws MLECallError On null arguments.
	 * @since 2023/08/21
	 */
	@SquirrelJMEVendorApi
	@NotNull
	public static native String pathString(@NotNull JavaPathBracket __path)
		throws MLECallError;
}
