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

import cc.squirreljme.jvm.ConfigRomType;
import cc.squirreljme.jvm.Constants;
import dev.shadowtail.classfile.mini.MinimizedClassFile;
import dev.shadowtail.classfile.mini.MinimizedField;
import dev.shadowtail.classfile.mini.MinimizedMethod;
import dev.shadowtail.classfile.mini.MinimizedPool;
import dev.shadowtail.classfile.nncc.NativeCode;
import dev.shadowtail.jarfile.MinimizedJarHeader;
import dev.shadowtail.packfile.MinimizedPackHeader;
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
import java.io.DataOutputStream;
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
	/** Default size of RAM. */
	public static final int DEFAULT_RAM_SIZE =
		16777216;
	
	/** The base address for suites. */
	public static final int SUITE_BASE_ADDR =
		0x40000000;
	
	/** Base address for configuration data. */
	public static final int CONFIG_BASE_ADDR =
		0x30000000;
	
	/** Size of the configuration area. */
	public static final int CONFIG_SIZE =
		65536;
	
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
		int rombase = SUITE_BASE_ADDR,
			romsize = 0;
		
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
				
				// Record size of ROM
				romsize = baos.size();
			}
			
			// {@squirreljme.error AE0c Could not load SummerCoat ROM. (File)}
			catch (IOException e)
			{
				throw new RuntimeException("AE0c " + romfile, e);
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
			
			// Rom size is derived from the chunk size
			romsize = sm.size;
		}
		
		// Initialize RAM
		int ramsize = SummerCoatFactory.DEFAULT_RAM_SIZE,
			ramstart = RAM_START_ADDRESS;
		vmem.mapRegion(new RawMemory(ramstart, ramsize));
		
		// Initialize configuration memory
		WritableMemory cmem = new RawMemory(CONFIG_BASE_ADDR, CONFIG_SIZE);
		vmem.mapRegion(cmem);
		
		// Write configuration information
		try (DataOutputStream dos = new DataOutputStream(
			new WritableMemoryOutputStream(cmem, 0, CONFIG_SIZE)))
		{
			// Version
			ConfigRomWriter.writeString(dos, ConfigRomType.JAVA_VM_VERSION,
				"0.3.0");
			
			// Name
			ConfigRomWriter.writeString(dos, ConfigRomType.JAVA_VM_NAME,
				"SquirrelJME SummerCoat");
			
			// Vendor
			ConfigRomWriter.writeString(dos, ConfigRomType.JAVA_VM_VENDOR,
				"Stephanie Gawroriski");
			
			// E-Mail
			ConfigRomWriter.writeString(dos, ConfigRomType.JAVA_VM_EMAIL,
				"xerthesquirrel@gmail.com");
			
			// URL
			ConfigRomWriter.writeString(dos, ConfigRomType.JAVA_VM_URL,
				"https://squirreljme.cc/");
			
			// Guest depth
			ConfigRomWriter.writeInteger(dos, ConfigRomType.GUEST_DEPTH,
				__gd);
			
			// Main class
			if (__maincl != null)
				ConfigRomWriter.writeString(dos, ConfigRomType.MAIN_CLASS,
					__maincl.replace('.', '/'));
			
			// Is midlet?
			ConfigRomWriter.writeInteger(dos, ConfigRomType.IS_MIDLET,
				(__ismid ? 1 : 0));
			
			// System properties
			if (__sprops != null)
				for (Map.Entry<String, String> e : __sprops.entrySet())
				{
					ConfigRomWriter.writeKeyValue(dos,
						ConfigRomType.DEFINE_PROPERTY,
						e.getKey(), e.getValue());
				}
			
			// Class path
			ConfigRomWriter.writeStrings(dos, ConfigRomType.CLASS_PATH,
				SummerCoatFactory.classPathToStringArray(__cp));
			
			// End
			dos.writeShort(ConfigRomType.END);
		}
		
		// {@squirreljme.error AE0d Could not write to configuration ROM.}
		catch (IOException e)
		{
			throw new VMException("AE0d", e);
		}
		
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
		
		// {@squirreljme.error AE0e Could not read the boot JAR header.}
		catch (IOException e)
		{
			throw new RuntimeException("AE0e", e);
		}
		
		// Load the bootstrap JAR header
		int bra = bootjaroff + bjh.bootoffset,
			lram;
		
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
		}
		
		// {@squirreljme.error AE0h Could not initialize the boot RAM for
		// the virtual machine.}
		catch (IOException e)
		{
			throw new VMException("AE0h", e);
		}
		
		// Setup non-cpu VM state
		MachineState ms = new MachineState();
		
		// Setup virtual execution CPU
		NativeCPU cpu = new NativeCPU(ms, vmem, 0, __ps);
		NativeCPU.Frame iframe = cpu.enterFrame(bootjaroff + bjh.bootstart,
			ramstart, ramsize, rombase, romsize,
			CONFIG_BASE_ADDR, CONFIG_SIZE);
		
		// Seed initial frame registers
		iframe._registers[NativeCode.POOL_REGISTER] =
			ramstart + bjh.bootpool;
		iframe._registers[NativeCode.STATIC_FIELD_REGISTER] =
			ramstart + bjh.bootsfieldbase;
		
		// Setup virtual machine with initial thread
		return new SummerCoatVirtualMachine(cpu);
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
		{
			String name = __cp[i].name();
			rv[i] = (name.endsWith(".jar") ? name : name + ".jar");
		}
		
		return rv;
	}
}

