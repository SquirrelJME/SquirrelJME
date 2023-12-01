# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: Various utilities (backwards compatible make)

all:							decode.sqk simple.sqk sourceize.sqk

decode$(HOST_EXE_SUFFIX):		decode.c
								$(CC) -o "$(OUTPUT_DIR)/$@" $<

decode.sqk:						decode$(HOST_EXE_SUFFIX)

simple$(HOST_EXE_SUFFIX):		simple.c
								$(CC) -o "$(OUTPUT_DIR)/$@" $<

simple.sqk:						simple$(HOST_EXE_SUFFIX)

sourceize$(HOST_EXE_SUFFIX):	sourceize.c
								$(CC) -o "$(OUTPUT_DIR)/$@" $<

sourceize.sqk:					sourceize$(HOST_EXE_SUFFIX)
