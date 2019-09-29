// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

import java.io.File;
import java.nio.file.Path;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.Callable;
import java.util.List;

/**
 * This class contains the ability to call a compiler from the bootstrap.
 *
 * For modern Java systems with a capable class library it will use the
 * internal class libraries.
 *
 * If one is not detected then it will call the host compiler by creating
 * processes as needed to perform the compilation.
 *
 * @since 2019/09/28
 */
public abstract class BootstrapCompiler
{
	/** Pre-cached compiler instance. */
	private static BootstrapCompiler _COMPILER;
	
	/**
	 * Performs the actual compilation step.
	 *
	 * @return {@code true} if compilation is a success.
	 * @since 2019/09/29
	 */
	public abstract boolean compile(Path __srcpath, Path __outpath,
		Iterable<Path> __classpath, Iterable<Path> __sources,
		Iterable<String> __args);
	
	/**
	 * Returns an instance of the compiler needed to build the basic
	 * SquirrelJME system.
	 *
	 * @throws UnsupportedOperationException If no compiler is supported.
	 * @since 2019/09/28
	 */
	public static final BootstrapCompiler getCompiler()
		throws UnsupportedOperationException
	{
		// Use pre-cached compiler
		BootstrapCompiler rv = BootstrapCompiler._COMPILER;
		if (rv != null)
			return rv;
		
		// Try using the standard tool compiler
		try
		{
			rv = StandardToolCompiler.getInstance();
		}
		
		// Is not supported, so try getting the one from the command line
		catch (UnsupportedOperationException e)
		{
			// Print the possible reason the built-in compiler is not
			// available
			e.printStackTrace();
			
			// Try getting command line compiler instead
			rv = CommandLineCompiler.getInstance();
		}
		
		// Cache and return
		BootstrapCompiler._COMPILER = rv;
		return rv;
	}
	
	/**
	 * This is a Java compiler which exists on the command line.
	 *
	 * @since 2019/09/28
	 */
	public static final class CommandLineCompiler
		extends BootstrapCompiler
	{
		/**
		 * {@inheritDoc}
		 * @since 2019/09/29
		 */
		@Override
		public boolean compile(Path __srcpath, Path __outpath,
			Iterable<Path> __classpath, Iterable<Path> __sources,
			Iterable<String> __args)
		{
			throw new Error("TODO");
		}
		
		/**
		 * Returns an instance of the compiler.
		 *
		 * @return The compiler instance.
		 * @throws UnsupportedOperationException If it is not a valid compiler.
		 * @since 2019/09/28
		 */
		public static final CommandLineCompiler getInstance()
			throws UnsupportedOperationException
		{
			throw new Error("TODO");
		}
	}
	
