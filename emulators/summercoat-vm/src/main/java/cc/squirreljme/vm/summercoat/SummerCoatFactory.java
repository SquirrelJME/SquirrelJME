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

import cc.squirreljme.emulator.profiler.ProfilerSnapshot;
import cc.squirreljme.emulator.vm.VMException;
import cc.squirreljme.emulator.vm.VMFactory;
import cc.squirreljme.emulator.vm.VMSuiteManager;
import cc.squirreljme.emulator.vm.VirtualMachine;
import cc.squirreljme.jvm.summercoat.constants.BootstrapConstants;
import cc.squirreljme.jvm.summercoat.constants.MemHandleKind;
import cc.squirreljme.jvm.summercoat.constants.PackProperty;
import cc.squirreljme.jvm.summercoat.constants.PackTocProperty;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.vm.VMClassLibrary;
import dev.shadowtail.classfile.nncc.NativeCode;
import dev.shadowtail.jarfile.MinimizedJarHeader;
import dev.shadowtail.jarfile.TableOfContents;
import dev.shadowtail.packfile.MinimizedPackHeader;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Map;

/**
 * This is the factory which is capable of creating instances of the
 * SummerCoat virtual machine.
 *
 * @since 2018/12/29
 */
public class SummerCoatFactory
	extends VMFactory
{
	/** The starting address for RAM. */
	public static final int RAM_START_ADDRESS =
		0x0010_0000;
	
	/** Default size of RAM. */
	@Deprecated
	public static final int DEFAULT_RAM_SIZE =
		33554432;
	
	/** Base address for configuration data. */
	@Deprecated
	public static final int CONFIG_BASE_ADDR =
		0x1000_0000;
	
	/** The base address for suites. */
	public static final int SUITE_BASE_ADDR =
		0x2000_0000;
	
	/** Size of the configuration area. */
	@Deprecated
	public static final int CONFIG_SIZE =
		65536;
	
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
		Map<String, String> __sysProps, String[] __args)
		throws IllegalArgumentException, NullPointerException, VMException
	{
		// Virtual memory which provides access to many parts of memory
		VirtualMemory vMem = new VirtualMemory();
		
		// Setup non-cpu VM state
		MachineState ms = new MachineState(vMem, __ps);
		MemHandleManager memHandles = ms.memHandles;
		
		// The ROM always starts here
		int romBase = SummerCoatFactory.SUITE_BASE_ADDR;
		
		// Load ROM file or generate dynamically for loaded classes
		ReadableMemory romMemory = this.__loadRom(romBase, __sm, __sysProps);
		vMem.mapRegion(romMemory);
		int romSize = romMemory.memRegionSize();
		
		// Read in the appropriate ROM header
		TableOfContents<MinimizedPackHeader>[] packTocOut =
			new TableOfContents[1];
		MinimizedPackHeader packHeader =
			SummerCoatFactory.__loadRomHeader(romMemory, packTocOut);
		TableOfContents<MinimizedPackHeader> packToc = packTocOut[0];
		
		// The index of the JAR we are booting into
		int bootDx = packHeader.get(PackProperty.INDEX_BOOT_JAR);
		
		Debugging.debugNote("PackToc: %s%n", packToc);
		
		// Load the boot JAR information
		MinimizedJarHeader bootJarHeader;
		TableOfContents<MinimizedJarHeader> bootJarToc;
		int bootJarOff;
		try (InputStream packIn = new ReadableMemoryInputStream(romMemory,
			(bootJarOff = packToc.get(bootDx, PackTocProperty.OFFSET_DATA)),
			packToc.get(bootDx, PackTocProperty.SIZE_DATA)))
		{
			// Read the pack header
			bootJarHeader = MinimizedJarHeader.decode(packIn);
			
			// Parse the table of contents
			try (InputStream packTocIn = new ReadableMemoryInputStream(
				romMemory, bootJarHeader.get(PackProperty.OFFSET_TOC),
				bootJarHeader.get(PackProperty.SIZE_TOC)))
			{
				bootJarToc = TableOfContents.<MinimizedJarHeader>decode(
					MinimizedJarHeader.class, packTocIn);
			}
		}
		catch (IOException e)
		{
			throw new VMException("Could not read boot JAR headers.", e);
		}
		
		// Initialize RAM
		int ramSize = SummerCoatFactory.DEFAULT_RAM_SIZE,
			ramAddr = SummerCoatFactory.RAM_START_ADDRESS;
		vMem.mapRegion(new RawMemory(ramAddr, ramSize));
		
		// Load the bootstrap JAR header
		int bootRamOff = bootJarOff + bootJarHeader.getBootoffset(),
			bootRamLen = bootJarHeader.getBootsize();
		
		// Load the boot RAM
		Debugging.debugNote("Loading BootRAM!");
		try (DataInputStream dis = new DataInputStream(
			new ReadableMemoryInputStream(romMemory, bootRamOff, bootRamLen)))
		{
			// BootRAM memory handle IDs to 
			Map<Integer, MemHandle> virtHandles = new HashMap<>();
			
			// Pre-load handles to determine all of their IDs and sizes
			// accordingly before the later load logic happens 
			for (;;)
			{
				// Determine the handle ID for this
				int handleId = dis.readInt();
				
				// No more remain?
				if (handleId == 0)
					break;
				
				// Invalid security bits
				if ((handleId & BootstrapConstants.HANDLE_SECURITY_MASK) !=
					BootstrapConstants.HANDLE_SECURITY_BITS)
					throw new VMException(
						"Invalid security bits: 0b" +
						Integer.toString(handleId, 2));
				
				// Read handle information
				int initCount = dis.readUnsignedShort();
				int byteSize = dis.readUnsignedShort();
				
				// Determine if the kind is valid
				int handleKind = dis.readUnsignedByte();
				if (handleKind <= 0 || handleKind >= MemHandleKind.NUM_KINDS)
					throw new VMException("Invalid MemHandle Kind: " +
						handleKind);
				
				// Setup memory handle for this
				MemHandle memHandle = memHandles.alloc(handleKind, byteSize);
				virtHandles.put(handleId, memHandle);
				
				// Set initial count since it would have gone up on
				// initialization
				memHandle.setCount(initCount);
			}
			
			// Check if the guards are valid or not
			int guardPA = dis.readInt();
			int guardPB = dis.readInt();
			if (guardPA != 0 || guardPB != BootstrapConstants.PRE_SEQ_GUARD)
				throw new VMException(String.format("Invalid Pre: %08x %08x",
					guardPA, guardPB));
			
			// Load handle data writing information
			for (;;)
			{
				// Read the handle we will be writing
				int handleId = dis.readInt();
				if (handleId == 0)
					break;
				
				// Ensure the handle is actually valid!
				MemHandle handle = virtHandles.get(handleId);
				if (handle == null)
					throw new VMException("Invalid handle: " + handleId);
				
				throw Debugging.todo();
			}
			
			// Check if the guards are valid or not
			int guardEA = dis.readInt();
			int guardEB = dis.readInt();
			if (guardEA != 0 || guardEB != BootstrapConstants.MEMORY_SEQ_GUARD)
				throw new VMException(String.format("Invalid End: %08x %08x",
					guardEA, guardEB));
		}
		
		// {@squirreljme.error AE0h Could not initialize the boot RAM for
		// the virtual machine.}
		catch (IOException e)
		{
			throw new VMException("AE0h", e);
		}
		
		throw Debugging.todo("Boot CPU!");
		/*
		// Setup virtual execution CPU
		NativeCPU cpu = new NativeCPU(ms, vMem, 0, __ps);
		NativeCPU.Frame iframe = cpu.enterFrame(bootjaroff + bjh
				.getBootstart());
		
		// Seed initial frame registers
		iframe._registers[NativeCode.POOL_REGISTER] =
			ramAddr + bjh.getBootpool();
		
		// Setup virtual machine with initial thread
		return new SummerCoatVirtualMachine(cpu);*/
	}
	
	/**
	 * Loads the ROM data.
	 * 
	 * @param __romBase The base of the ROM.
	 * @param __sm The suite memory to load from.
	 * @param __sysProps System properties used.
	 * @return The memory that makes up the ROM.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/12/09
	 */
	private ReadableMemory __loadRom(int __romBase, VMSuiteManager __sm,
		Map<String, String> __sysProps)
		throws NullPointerException
	{
		if (__sm == null || __sysProps == null)
			throw new NullPointerException("NARG");
		
		// Try to load a specific ROM file instead of the dynamically
		// generate one?
		String romFile = __sysProps.get("cc.squirreljme.romfile");
		if (romFile == null)
			try
			{
				romFile = System.getProperty("cc.squirreljme.romfile");
			}
			catch (SecurityException ignored)
			{
			}
		
		// Load existing ROM file
		if (romFile != null)
		{
			// Debug
			Debugging.debugNote("Using ROM %s", romFile);
			
			// Resultant ROM memory
			ByteArrayMemory romMemory;
			
			// Copy all of the file data
			Path p = Paths.get(romFile);
			try (InputStream in = Files.newInputStream(p,
					StandardOpenOption.READ);
				ByteArrayOutputStream baos = new ByteArrayOutputStream(
					(int)Files.size(p)))
			{
				// Read data
				byte[] buf = new byte[16384];
				for (;;)
				{
					int rc = in.read(buf);
					
					if (rc < 0)
						break;
					
					baos.write(buf, 0, rc);
				}
				
				// Initialize memory with the ROM data
				romMemory = new ByteArrayMemory(__romBase, baos.toByteArray());
			}
			
			// {@squirreljme.error AE0c Could not load SummerCoat ROM. (File)}
			catch (IOException e)
			{
				throw new RuntimeException("AE0c " + romFile, e);
			}
			
			return romMemory;
		}
		
		// Dynamically initialized suite memory
		else
		{
			// Create and map dynamic suite region
			SuitesMemory sm = new SuitesMemory(__romBase, __sm);
			
			// Initialize suite memory explicitly since we need it!
			sm.__init();
			
			return sm;
		}
	}
	
	/**
	 * Converts a class path to a string array.
	 *
	 * @param __cp The class path to convert.
	 * @return The resulting string array.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/21
	 */
	public static String[] classPathToStringArray(VMClassLibrary... __cp)
		throws NullPointerException
	{
		if (__cp == null)
			throw new NullPointerException("NARG");
		
		int n = __cp.length;
		String[] rv = new String[n];
		for (int i = 0; i < n; i++)
		{
			String name = __cp[i].name();
			rv[i] = (name.endsWith(".jar") ? name : name + ".jar");
		}
		
		return rv;
	}
	
	/**
	 * Loads the ROM header.
	 * 
	 * @param __rom The ROM to read from.
	 * @param __packToc The output table of contents.
	 * @return The read pack header.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/12/12
	 */
	private static MinimizedPackHeader __loadRomHeader(ReadableMemory __rom,
		TableOfContents<MinimizedPackHeader>[] __packToc)
		throws NullPointerException
	{
		if (__rom == null || __packToc == null)
			throw new NullPointerException("NARG");
		
		// Read header data from memory
		try (InputStream packIn = new ReadableMemoryInputStream(__rom))
		{
			// Read the pack header
			MinimizedPackHeader header = MinimizedPackHeader.decode(packIn);
			
			// Parse the table of contents
			try (InputStream packTocIn = new ReadableMemoryInputStream(
				__rom, header.get(PackProperty.OFFSET_TOC),
				header.get(PackProperty.SIZE_TOC)))
			{
				__packToc[0] = TableOfContents.<MinimizedPackHeader>decode(
					MinimizedPackHeader.class, packTocIn);
			}
			
			return header;
		}
		catch (IOException e)
		{
			throw new VMException("Could not read pack headers.", e);
		}
	}
}

