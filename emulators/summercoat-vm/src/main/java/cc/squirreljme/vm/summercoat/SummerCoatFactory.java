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
import cc.squirreljme.jvm.config.ConfigRomKey;
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
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Map;
import todo.DEBUG;

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
		try (InputStream packIn = new ReadableMemoryInputStream(romMemory,
			packToc.get(bootDx, PackTocProperty.OFFSET_DATA),
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
		
		// Address and decoded header of the boot JAR
		if (true)
			throw Debugging.todo();
		int bootJarAddr = 0;
		
		// Initialize RAM
		int ramSize = SummerCoatFactory.DEFAULT_RAM_SIZE,
			ramAddr = SummerCoatFactory.RAM_START_ADDRESS;
		vMem.mapRegion(new RawMemory(ramAddr, ramSize));
		
		// Initialize configuration memory
		WritableMemory cmem = new RawMemory(SummerCoatFactory.CONFIG_BASE_ADDR,
			SummerCoatFactory.CONFIG_SIZE);
		vMem.mapRegion(cmem);
		
		// Read the boot JAR offset of this packfile
		int bootjaroff = romBase + vMem.memReadInt(romBase +
				MinimizedPackHeader.OFFSET_OF_BOOTJAROFFSET),
			bootjarsize = vMem.memReadInt(romBase +
				MinimizedPackHeader.OFFSET_OF_BOOTJARSIZE);
		
		// Load the bootstrap JAR header
		MinimizedJarHeader bjh;
		try (InputStream bin = new ReadableMemoryInputStream(vMem,
			bootjaroff, bootjarsize))
		{
			bjh = MinimizedJarHeader.decode(bin);
		}
		
		// {@squirreljme.error AE0e Could not read the boot JAR header.}
		catch (IOException e)
		{
			throw new RuntimeException("AE0e", e);
		}
		
		// Write configuration information
		try (DataOutputStream dos = new DataOutputStream(
			new WritableMemoryOutputStream(cmem, 0,
				SummerCoatFactory.CONFIG_SIZE)))
		{
			// Version
			ConfigRomWriter.writeString(dos, ConfigRomKey.JAVA_VM_VERSION,
				"0.3.0");
			
			// Name
			ConfigRomWriter.writeString(dos, ConfigRomKey.JAVA_VM_NAME,
				"SquirrelJME SummerCoat");
			
			// Vendor
			ConfigRomWriter.writeString(dos, ConfigRomKey.JAVA_VM_VENDOR,
				"Stephanie Gawroriski");
			
			// E-Mail
			ConfigRomWriter.writeString(dos, ConfigRomKey.JAVA_VM_EMAIL,
				"xerthesquirrel@gmail.com");
			
			// URL
			ConfigRomWriter.writeString(dos, ConfigRomKey.JAVA_VM_URL,
				"https://squirreljme.cc/");
			
			// Guest depth
			/*ConfigRomWriter.writeInteger(dos, ConfigRomKey.GUEST_DEPTH,
				__gd);*/
			
			// Main class
			if (__maincl != null)
				ConfigRomWriter.writeString(dos, ConfigRomKey.MAIN_CLASS,
					__maincl.replace('.', '/'));
			
			// Is midlet?
			/*ConfigRomWriter.writeInteger(dos, ConfigRomKey.IS_MIDLET,
				(__ismid ? 1 : -1));*/
			
			// System properties
			if (__sysProps != null)
				for (Map.Entry<String, String> e : __sysProps.entrySet())
				{
					ConfigRomWriter.writeKeyValue(dos,
						ConfigRomKey.DEFINE_PROPERTY,
						e.getKey(), e.getValue());
				}
			
			// Class path
			ConfigRomWriter.writeStrings(dos, ConfigRomKey.CLASS_PATH,
				SummerCoatFactory.classPathToStringArray(__cp));
			
			// System call handler
			ConfigRomWriter.writeInteger(
				dos, ConfigRomKey.SYSCALL_STATIC_FIELD_POINTER,
				ramAddr + bjh.getSyscallsfp());
			ConfigRomWriter.writeInteger(
				dos, ConfigRomKey.SYSCALL_CODE_POINTER,
				bootjaroff + bjh.getSyscallhandler());
			ConfigRomWriter.writeInteger(
				dos, ConfigRomKey.SYSCALL_POOL_POINTER,
				ramAddr + bjh.getSyscallpool());
			
			// End
			dos.writeShort(ConfigRomKey.END);
		}
		
		// {@squirreljme.error AE0d Could not write to configuration ROM.}
		catch (IOException e)
		{
			throw new VMException("AE0d", e);
		}
		
		// Load the bootstrap JAR header
		int bra = bootjaroff + bjh.getBootoffset(),
			lram;
		
		// Debug
		if (NativeCPU.ENABLE_DEBUG)
			DEBUG.note("Unpacking BootRAM!");
		
		// Load the boot RAM
		try (DataInputStream dis = new DataInputStream(
			new ReadableMemoryInputStream(vMem, bra, bjh.getBootsize())))
		{
			// Indicate where it is
			Debugging.debugNote("BootRAM: Addr=%08x Len=%d",
				bra, bjh.getBootsize());
			
			// Read entire RAM space
			lram = dis.readInt();
			byte[] bram = new byte[lram];
			dis.readFully(bram);
			
			// Write into memory
			vMem.memWriteBytes(ramAddr, bram, 0, lram);
			
			// Handle RAM initializers
			int n = dis.readInt();
			for (int i = 0; i < n; i++)
			{
				int key = dis.readUnsignedByte(),
					addr = dis.readInt() + ramAddr,
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
						off = ramAddr;
						break;
					
						// JAR
					case 2:
						off = bootjaroff;
						break;
					
						// {@squirreljme.error AE0f Corrupt Boot RAM with
						// invalid value modifier. (Modifier)}
					default:
						throw new VMException("AE0f " + mod);
				}
				
				// Depends on operation size
				switch (siz)
				{
						// Byte
					case 1:
						vMem.memWriteByte(addr, dis.readByte() + off);
						break;
					
						// Short
					case 2:
						vMem.memWriteShort(addr, dis.readShort() + off);
						break;
					
						// Integer
					case 4:
						vMem.memWriteInt(addr, dis.readInt() + off);
						break;
						
						// Long
					case 8:
						{
							long v = dis.readLong() + off;
							vMem.memWriteInt(addr, (int)(v >>> 32));
							vMem.memWriteInt(addr + 4, (int)(v));
						}
						break;
					
						// {@squirreljme.error AE0g Corrupt Boot RAM with
						// invalid size. (Size)}
					default:
						throw new VMException("AE0g " + siz);
				}
			}
			
			// {@squirreljme.AE04 Expected value at end of initializer
			// memory, the Boot RAM is corrupt. (Key value)}
			int key;
			if (-1 != (key = dis.readInt()))
				throw new VMException("AE04 " + key);
				
			// Debug
			if (NativeCPU.ENABLE_DEBUG)
				DEBUG.note("BootRAM unpacked at %08x (size %d)!",
					ramAddr, lram);
		}
		
		// {@squirreljme.error AE0h Could not initialize the boot RAM for
		// the virtual machine.}
		catch (IOException e)
		{
			throw new VMException("AE0h", e);
		}
		
		// Setup virtual execution CPU
		NativeCPU cpu = new NativeCPU(ms, vMem, 0, __ps);
		NativeCPU.Frame iframe = cpu.enterFrame(bootjaroff + bjh
				.getBootstart(),
			ramAddr, ramSize, romBase, romSize,
			SummerCoatFactory.CONFIG_BASE_ADDR, SummerCoatFactory.CONFIG_SIZE);
		
		// Seed initial frame registers
		iframe._registers[NativeCode.POOL_REGISTER] =
			ramAddr + bjh.getBootpool();
		iframe._registers[NativeCode.STATIC_FIELD_REGISTER] =
			ramAddr + bjh.getBootsfieldbase();
		
		// Setup virtual machine with initial thread
		return new SummerCoatVirtualMachine(cpu);
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

