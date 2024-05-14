package com.yahoo.vespasignificance;


/**
 * This class contains the program parameters.
 *
 * @author MariusArhaug
 */
public class ClientParameters {
    // Show help page if true
    public final boolean help;

    // Input file for the program
    public final String inputFile;

    // Output file for the program
    public final String outputFile;

    // Field for the program
    public final String field;

    // Language for the program
    public final String language;

    public ClientParameters(
            boolean help,
            String inputFile,
            String outputFile,
            String field,
            String language) {
        this.help = help;
        this.inputFile = inputFile;
        this.outputFile = outputFile;
        this.field = field;
        this.language = language;
    }

    public static class Builder {
        private boolean help;
        private String inputFile;
        private String outputFile;
        private String field;
        private String language;

        public Builder setHelp(boolean help) {
            this.help = help;
            return this;
        }

        public Builder setInputFile(String inputFile) {
            this.inputFile = inputFile;
            return this;
        }

        public Builder setOutputFile(String outputFile) {
            this.outputFile = outputFile;
            return this;
        }

        public Builder setField(String field) {
            this.field = field;
            return this;
        }
        public Builder setLanguage(String language) {
            this.language = language;
            return this;
        }

        public ClientParameters build() {
            return new ClientParameters(help, inputFile, outputFile, field, language);
        }
    }
}
