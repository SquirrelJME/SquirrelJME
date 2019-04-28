// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package dev.shadowtail.jarfile;

import cc.squirreljme.runtime.cldc.vki.FixedClassIDs;
import cc.squirreljme.runtime.cldc.vki.HardcodedFunctions;
import cc.squirreljme.runtime.cldc.vki.Kernel;
import cc.squirreljme.vm.VMClassLibrary;
import dev.shadowtail.classfile.mini.MinimizedClassFile;
import dev.shadowtail.classfile.mini.MinimizedPool;
import dev.shadowtail.classfile.mini.MinimizedPoolEntryType;
import dev.shadowtail.classfile.mini.Minimizer;
import dev.shadowtail.classfile.nncc.ClassPool;
import dev.shadowtail.classfile.nncc.InvokedMethod;
import dev.shadowtail.classfile.nncc.WhereIsThis;
import dev.shadowtail.classfile.xlate.InvokeType;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.multiphasicapps.classfile.ClassFile;
import net.multiphasicapps.classfile.ClassName;
import net.multiphasicapps.classfile.ClassNames;

/**
 * This class is responsible for creating minimized Jar files which will then
 * be built into a ROM and used by SummerCoat and RatufaCoat.
 *
 * @since 2019/04/27
 */
public final class JarMinimizer
{
	/** The kernel's class. */
	private static final String _KERNEL_CLASS =
		"cc/squirreljme/runtime/cldc/vki/Kernel";
	
	/** The C class. */
	private static final String _C_CLASS =
		"cc/squirreljme/runtime/cldc/vki/C";
	
	/** Is this a boot JAR? */
	protected final boolean boot;
	
	/** The input JAR. */
	protected final VMClassLibrary input;
	
	/** Mini-classes, if this is a boot Jar. */
	private final Map<ClassName, MinimizedClassFile> _minicl;
	
	/** The offset to the mini-class data, within the JAR itself. */
	private final Map<ClassName, Integer> _minicloff;
	
	/**
	 * Initializes the minimizer worker.
	 *
	 * @param __boot Is this a boot JAR?
	 * @param __in The input library.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/27
	 */
	private JarMinimizer(boolean __boot, VMClassLibrary __in)
		throws NullPointerException
	{
		if (__in == null)
			throw new NullPointerException("NARG");
		
		this.boot = __boot;
		this.input = __in;
		
		// Only used if this is a boot JAR
		if (__boot)
		{
			this._minicl = new HashMap<>();
			this._minicloff = new HashMap<>();
		}
		
		// Not used
		else
		{
			this._minicl = null;
			this._minicloff = null;
		}
	}
	
	/**
	 * Returns the address of the given class.
	 *
	 * @param __cl The class to look for.
	 * @return The address of the given class.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/28
	 */
	public final int __addressOfClass(String __cl)
		throws NullPointerException
	{
		if (__cl == null)
			throw new NullPointerException("NARG");
		
		return this._minicloff.get(new ClassName(__cl));
	}
	
