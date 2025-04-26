// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.emulator;

import cc.squirreljme.jvm.mle.brackets.BucketBracket;
import cc.squirreljme.jvm.mle.exceptions.MLECallError;
import java.nio.file.Path;

/**
 * Emulated buckets.
 *
 * @since 2025/04/19
 */
public final class EmulatedBucketBracket
	implements BucketBracket
{
	/** The root where data is stored. */
	protected final Path root;
	
	/**
	 * Initializes the bucket. 
	 *
	 * @param __root The bucket data root.
	 * @throws NullPointerException On null arguments.
	 * @since 2025/04/19
	 */
	public EmulatedBucketBracket(Path __root)
		throws NullPointerException
	{
		if (__root == null)
			throw new NullPointerException("NARG");
		
		this.root = __root;
	}
	
	/**
	 * Resolves the given file.
	 *
	 * @param __fileName The file to resolve.
	 * @return The resultant path.
	 * @throws MLECallError If the file name is not valid.
	 * @since 2025/04/19
	 */
	Path __resolve(String __fileName)
		throws MLECallError
	{
		if (__fileName == null)
			throw new MLECallError("Null arguments.");
		
		if (__fileName.indexOf('/') >= 0 || __fileName.indexOf('\\') >= 0 ||
			__fileName.equals(".") || __fileName.equals(".."))
			throw new MLECallError("Filename is invalid: " + __fileName);
		
		return this.root.resolve(__fileName);
	}
}
