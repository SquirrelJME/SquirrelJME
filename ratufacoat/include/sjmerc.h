/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// --------------------------------------------------------------------------*/

/**
 * SquirrelJME RatufaCoat Header.
 *
 * @since 2019/06/02
 */

/** Header guard. */
#ifndef SJME_hGRATUFACOATSJMERCHSJMERCH
#define SJME_hGRATUFACOATSJMERCHSJMERCH

/** Standard Includes. */
#include <stddef.h>
#include <limits.h>
#include <string.h>
#include <stdlib.h>
#include <stdio.h>

/** Version string. */
#define SQUIRRELJME_VERSION_STRING "0.3.0"

/** Is this a 64-bit system? */
#if !defined(SJME_BITS)
	#if defined(_LP64) || defined(__LP64__) || defined(__x86_64__) || \
		defined(_M_X64) || defined(_M_AMD64) || defined(__aarch64__) || \
		defined(__ia64__) || defined(__ia64) || defined(_M_IA64) || \
		defined(__itanium__) || defined(__powerpc64__) || \
		defined(__ppc64__) || defined(_ARCH_PPC64) || defined(_ARCH_PPC64)
		#define SJME_BITS 64
	#else
		#define SJME_BITS 32
	#endif
#endif

/** Possibly detect endianess. */
#if !defined(SJME_BIG_ENDIAN) && !defined(SJME_LITTLE_ENDIAN)
	/** Defined by the system? */
	#if !defined(SJME_BIG_ENDIAN)
		#if defined(MSB_FIRST) || \
			(defined(WORDS_BIGENDIAN) && WORDS_BIGENDIAN != 0)
			#define SJME_BIG_ENDIAN
		#endif
	#endif
	
	/** Just set little endian if no endianess was defined */
	#if !defined(SJME_BIG_ENDIAN) && !defined(SJME_LITTLE_ENDIAN)
		#define SJME_LITTLE_ENDIAN
	#endif
	
	/** If both are defined, just set big endian. */
	#if defined(SJME_BIG_ENDIAN) && defined(SJME_LITTLE_ENDIAN)
		#undef SJME_LITTLE_ENDIAN
	#endif
#endif

/** Linux. */
#if defined(__linux__) || defined(__gnu_linux__)
	#define SJME_IS_LINUX 1
#endif

/** DOS? */
#if defined(MSDOS) || defined(_MSDOS) || defined(__MSDOS__) || defined(__DOS__)
	#define SJME_IS_DOS 1
	
	#include <malloc.h>
#endif

/** C99 includes. */
#if (defined(__STDC_VERSION__) && __STDC_VERSION__ >= 199901L) || \
	(defined(__WATCOMC__) && __WATCOMC__ >= 1270) || \
	(defined(_MSC_VER) && _MSC_VER >= 1600) || \
	(defined(__GNUC__) && __GNUC__ >= 4) || \
	(defined(PSP) || defined(PS2))
	#include <stdint.h>

/** Old Microsoft. */
#elif defined(_MSC_VER) && _MSC_VER < 1600
	typedef signed __int8 int8_t;
	typedef signed __int16 int16_t;
	typedef signed __int32 int32_t;
	
	typedef unsigned __int8 uint8_t;
	typedef unsigned __int16 uint16_t;
	typedef unsigned __int32 uint32_t;
	
	#define INT8_C(x) x
	#define INT16_C(x) x
	#define INT32_C(x) x
	
	#define UINT8_C(x) x##U
	#define UINT16_C(x) x##U
	#define UINT32_C(x) x##U

/** Palm OS. */
#elif defined(__palmos__)
	#include <PalmTypes.h>
	
	typedef Int8 int8_t;
	typedef Int16 int16_t;
	typedef Int32 int32_t;
	
	typedef UInt8 uint8_t;
	typedef UInt16 uint16_t;
	typedef UInt32 uint32_t;
	
	typedef int32_t intptr_t;
	typedef uint32_t uintptr_t;
	
	#define INT8_C(x) x
	#define INT16_C(x) x
	#define INT32_C(x) x##L
	
	#define UINT8_C(x) x##U
	#define UINT16_C(x) x##U
	#define UINT32_C(x) x##UL
	
	#define SIZE_MAX UINT32_C(0xFFFFFFFF)

