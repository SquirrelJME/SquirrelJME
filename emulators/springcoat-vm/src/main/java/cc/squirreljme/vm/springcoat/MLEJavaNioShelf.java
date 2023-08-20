// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.springcoat;

import cc.squirreljme.driver.nio.java.shelf.JavaNioShelf;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.vm.springcoat.brackets.JavaPathObject;
import cc.squirreljme.vm.springcoat.exceptions.SpringMLECallError;
import java.nio.file.FileSystems;
import java.nio.file.InvalidPathException;

/**
 * Java native I/O Shelf which provides {@link JavaNioShelf}.
 *
 * @since 2023/08/20
 */
public enum MLEJavaNioShelf
	implements MLEFunction
{
	/** {@link JavaNioShelf#getPath(String, int[])}. */
	GET_PATH("getPath:(Ljava/lang/String;[I)" +
		"Lcc/squirreljme/driver/nio/java/shelf/JavaPathBracket;")
	{
		/**
		 * {@inheritDoc}
		 * @since 2023/08/20
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			String path = __thread.asNativeObject(String.class, __args[0]);
			SpringArrayObjectInteger failingSegment =
				__thread.asNativeObject(SpringArrayObjectInteger.class,
					__args[1]);
			
			// This must be passed
			if (path == null)
				throw new SpringMLECallError("No path");
			
			// Get path and wrap it in a bracket
			try
			{
				return new JavaPathObject(__thread.machine,
					FileSystems.getDefault().getPath(path));
			}
			catch (InvalidPathException __e)
			{
				// Set failing index, if known
				if (failingSegment != null && failingSegment.length > 0)
					failingSegment.array()[0] = __e.getIndex();
				
				throw new SpringMLECallError("Invalid path");
			}
		}
	},
	
	/** {@link JavaNioShelf#getSeparator()}. */
	GET_SEPARATOR("getSeparator:()Ljava/lang/String;")
	{
		/**
		 * {@inheritDoc}
		 * @since 2023/08/20
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			return __thread.asVMObject(
				FileSystems.getDefault().getSeparator());
		}
	},
	
	/* End. */
	;
	
	/** The dispatch key. */
	protected final String key;
	
	/**
	 * Initializes the dispatcher info.
	 *
	 * @param __key The key.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/08/20
	 */
	MLEJavaNioShelf(String __key)
		throws NullPointerException
	{
		if (__key == null)
			throw new NullPointerException("NARG");
		
		this.key = __key;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/08/20
	 */
	@Override
	public String key()
	{
		return this.key;
	}
}
