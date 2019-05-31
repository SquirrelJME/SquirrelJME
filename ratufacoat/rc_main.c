/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// --------------------------------------------------------------------------*/

/**
 * Main entry point for the RatufaCoat VM.
 *
 * @since 2019/05/28
 */

#include "ratufac.h"

/* Offset to the BootJAR. */
#define RATUFACOAT_OFFSET_OF_BOOTJAROFFSET 12

/* Offset to the BootJAR size. */
#define RATUFACOAT_OFFSET_OF_BOOTJARSIZE 16

/** Magic number for the boot JAR. */
#define RATUFACOAT_JAR_MAGIC_NUMBER UINT32_C(0x00456570)

/** Boot initializer offset. */
#define RATUFACOAT_OFFSET_OF_JARBOOTOFFSET 20

/** Boot initializer size. */
#define RATUFACOAT_OFFSET_OF_JARBOOTSIZE 24

/** The boot pool offset. */
#define RATUFACOAT_OFFSET_OF_JARBOOTPOOL 28

/** Static field basein RAM. */
#define RATUFACOAT_OFFSET_OF_JARBOOTSFIELDBASE 32

/** The start method offset. */
#define RATUFACOAT_OFFSET_OF_JARBOOTSTART 36

/** The ClassDataV2 for {@code byte[]}. */
#define RATUFACOAT_OFFSET_OF_JARBOOTCLASSIDBA 40

/** The ClassDataV2 for {@code byte[][]}. */
#define RATUFACOAT_OFFSET_OF_JARBOOTCLASSIDBAA 44

/**
 * Searches for and initializes the BootRAM.
 * 
 * @param mach The machine being initialized.
 * @param cpu The bootstrap CPU.
 * @return Non-zero on success.
 * @since 2019/05/31
 */
