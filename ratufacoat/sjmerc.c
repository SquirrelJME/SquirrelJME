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

/** Virtual machine state. */
typedef struct sjme_jvm
{
	/** RAM. */
	void* ram;
	
	/** The size of RAM. */
	sjme_jint ramsize;
	
	/** ROM. */
	void* rom;
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

/** Executes code running within the JVM. */
int sjme_jvmexec(sjme_jvm* jvm)
{
	if (jvm == NULL)
		return 0;
	
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
	
	/* Invalid arguments. */
	if (rom == NULL || ram == NULL || ramsize <= 0 || jvm == NULL)
		return 0;
	
	/* Set boot pointer to start of ROM. */
	rp = rom;
	
	/* Check ROM magic number. */
	if (sjme_memjreadp(4, &rp) != SJME_ROM_MAGIC_NUMBER)
	{
		if (error != NULL)
			*error = SJME_ERROR_INVALIDROMMAGIC;
		
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
		return NULL;
	
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
