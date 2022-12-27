/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * SquirrelJME defined JNI interface.
 * 
 * @since 2022/12/11
 */

#ifndef SQUIRRELJME_SJMEJNI_H
#define SQUIRRELJME_SJMEJNI_H

#include "ccfeatures.h"

#if defined(SJME_HAS_STDINT_H)
	#include <stdint.h>
#endif

/* Standard Includes. */
#include <stdlib.h>
#include <stddef.h>
#include <limits.h>
#include <string.h>
#include <stdio.h>
#include <stdarg.h>

/* Anti-C++. */
#ifdef __cplusplus
	#ifndef SJME_CXX_IS_EXTERNED
		#define SJME_CXX_IS_EXTERNED
		#define SJME_CXX_SQUIRRELJME_SJME_JNI_H
extern "C" {
	#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif     /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/** Stringify. */
#define SJME_STRINGIFY(x) SJME_INTERNAL_STRINGIFY_X(x)

/** Stringify, do not use this internal one. */
#define SJME_INTERNAL_STRINGIFY_X(x) #x

/* Fixed size types. */
#if defined(SJME_HAS_STDINT_H)
	/** Byte. */
	typedef int8_t sjme_jbyte;

	/** Unsigned Byte. */
	typedef uint8_t sjme_jubyte;

	/** Character. */
	typedef uint16_t sjme_jchar;

	/** Short. */
	typedef int16_t sjme_jshort;

	/** Integer. */
	typedef int32_t sjme_jint;

	/** Unsigned Integer. */
	typedef uint32_t sjme_juint;

	/** Long. */
	typedef int64_t sjme_jlong;

	/** Unsigned Long. */
	typedef int64_t sjme_julong;

	/** Float. */
	typedef float sjme_jfloat;

	/** Double. */
	typedef double sjme_jdouble;

	/** Pointer. */
	typedef intptr_t sjme_jpointer;

	/** Unsigned Pointer. */
	typedef uintptr_t sjme_jupointer;

#elif defined(SJME_FEATURE_MSVC)
	/** Define stdint types backwards. */
	#define SJME_BACKWARDS_STDINT

	/** Byte. */
	typedef signed __int8 sjme_jbyte;

	/** Unsigned Byte. */
	typedef unsigned __int8 sjme_jubyte;

	/** Character. */
	typedef unsigned __int16 sjme_jchar;

	/** Short. */
	typedef signed __int16 sjme_jshort;

	/** Integer. */
	typedef signed __int32 sjme_jint;

	/** Unsigned Integer. */
	typedef unsigned __int32 sjme_juint;

	/** Long. */
	typedef signed __int64 sjme_jlong;

	/** Unsigned Long. */
	typedef unsigned __int64 sjme_julong;

	/** Float. */
	typedef float sjme_jfloat;

	/** Double. */
	typedef double sjme_jdouble;

	#if defined(_M_X64) || defined(_M_AMD64) || defined(_WIN64)
		/** Pointer. */
		typedef sjme_jlong sjme_jpointer;

		/** Unsigned pointer. */
		typedef sjme_julong sjme_jupointer;
	#else
		/** Pointer. */
		typedef sjme_jint sjme_jpointer;

		/** Unsigned pointer. */
		typedef sjme_juint sjme_jupointer;
	#endif

	/** Signed 8-bit constant. */
	#define INT8_C(x) x

	/** Signed 16-bit constant. */
	#define INT16_C(x) x

	/** Signed 32-bit constant. */
	#define INT32_C(x) x

	/** Unsigned 8-bit constant. */
	#define UINT8_C(x) x##U

	/** Unsigned 16-bit constant. */
	#define UINT16_C(x) x##U

	/** Unsigned 32-bit constant. */
	#define UINT32_C(x) x##U
#else
	#error No standard types are known.
#endif

/** Aliased unsigned short type. */
typedef sjme_jchar sjme_jushort;

#if defined(SJME_FEATURE_GCC)
	typedef enum __attribute__((__packed__)) sjme_jboolean
	{
		/** False. */
		sjme_false = INT8_C(0),

		/** True. */
		sjme_true = INT8_C(1)
	} sjme_jboolean;
#else
	/** False. */
	#define sjme_false INT8_C(0)

	/** True. */
	#define sjme_true INT8_C(1)

	#if defined(SJME_HAS_STDINT_H)
		/** Boolean. */
		typedef uint8_t sjme_jboolean;
	#elif defined(SJME_FEATURE_MSVC)
		/** Boolean. */
		typedef unsigned __int8 sjme_jboolean;
	#else
		#error Unknown boolean type.
	#endif
#endif

/** Interface version 1.1. */
#define SJME_INTERFACE_VERSION_1_1 INT32_C(0x00010001)

/** Interface version 1.2. */
#define SJME_INTERFACE_VERSION_1_2 INT32_C(0x00010002)

/** Interface version 1.4. */
#define SJME_INTERFACE_VERSION_1_4 INT32_C(0x00010004)

/** Interface version 1.6. */
#define SJME_INTERFACE_VERSION_1_6 INT32_C(0x00010006)

/**
 * Error codes within SquirrelJME.
 *
 * @since 2022/12/19
 */
typedef enum sjme_errorCode
{
	/** No interface error. */
	SJME_ERROR_NONE = INT32_C(0),

	/** Unknown/general error. */
	SJME_ERROR_UNKNOWN = INT32_C(-1),

	/** The virtual machine is detached. */
	SJME_ERROR_DETACHED = INT32_C(-2),

	/** The virtual machine version is incorrect. */
	SJME_ERROR_INVALID_VERSION = INT32_C(-3),

	/** No memory. */
	SJME_ERROR_NO_MEMORY = INT32_C(-4),

	/** Virtual machine already exists. */
	SJME_ERROR_VM_ALREADY_EXISTS = INT32_C(-5),

	/** Invalid argument. */
	SJME_ERROR_INVALID_ARGUMENT = INT32_C(-6),

	/** File does not exist. */
	SJME_ERROR_NOSUCHFILE = INT32_C(-7),

	/** End of file reached. */
	SJME_ERROR_ENDOFFILE = INT32_C(-8),

	/** No native ROM file specified. */
	SJME_ERROR_NONATIVEROM = INT32_C(-9),

	/** No support for files. */
	SJME_ERROR_NOFILES = INT32_C(-10),

	/** Invalid ROM magic number. */
	SJME_ERROR_INVALIDROMMAGIC = INT32_C(-11),

	/** Invalid JAR magic number. */
	SJME_ERROR_INVALIDJARMAGIC = INT32_C(-12),

	/** Invalid end of BootRAM. */
	SJME_ERROR_INVALIDBOOTRAMEND = INT32_C(-13),

	/** Invalid BootRAM seed. */
	SJME_ERROR_INVALIDBOOTRAMSEED = INT32_C(-14),

	/** CPU hit breakpoint. */
	SJME_ERROR_CPUBREAKPOINT = INT32_C(-15),

	/** Cannot write Java values. */
	SJME_ERROR_NOJAVAWRITE = INT32_C(-16),

	/** Read error. */
	SJME_ERROR_READ_ERROR = INT32_C(-17),

	/** Early end of file reached. */
	SJME_ERROR_EARLYEOF = INT32_C(-18),

	/** Virtual machine not ready. */
	SJME_ERROR_JVMNOTREADY = INT32_C(-19),

	/** The virtual machine has exited, supervisor boot okay. */
	SJME_ERROR_JVMEXIT_SUV_OKAY = INT32_C(-20),

	/** The virtual machine has exited, the supervisor did not flag! */
	SJME_ERROR_JVMEXIT_SUV_FAIL = INT32_C(-21),

	/** Thread returned at the top-most frame and not through a system call. */
	SJME_ERROR_THREADRETURN = INT32_C(-22),

	/** Bad memory access. */
	SJME_ERROR_BADADDRESS = INT32_C(-23),

	/** Invalid CPU operation. */
	SJME_ERROR_INVALIDOP = INT32_C(-24),

	/** Could not initialize the VMM. */
	SJME_ERROR_VMMNEWFAIL = INT32_C(-25),

	/** Invalid size. */
	SJME_ERROR_INVALIDSIZE = INT32_C(-26),

	/** Address resolution error. */
	SJME_ERROR_ADDRRESFAIL = INT32_C(-27),

	/** Invalid memory type. */
	SJME_ERROR_INVALIDMEMTYPE = INT32_C(-28),

	/** Register value overflowed. */
	SJME_ERROR_REGISTEROVERFLOW = INT32_C(-29),

	/** Could not map address. */
	SJME_ERROR_VMMMAPFAIL = INT32_C(-30),

	/** Null arguments. */
	SJME_ERROR_NULLARGS = INT32_C(-31),

	/** Negative size. */
	SJME_ERROR_NEGATIVE_SIZE = INT32_C(-32),

	/** Invalid memory handle kind. */
	SJME_ERROR_INVALID_MEMHANDLE_KIND = INT32_C(-33),

	/** Out of bounds. */
	SJME_ERROR_OUT_OF_BOUNDS = INT32_C(-24),

	/** Could not seed the RNG. */
	SJME_ERROR_COULD_NOT_SEED = INT32_C(-35),

	/** Destruction failed. */
	SJME_ERROR_DESTROY_FAIL = INT32_C(-36),

	/** Invalid memory handle. */
	SJME_ERROR_INVALID_HANDLE = INT32_C(-37),

	/** Invalid data type. */
	SJME_ERROR_INVALID_DATATYPE = INT32_C(-38),

	/** Invalid boot RAM. */
	SJME_ERROR_INVALID_BOOTRAM = INT32_C(-39),

	/** Unknown library. */
	SJME_ERROR_UNKNOWN_LIBRARY_FORMAT = INT32_C(-40),

	/** Driver not found. */
	SJME_ERROR_DRIVER_NOT_FOUND = INT32_C(-41),

	/** Unknown pack format. */
	SJME_ERROR_UNKNOWN_PACK_FORMAT = INT32_C(-42),

	/** Zero memory allocation. */
	SJME_ERROR_ZERO_MEMORY_ALLOCATION = INT32_C(-43),

	/** Could not initialize driver. */
	SJME_ERROR_BAD_DRIVER_INIT = INT32_C(-44),

	/** Invalid class version. */
	SJME_ERROR_INVALID_CLASS_VERSION = INT32_C(-45),

	/** Unknown format. */
	SJME_ERROR_UNKNOWN_FORMAT = INT32_C(-46),

	/** Invalid number of library entries. */
	SJME_ERROR_INVALID_NUM_LIBRARIES = INT32_C(-47),

	/** Could not close pack file. */
	SJME_ERROR_FAILED_TO_CLOSE_PACK = INT32_C(-48),

	/** The format is not within a valid state. */
	SJME_ERROR_INVALID_FORMAT_STATE = INT32_C(-49),

	/** Invalid counter state. */
	SJME_ERROR_INVALID_COUNTER_STATE = INT32_C(-50),

	/** Invalid load of a library. */
	SJME_ERROR_BAD_LOAD_LIBRARY = INT32_C(-51),

	/** Invalid pack file format. */
	SJME_ERROR_INVALID_PACK_FILE = INT32_C(-52),

	/** The TOC has been corrupted. */
	SJME_ERROR_CORRUPT_TOC = INT32_C(-53),

	/** An entry within a Pack is corrupt. */
	SJME_ERROR_CORRUPT_PACK_ENTRY = INT32_C(-54),

	/** Reverse closing of library failed from within a pack. */
	SJME_ERROR_BAD_PACK_LIB_CLOSE = INT32_C(-55),

	/** The JAR is not valid. */
	SJME_ERROR_INVALID_JAR_FILE = INT32_C(-56),

	/** Not implemented. */
	SJME_ERROR_NOT_IMPLEMENTED = INT32_C(-57),

	/** Invalid stream state. */
	SJME_ERROR_INVALID_STREAM_STATE = INT32_C(-58),

	/** Calculation error. */
	SJME_ERROR_CALCULATE_ERROR = INT32_C(-59),

	/** Not supported. */
	SJME_ERROR_NOT_SUPPORTED = INT32_C(-60),

	/** Engine not found. */
	SJME_ERROR_ENGINE_NOT_FOUND = INT32_C(-61),

	/** Engine initialization failure. */
	SJME_ERROR_ENGINE_INIT_FAILURE = INT32_C(-62),

	/** Invalid functional method. */
	SJME_ERROR_INVALID_FUNCTIONAL = INT32_C(-63),

	/** Invalid unlock key. */
	SJME_ERROR_INVALID_UNLOCK_KEY = INT32_C(-64),

	/** The lock owner is not valid. */
	SJME_ERROR_NOT_LOCK_OWNER = INT32_C(-65),

	/** Invalid lock. */
	SJME_ERROR_INVALID_LOCK = INT32_C(-66),

	/** Bad pipe initialize. */
	SJME_ERROR_BAD_PIPE_INIT = INT32_C(-67),

	/** Protected tag pointer violation. */
	SJME_ERROR_PROTECTED_TAG_VIOLATION = INT32_C(-68),

	/** Wrong sizeof() type used. */
	SJME_ERROR_TAGGED_WRONG_SIZE_OF = INT32_C(-69),

	/** Tagged pointer was not NULL. */
	SJME_ERROR_TAG_NOT_NULL = INT32_C(-70),

	/** Pointer is not null or initialized. */
	SJME_ERROR_POINTER_NOT_NULL = INT32_C(-71),

	/** Double free. */
	SJME_ERROR_DOUBLE_FREE = INT32_C(-72),

	/** The lowest error code. */
	SJME_ERROR_LOWEST_CODE = INT32_C(-73)
} sjme_errorCode;

/** Size type. */
typedef sjme_jint sjme_jsize;

/**
 * The reference type of an object.
 *
 * @since 2022/12/17
 */
typedef enum sjme_jobjectReferenceType
{
	/** Invalid reference type. */
	SJME_JOBJECT_REF_TYPE_INVALID = 0,

	/** Local reference type. */
	SJME_JOBJECT_REF_TYPE_LOCAL = 1,

	/** Global reference type. */
	SJME_JOBJECT_REF_TYPE_GLOBAL = 2,

	/** Weak global reference type. */
	SJME_JOBJECT_REF_TYPE_WEAK_GLOBAL = 3
} sjme_jobjectReferenceType;

/** Object type. */
typedef struct sjme_jobject_internal* sjme_jobject;

/** Class type. */
typedef sjme_jobject sjme_jclass;

/** String type. */
typedef sjme_jobject sjme_jstring;

/** Throwable. */
typedef sjme_jobject sjme_jthrowable;

/** Reference type. */

/** Weak reference. */
typedef sjme_jobject sjme_jweakReference;

/** Array type. */
typedef sjme_jobject sjme_jarray;

/** Boolean array type. */
typedef sjme_jarray sjme_jbooleanArray;

/** Byte array type. */
typedef sjme_jarray sjme_jbyteArray;

/** Character array type. */
typedef sjme_jarray sjme_jcharArray;

/** Short array type. */
typedef sjme_jarray sjme_jshortArray;

/** Integer array type. */
typedef sjme_jarray sjme_jintArray;

/** Long array type. */
typedef sjme_jarray sjme_jlongArray;

/** Float array type. */
typedef sjme_jarray sjme_jfloatArray;

/** Double array type. */
typedef sjme_jarray sjme_jdoubleArray;

/** Object array type. */
typedef sjme_jarray sjme_jobjectArray;

/**
 * Value type.
 *
 * @since 2022/12/11
 */
typedef union sjme_jvalue
{
	sjme_jboolean z;
	sjme_jbyte b;
	sjme_jchar c;
	sjme_jshort s;
	sjme_jint i;
	sjme_jlong j;
	sjme_jfloat f;
	sjme_jdouble d;
	sjme_jobject l;
} sjme_jvalue;

/** Field within the VM, is tagged. */
typedef struct sjme_vmField_internal* sjme_vmField;

/** Method within the VM, is tagged. */
typedef struct sjme_vmMethod_internal* sjme_vmMethod;

/** Stores error information. */
typedef struct sjme_error sjme_error;

/**
 * Virtual machine initialization options.
 *
 * @since 2022/12/11
 */
typedef struct sjme_vmInitOption
{
	/** The option specified on the launch of the virtual machine. */
	char* optionString;

	/** Extra argument information to the option. */
	void* extraInfo;
} sjme_vmInitOption;

/**
 * Initialization arguments for the virtual machine.
 *
 * @since 2022/12/11
 */
typedef struct sjme_vmCmdLine
{
	/** The requesting virtual machine version. */
	sjme_jint version;

	/** The number of specified options. */
	sjme_jint nOptions;

	/** Virtual machine options. */
	sjme_vmInitOption* options;

	/** Ignore unrecognized options? */
	sjme_jboolean ignoreUnrecognized;
} sjme_vmCmdLine;

/** Virtual machine functions. */
typedef struct sjme_vmThread_internal* sjme_vmThread;

/** Virtual machine state. */
typedef struct sjme_vmState_internal* sjme_vmState;

/**
 * Native method registration interface.
 *
 * @since 2022/12/17
 */
typedef struct sjme_vmRegisterNative
{
	/** The name of the method. */
	char* name;

	/** The type signature of the method. */
	char* signature;

	/** The function pointer of the method. */
	void* fnPtr;
} sjme_vmRegisterNative;

/**
 * Virtual machine state.
 *
 * @since 2022/12/11
 */
struct sjme_vmState_internal
{
	#include "sjmejni/tables/interfaceStructFields.h"
};

/**
 * Virtual machine functions.
 *
 * @since 2022/12/11
 */
struct sjme_vmThread_internal
{
	#include "sjmejni/tables/functionStructFields.h"
};

/**
 * Contains the functions which are used to interact with the system to
 * implement the core virtual machine and all of the various shelf interfaces.
 *
 * @since 2022/12/16
 */
typedef struct sjme_vmSysApi
{
	/**
	 * Returns the line ending type of the system.
	 *
	 * @return One of @c cc.squirreljme.jvm.mle.constants.LineEndingType .
	 * @since 2022/12/16
	 */
	sjme_jint (*runtimeLineEnding)(void);
} sjme_vmSysApi;

/**
 * Serialization function.
 *
 * @param buf The buffer containing bytes to write.
 * @param off The offset into the buffer.
 * @param len The number of bytes to write.
 * @return If the write was successful.
 * @since 2022/12/18
 */
typedef sjme_jboolean (*sjme_vmSerializeDataWriteFunc)(const sjme_jbyte* buf,
	sjme_jsize off, sjme_jsize len);

/**
 * Deserialization function.
 *
 * @param buf The buffer containing the buffer to read into.
 * @param off The offset into the buffer.
 * @param len The number of bytes to attempt read.
 * @return The number of bytes read or @c -1 on success, other negative values
 * are indicative of failure.
 * @since 2022/12/18
 */
typedef sjme_jint (*sjme_vmDeserializeDataReadFunc)(sjme_jbyte* buf,
	sjme_jsize off, sjme_jsize len);

/**
 * Called by the deserializer to attach to a given thread when the virtual
 * machine has been deserialized, this should setup any threads and restore
 * state accordingly.
 *
 * @param vm The virtual machine being utilized.
 * @param thread The thread that needs to be attached.
 * @return If thread attaching is successful.
 * @since 2022/12/18
 */
typedef sjme_jboolean (*sjme_vmDeserializeThreadAttach)(sjme_vmState* vm,
	sjme_vmThread* thread);

/**
 * Initializes the arguments with the defaults.
 *
 * @param vmArgs The arguments to fill.
 * @return One of the interface error codes.
 * @since 2022/12/11
 */
sjme_jint sjme_vmDefaultInitArgs(sjme_vmCmdLine* vmArgs);

/**
 * Returns all of the VMs which are active.
 *
 * @param vmBuf The output virtual machines.
 * @param bufLen The length of the buffer.
 * @param nVMs The number of written VMs.
 * @return One of the interface error codes.
 * @since 2022/12/11
 */
sjme_jint sjme_vmGetAllVms(sjme_vmState** vmBuf, sjme_jsize bufLen,
	sjme_jsize* nVMs);

/**
 * Creates a virtual machine along with the functions and state for it.
 *
 * @param outVm The virtual machine output.
 * @param outThread The output thread environment.
 * @param vmArgs The arguments to the virtual machine.
 * @param sysApi System API handles, interacts with the system for operations
 * such as file access and the other shelves.
 * @param error Any resultant error state.
 * @return If initialization was a success or failure.
 * @since 2022/12/11
 */
sjme_jboolean sjme_vmNew(sjme_vmState** outVm, sjme_vmThread** outThread,
	sjme_vmCmdLine* vmArgs, const sjme_vmSysApi* sysApi, sjme_error* error);

/**
 * Ticks the virtual machine executing any functions as needed and otherwise
 * for execution.
 *
 * @param vm The virtual machine being ticked.
 * @param thread The thread to be ticked
 * @param count The number of times to tick before returning.
 * @return If ticking was successful.
 * @since 2022/12/18
 */
sjme_jboolean sjme_vmTick(sjme_vmState* vm, sjme_vmThread* thread,
	sjme_jsize count);

/**
 * Serializes the virtual machine state into the given pointers.
 *
 * @param vmState The input virtual machine state.
 * @param write The serialization function.
 * @return If serialization was successful.
 * @since 2022/128
 */
sjme_jboolean sjme_vmSerialize(sjme_vmState* vmState,
	sjme_vmSerializeDataWriteFunc write);

/**
 * Deserializes the virtual machine and sets up a virtual machine state.
 *
 * @param vmState The input virtual machine state.
 * @param read The serialization function.
 * @param threadAttach This is called after deserialization to attach all
 * of the threads accordingly into the virtual machine.
 * @return If serialization was successful.
 * @since 2022/128
 */
sjme_jboolean sjme_vmDeserialize(sjme_vmState** vmState,
	sjme_vmDeserializeDataReadFunc read,
	sjme_vmDeserializeThreadAttach threadAttach);

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
	#ifdef SJME_CXX_SQUIRRELJME_SJME_JNI_H
}
		#undef SJME_CXX_SQUIRRELJME_SJME_JNI_H
		#undef SJME_CXX_IS_EXTERNED
	#endif /* #ifdef SJME_CXX_SQUIRRELJME_SJME_JNI_H */
#endif     /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_SJMEJNI_H */
