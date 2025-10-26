/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Opaque NanoCoat typedefs which mostly just declare aliases.
 *
 * @file
 * @since 2025/10/26
 */

#ifndef SJME_C_SQUIRRELJME_NVMTYPEDEFS_H
#define SJME_C_SQUIRRELJME_NVMTYPEDEFS_H

#include "sjme/config.h"
#include "sjme/atomic.h"
#include "sjme/list.h"
#include "sjme/nvm/nvm.h"

/* Anti-C++. */
#ifdef __cplusplus
#ifndef SJME_CXX_IS_EXTERNED
#define SJME_CXX_IS_EXTERNED
#define SJME_CXX_SQUIRRELJME_NVMTYPEDEFS_H

extern "C"
{
#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

#pragma region(NvmTypeDefs)
	
/**
 * Program counter address.
 * 
 * @since 2023/07/25
 */
typedef sjme_jint sjme_pcAddr;

/**
 * Static linkage type.
 * 
 * @since 2023/07/25
 */
typedef sjme_jint sjme_staticLinkageType;

/** Single byte code storage type. */
typedef sjme_jubyte sjme_byteCode;

/**
 * Represents an identifier to an interface.
 * 
 * @since 2025/03/26
 */
typedef struct sjme_jinterfaceIDBase sjme_jinterfaceIDBase;

/**
 * Represents an identifier to an interface.
 * 
 * @since 2025/03/26
 */
typedef sjme_jinterfaceIDBase* sjme_jinterfaceID;

/**
 * Represents an identifier to a member.
 * 
 * @since 2025/02/26
 */
typedef struct sjme_jmemberIDBase sjme_jmemberIDBase;

/**
 * Represents an identifier to a member.
 * 
 * @since 2025/02/26
 */
typedef sjme_jmemberIDBase* sjme_jmemberID;

/**
 * Represents an identifier to a method.
 * 
 * @since 2024/10/19
 */
typedef struct sjme_jmethodIDBase sjme_jmethodIDBase;

/**
 * Represents an identifier to a method.
 * 
 * @since 2024/10/19
 */
typedef sjme_jmethodIDBase* sjme_jmethodID;

/**
 * Represents an identifier to a field.
 * 
 * @since 2025/02/26
 */
typedef struct sjme_jfieldIDBase sjme_jfieldIDBase;

/**
 * Represents an identifier to a field.
 * 
 * @since 2025/02/26
 */
typedef sjme_jfieldIDBase* sjme_jfieldID;
	
/**
 * Core method information structure.
 *
 * @since 2024/01/03
 */
typedef struct sjme_nvm_class_methodInfoBase sjme_nvm_class_methodInfoBase;

/**
 * Opaque method information structure.
 *
 * @since 2024/01/03
 */
typedef sjme_nvm_class_methodInfoBase* sjme_nvm_class_methodInfo;

/**
 * Common data structure between all NanoCoat types.
 * 
 * @since 2024/08/09
 */
typedef struct sjme_nvm_commonBase sjme_nvm_commonBase;

/**
 * Common data structure pointer.
 * 
 * @since 2024/08/10
 */
typedef sjme_nvm_commonBase* sjme_nvm_common;
	
/**
 * Represents the virtual machine state.
 * 
 * @since 2023/08/08
 */
typedef struct sjme_nvm_stateBase sjme_nvm_stateBase;

/**
 * Represents the virtual machine state.
 * 
 * @since 2023/07/28
 */
typedef sjme_nvm_stateBase* sjme_nvm;

/**
 * Frame of execution within a thread.
 * 
 * @since 2023/08/08
 */
typedef struct sjme_nvm_frameBase sjme_nvm_frameBase;

/**
 * Frame of execution within a thread.
 * 
 * @since 2023/07/25
 */
typedef sjme_nvm_frameBase* sjme_nvm_frame;

/**
 * Base structure for virtual machine threads.
 * 
 * @since 2024/08/08
 */
typedef struct sjme_nvm_threadBase sjme_nvm_threadBase;

/**
 * A thread within SquirrelJME.
 * 
 * @since 2024/08/08
 */
typedef sjme_nvm_threadBase* sjme_nvm_thread;

/**
 * Boot parameters for NanoCoat.
 *
 * @since 2023/07/27
 */
typedef struct sjme_nvm_bootParam sjme_nvm_bootParam;

/**
 * Standard Suite structure.
 *
 * @since 2023/12/12
 */
typedef struct sjme_nvm_rom_suiteBase sjme_nvm_rom_suiteBase;

/**
 * Opaque suite structure type.
 *
 * @since 2023/12/22
 */
typedef sjme_nvm_rom_suiteBase* sjme_nvm_rom_suite;

/**
 * Structure for a single task.
 *
 * @since 2023/12/17
 */
typedef struct sjme_nvm_taskBase sjme_nvm_taskBase;
	
/**
 * Structure for a single task.
 *
 * @since 2023/12/17
 */
typedef sjme_nvm_taskBase* sjme_nvm_task;
	
/**
 * The configuration that stores the information needed for starting the task.
 *
 * @since 2023/12/17
 */
typedef struct sjme_nvm_task_taskNewConfig sjme_nvm_task_taskNewConfig;
	
/**
 * JDWP state structure.
 *
 * @since 2025/09/07
 */
typedef struct sjme_jdwpBase sjme_jdwpBase;

/**
 * JDWP state structure.
 *
 * @since 2025/09/07
 */
typedef sjme_jdwpBase* sjme_jdwp;

/**
 * Base structure for the class loader.
 * 
 * @since 2024/09/08
 */
typedef struct sjme_nvm_vmClass_loaderBase sjme_nvm_vmClass_loaderBase;

/**
 * Virtual machine equivalent to Java's @code{.java} ClassLoader @endcode .
 * 
 * @since 2024/09/08
 */
typedef sjme_nvm_vmClass_loaderBase* sjme_nvm_vmClass_loader;

/**
 * Specifies how the PC address should be adjusted.
 *
 * @since 2025/01/11
 */
typedef struct sjme_nvm_byteCode_pcNew sjme_nvm_byteCode_pcNew;

/**
 * Standard ROM library structure.
 *
 * @since 2023/12/12
 */
typedef struct sjme_nvm_rom_libraryBase sjme_nvm_rom_libraryBase;

/**
 * Standard ROM library structure.
 *
 * @since 2023/12/12
 */
typedef sjme_nvm_rom_libraryBase* sjme_nvm_rom_library;

/**
 * Core class information structure.
 *
 * @since 2024/01/01
 */
typedef struct sjme_nvm_class_infoCore sjme_nvm_class_infoCore;

/**
 * Opaque class information structure.
 *
 * @since 2024/01/01
 */
typedef struct sjme_nvm_class_infoBase sjme_nvm_class_infoBase;

/**
 * Opaque class information structure.
 *
 * @since 2024/01/01
 */
typedef sjme_nvm_class_infoBase* sjme_nvm_class_info;

/**
 * Opaque constant pool information.
 * 
 * @since 2024/09/13
 */
typedef struct sjme_nvm_class_poolInfoCore sjme_nvm_class_poolInfoCore;

/**
 * A @link SJME_NVM_CLASS_POOL_TYPE_CLASS @endlink which represents a class
 * or interface.
 *
 * @since 2024/01/04
 */
typedef struct sjme_nvm_class_poolEntryClass sjme_nvm_class_poolEntryClass;

/**
 * Opaque constant pool information.
 * 
 * @since 2024/09/13
 */
typedef struct sjme_nvm_class_poolInfoBase sjme_nvm_class_poolInfoBase;

/**
 * Opaque constant pool information.
 * 
 * @since 2024/09/13
 */
typedef sjme_nvm_class_poolInfoBase* sjme_nvm_class_poolInfo;

/**
 * Base field information structure.
 *
 * @since 2024/01/03
 */
typedef struct sjme_nvm_class_fieldInfoBase sjme_nvm_class_fieldInfoBase;

/**
 * Opaque field information structure.
 *
 * @since 2024/01/03
 */
typedef sjme_nvm_class_fieldInfoBase* sjme_nvm_class_fieldInfo;

/**
 * Method code information structure.
 *
 * @since 2024/01/03
 */
typedef struct sjme_nvm_class_codeInfoBase sjme_nvm_class_codeInfoBase;

/**
 * Opaque method code structure.
 *
 * @since 2024/01/03
 */
typedef sjme_nvm_class_codeInfoBase* sjme_nvm_class_codeInfo;

/**
 * A @link SJME_NVM_CLASS_POOL_TYPE_NAME_AND_TYPE @endlink which represents
 * a name and type of member without the class.
 *
 * @since 2024/01/04
 */
typedef struct sjme_nvm_class_poolEntryNameAndType
	sjme_nvm_class_poolEntryNameAndType;
	
/**
 * Exception handling information.
 *
 * @since 2024/01/03
 */
typedef struct sjme_nvm_class_exceptionHandler sjme_nvm_class_exceptionHandler;

#pragma endregion(NvmTypeDefs)
#pragma region(TypeOfSpecifiers)
	
/** sjme_nvm_class_methodInfo is a pointer. */
#define SJME_TYPEOF_IS_POINTER_sjme_nvm_class_methodInfo 1

/** NVM threads are pointers. */
#define SJME_TYPEOF_IS_POINTER_sjme_nvm_thread 1

/** NVM states are pointers. */
#define SJME_TYPEOF_IS_POINTER_sjme_nvm 1

/** NVM tasks are pointers. */
#define SJME_TYPEOF_IS_POINTER_sjme_nvm_task 1

/** NVM frames are pointers. */
#define SJME_TYPEOF_IS_POINTER_sjme_nvm_frame 1

/** Class loaders are pointers. */
#define SJME_TYPEOF_IS_POINTER_sjme_nvm_vmClass_loader 1

/** The type ID of ROM libraries. */
#define SJME_TYPEOF_BASIC_sjme_nvm_rom_library SJME_BASIC_TYPE_ID_OBJECT

/** ROM libraries are pointers. */
#define SJME_TYPEOF_IS_POINTER_sjme_nvm_rom_library 1

/** The basic type of @link sjme_nvm_class_fieldInfo @endlink . */
#define SJME_TYPEOF_BASIC_sjme_nvm_class_fieldInfo \
	SJME_BASIC_TYPE_ID_OBJECT
	
/** Pool class entries are not pointers. */
#define SJME_TYPEOF_IS_POINTER_sjme_nvm_class_poolEntryClass 0
	
/** Pool name and type entries are not pointers. */
#define SJME_TYPEOF_IS_POINTER_sjme_nvm_class_info 1

/** The basic type of @link sjme_nvm_class_methodInfo @endlink . */
#define SJME_TYPEOF_BASIC_sjme_nvm_class_methodInfo \
	SJME_BASIC_TYPE_ID_OBJECT

/** The basic type of @link sjme_nvm_class_exceptionHandler @endlink . */
#define SJME_TYPEOF_BASIC_sjme_nvm_class_exceptionHandler \
	SJME_BASIC_TYPE_ID_OBJECT

/** Pool name and type entries are not pointers. */
#define SJME_TYPEOF_IS_POINTER_sjme_nvm_class_poolEntryNameAndType 0

/** sjme_nvm_threadScheduleMode is not a pointer. */
#define SJME_TYPEOF_IS_POINTER_sjme_nvm_threadScheduleMode 0

/** sjme_nvm_thread_startType is not a pointer. */
#define SJME_TYPEOF_IS_POINTER_sjme_nvm_thread_startType 0

/** sjme_nvm_thread_statusType is not a pointer. */
#define SJME_TYPEOF_IS_POINTER_sjme_nvm_thread_statusType 0
	
#pragma endregion(TypeOfSpecifiers)
#pragma region(AtomicsAndLists)
	
/** List of fields. */
SJME_LIST_DECLARE(sjme_jfieldID, 0);

/** List of tasks. */
SJME_LIST_DECLARE(sjme_nvm_task, 0);
	
/** Atomic @link sjme_nvm_class_methodInfo @endlink . */
SJME_ATOMIC_DECLARE(sjme_nvm_class_methodInfo, 0);
	
/** List of threads. */
SJME_LIST_DECLARE(sjme_nvm_thread, 0);

/** Atomic NVM thread. */
SJME_ATOMIC_DECLARE(sjme_nvm_thread, 0);

/** Atomic NVM State. */
SJME_ATOMIC_DECLARE(sjme_nvm, 0);

/** Atomic NVM task. */
SJME_ATOMIC_DECLARE(sjme_nvm_task, 0);

/** Atomic NVM frame. */
SJME_ATOMIC_DECLARE(sjme_nvm_frame, 0);

/** Atomic class loader. */
SJME_ATOMIC_DECLARE(sjme_nvm_vmClass_loader, 0);

/** List of ROM libraries. */
SJME_LIST_DECLARE(sjme_nvm_rom_library, 0);

/** Atomic ROM library reference. */
SJME_ATOMIC_DECLARE(sjme_nvm_rom_library, 0);

/** Field list. */
SJME_LIST_DECLARE(sjme_nvm_class_fieldInfo, 0);

/** Atomic pointer to a @link sjme_nvm_class_poolEntryClass @endlink . */
SJME_ATOMIC_DECLARE(sjme_nvm_class_poolEntryClass, 1);
	
/** List of class information. */
SJME_LIST_DECLARE(sjme_nvm_class_info, 0);
	
/** Atomic pointer to a @link sjme_nvm_class_info @endlink . */
SJME_ATOMIC_DECLARE(sjme_nvm_class_info, 0);
	
/** Method list. */
SJME_LIST_DECLARE(sjme_nvm_class_methodInfo, 0);

/** Atomic pointer to a @link sjme_nvm_class_poolEntryNameAndType @endlink . */
SJME_ATOMIC_DECLARE(sjme_nvm_class_poolEntryNameAndType, 1);
	
#pragma endregion(AtomicsAndLists)

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
#ifdef SJME_CXX_SQUIRRELJME_NVMTYPEDEFS_H
}
#undef SJME_CXX_SQUIRRELJME_NVMTYPEDEFS_H
#undef SJME_CXX_IS_EXTERNED
#endif /* #ifdef SJME_CXX_SQUIRRELJME_NVMTYPEDEFS_H */
#endif /* #ifdef __cplusplus */

#endif /* SJME_C_SQUIRRELJME_NVMTYPEDEFS_H */
