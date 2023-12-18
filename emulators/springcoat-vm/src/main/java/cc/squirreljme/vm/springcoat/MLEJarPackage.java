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
import cc.squirreljme.jvm.manifest.JavaManifestAttributes;
import cc.squirreljme.jvm.mle.JarPackageShelf;
import cc.squirreljme.jvm.mle.brackets.JarPackageBracket;
import cc.squirreljme.runtime.cldc.debug.ErrorCode;
import cc.squirreljme.vm.VMClassLibrary;
import cc.squirreljme.vm.springcoat.brackets.JarPackageObject;
import cc.squirreljme.vm.springcoat.exceptions.SpringMLECallError;
import cc.squirreljme.vm.springcoat.exceptions.SpringVirtualMachineException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import net.multiphasicapps.classfile.MethodDescriptor;

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
			return MLEJarPackage.__jarObject(__args[0]).library() ==
				MLEJarPackage.__jarObject(__args[1]).library();
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
				MLEJarPackage.__jarObject(__args[0]).library());
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
			return MLEJarPackage.__jarObject(__args[0]).library().name();
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
			JarPackageObject jar = MLEJarPackage.__jarObject(__args[0]);
			
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
				
				// Copy everything to the a byte array, since it is easier to
				// handle resources without juggling special resource streams
				// and otherwise
				try (ByteArrayOutputStream baos = new ByteArrayOutputStream(
					Math.max(1024, in.available())))
				{
					// Copy all the data
					byte[] copy = new byte[4096];
					for (;;)
					{
						int rc = in.read(copy);
						
						if (rc < 0)
							break;
						
						baos.write(copy, 0, rc);
					}
					
					// Use this as the stream input
					return __thread.newInstance(__thread.loadClass(
						"java/io/ByteArrayInputStream"),
						new MethodDescriptor("([B)V"),
						__thread.asVMObject(baos.toByteArray()));
				}
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
			
			JarPackageObject __jar = MLEJarPackage.__jarObject(__args[0]);
			
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
			
			JarPackageObject __jar = MLEJarPackage.__jarObject(__args[0]);
			int __jarOffset = (int)__args[1];
			byte[] __b = ((SpringArrayObjectByte)__args[2]).array();
			int __o = (int)__args[3];
			int __l = (int)__args[4]; 
			
			if (__jarOffset < 0 || __b == null || __o < 0 || __l < 0 ||
				__o + __l > __b.length)
				throw new SpringMLECallError("Invalid parameters.");
			
			// Determine the path to the JAR
			Path path = __jar.library().path();
			if (path == null)
				throw new SpringMLECallError(
					"JAR is not physically backed.");
			
			// Seek through and find the data
			try (InputStream in = Files.newInputStream(path,
				StandardOpenOption.READ))
			{
				// Seek first, stop if EOF is hit
				for (int at = 0; at < __jarOffset; at++)
					if (in.read() < 0)
						return -1;
				
				// Do a standard read here
				return in.read(__b, __o, __l);
			}
			catch (IOException e)
			{
				throw new SpringMLECallError(
					"Could not raw read JAR.", e);
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
			JarPackageObject jar = MLEJarPackage.__jarObject(__args[0]);
			Path path = jar.library().path();
			if (path == null)
				return -1;
			
			// Use the file size directly
			try
			{
				return (int)Math.min(Integer.MAX_VALUE, Files.size(path));
			}
			
			// Size is not valid?
			catch (IOException e)
			{
				e.printStackTrace();
				
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
	
	/**
	 * Checks if this is a {@link JarPackageObject}.
	 * 
	 * @param __object The object to check.
	 * @return As a jar if this is one.
	 * @throws SpringMLECallError If this is not a jar.
	 * @since 2020/06/22
	 */
	static JarPackageObject __jarObject(Object __object)
		throws SpringMLECallError
	{
		if (!(__object instanceof JarPackageObject))
			throw new SpringMLECallError("Not a JarPackageObject.");
		
		return (JarPackageObject)__object; 
	}
}
