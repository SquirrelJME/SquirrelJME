/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Virtual machine configuration.
 * 
 * @since 2023/12/03
 */

#ifndef SQUIRRELJME_VMCONFIG_H
#define SQUIRRELJME_VMCONFIG_H

#include "sjme/nvm/nvm.h"

/* Anti-C++. */
#ifdef __cplusplus
	#ifndef SJME_CXX_IS_EXTERNED
		#define SJME_CXX_IS_EXTERNED
		#define SJME_CXX_SQUIRRELJME_VMCONFIG_H
extern "C" {
	#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif     /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/**
 * The type of setting this is.
 *
 * @since 2023/12/08
 */
typedef enum sjme_vmConfig_settingType
{
	/** The number of setting types. */
	SJME_NUM_VM_CONFIG_SETTING_TYPES
} sjme_vmConfig_settingType;

/**
 * Indicates the category a given configuration setting is in.
 *
 * @since 2023/12/08
 */
typedef enum sjme_vmConfig_categoryType
{
	/** The number of category types available. */
	SJME_NUM_VM_CONFIG_CATEGORY_TYPES
} sjme_vmConfig_categoryType;

/**
 * A flag used for the configuration.
 *
 * @since 2023/12/08
 */
typedef enum sjme_vmConfig_flag
{
	/** Default value is defined. */
	SJME_VM_CONFIG_FLAG_DEFAULT = 1,

	/** Min/Max is defined. */
	SJME_VM_CONFIG_FLAG_MIN_MAX = 2,

	/** Ticks are defined. */
	SJME_VM_CONFIG_FLAG_TICKS = 4,
} sjme_vmConfig_flag;

/**
 * Represents a single value.
 *
 * @since 2023/12/08
 */
typedef union sjme_vmConfig_value
{
	/** Integer value. */
	sjme_jint jint;

	/** String value. */
	sjme_lpcstr jstring;
} sjme_vmConfig_value;

/**
 * Represents a single virtual machine configuration.
 *
 * @since 2023/12/08
 */
typedef struct sjme_vmConfig_setting
{
	/** The category this setting is in. */
	sjme_vmConfig_categoryType category;

	/** The name of this setting. */
	sjme_lpcstr name;

	/** The type of setting this is. */
	sjme_vmConfig_settingType type;

	/** Flags for the configuration setting. */
	sjme_vmConfig_flag flags;

	/** Contains the various values within. */
	struct
	{
		/** The default value, if any. */
		const sjme_vmConfig_value initial;

		/** The minimum value, if applicable. */
		const sjme_vmConfig_value min;

		/** The maximum value, if applicable. */
		const sjme_vmConfig_value max;

		/** The option tick values that are possible, if applicable. */
		const sjme_vmConfig_value ticks[sjme_flexibleArrayCount];
	} value;
} sjme_vmConfig_setting;

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
	#ifdef SJME_CXX_SQUIRRELJME_VMCONFIG_H
}
		#undef SJME_CXX_SQUIRRELJME_VMCONFIG_H
		#undef SJME_CXX_IS_EXTERNED
	#endif /* #ifdef SJME_CXX_SQUIRRELJME_VMCONFIG_H */
#endif     /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_VMCONFIG_H */
