# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: Various utilities (backwards compatible make)

OUTPUT_DIR      ?= .
HOST_EXE_SUFFIX ?=

decode${HOST_EXE_SUFFIX}:		decode.c
								$(CC) -o "${OUTPUT_DIR}/$@" $<

decode.sqk:						decode${HOST_EXE_SUFFIX}

simple${HOST_EXE_SUFFIX}:		simple.c
								$(CC) -o "${OUTPUT_DIR}/$@" $<

simple.sqk:						simple${HOST_EXE_SUFFIX}

all:							decode.sqk simple.sqk