/** Guessed otherwise. */
#else
	#if defined(SCHAR_MAX) && SCHAR_MAX == 127
		typedef signed char int8_t;
		
		#define INT8_C(x) x
	#endif
	
	#if defined(INT_MAX) && INT_MAX == 32767
		typedef signed int int16_t;
		typedef unsigned int uint16_t;
		
		#define INT16_C(x) x
		#define UINT16_C(x) x##U
	#elif defined(SHORT_MAX) && SHORT_MAX == 32767
		typedef signed short int16_t;
		typedef unsigned short uint16_t;
		
		#define INT16_C(x) x
		#define UINT16_C(x) x##U
	#endif
	
	#if defined(INT_MAX) && INT_MAX > 32767
		typedef signed int int32_t;
		typedef unsigned int uint32_t;
		
		#define INT32_C(x) x
	#elif defined(LONG_MAX) && LONG_MAX > 32767
		typedef signed long int32_t;
		typedef unsigned long uint32_t;
		
		#define INT32_C(x) x##L
		#define UINT32_C(x) x##UL
	#endif
#endif

/** Anti-C++. */
#ifdef _cplusplus
#ifndef SJME_CXX_IS_EXTERNED
#define SJME_CXX_IS_EXTERNED
#define SJME_cXRATUFACOATSJMERCHSJMERCH
extern "C"
{
#endif /** #ifdef SJME_CXX_IS_EXTERNED */
#endif /** #ifdef __cplusplus */

/****************************************************************************/

/** {@code byte} type. */
typedef int8_t sjme_jbyte;

/** Unsigned {@code byte} type. */
typedef uint8_t sjme_ubyte;

/** {@code short} type. */
typedef int16_t sjme_jshort;

/** {@code char} type. */
typedef uint16_t sjme_jchar;

/** {@code int} type. */
typedef int32_t sjme_jint;

/** Unsigned {@code int} type. */
typedef uint32_t sjme_juint;

/** Constant value macros. */
#define SJME_JBYTE_C(x) INT8_C(x)
#define SJME_JSHORT_C(x) INT16_C(x)
#define SJME_JCHAR_C(x) UINT16_C(x)
#define SJME_JINT_C(x) INT32_C(x)
#define SJME_JUINT_C(x) UINT32_C(x)

/** Maximum values. */
#define SJME_JINT_MAX_VALUE INT32_C(0x7FFFFFFF)

/** Unsigned short mask. */
#define SJME_JINT_USHORT_MASK INT32_C(0xFFFF)

/** Pointer conversion. */
#define SJME_POINTER_TO_JINT(x) ((sjme_jint)((uintptr_t)(x)))
#define SJME_JINT_TO_POINTER(x) ((void*)((uintptr_t)((sjme_jint)(x))))

#define SJME_POINTER_TO_JMEM(x) (((uintptr_t)(x)))
#define SJME_JMEM_TO_POINTER(x) ((void*)((uintptr_t)((x))))

/** Pointer math. */
#define SJME_POINTER_OFFSET(p, o) SJME_JINT_TO_POINTER( \
	SJME_POINTER_TO_JINT(p) + ((sjme_jint)(o)))

/** Pointer math, no precision loss. */
#define SJME_POINTER_OFFSET_LONG(p, o) SJME_JMEM_TO_POINTER( \
	SJME_POINTER_TO_JMEM(p) + ((uintptr_t)(o)))

/** Standard C format for arguments. */
#define SJME_JVMARG_FORMAT_STDC SJME_JINT_C(1)

/** Open file for reading. */
#define SJME_OPENMODE_READ SJME_JINT_C(1)

/** Open file for read and writing. */
#define SJME_OPENMODE_READWRITE SJME_JINT_C(2)

/** Open file for read and writing, create new file or truncate existing. */
#define SJME_OPENMODE_READWRITETRUNCATE SJME_JINT_C(3)

/** No error. */
#define SJME_ERROR_NONE SJME_JINT_C(0)

/** Unknown error. */
#define SJME_ERROR_UNKNOWN SJME_JINT_C(-1)

/** File does not exist. */
#define SJME_ERROR_NOSUCHFILE SJME_JINT_C(-2)

/** Invalid argument. */
#define SJME_ERROR_INVALIDARG SJME_JINT_C(-3)

