package me.noci.advancedtooltip.v1_8_9.utils;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import net.labymod.api.nbt.NBTTagType;
import net.minecraft.nbt.*;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class NBTPrinter {

    //Minecraft version 1.20.1 code
    public static String prettyPrint(NBTBase $$0, boolean $$1) {
        return prettyPrint(new StringBuilder(), $$0, 0, $$1).toString();
    }

    public static StringBuilder prettyPrint(StringBuilder $$0, NBTBase $$1, int $$2, boolean $$3) {
        int var9;
        int $$30;
        int $$15;
        int $$7;
        String $$22;
        int $$18;
        switch ($$1.getId()) {
            case 0:
                break;
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 8:
                $$0.append($$1);
                break;
            case 7:
                NBTTagByteArray $$4 = (NBTTagByteArray)$$1;
                byte[] $$5 = $$4.getByteArray();
                $$15 = $$5.length;
                indent($$2, $$0).append("byte[").append($$15).append("] {\n");
                if (!$$3) {
                    indent($$2 + 1, $$0).append(" // Skipped, supply withBinaryBlobs true");
                } else {
                    indent($$2 + 1, $$0);

                    for($$7 = 0; $$7 < $$5.length; ++$$7) {
                        if ($$7 != 0) {
                            $$0.append(',');
                        }

                        if ($$7 % 16 == 0 && $$7 / 16 > 0) {
                            $$0.append('\n');
                            if ($$7 < $$5.length) {
                                indent($$2 + 1, $$0);
                            }
                        } else if ($$7 != 0) {
                            $$0.append(' ');
                        }

                        $$0.append(String.format(Locale.ROOT, "0x%02X", $$5[$$7] & 255));
                    }
                }

                $$0.append('\n');
                indent($$2, $$0).append('}');
                break;
            case 9:
                NBTTagList $$8 = (NBTTagList)$$1;
                int $$9 = $$8.tagCount();
                int $$10 = $$8.getTagType();
                $$22 = $$10 == 0 ? "undefined" : NBTTagType.getById((byte) $$10).name();
                indent($$2, $$0).append("list<").append($$22).append(">[").append($$9).append("] [");
                if ($$9 != 0) {
                    $$0.append('\n');
                }

                for($$18 = 0; $$18 < $$9; ++$$18) {
                    if ($$18 != 0) {
                        $$0.append(",\n");
                    }

                    indent($$2 + 1, $$0);
                    prettyPrint($$0, $$8.get($$18), $$2 + 1, $$3);
                }

                if ($$9 != 0) {
                    $$0.append('\n');
                }

                indent($$2, $$0).append(']');
                break;
            case 10:
                NBTTagCompound $$19 = (NBTTagCompound)$$1;
                List<String> $$20 = Lists.newArrayList($$19.getKeySet());
                Collections.sort($$20);
                indent($$2, $$0).append('{');
                if ($$0.length() - $$0.lastIndexOf("\n") > 2 * ($$2 + 1)) {
                    $$0.append('\n');
                    indent($$2 + 1, $$0);
                }

                $$15 = $$20.stream().mapToInt(String::length).max().orElse(0);
                $$22 = Strings.repeat(" ", $$15);

                for($$18 = 0; $$18 < $$20.size(); ++$$18) {
                    if ($$18 != 0) {
                        $$0.append(",\n");
                    }

                    String $$24 = (String)$$20.get($$18);
                    indent($$2 + 1, $$0).append('"').append($$24).append('"').append($$22, 0, $$22.length() - $$24.length()).append(": ");
                    prettyPrint($$0, $$19.getTag($$24), $$2 + 1, $$3);
                }

                if (!$$20.isEmpty()) {
                    $$0.append('\n');
                }

                indent($$2, $$0).append('}');
                break;
            case 11:
                NBTTagIntArray $$13 = (NBTTagIntArray)$$1;
                int[] $$14 = $$13.getIntArray();
                $$15 = 0;
                int[] var7 = $$14;
                $$18 = $$14.length;

                for(var9 = 0; var9 < $$18; ++var9) {
                    $$30 = var7[var9];
                    $$15 = Math.max($$15, String.format(Locale.ROOT, "%X", $$30).length());
                }

                $$7 = $$14.length;
                indent($$2, $$0).append("int[").append($$7).append("] {\n");
                if (!$$3) {
                    indent($$2 + 1, $$0).append(" // Skipped, supply withBinaryBlobs true");
                } else {
                    indent($$2 + 1, $$0);

                    for($$18 = 0; $$18 < $$14.length; ++$$18) {
                        if ($$18 != 0) {
                            $$0.append(',');
                        }

                        if ($$18 % 16 == 0 && $$18 / 16 > 0) {
                            $$0.append('\n');
                            if ($$18 < $$14.length) {
                                indent($$2 + 1, $$0);
                            }
                        } else if ($$18 != 0) {
                            $$0.append(' ');
                        }

                        $$0.append(String.format(Locale.ROOT, "0x%0" + $$15 + "X", $$14[$$18]));
                    }
                }

                $$0.append('\n');
                indent($$2, $$0).append('}');
                break;
            default:
                $$0.append("<UNKNOWN :(>");
        }

        return $$0;
    }

    private static StringBuilder indent(int $$0, StringBuilder $$1) {
        int $$2 = $$1.lastIndexOf("\n") + 1;
        int $$3 = $$1.length() - $$2;

        for(int $$4 = 0; $$4 < 2 * $$0 - $$3; ++$$4) {
            $$1.append(' ');
        }

        return $$1;
    }

}