static int ratufacoat_initbootram(ratufacoat_machine_t* mach,
	ratufacoat_cpu_t* cpu)
{
	void* ram;
	void* rom;
	void* bootjar;
	void* bootram;
	void* wa;
	void* wb;
	uint32_t readp;
	uint32_t bootlen, bootramlen;
	uint32_t initchunklen, initseeds, seed, check;
	uint32_t soff, ahi, alo, xhi, xlo;
	uint8_t skey, smod, ssiz;
	
	// CPU needs to be specified
	if (cpu == NULL)
		return 0;
	
	// Load RAM and ROM pointers
	ram = mach->ram;
	rom = mach->rom;
	
	// Read BootJAR position parameters
	bootjar = (void*)((uintptr_t)rom + ratufacoat_memreadjint(rom,
		RATUFACOAT_OFFSET_OF_BOOTJAROFFSET));
	bootlen = ratufacoat_memreadjint(rom,
		RATUFACOAT_OFFSET_OF_BOOTJARSIZE);
	
	// Note them
	ratufacoat_log("BootJAR @ %p (%d bytes)", bootjar, bootlen);
	
	// Double check magic
	if (ratufacoat_memreadjint(bootjar, 0) != RATUFACOAT_JAR_MAGIC_NUMBER)
	{
		ratufacoat_log("BootJAR has an invalid magic number!");
		
		return 0;
	}
	
	// Get BootRAM Position
	bootram = (void*)((uintptr_t)bootjar + ratufacoat_memreadjint(bootjar,
		RATUFACOAT_OFFSET_OF_JARBOOTOFFSET));
	bootramlen = ratufacoat_memreadjint(bootjar,
		RATUFACOAT_OFFSET_OF_JARBOOTSIZE);
	
	// Initial CPU parameters
	cpu->pc = (uint32_t)((uintptr_t)bootjar + ratufacoat_memreadjint(bootjar,
		RATUFACOAT_OFFSET_OF_JARBOOTSTART));
	cpu->r[RATUFACOAT_STATIC_FIELD_REGISTER] = (uint32_t)((uintptr_t)ram +
		ratufacoat_memreadjint(bootjar,
			RATUFACOAT_OFFSET_OF_JARBOOTSFIELDBASE));
	cpu->r[RATUFACOAT_POOL_REGISTER] = (uint32_t)((uintptr_t)ram +
		ratufacoat_memreadjint(bootjar,
			RATUFACOAT_OFFSET_OF_JARBOOTPOOL));
	
	// Note them
	ratufacoat_log("BootRAM @ %p (%d bytes)", bootram, bootramlen);
	
	// Determine the length of the seed chunk
	initchunklen = ratufacoat_memreadjint(bootram, 0);
	readp = 4;
	
	// Load the seed into RAM
	memmove(ram, (void*)((uintptr_t)bootram + readp), initchunklen);
	readp += initchunklen;
	
	// Read seed count
	initseeds = ratufacoat_memreadjint(bootram, readp);
	readp += 4;
	
	// Debug that
	ratufacoat_log("MvSeeds %d", initseeds);
	
	// Process all seeds
	for (seed = 0; seed < initseeds; seed++)
	{
		// Read key
		skey = ratufacoat_memreadjbyte(bootram, readp);
		readp += 1;
		
		// Decode key into modifier and size
		smod = (skey & 0x0F);
		ssiz = ((skey & 0xF0) >> 4);
		
		// Determine offset to the vlaue
		switch (smod)
		{
				// Nothing
			case 0:
				soff = 0;
				break;
				
				// RAM
			case 1:
				soff = (uint32_t)((uintptr_t)ram);
				break;
				
				// JAR
			case 2:
				soff = (uint32_t)((uintptr_t)bootjar);
				break;
				
				// Invalid
			default:
				ratufacoat_log("Corrupt BootRAM (modifier %d).", (int)smod);
				return 0;
		}
		
		// Read the address to write to
		wa = (void*)((uintptr_t)ram + ratufacoat_memreadjint(bootram, readp));
		wb = (void*)((uintptr_t)wa + 4);
		readp += 4;
		
		// Read value and calculate what to write at memory
		switch (ssiz)
		{
				// byte
			case 1:
				*((int8_t*)wa) = ((int8_t)(
					ratufacoat_memreadjint(bootram, readp) + soff));
				readp += 1;
				break;
				
				// short
			case 2:
				*((int16_t*)wa) = ((int16_t)(
					ratufacoat_memreadjint(bootram, readp) + soff));
				readp += 2;
				break;
				
				// int/float
			case 4:
				*((int32_t*)wa) = ((int32_t)(
					ratufacoat_memreadjint(bootram, readp) + soff));
				readp += 4;
				break;
				
				// long/double
			case 8:
				{
					// Read the high and low parts of the value in their
					// Java format (it does not matter if they get swapped
					// back for the native CPU)
					ahi = ratufacoat_memreadjint(bootram, readp);
					alo = ratufacoat_memreadjint(bootram, readp + 4);
					
					// Perform some 64-bit math to get the true offset for
					// this value. Note that the offset is always a low
					// value!
					xhi = ahi;
					xlo = alo + soff;
					
					// There was an overflow adding
					if (xlo < alo)
						xhi++;
					
					// Write both values to memory
					*((int32_t*)wa) = ahi;
					*((int32_t*)wb) = alo;
					
					// Read in the written high value and if it differs
					// from what the value is, then our system is likely
					// little endian. So write again in opposite order
					if (ratufacoat_memreadjint(wa, 0) != ahi)
					{
						*((int32_t*)wb) = ahi;
						*((int32_t*)wa) = alo;
					}
					
					// Read pointer goes up by eight
					readp += 8;
				}
				break;
			
				// Invalid
			default:
				ratufacoat_log("Corrupt BootRAM (size %d).", (int)ssiz);
				return 0;
		}
	}
	
	// Read in the check value
	check = ratufacoat_memreadjint(bootram, readp);
	if (check != UINT32_C(0xFFFFFFFF))
	{
		ratufacoat_log("Corrupt BootRAM (check %08x).", (int)check);
		return 0;
	}
	
	// Initialized okay!
	return 1;
}

