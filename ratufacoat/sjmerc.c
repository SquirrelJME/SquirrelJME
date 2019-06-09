/* ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// --------------------------------------------------------------------------*/

/**
 * SquirrelJME RatufaCoat Source.
 *
 * @since 2019/06/02
 */

#include "sjmerc.h"

/** Default RAM size. */
#define SJME_DEFAULT_RAM_SIZE SJME_JINT_C(16777216)

/** Magic number for ROMs. */
#define SJME_ROM_MAGIC_NUMBER SJME_JINT_C(0x58455223)

/** Magic number for JARs. */
#define SJME_JAR_MAGIC_NUMBER SJME_JINT_C(0x00456570)

/** Check magic for BootRAM. */
#define SJME_BOOTRAM_CHECK_MAGIC SJME_JINT_C(0xFFFFFFFF)

/** Maximum CPU registers. */
#define SJME_MAX_REGISTERS SJME_JINT_C(64)

/** The zero register. */
#define SJME_ZERO_REGISTER SJME_JINT_C(0)

/** The return value register (two slots, 1 + 2). */
#define SJME_RETURN_REGISTER SJME_JINT_C(1)

/** Second return register. */
#define SJME_RETURN_TWO_REGISTER SJME_JINT_C(2)

/** The exception register. */
#define SJME_EXCEPTION_REGISTER SJME_JINT_C(3)

/** The pointer containing static field data. */
#define SJME_STATIC_FIELD_REGISTER SJME_JINT_C(4)

/** Register which represents the current thread of execution. */
#define SJME_THREAD_REGISTER SJME_JINT_C(5)

/** Base for local registers (locals start here). */
#define SJME_LOCAL_REGISTER_BASE SJME_JINT_C(6)

/** The register containing the constant pool. */
#define SJME_POOL_REGISTER SJME_JINT_C(6)

/** The register which contains the next pool pointer to use. */
#define SJME_NEXT_POOL_REGISTER SJME_JINT_C(7)

/** The register of the first argument. */
#define SJME_ARGBASE_REGISTER SJME_JINT_C(8)

/** Maximum number of threads. */
#define SJME_THREAD_MAX SJME_JINT_C(32)

/** Mask for CPU threads. */
#define SJME_THREAD_MASK SJME_JINT_C(31)

/** Thread does not exist. */
#define SJME_THREAD_STATE_NONE 0

/** Thread is running. */
#define SJME_THREAD_STATE_RUNNING 1

/** Virtual CPU. */
typedef struct sjme_cpu
{
	/** The state of this thread. */
	sjme_jint state;
	
	/** PC. */
	void* pc;
	
	/** Registers. */
	sjme_jint r[SJME_MAX_REGISTERS];
} sjme_cpu;

/** Virtual machine state. */
typedef struct sjme_jvm
{
	/** RAM. */
	void* ram;
	
	/** The size of RAM. */
	sjme_jint ramsize;
	
	/** ROM. */
	void* rom;
	
	/** Linearly fair CPU execution engine. */
	sjme_jint fairthreadid;
	
	/** Threads. */
	sjme_cpu threads[SJME_THREAD_MAX];
} sjme_jvm;

/**
 * Allocates the given number of bytes.
 *
 * @param size The number of bytes to allocate.
 * @since 2019/06/07
 */
void* sjme_malloc(sjme_jint size)
{
	void* rv;
	
	/* These will never allocate. */
	if (size <= 0)
		return NULL;
	
	/* Round size and include extra 4-bytes for size storage. */
	size = ((size + SJME_JINT_C(3)) & (~SJME_JINT_C(3))) + SJME_JINT_C(4);
	
#if defined(SJME_IS_LINUX) && SJME_BITS > 32
	/* Use memory map on Linux so it is forced to a 32-bit pointer. */
	rv = mmap(NULL, size, PROT_READ | PROT_WRITE,
		MAP_PRIVATE | MAP_ANONYMOUS | MAP_32BIT, -1, 0);
	if (rv == MAP_FAILED)
		return NULL;
#else
	/* Use standard C function otherwise. */
	rv = calloc(1, size);
#endif

	/* Did not allocate? */
	if (rv == NULL)
		return NULL;
	
	/* Store the size into this memory block for later free. */
	*((sjme_jint*)rv) = size;
	
	/* Return the adjusted pointer. */
	return SJME_POINTER_OFFSET(rv, 4);
}