/** End of file reached. */
#define SJME_ERROR_ENDOFFILE SJME_JINT_C(-4)

/** No memory available. */
#define SJME_ERROR_NOMEMORY SJME_JINT_C(-5)

/** No native ROM file specified. */
#define SJME_ERROR_NONATIVEROM SJME_JINT_C(-6)

/** No support for files. */
#define SJME_ERROR_NOFILES SJME_JINT_C(-7)

/** Invalid ROM magic number. */
#define SJME_ERROR_INVALIDROMMAGIC SJME_JINT_C(-8)

/** Invalid JAR magic number. */
#define SJME_ERROR_INVALIDJARMAGIC SJME_JINT_C(-9)

/** Invalid end of BootRAM. */
#define SJME_ERROR_INVALIDBOOTRAMEND SJME_JINT_C(-10)

/** Invalid BootRAM seed. */
#define SJME_ERROR_INVALIDBOOTRAMSEED SJME_JINT_C(-11)

/** CPU hit breakpoint. */
#define SJME_ERROR_CPUBREAKPOINT SJME_JINT_C(-12)

/** Cannot write Java values. */
#define SJME_ERROR_NOJAVAWRITE SJME_JINT_C(-13)

/** Read error. */
#define SJME_ERROR_READERROR SJME_JINT_C(-14)

/** Early end of file reached. */
#define SJME_ERROR_EARLYEOF SJME_JINT_C(-15)

/** Virtual machine not ready. */
#define SJME_ERROR_JVMNOTREADY SJME_JINT_C(-16)

/** The virtual machine has exited, supervisor boot okay. */
#define SJME_ERROR_JVMEXIT_SUV_OKAY SJME_JINT_C(-17)

/** The virtual machine has exited, the supervisor did not flag! */
#define SJME_ERROR_JVMEXIT_SUV_FAIL SJME_JINT_C(-18)

/** Thread returned at the top-most frame and not through a system call. */
#define SJME_ERROR_THREADRETURN SJME_JINT_C(-19)

/** Bad memory access. */
#define SJME_ERROR_BADADDRESS SJME_JINT_C(-20)

/** Invalid CPU operation. */
#define SJME_ERROR_INVALIDOP SJME_JINT_C(-21)

/** Could not initialize the VMM. */
#define SJME_ERROR_VMMNEWFAIL SJME_JINT_C(-22)

/** Invalid size. */
#define SJME_ERROR_INVALIDSIZE SJME_JINT_C(-23)

/** Address resolution error. */
#define SJME_ERROR_ADDRRESFAIL SJME_JINT_C(-24)

/** Invalid memory type. */
#define SJME_ERROR_INVALIDMEMTYPE SJME_JINT_C(-25)

/** Register value overflowed. */
#define SJME_ERROR_REGISTEROVERFLOW SJME_JINT_C(-26)

/** Could not map address. */
#define SJME_ERROR_VMMMAPFAIL SJME_JINT_C(-27)

/** VMM Type: Byte. */
#define SJME_VMMTYPE_BYTE SJME_JINT_C(-1)

/** VMM Type: Short. */
#define SJME_VMMTYPE_SHORT SJME_JINT_C(-2)

/** VMM Type: Integer. */
#define SJME_VMMTYPE_INTEGER SJME_JINT_C(-3)

/** VMM Type: Java Short. */
#define SJME_VMMTYPE_JAVASHORT SJME_JINT_C(-4)

/** VMM Type: Java Integer. */
#define SJME_VMMTYPE_JAVAINTEGER SJME_JINT_C(-5)

/** Pixel format: Integer RGB888. */
#define SJME_PIXELFORMAT_INTEGER_RGB888 SJME_JINT_C(0)

/** Pixel format: Byte Indexed. */
#define SJME_PIXELFORMAT_BYTE_INDEXED SJME_JINT_C(1)

/** Pixel format: Short RGB565. */
#define SJME_PIXELFORMAT_SHORT_RGB565 SJME_JINT_C(2)

/** Pixel format: Packed 1-bit. */
#define SJME_PIXELFORMAT_PACKED_ONE SJME_JINT_C(3)

/** Pixel format: Packed 2-bit. */
#define SJME_PIXELFORMAT_PACKED_TWO SJME_JINT_C(4)