	/**
	 * Returns the address of the given method.
	 *
	 * @param __cl The class to look in.
	 * @param __mn The method name.
	 * @param __mt The method type, if {@code null} then the type is
	 * disregarded.
	 * @return The address of the given method.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/28
	 */
	public final int __addressOfMethod(String __cl, String __mn, String __mt)
		throws NullPointerException
	{
		if (__cl == null || __mn == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
	
	/**
	 * Processes the input JAR.
	 *
	 * @param __dos The output.
	 * @throws IOException On read/write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/27
	 */
	private final void __process(DataOutputStream __dos)
		throws IOException, NullPointerException
	{
		if (__dos == null)
			throw new NullPointerException("NARG");
		
		// This is processed for all entries
		VMClassLibrary input = this.input;
		
		// If this is a boot JAR, this will later be used and pre-initialized
		// boot memory will be setup accordingly
		Map<ClassName, MinimizedClassFile> minicl = this._minicl;
		Map<ClassName, Integer> minicloff = this._minicloff;
		
		// Need list of resources to determine
		String[] rcnames = input.listResources();
		int numrc = rcnames.length;
		
		// Table of contents
		ByteArrayOutputStream tbaos = new ByteArrayOutputStream(2048);
		DataOutputStream tdos = new DataOutputStream(tbaos);
		
		// JAR content data
		ByteArrayOutputStream jbaos = new ByteArrayOutputStream(1048576);
		DataOutputStream jdos = new DataOutputStream(jbaos);
		
		// Relative offset from header and table of contents
		int reloff = MinimizedJarHeader.HEADER_SIZE_WITH_MAGIC +
			(4 * 12);
		
		// Go through and minimize/concat all resources
		for (int i = 0; i < numrc; i++)
		{
			String rc = rcnames[i];
			ClassName ofclass = null;
			
			// The resulting byte array containing data
			byte[] bytes;
			
			// Open resource
			try (InputStream in = input.resourceAsStream(rc))
			{
				// Minimizing class
				if (rc.endsWith(".class"))
				{
					// Minimize file data
					bytes = Minimizer.minimize(ClassFile.decode(in));
					
					// If boot processing is going to be done, we need to
					// know about this class file
					if (minicl != null)
					{
						// Decode it
						MinimizedClassFile cf =
							MinimizedClassFile.decode(bytes);
						
						// And cache it for later
						minicl.put((ofclass = cf.thisName()), cf);
					}
				}
				
				// Plain resource, sent straight through
				else
				{
					// Copy bytes
					byte[] buf = new byte[512];
					try (ByteArrayOutputStream xbaos =
						new ByteArrayOutputStream(4096))
					{
						for (;;)
						{
							int ll = in.read(buf);
							
							if (ll < 0)
								break;
							
							xbaos.write(buf, 0, ll);
						}
						
						// Use this
						bytes = xbaos.toByteArray();
					}
				}
			}
			
			// Round data stream to 2 bytes (so string length is aligned)
			while ((jdos.size() & 1) != 0)
				jdos.write(0);
			
			// Record offset to resource name
			tdos.writeInt(reloff + jdos.size());
			jdos.writeUTF(rc);
			
			// Round data stream to 2 bytes
			while ((jdos.size() & 1) != 0)
				jdos.write(0);
			
			// Write offset to data stream and size
			int clpos;
			tdos.writeInt((clpos = (reloff + jdos.size())));
			tdos.writeInt(bytes.length);
			
			// Store offset to mini class data in the Jar
			if (minicloff != null)
				minicloff.put(ofclass, clpos);
			
			// Then write the actual data stream
			jdos.write(bytes);
		}
		
		// Write header
		__dos.writeInt(MinimizedJarHeader.MAGIC_NUMBER);
		
		// Number of resources
		__dos.writeInt(numrc);
		
		// Offset to table of contents
		__dos.writeInt(MinimizedJarHeader.HEADER_SIZE_WITH_MAGIC);
		
		// Building pre-boot state
		if (minicl != null)
		{
			// Round data stream to 4 bytes
			while ((jdos.size() & 3) != 0)
				jdos.write(0);
			
			// Write address to the boot table
			int baseaddr;
			__dos.writeInt(reloff + (baseaddr = jdos.size()));
			
			// Write integers which represent offsets to all the various
			// classes and methods
			jdos.writeInt(this.__addressOfMethod(
				_KERNEL_CLASS, "__start", null));
			
			// Write length of the boot function table
			__dos.writeInt(jdos.size() - baseaddr);
		}
		
		// No boot data
		else
		{
			// Hard coded function table
			__dos.writeInt(0);
			__dos.writeInt(0);
		}
		
		// Write table of contents and JAR data
		__dos.write(tbaos.toByteArray());
		__dos.write(jbaos.toByteArray());
	}
	
	/**
	 * Minimizes the specified Jar file.
	 *
	 * @param __boot Should pre-created boot memory be created to quickly
	 * initialize the virtual machine?
	 * @param __in The input JAR file.
	 * @return The resulting byte array of minimization.
	 * @throws IOException On read/write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/27
	 */
	public static final byte[] minimize(boolean __boot, VMClassLibrary __in)
		throws IOException, NullPointerException
	{
		if (__in == null)
			throw new NullPointerException("NARG");
		
		// Write to a temporary byte array
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream(1048576))
		{
			// Perform minimization
			JarMinimizer.minimize(__boot, __in, baos);
			
			// Return the generated array
			return baos.toByteArray();
		}
	}
	
	/**
	 * Minimizes the specified Jar file.
	 *
	 * @param __boot Should pre-created boot memory be created to quickly
	 * initialize the virtual machine?
	 * @param __in The input JAR file.
	 * @param __out The stream where JAR data will be placed.
	 * @throws IOException On read/write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/27
	 */
	public static final void minimize(boolean __boot, VMClassLibrary __in,
		OutputStream __out)
		throws IOException, NullPointerException
	{
		if (__in == null || __out == null)
			throw new NullPointerException("NARG");
		
		// Use helper class
		new JarMinimizer(__boot, __in).__process(new DataOutputStream(__out));
	}
}
