/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include <string.h>

#include "sjme/nvm/nvm.h"
#include "sjme/debug.h"
#include "3rdparty/libretro/libretro.h"
#include "frontend/libretro/shared.h"

/** The description of input controls. */
const struct retro_controller_description sjme_libretro_inputControlDesc[] =
{
	{"Dial Pad", RETRO_DEVICE_JOYPAD},
	{"Game Pad", RETRO_DEVICE_SUBCLASS(RETRO_DEVICE_JOYPAD, 1)}
};

/** Controller information. */
const struct retro_controller_info sjme_libretro_inputControllers[] =
{
	{sjme_libretro_inputControlDesc, 2},
	{0, 0}
};

/** Input descriptions:
 * [SOFT LEFT] [SOFT RIGHT]
 * [BACK]
 * [1  o~o] [2  abc] [3  def]
 * [4  ghi] [5  jkl] [6  mno]
 * [7 pqrs] [8  tuv] [9 wxyz]
 * [*  shf] [0    +] [# spac]
 * RetroPad looks like so:
 *    X-1
 * Y-7   A-3
 *    B-9
 */
static const struct retro_input_descriptor sjme_libretro_descDialPad[] =
{
	/* SOFT LEFT. */
	{SJME_LIBRETRO_JOYPAD_PORT, RETRO_DEVICE_JOYPAD, 0,
		RETRO_DEVICE_ID_JOYPAD_SELECT, "Soft 1 (Left Shoulder)"},

	/* SOFT RIGHT. */
	{SJME_LIBRETRO_JOYPAD_PORT, RETRO_DEVICE_JOYPAD, 0,
		RETRO_DEVICE_ID_JOYPAD_START, "Soft 2 (Right Shoulder)"},

	/* [1  o~o] */
	{SJME_LIBRETRO_JOYPAD_PORT, RETRO_DEVICE_JOYPAD, 0,
		RETRO_DEVICE_ID_JOYPAD_X, "1 Voicemail (Game A)"},

	/* [2  abc] */
	{SJME_LIBRETRO_JOYPAD_PORT, RETRO_DEVICE_JOYPAD, 0,
		RETRO_DEVICE_ID_JOYPAD_UP, "2 ABC (Game Up)"},

	/* [3  def] */
	{SJME_LIBRETRO_JOYPAD_PORT, RETRO_DEVICE_JOYPAD, 0,
		RETRO_DEVICE_ID_JOYPAD_A, "3 DEF (Game B)"},

	/* [4  ghi] */
	{SJME_LIBRETRO_JOYPAD_PORT, RETRO_DEVICE_JOYPAD, 0,
		RETRO_DEVICE_ID_JOYPAD_LEFT, "4 GHI (Game Left)"},

	/* [5  jkl] */
	{SJME_LIBRETRO_JOYPAD_PORT, RETRO_DEVICE_JOYPAD, 0,
		RETRO_DEVICE_ID_JOYPAD_L2, "5 JKL (Game Fire/Select)"},

	/* [6  mno] */
	{SJME_LIBRETRO_JOYPAD_PORT, RETRO_DEVICE_JOYPAD, 0,
		RETRO_DEVICE_ID_JOYPAD_RIGHT, "6 MNO (Game Right)"},

	/* [7 pqrs] */
	{SJME_LIBRETRO_JOYPAD_PORT, RETRO_DEVICE_JOYPAD, 0,
		RETRO_DEVICE_ID_JOYPAD_Y, "7 PQRS (Game C)"},

	/* [8  tuv] */
	{SJME_LIBRETRO_JOYPAD_PORT, RETRO_DEVICE_JOYPAD, 0,
		RETRO_DEVICE_ID_JOYPAD_DOWN, "8 TUV (Game Down)"},

	/* [9 wxyz] */
	{SJME_LIBRETRO_JOYPAD_PORT, RETRO_DEVICE_JOYPAD, 0,
		RETRO_DEVICE_ID_JOYPAD_B, "9 WXYZ (Game D)"},

	/* [*  shf] */
	{SJME_LIBRETRO_JOYPAD_PORT, RETRO_DEVICE_JOYPAD, 0,
		RETRO_DEVICE_ID_JOYPAD_L, "Star (Shift)"},

	/* [0    +] */
	{SJME_LIBRETRO_JOYPAD_PORT, RETRO_DEVICE_JOYPAD, 0,
		RETRO_DEVICE_ID_JOYPAD_R2, "0 Operator (Plus)"},

	/* [# spac] */
	{SJME_LIBRETRO_JOYPAD_PORT, RETRO_DEVICE_JOYPAD, 0,
		RETRO_DEVICE_ID_JOYPAD_R, "Pound (Space)"},

	/* Touchscreen (via pointer). */
	{SJME_LIBRETRO_POINTER_PORT, RETRO_DEVICE_POINTER, 0,
		RETRO_DEVICE_ID_POINTER_X, "Touchscreen X"},
	{SJME_LIBRETRO_POINTER_PORT, RETRO_DEVICE_POINTER, 0,
		RETRO_DEVICE_ID_POINTER_Y, "Touchscreen Y"},
	{SJME_LIBRETRO_POINTER_PORT, RETRO_DEVICE_POINTER, 0,
		RETRO_DEVICE_ID_POINTER_PRESSED, "Touchscreen Pressed"},

	/* End. */
	{0, 0, 0, 0, NULL}
};