/** Pixel format: Packed 4-bit. */
#define SJME_PIXELFORMAT_PACKED_FOUR SJME_JINT_C(5)

/**
 * Marker that indicates that a method returns failure.
 *
 * Will be one of @code SJME_RETURN_SUCCESS @endcode or
 * @code SJME_RETURN_FAIL @endcode.
 */
typedef sjme_jint sjme_returnFail;

/** Method success. @typedef sjme_returnFail. */
#define SJME_RETURN_SUCCESS ((sjme_returnFail)0)

/** Method failure. @typedef sjme_returnFail. */
#define SJME_RETURN_FAIL ((sjme_returnFail)1)

/** This represents an error. */
typedef struct sjme_error
{
	/** Error code. */
	sjme_jint code;
	
	/** The value of it. */
	sjme_jint value;
} sjme_error;

/**
 * Virtual memory information.
 *
 * @since 2019/06/25
 */
typedef struct sjme_vmem sjme_vmem;

/**
 * Virtual memory pointer.
 *
 * @since 2019/06/25
 */
typedef sjme_jint sjme_vmemptr;

/**
 * Virtual memory mapping.
 *
 * @since 2019/06/25
 */
typedef struct sjme_vmemmap
{
	/** The real memory pointer. */
	uintptr_t realptr;
	
	/** The fake memory pointer. */
	sjme_jint fakeptr;
	
	/** The memory region size. */
	sjme_jint size;
	
	/** Banked access function. */
	uintptr_t (*bank)(sjme_jint* offset);
} sjme_vmemmap;

/**
 * Java virtual machine arguments.
 *
 * @since 2019/06/03
 */
typedef struct sjme_jvmargs
{
	/** The format of the arguments. */
	int format;
	
	/** Arguments that can be used. */
	union
	{
		/** Standard C. */
		struct
		{
			/** Argument count. */
			int argc;
			
			/** Arguments. */
			char** argv;
		} stdc;
	} args;
} sjme_jvmargs;

/**
 * Options used to initialize the virtual machine.
 *
 * @since 2019/06/06
 */
typedef struct sjme_jvmoptions
{
	/** The amount of RAM to allocate, 0 is default. */
	sjme_jint ramsize;
	
	/** Preset ROM pointer, does not need loading? */
	void* presetrom;
	
	/** Preset ROM size. */
	sjme_jint romsize;
	
	/** If non-zero then the ROM needs to be copied (address unsafe). */
	sjme_jbyte copyrom;
	
	/** Command line arguments sent to the VM. */
	sjme_jvmargs args;
} sjme_jvmoptions;

/**
 * SQF Font information.
 *
 * @since 2019/06/20
 */
typedef struct sjme_sqf
{
	/** The pixel height of the font. */
	sjme_jint pixelheight;
	
	/** The ascent of the font. */
	sjme_jint ascent;
	
	/** The descent of the font. */
	sjme_jint descent;
	
	/** The bytes per scanline. */
	sjme_jint bytesperscan;
	
	/** Widths for each character. */
	sjme_jbyte* charwidths;
	
	/** Which characters are valid? */
	sjme_jbyte* isvalidchar;
	
	/** Which characters make up the bitmap? */
	sjme_jbyte* charbmp;
} sjme_sqf;

/**
 * Represents the framebuffer for SquirrelJME.
 *
 * @since 2019/06/20
 */
typedef struct sjme_framebuffer
{
	/** Video pixels. */
	sjme_jint* pixels;
	
	/** Screen width. */
	sjme_jint width;
	
	/** Screen height. */
	sjme_jint height;
	
	/** Scanline length. */
	sjme_jint scanlen;
	
	/** Scanline length in bytes. */
	sjme_jint scanlenbytes;
	
	/** The number of bits per pixel. */
	sjme_jint bitsperpixel;
	
	/** The number of available pixels. */
	sjme_jint numpixels;
	
	/** The pixel format used. */
	sjme_jint format;
	
	/** Flush the framebuffer. */
	void (*flush)(void);
} sjme_framebuffer;

/**
 * This represents the name of a file in native form, system dependent.
 *
 * @since 2019/06/08
 */
typedef struct sjme_nativefilename sjme_nativefilename;

/**
 * Represents an open file.
 *
 * @since 2019/06/08
 */
typedef struct sjme_nativefile sjme_nativefile;

