import java.io.*;
import java.util.*;

class macroPass1 {
    static List<String> MDT = new ArrayList<>();
    static Map<String, Integer> MNT = new LinkedHashMap<>();
    static Map<String, Integer> ALA = new LinkedHashMap<>(); // Unified ALA for all macros
    static List<String> IntermediateCode = new ArrayList<>();
    static int mdtp = 0;

    public static void main(String[] args) {
        String InputFile = "2.txt";
        try {
            processmacro(InputFile);
            printTables();
        } catch (IOException e) {
            System.out.println("Error in loading file: " + e.getMessage());
        }
    }

    static void processmacro(String InputFile) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(InputFile));
        String line;
        boolean ismacro = false;
        String macroname = null;

        while ((line = br.readLine()) != null) {
            line = line.trim();
            
            if (line.startsWith("MACRO")) {
                ismacro = true;
                macroname = null;
            } else if (ismacro) {
                String[] parts = line.split("\\s+");

                // If this is the first line in the macro (macro name with parameters)
                if (macroname == null) {
                    macroname = parts[0];
                    MNT.put(macroname, mdtp);

                    // Populate ALA with parameters if not already present
                    for (int i = 1; i < parts.length; i++) {
                        ALA.putIfAbsent(parts[i], ALA.size());
                    }
                }

                // Add line to MDT
                MDT.add(line);
                mdtp++;

                // Check for end of macro definition
                if (line.equals("MEND")) {
                    ismacro = false;
                    macroname = null;
                }
            } else {
                // If the line is not part of a macro definition, it's part of intermediate code
                IntermediateCode.add(line);
            }
        }
        br.close();
    }

    static void printTables() {
        System.out.println("=========== Macro Name Table (MNT) ===========");
        System.out.printf("%-10s | %-10s\n", "Macro Name", "MDT Index");
        System.out.println("-----------|------------");
        for (Map.Entry<String, Integer> entry : MNT.entrySet()) {
            System.out.printf("%-10s | %-10d\n", entry.getKey(), entry.getValue());
        }

        System.out.println("\n=========== Macro Definition Table (MDT) ===========");
        System.out.printf("%-10s | %-20s\n", "MDT Ptr", "Definition");
        System.out.println("-----------|----------------------");
        for (int i = 0; i < MDT.size(); i++) {
            System.out.printf("MDT[%d]     | %s\n", i, MDT.get(i));
        }

        System.out.println("\n=========== Argument List Array (ALA) ===========");
        System.out.printf("%-10s | %-10s\n", "Argument", "Position");
        System.out.println("-----------|-----------");
        for (Map.Entry<String, Integer> entry : ALA.entrySet()) {
            System.out.printf("%-10s | %-10d\n", entry.getKey(), entry.getValue());
        }

        System.out.println("\n=========== Intermediate Code ===========");
        for (String code : IntermediateCode) {
            System.out.println(code);
        }
    }
}