	/**
	 * This is a Java compiler which uses the standard tool system.
	 *
	 * This uses the following classes:
	 *  - {@code javax.tools.JavaCompiler}.
	 *  - {@code javax.tools.JavaFileManager}.
	 *  - {@code javax.tools.StandardJavaFileManager}.
	 *  - {@code javax.tools.StandardLocation}.
	 *  - {@code javax.tools.ToolProvider}.
	 *
	 * To make this work on systems which do not provide standard tool
	 * interfaces in the compiler this will only access them using
	 * reflection and such.
	 *
	 * @since 2019/09/28
	 */
	public static final class StandardToolCompiler
		extends BootstrapCompiler
	{
		/** The Java compiler instance. */
		protected final Object javac;
		
		/**
		 * Initializes the standard compiler.
		 *
		 * @param __javac The standard Java compiler instance.
		 * @throws NullPointerException On null arguments.
		 * @since 2019/09/29
		 */
		public StandardToolCompiler(Object __javac)
			throws NullPointerException
		{
			if (__javac == null)
				throw new NullPointerException("NARG");
			
			this.javac = __javac;
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2019/09/29
		 */
		@Override
		public boolean compile(Path __srcpath, Path __outpath,
			Iterable<Path> __classpath, Iterable<Path> __sources,
			Iterable<String> __args)
		{
			// Java compiler instance
			Object javac = this.javac;
			
			// Get stnadard file manager
			Object jfm = __Tool__.JAVA_COMPILER.invoke(
				"getStandardFileManager", javac, null, null, null);
			
			// Set source location
			__Tool__.STANDARD_JAVA_FILE_MANAGER.invoke(
				"setLocation", jfm, 
				__Tool__.STANDARD_LOCATION.enumConstant("SOURCE_PATH"),
				Arrays.<File>asList(__srcpath.toFile()));
			
			// Output to given directory
			__Tool__.STANDARD_JAVA_FILE_MANAGER.invoke(
				"setLocation", jfm,
				__Tool__.STANDARD_LOCATION.enumConstant("CLASS_OUTPUT"),
				Arrays.<File>asList(__outpath.toFile()));
			
			// Translate and set the class path
			List<File> classpath = new ArrayList<>();
			for (Path entry : __classpath)
				classpath.add(entry.toFile());
			__Tool__.STANDARD_JAVA_FILE_MANAGER.invoke(
				"setLocation", jfm,
				__Tool__.STANDARD_LOCATION.enumConstant("CLASS_PATH"),
				classpath);
			
			// Translate source paths to file
			List<File> sources = new ArrayList<>();
			for (Path entry : __sources)
				sources.add(entry.toFile());
			
			// Create compilation task
			Callable task = __Tool__.JAVA_COMPILER.<Callable>invoke(
				Callable.class, "getTask", javac,
				null, jfm, null, __args, null,
				__Tool__.STANDARD_JAVA_FILE_MANAGER.invoke(
					"getJavaFileObjectsFromFiles", jfm, sources));
			
			// Execute the task
			try
			{
				return ((Boolean)task.call()).booleanValue();
			}
			catch (Exception e)
			{
				// {@squirreljme.error NB25 Compiler invocation failed.}
				throw new RuntimeException("NB25", e);
			}
		}
		
		/**
		 * Returns an instance of the compiler.
		 *
		 * @return The compiler instance.
		 * @throws UnsupportedOperationException If it is not a valid compiler.
		 * @since 2019/09/28
		 */
		public static final StandardToolCompiler getInstance()
			throws UnsupportedOperationException
		{
			// {@squirreljme.error NB23 No standard Java compiler exists.}
			Object javac = __Tool__.TOOL_PROVIDER.invoke(
				"getSystemJavaCompiler", null);
			if (javac == null)
				throw new UnsupportedOperationException("NB23");
			
			// Make compiler with this instance
			return new StandardToolCompiler(javac);
		}
	}
	
	/**
	 * Enumeration to represent the standard tool classes.
	 *
	 * @since 2019/09/28
	 */
	static enum __Tool__
	{
		/** Java compiler. */
		JAVA_COMPILER("javax.tools.JavaCompiler"),
		
		/** Java file manager. */
		JAVA_FILE_MANAGER("javax.tools.JavaFileManager"),
		
		/** Standard Java File Manager. */
		STANDARD_JAVA_FILE_MANAGER("javax.tools.StandardJavaFileManager"),
		
		/** Standard location. */
		STANDARD_LOCATION("javax.tools.StandardLocation"),
		
		/** Tool provider. */
		TOOL_PROVIDER("javax.tools.ToolProvider");
		
		/** End. */
		;
		
		/** The class to access. */
		protected final String desireclass;
		
		/** Cached class. */
		private Class<?> _cacheclass;
		
		/**
		 * Initializes the entry.
		 *
		 * @param __dc The desired class.
		 * @throws NullPointerException On null arguments.
		 * @since 2019/09/28
		 */
		private __Tool__(String __dc)
			throws NullPointerException
		{
			if (__dc == null)
				throw new NullPointerException("NARG");
			
			this.desireclass = __dc;
		}
		
		/**
		 * Returns the enum constant of the given name.
		 *
		 * @param __name The name of the enum constant.
		 * @return The resulting constant.
		 * @throws NullPointerException On null arguments.
		 * @throws UnsupportedOperationException If the constant is not valid.
		 * @since 2019/09/29
		 */
		public final Enum<?> enumConstant(String __name)
			throws NullPointerException, UnsupportedOperationException
		{
			if (__name == null)
				throw new NullPointerException("NARG");
			
			// Working with this class
			Class<?> cl = this.lookupClass();
			
			// If there are no values, then this is likely not an enumeration
			Object[] values = cl.getEnumConstants();
			if (values != null)
			{
				// Find it
				for (Object value : values)
					if (__name.equals(((Enum)value).name()))
						return Enum.class.cast(value);
			}
			
			// {@squirreljme.error NB24 Enumeration constant not found.
			// (The class name; The constant name)}
			throw new UnsupportedOperationException(
				String.format("NB24 %s %s", cl.getName(), __name));
		}
		
		/**
		 * Looks up the specified class.
		 *
		 * @return The resulting class.
		 * @throws UnsupportedOperationException If it does not exist.
		 * @since 2019/09/28
		 */
		public final Class<?> lookupClass()
			throws UnsupportedOperationException
		{
			// Already cached?
			Class<?> rv = this._cacheclass;
			if (rv != null)
				return rv;
			
			// Lookup otherwise
			try
			{
				this._cacheclass = (rv = Class.forName(this.desireclass));
			}
			
			// {@squirreljme.error NB20 Could not lookup compiler class.}
			catch (ClassNotFoundException e)
			{
				throw new UnsupportedOperationException("NB20", e);
			}
			
			// Return it
			return rv;
		}
		
		/**
		 * Invokes the given method and returns the given class type.
		 *
		 * @param <T> The type to return.
		 * @param __t The type to return.
		 * @param __name The name of the method.
		 * @param __v The value to invoke on.
		 * @param __args Arguments to the method.
		 * @return The return value of the method.
		 * @throws ClassCastException If the type is not correct.
		 * @throws NullPointerException On null arguments.
		 * @since 2019/09/29
		 */
		public final <T> T invoke(Class<T> __t, String __name, Object __v,
			Object... __args)
			throws ClassCastException, NullPointerException
		{
			if (__t == null)
				throw new NullPointerException("NARG");
			
			return __t.cast(this.invoke(__name, __v, __args));
		}
		
		/**
		 * Invokes the given method.
		 *
		 * @param __name The name of the method.
		 * @param __v The value to invoke on.
		 * @param __args Arguments to the method.
		 * @return The return value of the method.
		 * @throws NullPointerException On null arguments.
		 * @since 2019/09/29
		 */
		public final Object invoke(String __name, Object __v, Object... __args)
			throws NullPointerException
		{
			if (__name == null)
				throw new NullPointerException("NARG");
			
			// Lookup our class
			Class<?> cl = this.lookupClass();
			
			// Number of arguments to match against
			int numargs = (__args == null ? 0 : __args.length);
			
			// Find matching according to name and argument count
			for (Method m : cl.getMethods())
				if (__name.equals(m.getName()) &&
					numargs == m.getParameterCount())
				try
				{
					return m.invoke(__v,
						(__args == null ? new Object[0] : __args));
				}
				catch (IllegalAccessException|InvocationTargetException e)
				{
					// {@squirreljme.error NB22 Could not invoke the given
					// method. (The class name; The method name)}
					throw new RuntimeException(String.format("NB22 %s %s",
						cl.getName(), __name), e);
				}
			
			// {@squirreljme.error NB21 Could not find the method with the
			// given name in the given class. (The class name; The method)}
			throw new UnsupportedOperationException(
				String.format("NB21 %s %s", cl.getName(), __name));
		}
	}
}

