// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.summercoat;

import dev.shadowtail.classfile.mini.MinimizedClassFile;
import dev.shadowtail.classfile.mini.MinimizedField;
import dev.shadowtail.classfile.mini.MinimizedMethod;
import cc.squirreljme.runtime.cldc.vki.FixedClassIDs;
import cc.squirreljme.runtime.cldc.vki.Kernel;
import cc.squirreljme.vm.VirtualMachine;
import cc.squirreljme.vm.VMClassLibrary;
import cc.squirreljme.vm.VMException;
import cc.squirreljme.vm.VMFactory;
import cc.squirreljme.vm.VMSuiteManager;
import java.io.InputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import net.multiphasicapps.io.HexDumpOutputStream;
import net.multiphasicapps.profiler.ProfilerSnapshot;

/**
 * This is the factory which is capable of creating instances of the
 * SummerCoat virtual machine.
 *
 * @since 2018/12/29
 */
public class SummerCoatFactory
	extends VMFactory
{
	/** The base address for suites. */
	public static final int SUITE_BASE_ADDR =
		0x40000000;
	
	/** The starting address for RAM. */
	public static final int RAM_START_ADDRESS =
		1048576;
	
	/** Default RAM size. */
	public static final int DEFAULT_RAM_SIZE =
		16777216;
	
	/**
	 * Initializes the factory.
	 *
	 * @since 2018/12/29
	 */
	public SummerCoatFactory()
	{
		super("summercoat");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/29
	 */
	@Override
	protected VirtualMachine createVM(ProfilerSnapshot __ps,
		VMSuiteManager __sm, VMClassLibrary[] __cp, String __maincl,
		boolean __ismid, int __gd, Map<String, String> __sprops,
		String[] __args)
		throws IllegalArgumentException, NullPointerException, VMException
	{
		// Initialize suite memory
		SuitesMemory sm = new SuitesMemory(SUITE_BASE_ADDR, __sm);
		
		// Size of RAM
		int ramsize = DEFAULT_RAM_SIZE;
		
		// Initialize and map virtual memory
		VirtualMemory vmem = new VirtualMemory();
		vmem.mapRegion(sm);
		vmem.mapRegion(new RawMemory(RAM_START_ADDRESS, ramsize));
		
		// Initialize the suite space and load the boot address
		sm.__init();
		int kernaddr = sm._kernelmcaddr,
			bootaddr = -1;
		
		// Write raw strings into memory which describe the various VM
		// arguments and such. This mostly refers to the class to start once
		// the VM has initialized itself
		int[] sssp = new int[]{RAM_START_ADDRESS};
		int xxclasspth = this.__memString(vmem, sssp,
				SummerCoatFactory.stringArrayToString(
				SummerCoatFactory.classPathToStringArray(__cp))),
			xxsysprops = this.__memString(vmem, sssp,
				SummerCoatFactory.stringArrayToString(
				SummerCoatFactory.stringMapToString(__sprops))),
			xxmainclss = this.__memString(vmem, sssp, __maincl),
			xxmainargs = this.__memString(vmem, sssp,
				SummerCoatFactory.stringArrayToString(__args));
		
		// The base address of the kernel object
		int kobjbase = (sssp[0] + 3) & (~3);
		
		// Write fixed class ID into object base and an initial reference
		// count of 1
		vmem.memWriteShort(kobjbase, FixedClassIDs.KERNEL);
		vmem.memWriteShort(kobjbase + 2, 1);
		
		// Static memory size, small region of starting memory for the boot
		// strings and such
		int staticmemsize = ((sssp[0] + 128 + 1024) + 1023) & (~1023);
		
		// Read the information on the boot class into memory, we need to
		// initialize and setup a bunch of fields for it
		try (InputStream kin = new ReadableMemoryInputStream(vmem,
			kernaddr, 1048576))
		{
			// Decode the class file so we can access the fields
			MinimizedClassFile minikern = MinimizedClassFile.decode(kin);
			
			// Find the kernel boot address
			for (MinimizedMethod mm : minikern.methods(false))
				if (mm.name.toString().equals("__start"))
				{
					// We can use the offsets from the image file
					bootaddr = kernaddr + minikern.header.imoff +
						mm.codeoffset;
					break;
				}
			
			// Base for fields, note that the object includes its class ID
			// and the reference count!
			int kfldbase = kobjbase + 4;
			
			// Go through instance fields and set their data fields
			for (MinimizedField mf : minikern.fields(false))
			{
				// Memory field offer
				int kfo = kfldbase + mf.offset;
				
				// Value depends on the field
				switch (mf.name.toString())
				{
						// ROM address
					case "romaddr":
						vmem.memWriteInt(kfo, SUITE_BASE_ADDR);
						break;
						
						// Kernel object base
					case "kobjbase":
						vmem.memWriteInt(kfo, kobjbase);
						break;
						
						// Static memory size
					case "staticmemsize":
						vmem.memWriteInt(kfo, staticmemsize);
						break;
						
						// Starting memory address
					case "memaddr":
						vmem.memWriteInt(kfo, RAM_START_ADDRESS);
						break;
					
						// Size of memory
					case "memsize":
						vmem.memWriteInt(kfo, ramsize);
						break;
						
						// Is this a MIDlet?
					case "ismidlet":
						vmem.memWriteInt(kfo, (__ismid ? 1 : 0));
						break;
						
						// The guest depth
					case "guestdepth":
						vmem.memWriteInt(kfo, __gd);
						break;
						
						// The classpath
					case "classpath":
						vmem.memWriteInt(kfo, xxclasspth);
						break;
						
						// System properties
					case "sysprops":
						vmem.memWriteInt(kfo, xxsysprops);
						break;
						
						// Main class
					case "mainclass":
						vmem.memWriteInt(kfo, xxmainclss);
						break;
						
						// Main arguments
					case "mainargs":
						vmem.memWriteInt(kfo, xxmainargs);
						break;
					
					default:
						throw new todo.OOPS(mf.name.toString());
				}
			}
		}
		
		// {@squirreljme.error AE0y Could not read the kernel class for
		// initialization}.
		catch (IOException e)
		{
			throw new RuntimeException("AE0y", e);
		}
		
		// Dump some memory
		todo.DEBUG.note("KOBJ");
		HexDumpOutputStream.dump(System.err,
			new ReadableMemoryInputStream(vmem, kernaddr, 1024));
		
		todo.DEBUG.note("BOOT");
		HexDumpOutputStream.dump(System.err,
			new ReadableMemoryInputStream(vmem, bootaddr, 1024));
		
		// Setup virtual CPU to execute
		NativeCPU cpu = new NativeCPU(vmem);
		cpu.enterFrame(bootaddr);
		
		// Execute the CPU to boot the machine
		cpu.run();
		
		if (true)
			throw new todo.TODO();
		
		// Setup root machine which has our base suite manager
		RootMachine rm = new RootMachine(__sm, __ps, __gd);
		
		// Need to map to cached VMs
		CachingSuiteManager suites = rm.suites;
		int n = __cp.length;
		CachingClassLibrary[] libs = new CachingClassLibrary[n];
		for (int i = 0; i < n; i++)
			libs[i] = suites.loadLibrary(__cp[i]);
		
		// Now create the starting main task
		return new ExitAwaiter(rm.statuses, rm.createTask(libs, __maincl,
			__ismid, __sprops, __args).status, __ps);
	}
	
	/**
	 * This writes a string to memory somewhere.
	 *
	 * @param __wm Memory that can be written to.
	 * @param __wp The write pointer of the string.
	 * @param __s The string to encode.
	 * @return The address the string was stored at.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/21
	 */
	private int __memString(WritableMemory __wm, int[] __wp, String __s)
		throws NullPointerException
	{
		if (__wm == null || __wp == null || __s == null)
			throw new NullPointerException("NARG");
		
		int ptr = __wp[0],
			base = ptr;
		
		// Encode bytes
		byte[] encode;
		try
		{
			encode = __s.getBytes("utf-8");
		}
		catch (UnsupportedEncodingException e)
		{
			encode = __s.getBytes();
		}
		
		// Write fixed ID, initial refcount, and length
		__wm.memWriteShort(ptr, FixedClassIDs.PRIMITIVE_BYTE_ARRAY);
		__wm.memWriteShort(ptr + 2, 1);
		__wm.memWriteInt(ptr + 4, encode.length);
		
		// Move pointer up
		ptr += 6;
		
		// Write byte data
		for (int i = 0, n = encode.length; i < n; i++)
			__wm.memWriteByte(ptr++, encode[i]);
		
		// Round pointer
		ptr = (ptr + 3) & (~3);
		
		// Store for next read
		__wp[0] = ptr;
		return base;
	}
	
	/**
	 * Converts a class path to a string array.
	 *
	 * @param __cp The class path to convert.
	 * @return The resulting string array.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/21
	 */
	public static final String[] classPathToStringArray(VMClassLibrary... __cp)
		throws NullPointerException
	{
		if (__cp == null)
			throw new NullPointerException("NARG");
		
		int n = __cp.length;
		String[] rv = new String[n];
		for (int i = 0; i < n; i++)
			rv[i] = __cp[i].name();
		
		return rv;
	}
	
	/**
	 * Converts a string array to a single string.
	 *
	 * @param __s The strings to convert.
	 * @return The resulting string.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/21
	 */
	public static final String stringArrayToString(String... __s)
		throws NullPointerException
	{
		if (__s == null)
			throw new NullPointerException("NARG");
		
		StringBuilder sb = new StringBuilder();
		
		for (String s : __s)
		{
			if (sb.length() > 0)
				sb.append((char)0);
			sb.append(s);
		}
		
		return sb.toString();
	}
	
	/**
	 * Converts a string array to a single string.
	 *
	 * @param __s The strings to convert.
	 * @return The resulting string.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/21
	 */
	public static final String stringMapToString(Map<String, String> __s)
		throws NullPointerException
	{
		if (__s == null)
			throw new NullPointerException("NARG");
		
		StringBuilder sb = new StringBuilder();
		
		for (Map.Entry<String, String> s : __s.entrySet())
		{
			if (sb.length() > 0)
				sb.append((char)0);
			
			// Key and value pair
			sb.append(s.getKey());
			sb.append((char)0);
			sb.append(s.getValue());
		}
		
		return sb.toString();
	}
}

