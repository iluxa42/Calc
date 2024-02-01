import java.util.Scanner;
class Main {
    public static void main(String[] args) throws Exception {
        Scanner scan = new Scanner(System.in);
        System.out.println("Введите арифметическое выражение с двумя числами: ");
        String exp = scan.nextLine();
        System.out.println("Результат: " + calc(exp));
    }

    public static String calc(String input) throws Exception {
        String operation = "";
        String[] operands = input.replaceAll(" ", "").toUpperCase().split("[-+*/]");

        if (operands.length != 2 || operands[0].isEmpty())
            throw new Exception("Должен быть один оператор и два операнда");

        if (input.contains("+")) operation = "+";
        else if (input.contains("-")) operation = "-";
        else if (input.contains("*")) operation = "*";
        else if (input.contains("/")) operation = "/";

        // тип операндов (0 - неизвестно, 1 - арабское число, 2 - римское число)
        int opType1 = isValidArabicNum(operands[0]) ? 1 : isValidRomanNum(operands[0]) ? 2 : 0;
        int opType2 = isValidArabicNum(operands[1]) ? 1 : isValidRomanNum(operands[1]) ? 2 : 0;

        if (opType1 == 0 || opType2 == 0) {
            throw new Exception("Один или оба операнда неизвестного типа");
        } else if (opType1 != opType2) {
            throw new Exception("Используются одновременно разные системы счисления");
        }

        if (opType1 == 1) {
            // два арабских числа
            int a1 = Integer.parseInt(operands[0]);
            int a2 = Integer.parseInt(operands[1]);
            int result1 = calcArabian(a1, a2, operation);

            return Integer.toString(result1);
        } else {
            // два римских числа
            int r1 = RomanToArabic(operands[0]);
            int r2 = RomanToArabic(operands[1]);
            int result2 = calcArabian(r1, r2, operation);

            if (result2 < 1)
                throw new Exception("Римское число в результате не может быть меньше 1 (I)");

            return ArabicToRoman(result2);
        }
    }

    public static int calcArabian(int a1, int a2, String operation) throws Exception {
        if (a1 < 1 || a1 > 10 || a2 < 1 || a2 > 10)
            throw new Exception("Операнды (арабские или римские числа) могут быть только от 1 (I) до 10 (X)");

        switch (operation) {
            case "+": return a1 + a2;
            case "-": return a1 - a2;
            case "*": return a1 * a2;
            case "/": return a1 / a2;
            default: return 0;
        }
    }

    public static boolean isValidRomanNum(String romanNum) {
        // Правила записи чисел римскими цифрами (wiki):
        // 1. Римское число может содержать только следующие символы: I, V, X, L, C, D, M.
        if (!romanNum.replaceAll("[IVXLCDM]", "").isEmpty())
            return false;

        // Далее идут каноничные правила, которые можно отключить, для упрощённой записи
        // 2. Цифры V, L, D  не могут повторяться в числе (т.к. VV = X, LL = C, DD = M).
        if (romanNum.replaceAll("(VV|LL|DD)", "").length() != romanNum.length())
            return false;

        // 3. Цифры I, X, C, M  могут повторяться в числе не более трех раз подряд.
        if (romanNum.replaceAll("(IIII|XXXX|CCCC|MMMM)", "").length() != romanNum.length())
            return false;

        // 4. Разряды. Необходимо сначала записать число тысяч, затем сотен, затем десятков и, наконец, единиц.
        // 5. Если меньшая (по значению) цифра стоит справа от большей, они складываются.
        // 6. Если меньшая (по значению) цифра стоит слева от большей, то меньшая вычитается из большей;
        // вычитаться могут только цифры, обозначающие 1 или степени 10 (I, X, C);
        // уменьшаемым может быть только цифра, ближайшая в числовом ряду к вычитаемой на две цифры
        // (вычитаемое, умноженное на 5 или 10); повторения меньшей цифры не допускаются;
        // Таким образом, существует только 6 вариантов «правила вычитания»:
        // IV = 4, IX = 9, XL = 40, XC = 90, CD = 400, CM = 900
        // Для проверки правил 4-6, нужно попробовать сконвертировать римское число в арабское
        return RomanToArabic(romanNum) != -1;
    }

    public static boolean isValidArabicNum(String arabicNum) {
        try {
            Integer.parseInt(arabicNum);
            return true;
        }
        catch (NumberFormatException nfe) {
            return false;
        }
    }

    static final String[] ROMAN_NUMS = {"M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"};
    static final   int[] ARABIC_NUMS = {1000, 900, 500,  400, 100,  90,   50,  40,   10,  9,    5,    4,   1 };

    public static String ArabicToRoman(int arabicNum) {
        if (arabicNum < 1 || arabicNum > 3999)
            throw new IllegalArgumentException("За пределами возможных значений римского числа (от 1 до 3999)");

        String result = "";

        for (int i = 0; i < ARABIC_NUMS.length; i++) {
            while (arabicNum >= ARABIC_NUMS[i]) {
                arabicNum -= ARABIC_NUMS[i];
                result += ROMAN_NUMS[i];
            }
        }

        return result;
    }

    public static int RomanToArabic(String romanNum) {
        int result = 0;

        for (int i = 0; i < ROMAN_NUMS.length; i++) {
            while (romanNum.indexOf(ROMAN_NUMS[i]) == 0) {
                result += ARABIC_NUMS[i];
                romanNum = romanNum.substring(ROMAN_NUMS[i].length());
            }
        }
        // Если остались символы после разбора, значит введённое римское число записано неверно:
        // нарушены правила разрядов(4), сложения(5) и/или вычитания(6)
        if (romanNum.length() > 0 )
            return -1;

        return result;
    }
}
    /* Вывод арабских и римских чисел от 1 до 3999
    for (int i = 1; i < 4000; i++) {
        System.out.println(i + " = " + ArabicToRoman(i));
    }*/