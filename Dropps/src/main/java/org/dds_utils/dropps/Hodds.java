package org.dds_utils.dropps;


import org.dds_utils.dropps.models.ExportConfiguration;
import org.jogl.DDSImage;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class Hodds {
    public static void main(String[] args) {
        Map<String, String> arguments = parseArguments(args);

        if (arguments.containsKey("in")) {
            String inputFile = arguments.get("in");
            String format = arguments.get("format");
            String outputFile = arguments.get("out");

            System.out.println("Input file: " + inputFile);
            System.out.println("Format: " + format);
            System.out.println("Output file: " + outputFile);

            File file = new File(inputFile);

            ExportConfiguration options = new ExportConfiguration(2048, 2048, DDSImage.D3DFMT_DXT5, true);

            ConvertController converter = new ConvertController(options);
            converter.convertFile(file);
        } else {
            System.out.println("Usage: hodds --in input.dds --format DDS --out output.dds");
        }
    }

    private static Map<String, String> parseArguments(String[] args) {
        Map<String, String> arguments = new HashMap<>();
        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "--in":
                    if (i + 1 < args.length) {
                        arguments.put("in", args[++i]);
                    }
                    break;
                case "--format":
                    if (i + 1 < args.length) {
                        arguments.put("format", args[++i]);
                    }
                    break;
                case "--out":
                    if (i + 1 < args.length) {
                        arguments.put("out", args[++i]);
                    }
                    break;
            }
        }
        return arguments;
    }
}