package ru.hyndo.hobfuscator.rename;

import ru.hyndo.hobfuscator.analyzed.NamedType;

public class OneWordRenamingStrategy implements RenamingStrategy {

    private final String sourceWord;
    private String word;
    private int movingIndex;
    private int lettersToChange = 1;
    private String currentWord;
    private int count;

    public OneWordRenamingStrategy(String word) {
        this.sourceWord = word.toLowerCase();
        this.word = word.toLowerCase();
        this.currentWord = word;
    }

    @Override
    public String getName(NamedType type) {
        return nextWord();
    }

    public static void main(String[] args) {
        OneWordRenamingStrategy abc = new OneWordRenamingStrategy("do");
        for (int i = 0; i < 50; i++) {
            System.out.println(abc.nextWord());
        }
    }

    @SuppressWarnings("UnnecessaryLocalVariable")
    private String nextWord() {
        int startAt = movingIndex++;
        if (startAt >= currentWord.length()) {
            movingIndex = 0;
            lettersToChange++;
            return nextWord();
        }
        if (lettersToChange > currentWord.length()) {
            this.movingIndex = 0;
            this.lettersToChange = 1;
            this.count++;
            this.currentWord = sourceWord + count;
            this.word = currentWord;
            return nextWord();
        }
        if (lettersToChange + movingIndex > currentWord.length() + 1) {
            movingIndex = 0;
            lettersToChange++;
            return nextWord();
        }
        StringBuilder builder = new StringBuilder(word);
        for (int i = 0; i < lettersToChange; i++) {
            int index = startAt++;
            char let = currentWord.charAt(index);
            let = Character.toUpperCase(let);
            builder.setCharAt(index, let);
        }
        return builder.toString();
    }

}