/**
 * Frees the given pointer.
 *
 * @param p The pointer to free.
 * @since 2019/06/07
 */
void sjme_free(void* p)
{
	void* basep;
	sjme_jint size;
	
	/* Ignore null pointers. */
	if (p == NULL)
		return;
	
	/* Base pointer which is size shifted. */
	basep = SJME_POINTER_OFFSET(p, -4);
	
	/* Read size. */
	size = *((sjme_jint*)basep);

#if defined(SJME_IS_LINUX) && SJME_BITS > 32
	/* Remove the memory mapping. */
	munmap(basep, size);
	
#else
	/* Use Standard C free. */
	free(basep);
#endif
}

/**
 * Reads a value from memory.
 *
 * @param size The size of the value to read.
 * @param ptr The pointer to read from.
 * @param off The offset.
 * @return The read value.
 * @since 2019/06/08
 */
sjme_jint sjme_memread(sjme_jint size, void* ptr, sjme_jint off)
{
	sjme_jint rv;
	
	/* Get the true pointer. */
	ptr = SJME_POINTER_OFFSET(ptr, off);
	
	/* Read value from memory. */
	switch (size)
	{
			/* Byte */
		case 1:
			return *((sjme_jbyte*)ptr);
		
			/* Short */
		case 2:
			return *((sjme_jshort*)ptr);
			
			/* Integer */
		case 4:
		default:
			return *((sjme_jint*)ptr);
	}
}

/**
 * Writes a value to memory.
 *
 * @param size The size of the value to write.
 * @param ptr The pointer to read to.
 * @param off The offset.
 * @param value The value to write.
 * @since 2019/06/08
 */
void sjme_memwrite(sjme_jint size, void* ptr, sjme_jint off, sjme_jint value)
{
	sjme_jint rv;
	
	/* Get the true pointer. */
	ptr = SJME_POINTER_OFFSET(ptr, off);
	
	/* Write value to memory. */
	switch (size)
	{
			/* Byte */
		case 1:
			*((sjme_jbyte*)ptr) = (sjme_jbyte)value;
			break;
		
			/* Short */
		case 2:
			*((sjme_jshort*)ptr) = (sjme_jshort)value;
			break;
			
			/* Integer */
		case 4:
		default:
			*((sjme_jint*)ptr) = value;
			break;
	}
}

/**
 * Reads a big endian Java value from memory.
 * 
 * @param size The size of the value to read.
 * @param ptr The pointer to read from.
 * @param off The offset.
 * @return The read value.
 * @since 2019/06/08
 */
sjme_jint sjme_memjread(sjme_jint size, void* ptr, sjme_jint off)
{
	sjme_jint rv;
	
	/* Get the true pointer. */
	ptr = SJME_POINTER_OFFSET(ptr, off);
	
	/* Read value from memory. */
	switch (size)
	{
			/* Byte */
		case 1:
			return *((sjme_jbyte*)ptr);
		
			/* Short */
		case 2:
			rv = *((sjme_jshort*)ptr);
#if defined(SJME_LITTLE_ENDIAN)
			rv = (rv & SJME_JINT_C(0xFFFF0000)) |
				(((rv << SJME_JINT_C(8)) & SJME_JINT_C(0xFF00)) |
				((rv >> SJME_JINT_C(8)) & SJME_JINT_C(0x00FF)));
#endif
			return rv;
			
			/* Integer */
		case 4:
		default:
			rv = *((sjme_jint*)ptr);
#if defined(SJME_LITTLE_ENDIAN)
			rv = (((rv >> SJME_JINT_C(24)) & SJME_JINT_C(0x000000FF)) |
				((rv >> SJME_JINT_C(8)) & SJME_JINT_C(0x0000FF00)) |
				((rv << SJME_JINT_C(8)) & SJME_JINT_C(0x00FF0000)) |
				((rv << SJME_JINT_C(24)) & SJME_JINT_C(0xFF000000)));
#endif
			return rv;
	}
}

/**
 * Read Java value from memory and increment pointer.
 *
 * @param size The size of value to read.
 * @param ptr The pointer to read from.
 * @return The resulting value.
 * @since 2019/06/08
 */
sjme_jint sjme_memjreadp(sjme_jint size, void** ptr)
{
	sjme_jint rv;
	
	/* Read pointer value. */
	rv = sjme_memjread(size, *ptr, 0);
	
	/* Increment pointer. */
	*ptr = SJME_POINTER_OFFSET(*ptr, size);
	
	/* Return result. */
	return rv;
}

