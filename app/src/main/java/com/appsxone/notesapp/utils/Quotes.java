package com.appsxone.notesapp.utils;

import java.util.ArrayList;
import java.util.Random;

public class Quotes {
    public static String getRandomQuoteFromList() {
        ArrayList<String> stringArrayList = new ArrayList<>();
        stringArrayList.clear();
        stringArrayList.add("Do not confuse motion and progress. A rocking horse keeps moving but does not make any progress");
        stringArrayList.add("I don’t know the key to success, but the key to failure is trying to please everybody");
        stringArrayList.add("Be yourself; everyone else is already taken");
        stringArrayList.add("There is a great difference between worry and concern. A worried person sees a problem, and a concerned person solves a problem");
        stringArrayList.add("Put the past behind you, the present moment is all that matters, in the life of those who want to be happy");
        stringArrayList.add("Don't try to be perfect. Just try to be better than you were yesterday");
        stringArrayList.add("Believe in yourself and you will be unstoppable");
        stringArrayList.add("Respect yourself enough to let go of someone who doesn't see your worth");
        stringArrayList.add("If you don't sacrifice for what you want, what you want becomes the sacrifice");
        stringArrayList.add("Stay strong, be brave, love hard and true, and you will have nothing to lose");
        stringArrayList.add("Everything will be so good so soon just hang in there and don't worry about it too much");
        stringArrayList.add("Work hard, stay disciplined and be patient. your time will come");
        stringArrayList.add("You can't have a positive life with a negative mind");
        stringArrayList.add("Enjoy life today, Yesterday is gone Tomorrow may never come");
        stringArrayList.add("Never let your failures get to your heart and never let your success get to your head");
        stringArrayList.add("Keep Calm because Life's too short to stress over people who don't deserve to be an issue");
        stringArrayList.add("If you have the courage to begin, you have the courage to succeed");
        stringArrayList.add("Don't be afraid of failure. Learn from it and keep going. Persistence is what creates excellence");
        stringArrayList.add("Don't worry about what others think. People are always negative, don't let it bother you. Stay strong. Move on");
        Random r = new Random();
        int randomQuoteIndex = r.nextInt(stringArrayList.size());
        String randomQuote = stringArrayList.remove(randomQuoteIndex);
        return randomQuote;
    }

    public static ArrayList<String> quoteList() {
        ArrayList<String> stringArrayList = new ArrayList<>();
        stringArrayList.clear();
        stringArrayList.add("Do not confuse motion and progress. A rocking horse keeps moving but does not make any progress");
        stringArrayList.add("I don’t know the key to success, but the key to failure is trying to please everybody");
        stringArrayList.add("Be yourself, everyone else is already taken");
        stringArrayList.add("There is a great difference between worry and concern. A worried person sees a problem, and a concerned person solves a problem");
        stringArrayList.add("Put the past behind you, the present moment is all that matters, in the life of those who want to be happy");
        stringArrayList.add("Don't try to be perfect. Just try to be better than you were yesterday");
        stringArrayList.add("Believe in yourself and you will be unstoppable");
        stringArrayList.add("Respect yourself enough to let go of someone who doesn't see your worth");
        stringArrayList.add("If you don't sacrifice for what you want, what you want becomes the sacrifice");
        stringArrayList.add("Stay strong, be brave, love hard and true, and you will have nothing to lose");
        stringArrayList.add("Everything will be so good so soon just hang in there and don't worry about it too much");
        stringArrayList.add("Work hard, stay disciplined and be patient. your time will come");
        stringArrayList.add("You can't have a positive life with a negative mind");
        stringArrayList.add("Enjoy life today, Yesterday is gone Tomorrow may never come");
        stringArrayList.add("Never let your failures get to your heart and never let your success get to your head");
        stringArrayList.add("Keep Calm because Life's too short to stress over people who don't deserve to be an issue");
        stringArrayList.add("If you have the courage to begin, you have the courage to succeed");
        stringArrayList.add("Don't be afraid of failure. Learn from it and keep going. Persistence is what creates excellence");
        stringArrayList.add("Don't worry about what others think. People are always negative, don't let it bother you. Stay strong. Move on");
        return stringArrayList;
    }
}