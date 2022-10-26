package com.example.bookworld;

public class MoodCategory {
    private String mood, mood1, mood2;
    private int upper_mood, lower_mood;

    MoodCategory(String mood) {
        this.mood = mood;

        switch(mood) {
            case ("0"):
                mood1 = m0;
                mood2 = m0;
                upper_mood = 0;
                lower_mood = 0;
                break;
            case ("1"):
                mood1 = m1;
                mood2 = m1_1;
                upper_mood = 1;
                lower_mood = 1;
                break;
            case ("2"):
                mood1 = m1;
                mood2 = m1_2;
                upper_mood = 1;
                lower_mood = 2;
                break;
            case("3"):
                mood1 = m1;
                mood2 = m1_3;
                upper_mood = 1;
                lower_mood = 3;
                break;
            case ("4"):
                mood1 = m1;
                mood2 = m1_4;
                upper_mood = 1;
                lower_mood = 4;
                break;
            case ("5"):
                mood1 = m2;
                mood2 = m2_1;
                upper_mood = 2;
                lower_mood = 1;
                break;
            case("6") :
                mood1 = m2;
                mood2 = m2_2;
                upper_mood = 2;
                lower_mood = 2;
                break;
            case ("7"):
                mood1 = m2;
                mood2 = m2_3;
                upper_mood = 2;
                lower_mood = 3;
                break;
            case ("8"):
                mood1 = m2;
                mood2 = m2_4;
                upper_mood = 2;
                lower_mood = 4;
                break;
            case("9") :
                mood1 = m3;
                mood2 = m3_1;
                upper_mood = 3;
                lower_mood = 1;
                break;
            case ("10"):
                mood1 = m3;
                mood2 = m3_2;
                upper_mood = 3;
                lower_mood = 2;
                break;
            case ("11"):
                mood1 = m3;
                mood2 = m3_3;
                upper_mood = 3;
                lower_mood = 3;
                break;
            case("12") :
                mood1 = m3;
                mood2 = m3_4;
                upper_mood = 3;
                lower_mood = 4;
                break;
            case ("13"):
                mood1 = m4;
                mood2 = m4_1;
                upper_mood = 4;
                lower_mood = 1;
                break;
            case ("14"):
                mood1 = m4;
                mood2 = m4_2;
                upper_mood = 4;
                lower_mood = 2;
                break;
            case("15") :
                mood1 = m4;
                mood2 = m4_3;
                upper_mood = 4;
                lower_mood = 3;
                break;
            case ("16"):
                mood1 = m4;
                mood2 = m4_4;
                upper_mood = 4;
                lower_mood = 4;
                break;
        }
    }

    String getMood1() {
        return mood1;
    }
    String getMood2() {
        return mood2;
    }
    int getUpper_mood() {return upper_mood;}
    int getLower_mood() {return lower_mood;}

    final static String m0 = "무드카테고리를 선택해주세요";

    final static String m1 = "난 발전하고 싶어요";
    final static String m2 = "난 너무 지쳐서";
    final static String m3 = "모든 게 내 마음 같지 않아서";
    final static String m4 = "난 요즘 행복해서";

    final static String m1_1 = "좋은 습관을 키우고 싶어요";
    final static String m1_2 = "깊이 있는 책을 읽고 싶어요";
    final static String m1_3 = "세상 보는 눈을 키우고 싶어요";
    final static String m1_4 = "시간 관리를 잘 하고 싶어요";

    final static String m2_1 = "희망이 필요해요";
    final static String m2_2 = "위로받고 싶어요";
    final static String m2_3 = "가족이 그리워요";
    final static String m2_4 = "힐링이 필요해요";

    final static String m3_1 = "불안해요";
    final static String m3_2 = "답답해요";
    final static String m3_3 = "힘들어요";
    final static String m3_4 = "잠이 오지 않아요";

    final static String m4_1 = "기분이 몽글몽글해요";
    final static String m4_2 = "마음이 따뜻해지는 책을 읽고 싶어요";
    final static String m4_3 = "에너지가 넘쳐요";
    final static String m4_4 = "가슴이 설레는 로맨스를 읽고 싶어요";

}