/**
 * Executes single CPU state.
 *
 * @param jvm JVM state.
 * @param cpu CPU state.
 * @param cycles The number of cycles to execute, a negative value means
 * forever.
 * @return The number of remaining cycles.
 * @since 2019/06/08
 */
sjme_jint sjme_cpuexec(sjme_jvm* jvm, sjme_cpu* cpu, sjme_jint cycles)
{
	if (jvm == NULL || cpu == NULL)
		return cycles;
	
	/* Near-Infinite execution loop. */
	for (;;)
	{
		/* Check if we ran out of cycles. */
		if (cycles >= 0)
		{
			if (cycles == 0)
				break;
			if ((--cycles) <= 0)
				break;
		}
	}
	
	/* Return remaining cycles. */
	return cycles;
}

/** Executes code running within the JVM. */
sjme_jint sjme_jvmexec(sjme_jvm* jvm, sjme_jint cycles)
{
	sjme_jint threadid, parkid;
	sjme_cpu* cpu;
	
	/* Do nothing. */
	if (jvm == NULL)
		return 0;
	
	/* Run cooperatively threaded style CPU. */
	for (threadid = jvm->fairthreadid;;
		threadid = ((threadid + 1) & SJME_THREAD_MASK))
	{
		/* Have we used all our execution cycles? */
		if (cycles >= 0)
		{
			if (cycles == 0)
				break;
			if ((--cycles) <= 0)
				break;
		}
		
		/* Ignore CPUs which are not turned on. */
		cpu = &jvm->threads[threadid];
		if (cpu->state == SJME_THREAD_STATE_NONE)
			continue;
		
		/* Execute CPU engine. */
		cycles = sjme_cpuexec(jvm, cpu, cycles);
	}
	
	/* Start next run on the CPU that was last executing. */
	jvm->fairthreadid = (threadid & SJME_THREAD_MASK);
	
	return 0;
}

/**
 * Attempts to load a built-in ROM file.
 *
 * @param nativefuncs Native functions.
 * @param error Error flag.
 * @return The loaded ROM data or {@code NULL} if no ROM was loaded.
 * @since 2019/06/07
 */
void* sjme_loadrom(sjme_nativefuncs* nativefuncs, sjme_jint* error)
{
	void* rv;
	sjme_nativefilename* fn;
	sjme_nativefile* file;
	sjme_jint romsize, xerror, readat, readcount;
	
	/* Need native functions. */
	if (nativefuncs == NULL || nativefuncs->nativeromfile == NULL ||
		nativefuncs->fileopen == NULL || nativefuncs->filesize == NULL ||
		nativefuncs->fileread == NULL)
	{
		if (error != NULL)
			*error = SJME_ERROR_NOFILES;
		
		return NULL;
	}
	
	/* Load file name used for the native ROM. */
	fn = nativefuncs->nativeromfile();
	if (fn == NULL)
	{
		if (error != NULL)
			*error = SJME_ERROR_NONATIVEROM;
		
		return NULL;
	}
	
	/* Set to nothing. */
	rv = NULL;
	
	/* Open ROM. */
	file = nativefuncs->fileopen(fn, SJME_OPENMODE_READ, error);
	if (file != NULL)
	{
		/* Need ROM size. */
		romsize = nativefuncs->filesize(file, error);
		
		/* Allocate ROM into memory. */
		rv = sjme_malloc(romsize);
		if (rv != NULL)
		{
			/* Read whatever is possible. */
			for (readat = 0; readat < romsize;)
			{
				/* Read into raw memory. */
				readcount = nativefuncs->fileread(file,
					SJME_POINTER_OFFSET(rv, readat), romsize - readat,
					&xerror);
				
				/* EOF or error? */
				if (readcount < 0)
				{
					/* End of file reached? */
					if (xerror == SJME_ERROR_ENDOFFILE)
						break;
					
					/* Otherwise fail */
					else
					{
						/* Copy error over. */
						*error = xerror;
						
						/* Free resources. */
						sjme_free(rv);
						rv = NULL;
					}
				}
				
				/* Read count goes up. */
				readat += readcount;
			}
		}
		
		/* Just set error. */
		else
		{
			if (error != NULL)
				*error = SJME_ERROR_NOMEMORY;
		}
		
		/* Close when done. */
		if (nativefuncs->fileclose != NULL)
			nativefuncs->fileclose(file, NULL);
	}
	
	/* Free file name when done. */
	if (nativefuncs->freefilename != NULL)
		nativefuncs->freefilename(fn);
	
	/* Whatever value was used, if possible */
	return rv;
}

