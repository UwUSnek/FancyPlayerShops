package com.snek.fancyplayershops;


import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;








public abstract class FontWidthGenerator {
    private static String CLASS_NAME   = "FontSize";
    private static String DIR_PATH     = "fancyplayershops/generated";
    private static String FILE_PATH    = DIR_PATH + "/" + CLASS_NAME + ".java";
    private static String PACKAGE_NAME = "com.snek.fancyplayershops.generated";
    public List<Integer> a = new ArrayList<>();




    /**
     * Retrieves the width of every character in the extended ASCII and saves it in a newly created source file.
     */
    public static void generate() {
        TextRenderer renderer = MinecraftClient.getInstance().textRenderer;
        try {
            Files.createDirectories(FabricLoader.getInstance().getConfigDir().resolve(DIR_PATH));
        } catch (IOException e) {
            e.printStackTrace();
        }


        // Print the widths to the source filetry {
        try(FileWriter f = new FileWriter(FabricLoader.getInstance().getConfigDir().resolve(FILE_PATH).toString())) {
            f.write(
                "package " + PACKAGE_NAME + ";\n" +
                "import java.util.ArrayList;\n" +
                "import java.util.List;\n" +
                "\n\n\n\n" +
                "public final class " + CLASS_NAME + "{\n" +
                "    private static final List<Integer> widths = new ArrayList<>();\n" +
                "    static {\n"
            );


            // Loop extended ASCII characters
            for (char c = 0; c < 32; c++) {
                f.write("        widths.add(" + 0 + ");\n");
            }
            for (char c = 32; c < 256; c++) {
                int width = renderer.getWidth(String.valueOf(c));
                f.write("        widths.add(" + width + ");\n");
            }
            f.write("    }\n");


            // Write string length function
            f.write(
                "\n\n"+
                "    /**\n" +
                "     * Calculates the width a string would have when rendered.\n" +
                "     */\n" +
                "    public static int getWidth(String s) {\n" +
                "        int r = 0;\n" +
                "        for(int i = 0; i < s.length(); ++i) {\n" +
                "            i += widths.get(s.charAt(i));\n" +
                "        }\n" +
                "        if(!s.isEmpty()) r += s.length() - 1;\n" +
                "        return r;\n" +
                "    }\n"
            );


            // Write string width function
            f.write(
                "\n\n" +
                "    /**\n" +
                "     * Returns the height a line would have when rendered.\n" +
                "     */\n" +
                "    public static int getHeight() {\n" +
                "        return 11;\n" + //TODO actually calculate the height
                "    }\n"
            );


            f.write("}");
        } catch (IOException e) {
            e.printStackTrace();
        }


        // Print output notice
        System.err.println("Character dimensions written to \"config/" + FILE_PATH + "\"");
    }
}
