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

import cc.squirreljme.jvm.Constants;
import dev.shadowtail.classfile.mini.MinimizedClassFile;
import dev.shadowtail.classfile.mini.MinimizedField;
import dev.shadowtail.classfile.mini.MinimizedMethod;
import dev.shadowtail.classfile.mini.MinimizedPool;
import dev.shadowtail.classfile.nncc.NativeCode;
import dev.shadowtail.jarfile.MinimizedJarHeader;
import dev.shadowtail.packfile.MinimizedPackHeader;
import cc.squirreljme.runtime.cldc.vki.DefaultConfiguration;
import cc.squirreljme.vm.VirtualMachine;
import cc.squirreljme.vm.VMClassLibrary;
import cc.squirreljme.vm.VMException;
import cc.squirreljme.vm.VMFactory;
import cc.squirreljme.vm.VMSuiteManager;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
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
	
	/** Base address for configuration data. */
	public static final int CONFIG_BASE_ADDR =
		0x30000000;
	
	/** The starting address for RAM. */
	public static final int RAM_START_ADDRESS =
		1048576;
	
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
		// Virtual memory which provides access to many parts of memory
		VirtualMemory vmem = new VirtualMemory();
		
		// The ROM always starts here
		int rombase = SUITE_BASE_ADDR;
		
		// Try to load a specific ROM file instead of the dynamically
		// generate one?
		String romfile = __sprops.get("cc.squirreljme.romfile");
		if (romfile == null)
			try
			{
				romfile = System.getProperty("cc.squirreljme.romfile");
			}
			catch (SecurityException e)
			{
			}
		
		// Load existing ROM file
		if (romfile != null)
		{
			// Debug
			todo.DEBUG.note("Using ROM %s", romfile);
			
			// Copy all of the file data
			Path p = Paths.get(romfile);
			try (InputStream in = Files.newInputStream(p,
					StandardOpenOption.READ);
				ByteArrayOutputStream baos = new ByteArrayOutputStream(
					(int)Files.size(p)))
			{
				// Read data
				byte[] buf = new byte[512];
				for (;;)
				{
					int rc = in.read(buf);
					
					if (rc < 0)
						break;
					
					baos.write(buf, 0, rc);
				}
				
				// Initialize memory with the ROM data
				ReadableMemory sm = new ByteArrayMemory(rombase,
					baos.toByteArray());
				vmem.mapRegion(sm);
			}
			
			// {@squirreljme.error AE05 Could not load SummerCoat ROM. (File)}
			catch (IOException e)
			{
				throw new RuntimeException("AE05 " + romfile, e);
			}
		}
		
		// Dynamically initialized suite memory
		else
		{
			// Create and map dynamic suite region
			SuitesMemory sm = new SuitesMemory(rombase, __sm);
			vmem.mapRegion(sm);
			
			// Initialize suite memory explicitly since we need it!
			sm.__init();
		}
		
		// Initialize RAM
		int ramsize = DefaultConfiguration.DEFAULT_RAM_SIZE,
			ramstart = RAM_START_ADDRESS;
		vmem.mapRegion(new RawMemory(ramstart, ramsize));
		
		// Initialize configuration memory
		WritableMemory cmem = new RawMemory(CONFIG_BASE_ADDR, 8192);
		vmem.mapRegion(cmem);
		
		// Read the boot JAR offset of this packfile
		int bootjaroff = rombase + vmem.memReadInt(rombase +
				MinimizedPackHeader.OFFSET_OF_BOOTJAROFFSET),
			bootjarsize = vmem.memReadInt(rombase +
				MinimizedPackHeader.OFFSET_OF_BOOTJARSIZE);
		
		// Load the bootstrap JAR header
		MinimizedJarHeader bjh;
		try (InputStream bin = new ReadableMemoryInputStream(vmem,
			bootjaroff, bootjarsize))
		{
			bjh = MinimizedJarHeader.decode(bin);
		}
		
		// {@squirreljme.error AE06 Could not read the boot JAR header.}
		catch (IOException e)
		{
			throw new RuntimeException("AE06", e);
		}
		
		// Load the bootstrap JAR header
		int bra = bootjaroff + bjh.bootoffset,
			lram;
		
		// Allocate and initialize configuration data
		StaticAllocator confalloc = new StaticAllocator(CONFIG_BASE_ADDR);
		int xxclasspth = this.__memStrings(bjh, ramstart, vmem, confalloc,
				SummerCoatFactory.classPathToStringArray(__cp)),
			xxsysprops = this.__memStrings(bjh, ramstart, vmem, confalloc,
				SummerCoatFactory.stringMapToStrings(__sprops)),
			xxmainclss = this.__memString(bjh, ramstart, vmem, confalloc,
				__maincl),
			xxmainargs = this.__memStrings(bjh, ramstart, vmem, confalloc,
				__args);
		
		// Load the boot RAM
		try (DataInputStream dis = new DataInputStream(
			new ReadableMemoryInputStream(vmem, bra, bjh.bootsize)))
		{
			// Read entire RAM space
			lram = dis.readInt();
			byte[] bram = new byte[lram];
			dis.readFully(bram);
			
			// Write into memory
			vmem.memWriteBytes(ramstart, bram, 0, lram);
			
			// Handle RAM initializers
			int n = dis.readInt();
			for (int i = 0; i < n; i++)
			{
				int key = dis.readUnsignedByte(),
					addr = dis.readInt() + ramstart,
					mod = (key & 0x0F),
					siz = ((key & 0xF0) >>> 4);
				
				// Which offset is used?
				int off;
				switch (mod)
				{
						// Nothing
					case 0:
						off = 0;
						break;
						
						// RAM
					case 1:
						off = ramstart;
						break;
					
						// JAR
					case 2:
						off = bootjaroff;
						break;
					
						// {@squirreljme.error AE02 Corrupt Boot RAM with
						// invalid value modifier. (Modifier)}
					default:
						throw new VMException("AE02 " + mod);
				}
				
				// Depends on operation size
				switch (siz)
				{
						// Byte
					case 1:
						vmem.memWriteByte(addr, dis.readByte() + off);
						break;
					
						// Short
					case 2:
						vmem.memWriteShort(addr, dis.readShort() + off);
						break;
					
						// Integer
					case 4:
						vmem.memWriteInt(addr, dis.readInt() + off);
						break;
						
						// Long
					case 8:
						{
							long v = dis.readLong() + off;
							vmem.memWriteInt(addr, (int)(v >>> 32));
							vmem.memWriteInt(addr + 4, (int)(v));
						}
						break;
					
						// {@squirreljme.error AE03 Corrupt Boot RAM with
						// invalid size. (Size)}
					default:
						throw new VMException("AE03 " + siz);
				}
			}
			
			// {@squirreljme.AE04 Expected value at end of initializer
			// memory, the Boot RAM is corrupt. (Key value)}
			int key;
			if (-1 != (key = dis.readInt()))
				throw new VMException("AE04 " + key);
		}
		
		// {@squirreljme.error AE01 Could not initialize the boot RAM for
		// the virtual machine.}
		catch (IOException e)
		{
			throw new VMException("AE01", e);
		}
		
		// Setup virtual execution CPU
		NativeCPU cpu = new NativeCPU(vmem);
		NativeCPU.Frame iframe = cpu.enterFrame(bootjaroff + bjh.bootstart,
			ramstart, ramsize, lram, xxclasspth, xxsysprops, xxmainclss,
			xxmainargs, (__ismid ? 1 : 0), __gd, rombase);
		
		// Seed initial frame registers
		iframe._registers[NativeCode.POOL_REGISTER] =
			ramstart + bjh.bootpool;
		iframe._registers[NativeCode.STATIC_FIELD_REGISTER] =
			ramstart + bjh.bootsfieldbase;
		
		// Execute the CPU to boot the machine
		cpu.run();
		
		if (true)
			throw new todo.TODO();
		
		throw new todo.TODO();
		/*
		// Size of RAM
		int ramsize = DefaultConfiguration.DEFAULT_RAM_SIZE,
			sfspace = DefaultConfiguration.DEFAULT_STATIC_FIELD_SIZE;
		
		// Initialize and map virtual memory
		VirtualMemory vmem = new VirtualMemory();
		vmem.mapRegion(sm);
		vmem.mapRegion(new RawMemory(RAM_START_ADDRESS, ramsize));
		
		// Initialize the suite space and load the boot address
		sm.__init();
		int kernaddr = sm._bootjaroff,
			bootaddr = -1;
		
		// Static memory allocation
		StaticAllocator statalloc = new StaticAllocator(RAM_START_ADDRESS);
		
		// Write raw strings into memory which describe the various VM
		// arguments and such. This mostly refers to the class to start once
		// the VM has initialized itself
		int xxclasspth = this.__memStrings(vmem, statalloc,
				SummerCoatFactory.classPathToStringArray(__cp)),
			xxsysprops = this.__memStrings(vmem, statalloc,
				SummerCoatFactory.stringMapToStrings(__sprops)),
			xxmainclss = this.__memString(vmem, statalloc, __maincl),
			xxmainargs = this.__memStrings(vmem, statalloc, __args);
		
		// The base address of the kernel object
		int kobjbase;
		
		// Read the information on the boot class into memory, we need to
		// initialize and setup a bunch of fields for it
		int spoolbase;
		try (InputStream kin = new ReadableMemoryInputStream(vmem,
			kernaddr, 1048576))
		{
			// Decode the class file so we can perform some primitive
			// pre-initialization of the kernel. This is needed because the
			// bootstrap needs access to fields and other methods in the
			// bootstrap class.
			MinimizedClassFile minikern = MinimizedClassFile.decode(kin);
			
			// Allocate the kernel object
			kobjbase = statalloc.allocate(Kernel.OBJECT_BASE_SIZE +
				minikern.header.ifbytes);
			
			// Write fixed class ID into object base and an initial reference
			// count of 1
			vmem.memWriteInt(kobjbase + Kernel.OBJECT_CLASS_OFFSET,
				FixedClassIDs.KERNEL);
			vmem.memWriteInt(kobjbase + Kernel.OBJECT_COUNT_OFFSET,
				1);
			
			// The base address of the static pool along with its size
			int poolcount = minikern.header.poolcount,
				spoolsize = poolcount * 4;
			spoolbase = statalloc.allocate(spoolsize);
			
			// Offset to the code area in this minimized method
			int scodebase = kernaddr + minikern.header.smoff,
				icodebase = kernaddr + minikern.header.imoff;
			
			// Initialize the static pool
			MinimizedPool pool = minikern.pool;
			for (int i = 1; i < poolcount; i++)
			{
				// The resulting converted value
				int cv;
				
				// Handle the type and value
				Object pv = pool.get(i);
				switch (pool.type(i))
				{
						// Just map strings to null
					case STRING:
						cv = 0;
						break;
						
						// Read of another field, will be the field offset
					case ACCESSED_FIELD:
						AccessedField af = (AccessedField)pv;
						
						// The kernel class
						if (af.field().className().equals(minikern.thisName()))
							if (af.type().isStatic())
								cv = Kernel.OBJECT_BASE_SIZE + minikern.field(
									true, af.field().memberNameAndType()).
									offset;
							else
								cv = Kernel.OBJECT_BASE_SIZE + minikern.field(
									false, af.field().memberNameAndType()).
									offset;
						
						// Some other class
						else
							cv = _BAD_POOL_VALUE;
						break;
						
						// Try to map a class index to a pre-existing ID
					case CLASS_NAME:
						cv = FixedClassIDs.of(pv.toString());
						if (cv < 0)
							cv = _BAD_POOL_VALUE;
						break;
						
						// The pointer to the run-time pool for the given
						// class
					case CLASS_POOL:
						if (((ClassPool)pv).name.toString().equals(
							"cc/squirreljme/runtime/cldc/vki/Kernel"))
							cv = spoolbase;
						else
							cv = _BAD_POOL_VALUE;
						break;
						
						// Invoked method
					case INVOKED_METHOD:
						InvokedMethod iv = (InvokedMethod)pv;
						
						// The kernel class
						if (iv.handle().outerClass().equals(
							minikern.thisName()))
							if (iv.type().isStatic())
								cv = scodebase + minikern.method(true, iv.
									handle().nameAndType()).codeoffset;
							else
								cv = icodebase + minikern.method(false, iv.
									handle().nameAndType()).codeoffset;
						
						// Some other class
						else
							cv = _BAD_POOL_VALUE;
						break;
						
						// Integer
					case INTEGER:
						cv = (Integer)pv;
						break;
						
						// Float
					case FLOAT:
						cv = Float.floatToRawIntBits((Float)pv);
						break;
						
						// Long
					case LONG:
						throw new todo.TODO();
						
						// Double
					case DOUBLE:
						throw new todo.TODO();
						
						// Where is this information
					case WHERE_IS_THIS:
						{
							// Get method index part
							int id = pool.part(i, 0);
							
							// Instance method? And get the index
							boolean isinstance = ((id &
								WhereIsThis.INSTANCE_BIT) != 0);
							int mdx = (id & (~WhereIsThis.INSTANCE_BIT));
							
							// Set this to the where offset
							cv = (isinstance ? icodebase : scodebase) +
								minikern.methods(!isinstance)[mdx].whereoffset;
						}
						break;
						
						// These are just informational, ignore for now
					case METHOD_DESCRIPTOR:
					case CLASS_NAMES:
						cv = _BAD_POOL_VALUE;
						break;
						
					default:
						throw new todo.OOPS(pool.type(i).name());
				}
				
				// Store into pool area
				int sld;
				vmem.memWriteInt((sld = spoolbase + (4 * i)), cv);
			}
			
			// Find pointers to methods within the kernel
			for (MinimizedMethod mm : minikern.methods(false))
			{
				// Where this method is located
				int codeoff = (mm.flags().isStatic() ? scodebase : icodebase) +
					mm.codeoffset;
				
				// Depends on the name
				switch (mm.name.toString())
				{
						// The starting point of the kernel (boot)
					case "__start":
						bootaddr = codeoff;
						break;
					
						// Ignore
					default:
						continue;
				}
			}
			
			// Go through instance fields and set their data fields
			for (MinimizedField mf : minikern.fields(false))
			{
				// Memory field offer
				int kfo = kobjbase + Kernel.OBJECT_BASE_SIZE + mf.offset;
				
				// Value depends on the field
				switch (mf.name.toString())
				{
						// ROM address
					case "romaddr":
						vmem.memWriteInt(kfo, SUITE_BASE_ADDR);
						break;
						
						// Kernel class definition (miniform)
					case "kernaddr":
						vmem.memWriteInt(kfo, kernaddr);
						
						// Kernel object base
					case "kobjbase":
						vmem.memWriteInt(kfo, kobjbase);
						break;
						
						// Static memory size
					case "staticmemsize":
						vmem.memWriteInt(kfo, statalloc.size());
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
						
						// Static field space
					case "sfspace":
						vmem.memWriteInt(kfo, sfspace);
						break;
					
						// Ignore
					default:
						continue;
				}
			}
		}
		
		// {@squirreljme.error AE0y Could not read the kernel class for
		// initialization}.
		catch (IOException e)
		{
			throw new RuntimeException("AE0y", e);
		}
		
		// Setup virtual CPU to execute
		NativeCPU cpu = new NativeCPU(vmem);
		NativeCPU.Frame iframe = cpu.enterFrame(bootaddr, kobjbase);
		
		// Seed initial frame registers
		iframe._registers[NativeCode.POOL_REGISTER] = spoolbase;
		
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
		*/
	}
	
	/**
	 * This writes a string to memory somewhere.
	 *
	 * @param __mjh Minimized JAR header.
	 * @param __rams RAM start address.
	 * @param __wm Memory that can be written to.
	 * @param __sa Static allocator.
	 * @param __s The string to encode.
	 * @return The address the string was stored at.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/21
	 */
	private int __memString(MinimizedJarHeader __mjh, int __rams,
		WritableMemory __wm, StaticAllocator __sa, String __s)
		throws NullPointerException
	{
		if (__wm == null || __sa == null || __s == null)
			throw new NullPointerException("NARG");
		
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
		
		// Allocate data
		int rv = __sa.allocate(Constants.ARRAY_BASE_SIZE + encode.length);
		
		// Write class ID, initial refcount, and length
		__wm.memWriteInt(rv + Constants.OBJECT_CLASS_OFFSET,
			__rams + __mjh.bootclassidba);
		__wm.memWriteInt(rv + Constants.OBJECT_COUNT_OFFSET,
			1);
		__wm.memWriteInt(rv + Constants.ARRAY_LENGTH_OFFSET,
			encode.length);
		
		// Write byte data
		int vbase = rv + Constants.ARRAY_BASE_SIZE;
		for (int i = 0, n = encode.length; i < n; i++, vbase++)
			__wm.memWriteByte(vbase, encode[i]);
		
		// Return pointer
		return rv;
	}
	
	/**
	 * Creates an array which is an array of bytes containing all of the
	 * various strings.
	 *
	 * @param __mjh Minimized JAR header.
	 * @param __rams RAM start address.
	 * @param __wm The memory to write to.
	 * @param __sa The allocator used.
	 * @param __ss The strings to convert.
	 * @return The memory address of the {@code byte[][]} array.
	 * @since 2019/04/27
	 */
	private int __memStrings(MinimizedJarHeader __mjh, int __rams,
		WritableMemory __wm, StaticAllocator __sa, String... __ss)
		throws NullPointerException
	{
		if (__sa == null)
			throw new NullPointerException("NARG");
		
		// Force to exist
		if (__ss == null)
			__ss = new String[0];
		
		// The number of elements to store
		int n = __ss.length;
		
		// Allocate data
		int rv = __sa.allocate(Constants.ARRAY_BASE_SIZE + (n * 4));
		
		// Write class ID, initial refcount, and length
		__wm.memWriteInt(rv + Constants.OBJECT_CLASS_OFFSET,
			__rams + __mjh.bootclassidbaa);
		__wm.memWriteInt(rv + Constants.OBJECT_COUNT_OFFSET,
			1);
		__wm.memWriteInt(rv + Constants.ARRAY_LENGTH_OFFSET,
			n);
		
		// Write byte data
		int vbase = rv + Constants.ARRAY_BASE_SIZE;
		for (int i = 0; i < n; i++, vbase += 4)
		{
			String s = __ss[i];
			__wm.memWriteInt(vbase, (s == null ? 0 :
				this.__memString(__mjh, __rams, __wm, __sa, s)));
		}
		
		// Return pointer
		return rv;
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
	 * Converts a string array to a string array.
	 *
	 * @param __s The strings to convert.
	 * @return The resulting string.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/21
	 */
	public static final String[] stringMapToStrings(Map<String, String> __s)
		throws NullPointerException
	{
		if (__s == null)
			throw new NullPointerException("NARG");
		
		// Setup
		int n = __s.size() * 2;
		String[] rv = new String[n];
		
		// Map in keys and values
		int i = 0;
		for (Map.Entry<String, String> s : __s.entrySet())
		{
			rv[i++] = s.getKey();
			rv[i++] = s.getValue();
		}
		
		// Done
		return rv;
	}
}