/**
 * Initializes the BootRAM, loading it from ROM.
 *
 * @param rom The ROM.
 * @param ram The RAM.
 * @param ramsize The size of RAM.
 * @param jvm The Java VM to initialize.
 * @param error Error flag.
 * @return Non-zero on success.
 * @since 2019/06/07
 */
sjme_jint sjme_initboot(void* rom, void* ram, sjme_jint ramsize, sjme_jvm* jvm,
	sjme_jint* error)
{
	void* rp;
	void* bootjar;
	sjme_jbyte* byteram;
	sjme_jint bootoff, i, n, seedop, seedaddr, seedvalh, seedvall, seedsize;
	sjme_cpu* cpu;
	
	/* Invalid arguments. */
	if (rom == NULL || ram == NULL || ramsize <= 0 || jvm == NULL)
		return 0;
	
	/* Set initial CPU (the first). */
	cpu = &jvm->threads[0];
	cpu->state = SJME_THREAD_STATE_RUNNING;
	
	/* Set boot pointer to start of ROM. */
	rp = rom;
	
	/* Check ROM magic number. */
	if (sjme_memjreadp(4, &rp) != SJME_ROM_MAGIC_NUMBER)
	{
		if (error != NULL)
			*error = SJME_ERROR_INVALIDROMMAGIC;
		
		return 0;
	}
	
	/* Ignore JAR count and BootJAR index. */
	sjme_memjreadp(4, &rp);
	sjme_memjreadp(4, &rp);
	
	/* Read and calculate BootJAR position. */
	rp = bootjar = SJME_POINTER_OFFSET(rom, sjme_memjreadp(4, &rp));
	
	/* Check JAR magic number. */
	if (sjme_memjreadp(4, &rp) != SJME_JAR_MAGIC_NUMBER)
	{
		if (error != NULL)
			*error = SJME_ERROR_INVALIDJARMAGIC;
		
		return 0;
	}
	
	/* Ignore numrc, tocoffset, manifestoff, manifestlen. */
	sjme_memjreadp(4, &rp);
	sjme_memjreadp(4, &rp);
	sjme_memjreadp(4, &rp);
	sjme_memjreadp(4, &rp);
	
	/* Read boot offset for later. */
	bootoff = sjme_memjreadp(4, &rp);
	
	/* Ignore bootsize. */
	sjme_memjreadp(4, &rp);
	
	/* Seed initial CPU state. */
	cpu->r[SJME_POOL_REGISTER] = sjme_memjreadp(4, &rp);
	cpu->r[SJME_STATIC_FIELD_REGISTER] = sjme_memjreadp(4, &rp);
	cpu->pc = SJME_JINT_TO_POINTER(sjme_memjreadp(4, &rp));
	
	/* Bootstrap entry arguments. */
	/* (int __rambase, int __ramsize, int __bootsize, byte[][] __classpath, */
	/* byte[][] __sysprops, byte[] __mainclass, byte[][] __mainargs, */
	/* boolean __ismidlet, int __gd, int __rombase) */
	cpu->r[SJME_ARGBASE_REGISTER + 0] = SJME_POINTER_TO_JINT(ram);
	cpu->r[SJME_ARGBASE_REGISTER + 1] = ramsize;
	cpu->r[SJME_ARGBASE_REGISTER + 2] = 0;
	cpu->r[SJME_ARGBASE_REGISTER + 3] = 0;
	cpu->r[SJME_ARGBASE_REGISTER + 4] = 0;
	cpu->r[SJME_ARGBASE_REGISTER + 5] = 0;
	cpu->r[SJME_ARGBASE_REGISTER + 6] = 0;
	cpu->r[SJME_ARGBASE_REGISTER + 7] = 0;
	cpu->r[SJME_ARGBASE_REGISTER + 8] = 0;
	cpu->r[SJME_ARGBASE_REGISTER + 9] = SJME_POINTER_TO_JINT(rom);
	
	/* Address where the BootRAM is read from. */
	rp = SJME_POINTER_OFFSET(bootjar, bootoff);
	
	/* Copy initial base memory bytes, which is pure big endian. */
	byteram = (sjme_jbyte*)ram;
	n = sjme_memjreadp(4, &rp);
	for (i = 0; i < n; i++)
		byteram[i] = sjme_memjreadp(1, &rp);
	
	/* Load all seeds, which restores natural byte order. */
	n = sjme_memjreadp(4, &rp);
	for (i = 0; i < n; i++)
	{
		/* Read seed information. */
		seedop = sjme_memjreadp(1, &rp);
		seedsize = (seedop >> SJME_JINT_C(4)) & SJME_JINT_C(0xF);
		seedop = (seedop & SJME_JINT_C(0xF));
		seedaddr = sjme_memjreadp(4, &rp);
		
		/* Wide value. */
		if (seedsize == 8)
		{
			seedvalh = sjme_memjreadp(4, &rp);
			seedvall = sjme_memjreadp(4, &rp);
		}
		
		/* Narrow value. */
		else
			seedvalh = sjme_memjreadp(seedsize, &rp);
		
		/* Make sure the seed types are correct. */
		if ((seedsize != 1 && seedsize != 2 &&
			seedsize != 4 && seedsize != 8) || 
			(seedop != 0 && seedop != 1 && seedop != 2) ||
			(seedsize == 8 && seedop != 0))
		{
			if (error != NULL)
				*error = SJME_ERROR_INVALIDBOOTRAMSEED;
			
			return 0;
		}
		
		/* Offset value if it is in RAM or JAR ROM. */
		if (seedop == 1)
			seedvalh += SJME_POINTER_TO_JINT(ram);
		else if (seedop == 2)
			seedvalh += SJME_POINTER_TO_JINT(bootjar);
		
		/* Write long value. */
		if (seedsize == 8)
		{
#if defined(SJME_BIG_ENDIAN)
			sjme_memwrite(4, ram, seedaddr, seedvalh);
			sjme_memwrite(4, ram, seedaddr + SJME_JINT_C(4), seedvall);
#else
			sjme_memwrite(4, ram, seedaddr, seedvall);
			sjme_memwrite(4, ram, seedaddr + SJME_JINT_C(4), seedvalh);
#endif
		}
		
		/* Write narrow value. */
		else
			sjme_memwrite(seedsize, ram, seedaddr, seedvalh);
	}
	
	/* Check end value. */
	if (sjme_memjreadp(4, &rp) != SJME_BOOTRAM_CHECK_MAGIC)
	{
		if (error != NULL)
			*error = SJME_ERROR_INVALIDBOOTRAMEND;
		
		return 0;
	}
}

