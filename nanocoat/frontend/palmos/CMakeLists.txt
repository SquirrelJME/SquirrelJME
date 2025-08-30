# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------

# ARM:
# -static-pie -mdirect-extern-access -fpie -fno-jump-tables
# -msingle-pic-base -mpic-register=r9/r10
# -march=armv4t -Wl,--emit-relocs
# -ffixed-reg?? -fcall-used-reg?? -fcall-saved-reg??
# -nostartfiles -nodefaultlibs -nolibc -nostdlib -nostdlib++
