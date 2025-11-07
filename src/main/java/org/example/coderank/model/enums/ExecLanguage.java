package org.example.coderank.model.enums;

public enum ExecLanguage {

    JAVA("java", "Main.java", new String[]{"javac Main.java"}, new String[]{"java -classpath . Main"}, "coderank/java-runner:latest"),
    PYTHON("py", "main.py", null, new String[]{"python3 main.py"}, "coderank/python-runner:latest"),
    CPP("cpp", "main.cpp", new String[]{"g++ -O2 -std=gnu++17 main.cpp -o main"}, new String[]{"./main"}, "coderank/cpp-runner:latest");

    private final String code;
    private final String fileName;
    private final String[] compileCmd;
    private final String[] runCmd;
    private final String image;

    ExecLanguage(String code, String fileName, String[] compileCmd, String[] runCmd, String image) {
        this.code = code;
        this.fileName = fileName;
        this.compileCmd = compileCmd;
        this.runCmd = runCmd;
        this.image = image;
    }

    public String getCode() { return code; }
    public String getFileName() { return fileName; }
    public String[] getCompileCmd() { return compileCmd; } // null if interpreted
    public String[] getRunCmd() { return runCmd; }
    public String getImage() { return image; }

    public static ExecLanguage fromCode(String code) {
        for (ExecLanguage l : values()) if (l.code.equalsIgnoreCase(code)) return l;
        throw new IllegalArgumentException("Unsupported language: " + code);
    }
}