/** Creates a new instance of the JVM. */
sjme_jvm* sjme_jvmnew(sjme_jvmoptions* options, sjme_nativefuncs* nativefuncs,
	sjme_jint* error)
{
	sjme_jvmoptions nulloptions;
	void* ram;
	void* rom;
	sjme_jvm* rv;
	
	/* We need native functions. */
	if (nativefuncs == NULL)
		return NULL;
	
	/* Allocate VM state. */
	rv = sjme_malloc(sizeof(*rv));
	if (rv == NULL)
	{
		if (error != NULL)
			*error = SJME_ERROR_NOMEMORY;
		
		return NULL;
	}
	
	/* If there were no options specified, just use a null set. */
	if (options == NULL)
	{
		memset(&nulloptions, 0, sizeof(nulloptions));
		options = &nulloptions;
	}
	
	/* If no RAM size was specified then use the default. */
	if (options->ramsize <= 0)
		options->ramsize = SJME_DEFAULT_RAM_SIZE;
	
	/* Allocate RAM. */
	ram = sjme_malloc(options->ramsize);
	if (ram == NULL)
	{
		if (error != NULL)
			*error = SJME_ERROR_NOMEMORY;
		
		return NULL;
	}
	
	/* Needed by the VM. */
	rv->ram = ram;
	rv->ramsize = options->ramsize;
	
	/* Load the ROM? */
	rom = options->presetrom;
	if (rom == NULL)
	{
		/* Call sub-routine which can load the ROM. */
		rom = sjme_loadrom(nativefuncs, error);
		
		/* Could not load the ROM? */
		if (rom == NULL)
		{
			sjme_free(rv);
			sjme_free(ram);
			return NULL;
		}
	}
	
	/* Set JVM rom space. */
	rv->rom = rom;
	
	/* Initialize the BootRAM and boot the CPU. */
	if (sjme_initboot(rom, ram, options->ramsize, rv, error) == 0)
	{
		sjme_free(rv);
		sjme_free(ram);
		return NULL;
	}
	
	/* The JVM is ready to use. */
	return rv;
}
