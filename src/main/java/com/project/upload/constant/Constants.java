package com.project.upload.constant;

public class Constants {

    private Constants() {
    }

    public static final String LINK = "https://firebasestorage.googleapis.com/v0/b/";

    public static final String ALT_MEDIA = "?alt=media";

    public static class FirebaseStatus {
        private FirebaseStatus() {
        }

        public static final int NOT_EXIST = 0;
        public static final int EXIST = 1;
    }
}
