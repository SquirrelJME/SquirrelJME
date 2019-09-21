// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package dev.shadowtail.jarfile;

import cc.squirreljme.jvm.ClassInfo;
import cc.squirreljme.jvm.Constants;
import cc.squirreljme.vm.VMClassLibrary;
import dev.shadowtail.classfile.mini.DualPoolEncoder;
import dev.shadowtail.classfile.mini.DualPoolEncodeResult;
import dev.shadowtail.classfile.mini.MinimizedClassFile;
import dev.shadowtail.classfile.mini.MinimizedField;
import dev.shadowtail.classfile.mini.MinimizedMethod;
import dev.shadowtail.classfile.mini.MinimizedPoolEntryType;
import dev.shadowtail.classfile.mini.Minimizer;
import dev.shadowtail.classfile.pool.AccessedField;
import dev.shadowtail.classfile.pool.ClassPool;
import dev.shadowtail.classfile.pool.DualClassRuntimePool;
import dev.shadowtail.classfile.pool.DualClassRuntimePoolBuilder;
import dev.shadowtail.classfile.pool.InvokedMethod;
import dev.shadowtail.classfile.pool.InvokeType;
import dev.shadowtail.classfile.pool.MethodIndex;
import dev.shadowtail.classfile.pool.UsedString;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import net.multiphasicapps.classfile.FieldDescriptor;
import net.multiphasicapps.classfile.FieldName;
import net.multiphasicapps.classfile.ClassFile;
import net.multiphasicapps.classfile.ClassName;
import net.multiphasicapps.classfile.ClassNames;
import net.multiphasicapps.classfile.InvalidClassFormatException;
import net.multiphasicapps.classfile.MethodDescriptor;
import net.multiphasicapps.classfile.MethodName;
import net.multiphasicapps.classfile.MethodNameAndType;
import net.multiphasicapps.io.TableSectionOutputStream;

/**
 * This class is responsible for creating minimized Jar files which will then
 * be built into a ROM and used by SummerCoat and RatufaCoat.
 *
 * @since 2019/04/27
 */
public final class JarMinimizer
{
	/**
	 * {@squirreljme.property dev.shadowtail.jarfile.debug=boolean
	 * Should debugging text be printed for the JAR minimizer?}
	 */
	static final boolean _ENABLE_DEBUG =
		Boolean.getBoolean("dev.shadowtail.jarfile.debug");
	
	/** The state of the bootstrap. */
	protected final BootstrapState bootstrap;
	
	/** Is this a boot JAR? */
	protected final boolean boot;
	
	/** The input JAR. */
	protected final VMClassLibrary input;
	
	/** The dual-combined constant pool. */
	protected final DualClassRuntimePoolBuilder dualpool;
	
	/** Are we using our own dual pool? */
	protected final boolean owndualpool;
	
	/** The resulting JAR header. */
	private MinimizedJarHeader _jheader;
	
	/**
	 * Initializes the minimizer worker.
	 *
	 * @param __dp The global dual constant pool, may be {@code null} to not
	 * use the pack-file global one.
	 * @param __boot Is this a boot JAR?
	 * @param __in The input library.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/27
	 */
	private JarMinimizer(DualClassRuntimePoolBuilder __dp, boolean __boot,
		VMClassLibrary __in)
		throws NullPointerException
	{
		if (__in == null)
			throw new NullPointerException("NARG");
			
		this.boot = __boot;
		this.input = __in;
		
		// Use the passed pool if it was passed, but otherwise just use one
		// in the event one was not passed through (uses our own pool)
		DualClassRuntimePoolBuilder usedp;
		this.dualpool = (usedp = (__boot ? null :
			(__dp != null ? __dp : new DualClassRuntimePoolBuilder())));
		this.owndualpool = (usedp == null);
		
		// Setup bootstrap, but only if booting
		this.bootstrap = (__boot ? new BootstrapState() : null);
	}
	
