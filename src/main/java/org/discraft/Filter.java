package org.discraft;

import java.util.*;
import java.util.regex.*;

public class Filter {
    private final Set<String> bads = new HashSet<>();
    private final Map<Character, Character> leetMap = new HashMap<>();
    private final Pattern wordPattern = Pattern.compile("\\b\\w+\\b");

    public Filter() {
        Collections.addAll(bads,
                "fuck", "shit", "bitch", "asshole", "dick", "cunt", "nigger", "nigga",
                "faggot", "bastard", "slut", "whore", "ass", "motherfucker", "dickhead",
                "shithead", "asshat", "twat", "prick", "cock", "ballsack", "pussy", "arse",
                "wanker", "retard", "moron", "beaner", "chink", "kike", "spic", "tranny",
                "dyke", "fag", "cockhead", "piss", "shag", "pissed", "wank",
                "cocksucker", "vagina", "cum", "cummy", "bukkake", "fisting", "bitchass",
                "slutty", "asswipe", "prickhead", "smegma", "tits", "boobs", "numbnuts", "fucking",
                "cockblock", "freakin", "suck", "lick", "chode", "fuckface", "motherfucking",
                "asscrack", "balls", "butthole", "bitching", "shitting", "cuntface", "fuckery", "jerkoff",
                "douchebag", "douche", "shitbag", "assclown", "skank", "hooker", "cuntwaffle",
                "buttfucker", "fatass", "retarded", "shitfaced", "crackhead", "speedfreak", "whorebag",
                "pussylicker", "pissflaps", "cockring", "turd", "spazz", "whoreish", "cocktail", "genitalia",
                "cameltoe", "dicknose", "fucktard", "arsehole", "dickbag", "cumdumpster", "cuntmuffin",
                "motherchucker", "penis", "pisshead", "shitstorm", "shitshow", "clit", "ballbag", "dickslap",
                "nutsack", "gaylord", "rape", "incest", "prickwanker", "sexist",
                "lardass", "bastardo", "queer", "cuntish", "twatwaffle", "cumshot", "headfucker", "shitlicker",
                "cumslut", "pussyhole", "dickwad", "assdick", "bitchslap", "slutbag", "b!tch", "f!ck", "sperm",
                "jerking", "sex"
        );

        leetMap.put('@', 'a'); leetMap.put('4', 'a');
        leetMap.put('3', 'e');
        leetMap.put('1', 'i'); leetMap.put('!', 'i'); leetMap.put('|', 'i');
        leetMap.put('0', 'o');
        leetMap.put('$', 's'); leetMap.put('5', 's');
        leetMap.put('7', 't');
    }

    private String normalize(String input) {
        StringBuilder sb = new StringBuilder();
        input = input.toLowerCase();
        for (char c : input.toCharArray()) {
            if (leetMap.containsKey(c)) {
                sb.append(leetMap.get(c));
            } else if (Character.isLetter(c)) {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    public boolean isProfane(String word) {
        String norm = normalize(word);
        if (bads.contains(norm)) return true;

        for (String bad : bads) {
            if (norm.startsWith(bad)) return true;
        }
        return false;
    }

    public List<String> getFlagged(String text) {
        List<String> flagged = new ArrayList<>();
        Matcher matcher = wordPattern.matcher(text);
        while (matcher.find()) {
            String word = matcher.group();
            if (isProfane(word)) {
                flagged.add(word);
            }
        }
        return flagged;
    }

    public String censor(String text) {
        Matcher matcher = wordPattern.matcher(text);
        StringBuffer result = new StringBuffer();

        while (matcher.find()) {
            String word = matcher.group();
            if (isProfane(word)) {
                String replacement = "*".repeat(word.length());
                matcher.appendReplacement(result, Matcher.quoteReplacement(replacement));
            } else {
                matcher.appendReplacement(result, Matcher.quoteReplacement(word));
            }
        }
        matcher.appendTail(result);
        return result.toString();
    }
}
