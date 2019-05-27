package com.example.hetkataria.alarmapp.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class AutocompleteCity {

    @SerializedName("predictions")
    public ArrayList<Predict> Predictions;

    public class Predict {

        @SerializedName("description")
        public String description;

        @SerializedName("id")
        private String ID;

        @SerializedName("matched_substrings")
        private ArrayList<MatchedSubstring> MatchedSubstrings;

        private class MatchedSubstring{

            @SerializedName("offset")
            private int length;

            @SerializedName("length")
            private int offset;
        }

        @SerializedName("place_id")
        private String placeID;

        @SerializedName("reference")
        private String reference;

        @SerializedName("structured_formatting")
        public StructuredFormat structuredFormat;

        public class StructuredFormat{

            @SerializedName("main_text")
            public String mainText;

            @SerializedName("main_text_matched_substrings")
            private ArrayList<MatchedSubstring> MatchedSubstrings;

            @SerializedName("secondary_Text")
            private String secondaryText;
        }

        @SerializedName("terms")
        public ArrayList<Term> terms;

        public class Term{

            @SerializedName("offset")
            public int offset;

            @SerializedName("value")
            public String value;
        }

        @SerializedName("types")
        public ArrayList<String> types;

    }
}