/** Game pad controller. */
static const struct retro_input_descriptor sjme_libretro_descGamePad[] =
{
	/* Shoulders. */
	{SJME_LIBRETRO_JOYPAD_PORT, RETRO_DEVICE_JOYPAD, 0,
		RETRO_DEVICE_ID_JOYPAD_SELECT, "Soft 1 (Left Command)"},
	{SJME_LIBRETRO_JOYPAD_PORT, RETRO_DEVICE_JOYPAD, 0,
		RETRO_DEVICE_ID_JOYPAD_START, "Soft 2 (Right Command)"},
	
	/* Digital pad. */
	{SJME_LIBRETRO_JOYPAD_PORT, RETRO_DEVICE_JOYPAD, 0,
		RETRO_DEVICE_ID_JOYPAD_UP, "Digital Up"},
	{SJME_LIBRETRO_JOYPAD_PORT, RETRO_DEVICE_JOYPAD, 0,
		RETRO_DEVICE_ID_JOYPAD_DOWN, "Digital Down"},
	{SJME_LIBRETRO_JOYPAD_PORT, RETRO_DEVICE_JOYPAD, 0,
		RETRO_DEVICE_ID_JOYPAD_LEFT, "Digital Left"},
	{SJME_LIBRETRO_JOYPAD_PORT, RETRO_DEVICE_JOYPAD, 0,
		RETRO_DEVICE_ID_JOYPAD_RIGHT, "Digital Right"},
	{SJME_LIBRETRO_JOYPAD_PORT, RETRO_DEVICE_JOYPAD, 0,
		RETRO_DEVICE_ID_JOYPAD_L, "Digital Center"},

	/* Just Game Keys. */
	{SJME_LIBRETRO_JOYPAD_PORT, RETRO_DEVICE_JOYPAD, 0,
		RETRO_DEVICE_ID_JOYPAD_R, "Game Fire/Select"},
	{SJME_LIBRETRO_JOYPAD_PORT, RETRO_DEVICE_JOYPAD, 0,
		RETRO_DEVICE_ID_JOYPAD_X, "Game A"},
	{SJME_LIBRETRO_JOYPAD_PORT, RETRO_DEVICE_JOYPAD, 0,
		RETRO_DEVICE_ID_JOYPAD_A, "Game B"},
	{SJME_LIBRETRO_JOYPAD_PORT, RETRO_DEVICE_JOYPAD, 0,
		RETRO_DEVICE_ID_JOYPAD_Y, "Game C"},
	{SJME_LIBRETRO_JOYPAD_PORT, RETRO_DEVICE_JOYPAD, 0,
		RETRO_DEVICE_ID_JOYPAD_B, "Game D"},

	/* Touchscreen (via pointer). */
	{SJME_LIBRETRO_POINTER_PORT, RETRO_DEVICE_POINTER, 0,
		RETRO_DEVICE_ID_POINTER_X, "Touchscreen X"},
	{SJME_LIBRETRO_POINTER_PORT, RETRO_DEVICE_POINTER, 0,
		RETRO_DEVICE_ID_POINTER_Y, "Touchscreen Y"},
	{SJME_LIBRETRO_POINTER_PORT, RETRO_DEVICE_POINTER, 0,
		RETRO_DEVICE_ID_POINTER_PRESSED, "Touchscreen Pressed"},

	/* End. */
	{0, 0, 0, 0, NULL}
};

