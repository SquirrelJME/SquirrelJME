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
import cc.squirreljme.driver.nio.java.shelf.JavaPathBracket;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.vm.springcoat.brackets.JavaPathObject;
import cc.squirreljme.vm.springcoat.exceptions.SpringMLECallError;
import java.nio.file.FileSystems;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;

/**
 * Java native I/O Shelf which provides {@link JavaNioShelf}.
 *
 * @since 2023/08/20
 */
public enum MLEJavaNioShelf
	implements MLEFunction
{
	/** {@link JavaNioShelf#fsPath(String, int[])}. */
	FS_PATH("fsPath:(Ljava/lang/String;[I)" +
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
			int[] failingSegment =
				__thread.asNativeObject(int[].class, __args[1]);
			
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
					failingSegment[0] = __e.getIndex();
				
				throw new SpringMLECallError("Invalid path");
			}
		}
	},
	
	/** {@link JavaNioShelf#fsReadAttributes(Path, boolean)}. */
	FS_READ_ATTRIBUTES("fsReadAttributes:(Ljava/nio/file/Path;Z)" +
		"Lcc/squirreljme/driver/nio/java/shelf/JavaFileAttributesBracket;")
	{
		/**
		 * {@inheritDoc}
		 * @since 2023/08/21
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			throw Debugging.todo();
		}
	},
	
	/** {@link JavaNioShelf#fsSeparator()}. */
	FS_SEPARATOR("fsSeparator:()Ljava/lang/String;")
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
	
	/** {@link JavaNioShelf#pathName(JavaPathBracket, int)}. */
	PATH_NAME("pathName:(Lcc/squirreljme/driver/nio/java/shelf/" +
		"JavaPathBracket;I)" +
		"Lcc/squirreljme/driver/nio/java/shelf/JavaPathBracket;")
	{
		/**
		 * {@inheritDoc}
		 * @since 2023/08/20
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			try
			{
				Path result = MLEJavaNioShelf.__pathObject(__args[0]).path
					.getName((Integer)__args[1]);
				
				if (result == null)
					return SpringNullObject.NULL;
				return new JavaPathObject(__thread.machine, result);
			}
			catch (IllegalArgumentException|IndexOutOfBoundsException|
				   NullPointerException|ClassCastException __e)
			{
				throw new SpringMLECallError(__e);
			}
		}
	},
	
	/** {@link JavaNioShelf#pathNameCount(JavaPathBracket)}. */
	PATH_NAME_COUNT("pathNameCount:" +
		"(Lcc/squirreljme/driver/nio/java/shelf/JavaPathBracket;)I")
	{
		/**
		 * {@inheritDoc}
		 * @since 2023/08/20
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			return MLEJavaNioShelf.__pathObject(__args[0])
				.path.getNameCount();
		}
	},
	
	/** {@link JavaNioShelf#pathRoot(JavaPathBracket)}. */
	PATH_ROOT("pathRoot:(Lcc/squirreljme/driver/nio/java/shelf/" +
		"JavaPathBracket;)" +
		"Lcc/squirreljme/driver/nio/java/shelf/JavaPathBracket;")
	{
		/**
		 * {@inheritDoc}
		 * @since 2023/08/20
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			JavaPathObject path = MLEJavaNioShelf.__pathObject(__args[0]);
			
			Path root = path.path.getRoot();
			if (root == null)
				return SpringNullObject.NULL;
			return new JavaPathObject(__thread.machine, root);
		}
	},
	
	/** {@link JavaNioShelf#pathString(JavaPathBracket)}. */
	PATH_STRING("pathString:(Lcc/squirreljme/driver/nio/java/shelf/" +
		"JavaPathBracket;)Ljava/lang/String;")
	{
		/**
		 * {@inheritDoc}
		 * @since 2023/08/21
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			return __thread.asVMObject(MLEJavaNioShelf.__pathObject(__args[0])
				.path.toString());
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
	/**
	 * Checks if this is a {@link JavaPathObject}.
	 * 
	 * @param __object The object to check.
	 * @return As a path if this is one.
	 * @throws SpringMLECallError If this is not a path.
	 * @since 2023/08/20
	 */
	static JavaPathObject __pathObject(Object __object)
		throws SpringMLECallError
	{
		if (!(__object instanceof JavaPathObject))
			throw new SpringMLECallError("Not a JavaPathObject.");
		
		return (JavaPathObject)__object; 
	}
}