/** Creates RatufaCoat machine. */
ratufacoat_machine_t* ratufacoat_createmachine(ratufacoat_boot_t* boot,
	ratufacoat_cpu_t** xcpu)
{
	ratufacoat_machine_t* rv;
	void* ram;
	void* rom;
	uint32_t ramsize, romsize;
	ratufacoat_cpu_t* cpu;
	
	// This needs to be passed, if not then it shall fail
	if (boot == NULL || boot->native == NULL || xcpu == NULL)
		return NULL;
	
	// Entry point banner
	ratufacoat_log("SquirrelJME RatufaCoat");
	ratufacoat_log("Copyright (C) Stephanie Gawroriski");
	ratufacoat_log("https://squirreljme.cc/");
	
	// Try to allocate the machine data
	rv = ratufacoat_memalloc(sizeof(*rv));
	if (rv == NULL)
		return NULL;
	
	// Copy some basic boot parameters
	rv->native = boot->native;
	rv->args = boot->args;
	
	// Allocate RAM (default to a given RAM size)
	ramsize = boot->ramsize;
	if (ramsize == 0)
		ramsize = RATUFACOAT_DEFAULT_MEMORY_SIZE;
	ram = ratufacoat_memalloc(ramsize);
	
	// Allocate boot CPU as well
	cpu = ratufacoat_memalloc(sizeof(*cpu));
	
	// Make sure they really got allocated
	if (ram == NULL || cpu == NULL)
	{
		ratufacoat_memfree(rv);
		ratufacoat_memfree(cpu);
		
		return NULL;
	}
	
	// Note
	ratufacoat_log("RAM @ %p (%d bytes)", ram, (int)ramsize);
	
	// Set the machine RAM space
	rv->ram = ram;
	rv->ramsize = ramsize;
	
	// Set and note ROM address
	rom = boot->rom;
	romsize = boot->romsize;
	rv->rom = rom;
	rv->romsize = romsize;
	
	// Note it
	ratufacoat_log("ROM @ %p (%d bytes)", rom, (int)romsize);
	
	// Setup common CPU stuff
	cpu->machine = rv;
	
	// Initialize the BootRAM
	if (!ratufacoat_initbootram(rv, cpu))
	{
		ratufacoat_log("Could not initialize the BootRAM!");
		
		ratufacoat_memfree(ram);
		ratufacoat_memfree(rv);
		
		return NULL;
	}
	
	// Setup remaining CPU parameters
	// __rambase
	cpu->r[RATUFACOAT_ARGUMENT_REGISTER_BASE + 0] =
		(uint32_t)((uintptr_t)ram);
	// __ramsize
	cpu->r[RATUFACOAT_ARGUMENT_REGISTER_BASE + 1] =
		ramsize;
	// __bootsize (old)
	cpu->r[RATUFACOAT_ARGUMENT_REGISTER_BASE + 2] =
		0;
	// __classpath
	cpu->r[RATUFACOAT_ARGUMENT_REGISTER_BASE + 3] =
		0;
	// __sysprops
	cpu->r[RATUFACOAT_ARGUMENT_REGISTER_BASE + 4] =
		0;
	// __mainclass
	cpu->r[RATUFACOAT_ARGUMENT_REGISTER_BASE + 5] =
		0;
	// __mainargs
	cpu->r[RATUFACOAT_ARGUMENT_REGISTER_BASE + 6] =
		0;
	// __ismidlet
	cpu->r[RATUFACOAT_ARGUMENT_REGISTER_BASE + 7] =
		0;
	// __gd
	cpu->r[RATUFACOAT_ARGUMENT_REGISTER_BASE + 8] =
		0;
	// __rombase
	cpu->r[RATUFACOAT_ARGUMENT_REGISTER_BASE + 9] =
		(uint32_t)((uintptr_t)rom);
	
	// Set resulting CPU
	*xcpu = cpu;
	
	return rv;
}

/** Logs a message. */
void ratufacoat_log(char* fmt, ...)
{
	va_list args;
	int len;
	
	// Load arguments
	va_start(args, fmt);
	
	// Forward
	vfprintf(stderr, fmt, args);
	
	// End
	va_end(args);
	
	// Always include the ending newline if it was missing
	len = strlen(fmt);
	if (len > 0 && fmt[len - 1] != '\n')
		fprintf(stderr, "\n");
}

/** Fails with a ToDo message. */
void ratufacoat_todo(void)
{
	ratufacoat_log("TODO hit!");
	raise(SIGABRT);
	exit(7);
}