#if defined(RETRO_ENVIRONMENT_SET_EXTENDED_RETROPAD)
/** Extra core buttons. */
static const struct retro_core_extended_retropad_button
	sjme_libretro_descExtPad[] =
{
	{SJME_LIBRETRO_JOYPAD_PORT, RETRO_DEVICE_JOYPAD, 0,
		SJME_LIBRETRO_EXTRA_ID_SOFT_THREE,
		"Soft 3 (Middle Command)", NULL},

	{SJME_LIBRETRO_JOYPAD_PORT, RETRO_DEVICE_JOYPAD, 0,
		SJME_LIBRETRO_EXTRA_ID_VOLUME_UP,
		"Volume Up", NULL},
	{SJME_LIBRETRO_JOYPAD_PORT, RETRO_DEVICE_JOYPAD, 0,
		SJME_LIBRETRO_EXTRA_ID_VOLUME_DOWN,
		"Volume Down", NULL},

	{SJME_LIBRETRO_JOYPAD_PORT, RETRO_DEVICE_JOYPAD, 0,
		SJME_LIBRETRO_EXTRA_ID_START_CALL,
		"Start Call", NULL},
	{SJME_LIBRETRO_JOYPAD_PORT, RETRO_DEVICE_JOYPAD, 0,
		SJME_LIBRETRO_EXTRA_ID_END_CALL,
		"End Call", NULL},

	{SJME_LIBRETRO_JOYPAD_PORT, RETRO_DEVICE_JOYPAD, 0,
		SJME_LIBRETRO_EXTRA_ID_IAPPLI,
		"i-Appli/i-Mode Button", NULL},
	{SJME_LIBRETRO_JOYPAD_PORT, RETRO_DEVICE_JOYPAD, 0,
		SJME_LIBRETRO_EXTRA_ID_CAMERA,
		"Camera Button", NULL},
	{SJME_LIBRETRO_JOYPAD_PORT, RETRO_DEVICE_JOYPAD, 0,
		SJME_LIBRETRO_EXTRA_ID_WEB_BROWSER,
		"Web Browser Button", NULL},
	{SJME_LIBRETRO_JOYPAD_PORT, RETRO_DEVICE_JOYPAD, 0,
		SJME_LIBRETRO_EXTRA_ID_STOREFRONT,
		"Storefront Button", NULL},

	/* End. */
	{0, 0, 0, 0, NULL, NULL}
};

static void sjme_libretro_inputSetExtPad(void)
{
	struct retro_core_extended_retropad config;

	/* Setup config. */
	memset(&config, 0, sizeof(config));
	config.actions = sjme_libretro_descExtPad;

	/* Initialize extra commands accordingly. */
	sjme_libretro_envCallback(RETRO_ENVIRONMENT_SET_EXTENDED_RETROPAD,
		(void*)&config);
}
#endif

sjme_attrUnused RETRO_API void retro_set_input_poll(retro_input_poll_t poll)
{
	static sjme_jint trigger;
	if (!(trigger++))
		sjme_message("Impl. %s?", __func__);
}

sjme_attrUnused RETRO_API void retro_set_input_state(
	retro_input_state_t state)
{
	static sjme_jint trigger;
	if (!(trigger++))
		sjme_message("Impl. %s?", __func__);
}

sjme_attrUnused RETRO_API void retro_set_controller_port_device(
	unsigned port, unsigned device)
{
	const struct retro_input_descriptor* baseDesc;
	sjme_jint numBaseDesc, at;

	/* Which device to use? */
	if (device == RETRO_DEVICE_JOYPAD)
		baseDesc = sjme_libretro_descDialPad;
	else
		baseDesc = sjme_libretro_descGamePad;

	/* Count the number of base desc items. */
	numBaseDesc = 0;
	for (at = 0; baseDesc[at].description != NULL; at++)
		numBaseDesc++;

/* Extended controls. */
#if defined(RETRO_ENVIRONMENT_SET_EXTENDED_RETROPAD)
	sjme_libretro_inputSetExtPad();
#endif

	/* Set standard controls. */
	sjme_libretro_envCallback(RETRO_ENVIRONMENT_SET_INPUT_DESCRIPTORS,
		(void*)baseDesc);

	/* Set input controllers. */
	sjme_libretro_envCallback(RETRO_ENVIRONMENT_SET_CONTROLLER_INFO,
		(void*)&sjme_libretro_inputControllers);
}
