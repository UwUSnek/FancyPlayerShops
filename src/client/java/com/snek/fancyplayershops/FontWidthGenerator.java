package com.snek.fancyplayershops;


import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;








public abstract class FontWidthGenerator {
    private FontWidthGenerator(){}
    public static final String CLASS_NAME   = "FontSize";
    public static final String DIR_PATH     = "fancyplayershops/generated";
    public static final String FILE_PATH    = DIR_PATH + "/" + CLASS_NAME + ".java";
    public static final String PACKAGE_NAME = "com.snek.framework.generated";




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


        // Print the widths to the source file
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
                """
                    /**
                     * Calculates the width a string would have when rendered.
                     * This includes the space between each character.
                     */
                    public static int getWidth(String s) {
                        int r = 0;
                        for(int i = 0; i < s.length(); ++i) {
                            final char c = s.charAt(i);
                            if(c >= 256) r += 9;
                            else r += widths.get(c);
                        }
                        return r;
                    }
                """
            );


            //TODO actually calculate the height
            // Write string width function
            f.write(
                """
                    /**
                     * Returns the height a line would have when rendered.
                     * This does NOT include the space between lines.
                     */
                    public static int getHeight() {
                        return 8;
                    }
                """
            );


            f.write("}");
        } catch (IOException e) {
            e.printStackTrace();
        }


        // Print output notice
        System.out.println("Character dimensions written to \"config/" + FILE_PATH + "\"");
    }
}