/**
 * Instance of the JVM.
 *
 * @since 2019/06/03
 */
typedef struct sjme_jvm sjme_jvm;

/**
 * Native functions available for the JVM to use.
 *
 * @since 2019/06/03
 */
typedef struct sjme_nativefuncs
{
	/** Current monotonic nano-seconds, returns low nanos. */
	sjme_jint (*nanotime)(sjme_jint* hi);
	
	/** Current system clock in Java time, returns low time. */
	sjme_jint (*millitime)(sjme_jint* hi);
	
	/** The filename to use for the native ROM. */
	sjme_nativefilename* (*nativeromfile)(void);
	
	/** Converts the Java char sequence to native filename. */
	sjme_nativefilename* (*nativefilename)(sjme_jint len, sjme_jchar* chars);
	
	/** Frees the specified filename. */
	void (*freefilename)(sjme_nativefilename* filename);
	
	/** Opens the specified file. */
	sjme_nativefile* (*fileopen)(sjme_nativefilename* filename,
		sjme_jint mode, sjme_error* error);
	
	/** Closes the specified file. */
	void (*fileclose)(sjme_nativefile* file, sjme_error* error);
	
	/** Returns the size of the file. */
	sjme_jint (*filesize)(sjme_nativefile* file, sjme_error* error);
	
	/** Reads part of a file. */
	sjme_jint (*fileread)(sjme_nativefile* file, void* dest, sjme_jint len,
		sjme_error* error);
	
	/** Writes single byte to standard output. */
	sjme_jint (*stdout_write)(sjme_jint b);
	
	/** Writes single byte to standard error. */
	sjme_jint (*stderr_write)(sjme_jint b);
	
	/** Obtains the framebuffer. */
	sjme_framebuffer* (*framebuffer)(void);
	
	/** Returns information on where to load optional JAR from. */
	sjme_jint (*optional_jar)(void** ptr, sjme_jint* size);
} sjme_nativefuncs;

/**
 * Allocates the given number of bytes.
 *
 * @param size The number of bytes to allocate.
 * @since 2019/06/07
 */
void* sjme_malloc(sjme_jint size);

/**
 * Frees the given pointer.
 *
 * @param p The pointer to free.
 * @since 2019/06/07
 */
void sjme_free(void* p);

/**
 * Sets the error code.
 *
 * @param error The error to set.
 * @param code The error code.
 * @param value The error value.
 * @since 2019/06/25
 */
void sjme_seterror(sjme_error* error, sjme_jint code, sjme_jint value);

/**
 * Executes code running within the JVM.
 *
 * @param jvm The JVM to execute.
 * @param error JVM execution error.
 * @param cycles The number of cycles to execute for.
 * @return Non-zero if the JVM is resuming, otherwise zero on its exit.
 * @since 2019/06/05
 */
sjme_jint sjme_jvmexec(sjme_jvm* jvm, sjme_error* error, sjme_jint cycles);

/**
 * Destroys the virtual machine instance.
 *
 * @param jvm The JVM to destroy.
 * @param error The error state.
 * @return Non-zero if successful.
 * @since 2019/06/09
 */
sjme_jint sjme_jvmdestroy(sjme_jvm* jvm, sjme_error* error);

/**
 * Creates a new instance of a SquirrelJME JVM.
 *
 * @param args Arguments to the JVM.
 * @param options Options used to initialize the JVM.
 * @param nativefuncs Native functions used in the JVM.
 * @param error Error flag.
 * @return The resulting JVM or {@code NULL} if it could not be created.
 * @since 2019/06/03
 */
sjme_jvm* sjme_jvmNew(sjme_jvmoptions* options, sjme_nativefuncs* nativefuncs,
	sjme_error* error);

/**
 * Creates a new virtual memory manager.
 *
 * @param error The error state.
 * @return The virtual memory manager.
 * @since 2019/06/25
 */
sjme_vmem* sjme_vmmnew(sjme_error* error);

/**
 * Virtually maps the given region of memory.
 *
 * @param vmem The virtual memory to map to.
 * @param at The address to map to, if possible to map there.
 * @param ptr The region to map.
 * @param size The number of bytes to map.
 * @param error The error state.
 * @return The memory mapping information, returns @code NULL @endcode on
 * error.
 * @since 2019/06/25
 */
