// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

import java.io.InputStream;
import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashSet;
import java.util.jar.Manifest;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.Set;
import java.util.TreeSet;

/**
 * This is the bootstrap which builds the bootstrap system and then launches
 * it. It bridges the bootstrap compiler and launcher system to the host
 * Jave SE VM.
 *
 * @since 2016/09/18
 */
public class Bootstrap
	implements Runnable
{
	/** The project which contains the bootstrap to build. */
	public static final String BOOTSTRAP_PROJECT =
		"bootstrap";
	
	/** Project root directory. */
	public static final Path PROJECT_ROOT;
	
	/** Source root. */
	public static final Path SOURCE_ROOT;
	
	/** Contrib root. */
	public static final Path CONTRIB_ROOT;
	
	/** Input program arguments. */
	private final String[] _args;
	
	/**
	 * Initializes some details.
	 *
	 * @since 2016/09/18
	 */
	static
	{
		// Get the root and make sure it exists
		PROJECT_ROOT = Paths.get(Objects.<String>requireNonNull(
			System.getProperty("project.root"),
			"The system property `project.root` was not specified."));
		
		// Resolve other paths
		SOURCE_ROOT = PROJECT_ROOT.resolve("src");
		CONTRIB_ROOT = PROJECT_ROOT.resolve("contrib");
	}
	
	/**
	 * Bootstrap starter instance.
	 *
	 * @param __args Program arguments.
	 * @since 2016/09/18
	 */
	public Bootstrap(String... __args)
	{
		// Force
		if (__args == null)
			__args = new String[0];
		
		// Set
		this._args = __args.clone();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/18
	 */
	@Override
	public void run()
	{
		// Could fail
		try
		{
			// Locate the directories that contains all of the source code
			// along with dependencies for the bootstrap
			Path[] sources = __locateSources();
			System.err.printf("DEBUG -- Located: %s%n",
				Arrays.asList(sources));
		
			throw new Error("TODO");
		}
		
		// Failed
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Locates all source directories which are associated with the bootstrap
	 * and all dependencies.
	 *
	 * @return An array containing the set of source directories that are
	 * considered part of the bootstrap.
	 * @throws IOException On read errors.
	 * @since 2016/09/18
	 */
	private Path[] __locateSources()
		throws IOException
	{
		// Setup return value
		Set<Path> rv = new TreeSet<>();
		
		// Queue dependencies and parse them
		Set<String> did = new HashSet<>();
		Deque<String> q = new ArrayDeque<>();
		q.push(BOOTSTRAP_PROJECT);
		while (!q.isEmpty())
		{
			// Grab
			String p = q.remove();
			
			// If it was done already or is ignored, do not handle it
			if (did.contains(p) || __ignoreProject(p))
				continue;
			
			// The source directory is included in compilation
			Path srcdir = SOURCE_ROOT.resolve(p);
			rv.add(srcdir);
			
			// Obtain the path to the manifest
			Path manpath = srcdir.resolve("META-INF").
				resolve("MANIFEST.MF");
			
			// Try to open it
			try (InputStream is = Channels.newInputStream(
				FileChannel.open(manpath, StandardOpenOption.READ)))
			{
				// Decode the manifest
				Manifest man = new Manifest(is);
				
				// Get the dependencies for this, if they exist, if they do
				// then add them to the queue for handling
				String deps = man.getMainAttributes().
					getValue("x-squirreljme-depends");
				if (deps != null)
					for (String d : deps.split(Pattern.quote(" ")))
						q.push(d);
			}
		}
		
		// Return them
		return rv.<Path>toArray(new Path[rv.size()]);
	}
	
	/**
	 * Main entry point for the bootstrap bootstrapper.
	 *
	 * @param __args Program arguments.
	 * @since 2016/09/18
	 */
	public static void main(String... __args)
	{
		// Create and run
		new Bootstrap(__args).run();
	}
	
	/**
	 * Checks if the specified project name is to be ignored so that it is
	 * not included for compilation of the bootstrap.
	 *
	 * @param __s The project to check if it is ignored.
	 * @return {@code true} if the project is to be ignored.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/18
	 */
	private static boolean __ignoreProject(String __s)
		throws NullPointerException
	{
		// Check
		if (__s == null)
			throw new NullPointerException("NARG");
		
		// Depends
		switch (__s)
		{
				// Uses the host Java SE library, so these do not need to
				// be compiled at all
			case "cldc-compact":
			case "cldc-full":
				return true;
			
				// Do not ignore
			default:
				return false;
		}
	}
	
	/**
	 * This acts as a proxy for the bootstrap system so it has access to the
	 * system's Java compiler.
	 *
	 * @since 2016/09/18
	 */
	private class __CompilerProxy__
		implements InvocationHandler
	{
		/**
		 * {@inheritDoc}
		 * @since 2016/09/18
		 */
		@Override
		public Object invoke(Object __p, Method __m, Object[] __args)
			throws Throwable
		{
			throw new Error("TODO");
		}
	}
	
	/**
	 * This acts as a proxy for the bootstrap launcher system so that JARs
	 * can be loaded and execute natively.
	 *
	 * @since 2016/09/18
	 */
	private class __LauncherProxy__
		implements InvocationHandler
	{
		/**
		 * {@inheritDoc}
		 * @since 2016/09/18
		 */
		@Override
		public Object invoke(Object __p, Method __m, Object[] __args)
			throws Throwable
		{
			throw new Error("TODO");
		}
	}
}

