package org.example.coderank.dto;

public class ProblemListDTO {

        private String id;
        private String title;
        private String level;


        // constructors, getters, setters
        public ProblemListDTO() {}
        public ProblemListDTO(String id, String title, String level) {
            this.id = id; this.title = title; this.level = level;
        }
        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }
        public String getLevel() { return level; }
        public void setLevel(String level) { this.level = level; }

    }