sjme_vmemmap* sjme_vmmmap(sjme_vmem* vmem, sjme_jint at, void* ptr,
	sjme_jint size, sjme_error* error);

/**
 * Resolves the given memory pointer.
 *
 * @param vmem The virtual memory.
 * @param ptr The pointer to resolve.
 * @param off The offset.
 * @param error The error.
 * @since 2019/06/27
 */
void* sjme_vmmresolve(sjme_vmem* vmem, sjme_vmemptr ptr, sjme_jint off,
	sjme_error* error);

/**
 * Convert size to Java type.
 *
 * @param type The input size.
 * @param error The error state.
 * @return The resulting type.
 * @since 2019/06/25
 */
sjme_jint sjme_vmmsizetojavatype(sjme_jint size, sjme_error* error);

/**
 * Convert size to type.
 *
 * @param type The input size.
 * @param error The error state.
 * @return The resulting type.
 * @since 2019/06/25
 */
sjme_jint sjme_vmmsizetotype(sjme_jint size, sjme_error* error);

/**
 * Reads from virtual memory.
 *
 * @param vmem Virtual memory.
 * @param type The type to utilize.
 * @param ptr The pointer address.
 * @param off The offset.
 * @param error The error state.
 * @return The read value.
 * @since 2019/06/25
 */
sjme_jint sjme_vmmread(sjme_vmem* vmem, sjme_jint type, sjme_vmemptr ptr,
	sjme_jint off, sjme_error* error);

/**
 * Reads from virtual memory.
 *
 * @param vmem Virtual memory.
 * @param type The type to utilize.
 * @param ptr The pointer address, is incremented accordingly.
 * @param error The error state.
 * @return The read value.
 * @since 2019/06/25
 */
sjme_jint sjme_vmmreadp(sjme_vmem* vmem, sjme_jint type, sjme_vmemptr* ptr,
	sjme_error* error);

/**
 * Write to virtual memory.
 *
 * @param vmem Virtual memory.
 * @param type The type to utilize.
 * @param ptr The pointer address.
 * @param off The offset.
 * @param val The value to write.
 * @param error The error state.
 * @since 2019/06/25
 */
void sjme_vmmwrite(sjme_vmem* vmem, sjme_jint type, sjme_vmemptr ptr,
	sjme_jint off, sjme_jint val, sjme_error* error);

/**
 * Write to virtual memory.
 *
 * @param vmem Virtual memory.
 * @param type The type to utilize.
 * @param ptr The pointer address, is incremented accordingly.
 * @param val The value to write.
 * @param error The error state.
 * @since 2019/06/25
 */
void sjme_vmmwritep(sjme_vmem* vmem, sjme_jint type, sjme_vmemptr* ptr,
	sjme_jint val, sjme_error* error);

/**
 * Atomically reads, checks, and then sets the value.
 *
 * @param vmem Virtual memory.
 * @param check The check value.
 * @param set The set value.
 * @param ptr The pointer address.
 * @param off The offset.
 * @param error The error state.
 * @return The value which was read.
 * @since 2019/07/01
 */
sjme_jint sjme_vmmatomicintcheckgetandset(sjme_vmem* vmem, sjme_jint check,
	sjme_jint set, sjme_vmemptr ptr, sjme_jint off, sjme_error* error);

/**
 * Atomically increments and integer and then gets its value.
 *
 * @param vmem Virtual memory.
 * @param ptr The pointer address.
 * @param off The offset.
 * @param add The value to add.
 * @param error The error state.
 * @return The value after the addition.
 * @since 2019/06/25
 */
sjme_jint sjme_vmmatomicintaddandget(sjme_vmem* vmem,
	sjme_vmemptr ptr, sjme_jint off, sjme_jint add, sjme_error* error);

/****************************************************************************/

/** Anti-C++. */
#ifdef __cplusplus
#ifdef SJME_cXRATUFACOATSJMERCHSJMERCH
}
#undef SJME_cXRATUFACOATSJMERCHSJMERCH
#undef SJME_CXX_IS_EXTERNED
#endif /** #ifdef SJME_cXRATUFACOATSJMERCHSJMERCH */
#endif /** #ifdef __cplusplus */

/** Header guard. */
#endif /* #ifndef SJME_hGRATUFACOATSJMERCHSJMERCH */

