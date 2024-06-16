// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.springcoat;

import cc.squirreljme.emulator.vm.VMSuiteManager;
import cc.squirreljme.jvm.manifest.JavaManifest;
import cc.squirreljme.jvm.mle.JarPackageShelf;
import cc.squirreljme.jvm.mle.brackets.JarPackageBracket;
import cc.squirreljme.runtime.cldc.debug.ErrorCode;
import cc.squirreljme.vm.RawVMClassLibrary;
import cc.squirreljme.vm.VMClassLibrary;
import cc.squirreljme.vm.springcoat.brackets.JarPackageObject;
import cc.squirreljme.vm.springcoat.exceptions.SpringMLECallError;
import cc.squirreljme.vm.springcoat.exceptions.SpringVirtualMachineException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Functions for {@link JarPackageShelf}.
 *
 * @since 2020/06/18
 */
public enum MLEJarPackage
	implements MLEFunction
{
	/** {@link JarPackageShelf#classPath()}. */
	CLASS_PATH("classPath:()[Lcc/squirreljme/jvm/mle/brackets/" +
		"JarPackageBracket;")
	{
		/**
		 * {@inheritDoc}
		 * @since 2020/06/18
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			VMClassLibrary[] springPath = __thread.machine.classloader
				.classPath();
		
			// Wrap the classpath in package objects
			int n = springPath.length;
			SpringObject[] rv = new SpringObject[n];
			for (int i = 0; i < n; i++)
				rv[i] = new JarPackageObject(__thread.machine, springPath[i]);
			
			// Wrap
			return __thread.asVMObjectArray(__thread.resolveClass(
				"[Lcc/squirreljme/jvm/mle/brackets/JarPackageBracket;"),
				rv);
		}
	},
	
	/** {@link JarPackageShelf#equals(JarPackageBracket, JarPackageBracket)}.*/ 
	EQUALS("equals:(Lcc/squirreljme/jvm/mle/brackets/" +
		"JarPackageBracket;Lcc/squirreljme/jvm/mle/brackets/" +
		"JarPackageBracket;)Z")
	{
		/**
		 * {@inheritDoc}
		 * @since 2020/07/06
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			return MLEObjects.jarPackage(__args[0]).library() ==
				MLEObjects.jarPackage(__args[1]).library();
		}
	},
	
	/** {@link JarPackageShelf#libraries()}. */
	LIBRARIES("libraries:()[Lcc/squirreljme/jvm/mle/brackets/" +
		"JarPackageBracket;")
	{
		/**
		 * {@inheritDoc}
		 * @since 2020/10/31
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			VMSuiteManager suites = __thread.machine.suites;
			
			String[] names = suites.listLibraryNames();
			int n = names.length;
			
			// Load each library as a reference
			SpringObject[] result = new SpringObject[n];
			for (int i = 0; i < n; i++)
				result[i] = new JarPackageObject(__thread.machine,
					suites.loadLibrary(names[i]));
			
			return __thread.asVMObjectArray(__thread.resolveClass(
				"[Lcc/squirreljme/jvm/mle/brackets/JarPackageBracket;"),
				result);
		}
	},
	
	/** {@link JarPackageShelf#libraryId(JarPackageBracket)}. */
	LIBRARY_ID("libraryId:(Lcc/squirreljme/jvm/mle/brackets/" +
		"JarPackageBracket;)I")
	{
		/**
		 * {@inheritDoc}
		 * @since 2023/12/18
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			return __thread.machine.suites.libraryId(
				MLEObjects.jarPackage(__args[0]).library());
		}
	},
	
	/** {@link JarPackageShelf#libraryPath(JarPackageBracket)}. */ 
	LIBRARY_PATH("libraryPath:(Lcc/squirreljme/jvm/mle/brackets/" +
		"JarPackageBracket;)Ljava/lang/String;")
	{
		/**
		 * {@inheritDoc}
		 * @since 2020/06/18
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			return MLEObjects.jarPackage(__args[0]).library().name();
		}
	},
	
	/** {@link JarPackageShelf#openResource(JarPackageBracket, String)}. */
	OPEN_RESOURCE("openResource:(Lcc/squirreljme/jvm/mle/brackets/" +
		"JarPackageBracket;Ljava/lang/String;)Ljava/io/InputStream;")
	{
		/**
		 * {@inheritDoc}
		 * @since 2020/06/18
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			JarPackageObject jar = MLEObjects.jarPackage(__args[0]);
			
			String rcName = __thread.<String>asNativeObject(
				String.class, __args[1]);
			if (rcName == null)
				throw new SpringMLECallError("Null resource string.");
			
			// Locate the resource
			try (InputStream in = jar.library().resourceAsStream(rcName))
			{
				// Not found
				if (in == null)
					return SpringNullObject.NULL;
				
				return __thread.proxyInputStream(in);
			}
			
			// Could not read it
			catch (IOException e)
			{
				throw new SpringVirtualMachineException(
					"Failed to read resource", e);
			}
		}
	},
	
	/** {@link JarPackageShelf#prefixCode(JarPackageBracket)}. */
	PREFIX_CODE("prefixCode:(Lcc/squirreljme/jvm/mle/brackets/" +
		"JarPackageBracket;)I")
	{
		/**
		 * {@inheritDoc}
		 * @since 2023/07/19
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			if (__args[0] == null)
				throw new SpringMLECallError("No JAR specified.");
			
			JarPackageObject __jar = MLEObjects.jarPackage(__args[0]);
			
			// Load manifest to get the info
			try (InputStream in = __jar.library()
				.resourceAsStream("META-INF/MANIFEST.MF"))
			{
				if (in == null)
					return -1;
				
				// Load in manifest
				JavaManifest manifest = new JavaManifest(in);
				
				// Load in value
				String value = manifest.getMainAttributes()
					.getValue(ErrorCode.PREFIX_PROPERTY);
				if (value == null)
					return -1;
				
				// Too short?
				if (value.length() < 2)
					return -1;
				
				// Get both characters for radix calculation
				char a = value.charAt(0);
				char b = value.charAt(1);
				
				// Calculate prefix code
				return (Character.digit(a, Character.MAX_RADIX) * 
					Character.MAX_RADIX) +
					Character.digit(b, Character.MAX_RADIX);
			}
			catch (IOException ignored)
			{
				return -1;
			}
		}
	},
	
	/**
	 * {@link JarPackageShelf#rawData(JarPackageBracket, int, byte[], int,
	 * int)}.
	 */
	RAW_DATA("rawData:(" +
		"Lcc/squirreljme/jvm/mle/brackets/JarPackageBracket;I[BII)I")
	{
		/**
		 * {@inheritDoc}
		 * @since 2022/03/05
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			if (__args[0] == null)
				throw new SpringMLECallError("No JAR specified.");
			
			JarPackageObject __jar = MLEObjects.jarPackage(__args[0]);
			int __jarOffset = (int)__args[1];
			byte[] __b = ((SpringArrayObjectByte)__args[2]).array();
			int __o = (int)__args[3];
			int __l = (int)__args[4]; 
			
			if (__jarOffset < 0 || __b == null || __o < 0 || __l < 0 ||
				__o + __l > __b.length)
				throw new SpringMLECallError("Invalid parameters.");
			
			// Check to see if it is supported...
			RawVMClassLibrary lib = MLEObjects.libraryRaw(
				__jar.library());
			if (lib == null)
				return -1;
			
			// Otherwise request it
			try
			{
				lib.rawData(__jarOffset, __b, __o, __l);
				return __l;
			}
			catch (Throwable __t)
			{
				__t.printStackTrace();
				
				return -1;
			}
		}
	},
	
	/** {@link JarPackageShelf#rawSize(JarPackageBracket)}. */ 
	RAW_SIZE("rawSize:" +
		"(Lcc/squirreljme/jvm/mle/brackets/JarPackageBracket;)I")
	{
		/**
		 * {@inheritDoc}
		 * @since 2022/03/05
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			if (__args[0] == null)
				throw new SpringMLECallError("No JAR specified.");
			
			// Determine the path to the JAR
			JarPackageObject jar = MLEObjects.jarPackage(__args[0]);
			
			// Check to see if it is supported...
			RawVMClassLibrary lib = MLEObjects.libraryRaw(jar.library());
			if (lib == null)
				return -1;
			
			// Otherwise request it
			try
			{
				return lib.rawSize();
			}
			catch (Throwable __t)
			{
				__t.printStackTrace();
				
				return -1;
			}
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
	 * @since 2020/06/18
	 */
	MLEJarPackage(String __key)
		throws NullPointerException
	{
		if (__key == null)
			throw new NullPointerException("NARG");
		
		this.key = __key;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/06/18
	 */
	@Override
	public String key()
	{
		return this.key;
	}
	
}