	/**
	 * Processes the input JAR.
	 *
	 * @param __out The output.
	 * @throws IOException On read/write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/27
	 */
	private final void __process(OutputStream __sout)
		throws IOException, NullPointerException
	{
		if (__sout == null)
			throw new NullPointerException("NARG");
		
		// The current state of the bootstrap
		BootstrapState bootstrap = this.bootstrap;
		Initializer initializer = (bootstrap == null ? null :
			bootstrap.initializer);
		
		// This is processed for all entries
		VMClassLibrary input = this.input;
		
		// Need list of resources to determine
		String[] rcnames = input.listResources();
		int numrc = rcnames.length;
		
		// Sort all the resources so that it is faster to find the entries
		Arrays.sort(rcnames);
		
		// Manifest offset and length
		int manifestoff = 0,
			manifestlen = 0;
		
		// Table of the entire JAR for writing
		TableSectionOutputStream out = new TableSectionOutputStream();
		
		// Start the header and table of contents
		// These are fixed size because the bootstrapper needs to know the
		// true pointer of the minified class file in the JAR
		TableSectionOutputStream.Section header = out.addSection(
			MinimizedJarHeader.HEADER_SIZE_WITH_MAGIC, 4);
		TableSectionOutputStream.Section toc = out.addSection(
			numrc * 16, 4);
		
		// Write base header and contents information
		header.writeInt(MinimizedJarHeader.MAGIC_NUMBER);
		header.writeInt(numrc);
		header.writeSectionAddressInt(toc);
		
		// The global dual-constant pool if one is available
		DualClassRuntimePoolBuilder dualpool = this.dualpool;
		
		// Buffer for byte copies
		byte[] copybuf = new byte[512];
		
		// Go through and add every resource
		for (int i = 0; i < numrc; i++)
		{
			// Resource to encode/copy
			String rc = rcnames[i];
			
			// Section to contain the data for this resource
			TableSectionOutputStream.Section rcdata = out.addSection(
				TableSectionOutputStream.VARIABLE_SIZE, 4);
			
			// Process the resource
			try (InputStream in = input.resourceAsStream(rc))
			{
				// Minimizing class file
				if (rc.endsWith(".class"))
				{
					// Minimize the class
					byte[] bytes = Minimizer.minimize(dualpool,
						ClassFile.decode(in));
					
					// Write to ROM!
					rcdata.write(bytes);
					
					// Load class file if booting
					if (bootstrap != null)
						bootstrap.loadClassFile(bytes,
							out.sectionAddress(rcdata));
				}
				
				// Plain resource copy
				else
				{
					for (;;)
					{
						int ll = in.read(copybuf);
						
						// EOF?
						if (ll < 0)
							break;
						
						// Write
						rcdata.write(copybuf);
					}
				}
			}
			
			// Write the hash code of the entry name
			toc.writeInt(rc.hashCode());
			
			// Write name of the resource
			TableSectionOutputStream.Section rcname = out.addSection(
				TableSectionOutputStream.VARIABLE_SIZE, 4);
			rcname.writeUTF(rc);
			toc.writeSectionAddressInt(rcname);
			
			// Write position and size of the data
			toc.writeSectionAddressInt(rcdata);
			toc.writeSectionSizeInt(rcdata);
		}
		
		// Uncompressed and copied manifest?
		try (InputStream in = input.resourceAsStream("META-INF/MANIFEST.MF"))
		{
			// There is a manifest
			if (in != null)
			{
				TableSectionOutputStream.Section manifest = out.addSection(
					TableSectionOutputStream.VARIABLE_SIZE, 4);
				
				// Copy the manifest to an uncompressed section
				for (;;)
				{
					int ll = in.read(copybuf);
					
					// EOF?
					if (ll < 0)
						break;
					
					// Write
					manifest.write(copybuf);
				}
				
				// Manifest offset and length
				header.writeSectionAddressInt(manifest);
				header.writeSectionSizeInt(manifest);
			}
			
			// There is none
			else
			{
				header.writeInt(0);
				header.writeInt(0);
			}
		}
		
		// Doing bootstrapping?
		if (bootstrap != null)
		{
			// The class being booted
			LoadedClassInfo booting = bootstrap.findClass(
				"cc/squirreljme/jvm/Bootstrap");
			
			// Get all the bootstrap information before it is written!
			int bootpool = booting.poolPointer();
			int bootsfbp = bootstrap.staticFieldAreaAddress();
			int bootmeth = booting.methodCodeAddress(
				new MethodName("__start"), null);
			int bootidba = bootstrap.findClass("[B").infoPointer();
			int bootidbd = bootstrap.findClass("[[B").infoPointer();
			
			// Setup the BootRAM
			TableSectionOutputStream.Section bootram = out.addSection(
				bootstrap.initializer.toByteArray(), 4);
			
			// Boot memory offset, size
			header.writeSectionAddressInt(bootram);
			header.writeSectionSizeInt(bootram);
			
			// Pool, sfa, code, classidba, classidbaa
			header.writeInt(bootpool);
			header.writeInt(bootsfbp);
			header.writeInt(bootmeth);
			header.writeInt(bootidba);
			header.writeInt(bootidbd);
			
			// Debug
			if (JarMinimizer._ENABLE_DEBUG)
				todo.DEBUG.note("Boot entry: %d/0x%08x", bootmeth, bootmeth);
		}
		
		// No bootstrapping being done
		else
		{
			// Boot memory offset, size
			header.writeInt(0);
			header.writeInt(0);
			
			// Pool, sfa, code, classidba, classidbaa
			header.writeInt(0);
			header.writeInt(0);
			header.writeInt(0);
			header.writeInt(0);
			header.writeInt(0);
		}
		
		// We are using our own dual pool, so write it out as if it were
		// in the pack file. It is only local to this JAR.
		if (this.owndualpool && dualpool != null)
		{
			// Where our pools are going
			TableSectionOutputStream.Section lpd = out.addSection();
			
			// Encode the pools
			DualPoolEncodeResult der = DualPoolEncoder.encode(dualpool, lpd);
			
			// Static pool
			header.writeSectionAddressInt(lpd, der.staticpooloff);
			header.writeInt(der.staticpoolsize);
			
			// Run-time pool
			header.writeSectionAddressInt(lpd, der.runtimepooloff);
			header.writeInt(der.runtimepoolsize);
		}
		
		// We are using the global pack pool, so set special indicators
		// that we are doing as such! The minimized class will use special
		// a special aliased pool for the pack file.
		else
		{
			// Static pool offset and size
			header.writeInt(-1);
			header.writeInt(-1);
			
			// Runtime pool offset and size
			header.writeInt(-1);
			header.writeInt(-1);
		}
		
		// Since we need the header we need the byte array for the JAR
		byte[] jardata = out.toByteArray();
		
		// Get header for returning
		this._jheader = MinimizedJarHeader.decode(
			new ByteArrayInputStream(jardata));
		
		// Write to output
		__sout.write(jardata);
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
			JarMinimizer.minimize(null, __boot, __in, baos, null);
			
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
		JarMinimizer.minimize(null, __boot, __in, __out, null);
	}
	
