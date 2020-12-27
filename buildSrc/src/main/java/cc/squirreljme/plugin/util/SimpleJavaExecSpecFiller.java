// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.util;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.function.Supplier;

/**
 * This is a simple Java execution specification filler which provides the
 * minimal functionality.
 *
 * @since 2020/12/26
 */
public class SimpleJavaExecSpecFiller
	implements JavaExecSpecFiller
{
	/** Program arguments. */
	private final List<String> _args =
		new ArrayList<>();
	
	/** System properties. */
	private final Map<String, String> _sysProps =
		new LinkedHashMap<>();
	
	/** Potential classPath objects. */
	private final List<Object> _classPath =
		new ArrayList<>();
	
	/** The main class to use. */
	private String _mainClass;
	
	/**
	 * {@inheritDoc}
	 * @since 2020/12/26
	 */
	@Override
	public void classpath(Collection<Object> __classPath)
	{
		List<Object> classPath = this._classPath;
		
		classPath.clear();
		classPath.addAll(__classPath);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/12/26
	 */
	@Override
	public Iterable<String> getCommandLine()
	{
		// The final arguments used
		List<String> result = new ArrayList<>();
		
		// Find the Java executable
		result.add(SimpleJavaExecSpecFiller.__findJavaExe().toString());
		
		// Define the classpath
		StringBuilder classPath = new StringBuilder();
		String pathSep = File.pathSeparator;
		for (Path path : SimpleJavaExecSpecFiller.__resolve(this._classPath))
		{
			// We need the path separator between libraries
			if (classPath.length() > 0)
				classPath.append(pathSep);
			
			// Add the path
			classPath.append(path);
		}
		
		// Was a classpath defined?
		if (classPath.length() > 0)
		{
			result.add("-classpath");
			result.add(classPath.toString());
		}
		
		// Define any system properties
		for (Map.Entry<String, String> sysProp : this._sysProps.entrySet())
			result.add(String.format("-D%s=%s",
				sysProp.getKey(), sysProp.getValue()));
		
		// Main class to run
		result.add(this._mainClass);
		
		// Any arguments to pass to the VM
		result.addAll(this._args);
		
		return result;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/12/26
	 */
	@Override
	public void setMain(String __mainClass)
	{
		this._mainClass = __mainClass;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/12/26
	 */
	@Override
	public void setArgs(Collection<String> __asList)
	{
		List<String> args = this._args;
		
		args.clear();
		args.addAll(__asList);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/12/26
	 */
	@Override
	public void systemProperties(Map<String, String> __sysProps)
	{
		Map<String, String> sysProps = this._sysProps;
		
		sysProps.clear();
		sysProps.putAll(__sysProps);
	}
	
	/**
	 * Finds the Java executable.
	 * 
	 * @return The Java executable.
	 * @since 2020/12/27
	 */
	private static Path __findJavaExe()
	{
		return Paths.get("java");
	}
	
	/**
	 * Resolves path elements as needed to determine where a file is.
	 * 
	 * @param __in The input to resolve.
	 * @return The path of the given object.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/12/27
	 */
	private static Iterable<Path> __resolve(Object __in)
		throws NullPointerException
	{
		if (__in == null)
			throw new NullPointerException("NARG");
		
		// Direct file paths
		if (__in instanceof Path)
			return Collections.singleton((Path)__in);
		else if (__in instanceof File)
			return Collections.singleton(((File)__in).toPath());
		
		// A produced value
		else if (__in instanceof Callable)
			try
			{
				return SimpleJavaExecSpecFiller.__resolve(
					((Callable<?>)__in).call());
			}
			catch (Exception e)
			{
				if (e instanceof RuntimeException)
					throw (RuntimeException)e;
				
				throw new RuntimeException("Could not run Callable.", e);
			}
		
		// A supplied value
		else if (__in instanceof Supplier)
			return SimpleJavaExecSpecFiller.__resolve(
					((Supplier<?>)__in).get());
		
		// An iterable sequence
		else if (__in instanceof Iterable)
		{
			List<Path> result = new ArrayList<>();
			
			// Process each one
			for (Object obj : (Iterable<?>)__in)
				for (Path sub : SimpleJavaExecSpecFiller.__resolve(obj))
					result.add(sub);
			
			return result;
		}
		
		// Unknown
		else
			throw new RuntimeException(String.format(
				"Unknown input path type %s", __in.getClass()));			
	}
}