	/**
	 * Minimizes the specified Jar file.
	 *
	 * @param __boot Should pre-created boot memory be created to quickly
	 * initialize the virtual machine?
	 * @param __in The input JAR file.
	 * @param __out The stream where JAR data will be placed.
	 * @param __mjh The output JAR header.
	 * @throws IOException On read/write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/07/17
	 */
	public static final void minimize(boolean __boot, VMClassLibrary __in,
		OutputStream __out, MinimizedJarHeader[] __mjh)
		throws IOException, NullPointerException
	{
		JarMinimizer.minimize(null, __boot, __in, __out, __mjh);
	}
	
	/**
	 * Minimizes the specified Jar file.
	 *
	 * @param __dp The dual-pool.
	 * @param __boot Should pre-created boot memory be created to quickly
	 * initialize the virtual machine?
	 * @param __in The input JAR file.
	 * @param __out The stream where JAR data will be placed.
	 * @param __mjh The output JAR header.
	 * @throws IOException On read/write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/05/29
	 */
	public static final void minimize(DualClassRuntimePoolBuilder __dp,
		boolean __boot, VMClassLibrary __in, OutputStream __out,
		MinimizedJarHeader[] __mjh)
		throws IOException, NullPointerException
	{
		if (__in == null || __out == null)
			throw new NullPointerException("NARG");
		
		// Use helper class
		JarMinimizer jm = new JarMinimizer(__dp, __boot, __in);
		jm.__process(__out);
		
		// Set header that was generated
		if (__mjh != null && __mjh.length > 0)
			__mjh[0] = jm._jheader;
	}
}
