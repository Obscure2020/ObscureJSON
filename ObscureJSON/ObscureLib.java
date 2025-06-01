package ObscureJSON;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;

final class ObscureLib {

    public static final BigInteger BIG_HUNDRED = BigInteger.TEN.pow(2);
    public static final BigInteger BIG_THOUSAND = BigInteger.TEN.pow(3);
    public static final BigInteger BIG_TEN_THOUSAND = BigInteger.TEN.pow(4);
    public static final BigInteger BIG_LONG_MAX = BigInteger.valueOf(Long.MAX_VALUE);
    public static final BigInteger BIG_UPPER_EXCLUDE = BigInteger.TEN.pow(66);

    private static final String[] ENGLISH_SUFFIXES = {"", " thousand", " million", " billion", " trillion", " quadrillion", " quintillion", " sextillion", " septillion", " octillion", " nonillion", " decillion", " undecillion", " duodecillion", " tredecillion", " quattuordecillion", " quindecillion", " sexdecillion", " septendecillion", " octodecillion", " novemdecillion", " vigintillion"};

    private static void obscureAssert(boolean check, String message){
        if(!check){
            throw new AssertionError(message);
        }
    }

    public static String leftPad(String body, char pad, int length){
        if(body.length() >= length) return body;
        StringBuilder sb = new StringBuilder(body);
        while(sb.length() < length) sb.insert(0, pad);
        return sb.toString();
    }

    public static String rightPad(String body, char pad, int length){
        if(body.length() >= length) return body;
        StringBuilder sb = new StringBuilder(body);
        while(sb.length() < length) sb.append(pad);
        return sb.toString();
    }

    public static String sliceAfter(String full, String match){
        int spot = full.indexOf(match);
        obscureAssert(spot>=0, "Slice match target not found in full string.");
        return full.substring(spot + match.length());
    }

    public static String sliceAfterKeep(String full, String match){
        int spot = full.indexOf(match);
        obscureAssert(spot>=0, "Slice match target not found in full string.");
        return full.substring(spot);
    }

    public static String sliceBefore(String full, String match){
        int spot = full.indexOf(match);
        obscureAssert(spot>=0, "Slice match target not found in full string.");
        return full.substring(0, spot);
    }

    public static String sliceBeforeKeep(String full, String match){
        int spot = full.indexOf(match);
        obscureAssert(spot>=0, "Slice match target not found in full string.");
        return full.substring(0, spot + match.length());
    }

    public static boolean matchesAlphabet(String message, String alphabet){
        if(message.length() == 0) return true;
        if(alphabet.length() == 0) return false;
        int[] alphabet_codepoints = alphabet.codePoints().sorted().distinct().toArray();
        return message.codePoints().sorted().distinct().allMatch(i -> (Arrays.binarySearch(alphabet_codepoints, i) >= 0));
    }

    public static String longWithCommas(long number){
        String plain = Long.toString(number);
        if((number > -10000) && (number < 10000)){
            return plain;
        }
        StringBuilder sb = new StringBuilder();
        int count = 0;
        int stop = (number < 0) ? 1 : 0;
        for(int i=plain.length()-1; i>=stop; i--){
            if(count == 3){
                sb.append(',');
                count = 0;
            }
            sb.append(plain.charAt(i));
            count++;
        }
        if(number < 0) sb.append('-');
        return sb.reverse().toString();
    }

    public static String longToEnglish(long number){
        if(number < 0){
            if(number == Long.MIN_VALUE){
                return "negative nine quintillion, two hundred and twenty-three quadrillion, three hundred and seventy-two trillion, thirty-six billion, eight hundred and fifty-four million, seven hundred and seventy-five thousand, eight hundred and eight";
            }
            return "negative " + longToEnglish(-number);
        }
        if(number < 20){
            return switch((int) number){
                case 0 -> "zero";
                case 1 -> "one";
                case 2 -> "two";
                case 3 -> "three";
                case 4 -> "four";
                case 5 -> "five";
                case 6 -> "six";
                case 7 -> "seven";
                case 8 -> "eight";
                case 9 -> "nine";
                case 10 -> "ten";
                case 11 -> "eleven";
                case 12 -> "twelve";
                case 13 -> "thirteen";
                case 14 -> "fourteen";
                case 15 -> "fifteen";
                case 16 -> "sixteen";
                case 17 -> "seventeen";
                case 18 -> "eighteen";
                case 19 -> "nineteen";
                default -> throw new AssertionError("This should not be possible.\nFailiure in the \"under 20\" switch statement.");
            };
        }
        if(number < 100){
            int tens = (int) ((number / 10) % 10);
            int ones = (int) (number % 10);
            String prefix = switch(tens){
                case 2 -> "twenty";
                case 3 -> "thirty";
                case 4 -> "forty";
                case 5 -> "fifty";
                case 6 -> "sixty";
                case 7 -> "seventy";
                case 8 -> "eighty";
                case 9 -> "ninety";
                default -> throw new AssertionError("This should not be possible.\nFailiure in the \"under 100\" switch statement.");
            };
            if(ones == 0){
                return prefix;
            }
            return prefix + "-" + longToEnglish(ones);
        }
        if(number < 1000){
            int hundreds = (int) ((number / 100) % 10);
            int less = (int) (number % 100);
            String base = longToEnglish(hundreds) + " hundred";
            if(less == 0){
                return base;
            }
            return base + " and " + longToEnglish(less);
        }
        //We now know the number is at least 1000.
        ArrayList<String> chunks = new ArrayList<>();
        int stage = 0;
        int lastAddedStage = -1;
        long residual = number;
        while(residual > 0){
            long chunk = residual % 1000;
            if(chunk > 0){
                String body = longToEnglish(chunk) + ENGLISH_SUFFIXES[stage];
                String joiner = ", ";
                if(chunks.isEmpty()){
                    joiner = "";
                } else if((lastAddedStage == 0) && ((number % 1000) < 100)){
                    joiner = " and ";
                }
                chunks.add(body + joiner);
                lastAddedStage = stage;
            }
            stage++;
            residual /= 1000;
        }
        StringBuilder sb = new StringBuilder();
        while(!chunks.isEmpty()) sb.append(chunks.removeLast());
        return sb.toString();
    }

    public static String longToEnglishOrdinal(long number){
        if(number < 0){
            if(number == Long.MIN_VALUE){
                return "negative nine quintillion, two hundred and twenty-three quadrillion, three hundred and seventy-two trillion, thirty-six billion, eight hundred and fifty-four million, seven hundred and seventy-five thousand, eight hundred and eighth";
            }
            return "negative " + longToEnglishOrdinal(-number);
        }
        if(number < 20){
            return switch((int) number){
                case 0 -> "zeroth";
                case 1 -> "first";
                case 2 -> "second";
                case 3 -> "third";
                case 4 -> "fourth";
                case 5 -> "fifth";
                case 6 -> "sixth";
                case 7 -> "seventh";
                case 8 -> "eighth";
                case 9 -> "ninth";
                case 10 -> "tenth";
                case 11 -> "eleventh";
                case 12 -> "twelfth";
                case 13 -> "thirteenth";
                case 14 -> "fourteenth";
                case 15 -> "fifteenth";
                case 16 -> "sixteenth";
                case 17 -> "seventeenth";
                case 18 -> "eighteenth";
                case 19 -> "nineteenth";
                default -> throw new AssertionError("This should not be possible.\nFailiure in the \"under 20\" switch statement.");
            };
        }
        if(number < 100){
            int tens = (int) ((number / 10) % 10);
            int ones = (int) (number % 10);
            if(ones == 0){
                return switch(tens){
                    case 2 -> "twentieth";
                    case 3 -> "thirtieth";
                    case 4 -> "fortieth";
                    case 5 -> "fiftieth";
                    case 6 -> "sixtieth";
                    case 7 -> "seventieth";
                    case 8 -> "eightieth";
                    case 9 -> "ninetieth";
                    default -> throw new AssertionError("This should not be possible.\nFailiure in the \"under 100, ones equals 0\" switch statement.");
                };
            }
            String prefix = switch(tens){
                case 2 -> "twenty-";
                case 3 -> "thirty-";
                case 4 -> "forty-";
                case 5 -> "fifty-";
                case 6 -> "sixty-";
                case 7 -> "seventy-";
                case 8 -> "eighty-";
                case 9 -> "ninety-";
                default -> throw new AssertionError("This should not be possible.\nFailiure in the \"under 100, ones nonzero\" switch statement.");
            };
            return prefix + longToEnglishOrdinal(ones);
        }
        if(number < 1000){
            int hundreds = (int) ((number / 100) % 10);
            int less = (int) (number % 100);
            String base = longToEnglish(hundreds) + " hundred";
            if(less == 0){
                return base + "th";
            }
            return base + " and " + longToEnglishOrdinal(less);
        }
        //We now know the number is at least 1000.
        ArrayList<String> chunks = new ArrayList<>();
        int stage = 0;
        int lastAddedStage = -1;
        long residual = number;
        while(residual > 0){
            long chunk = residual % 1000;
            if(chunk > 0){
                if(stage < 1){
                    chunks.add(longToEnglishOrdinal(chunk));
                } else {
                    if(chunks.isEmpty()){
                        chunks.add(longToEnglish(chunk) + ENGLISH_SUFFIXES[stage] + "th");
                    } else {
                        String joiner = ", ";
                        if((lastAddedStage == 0) && ((number % 1000) < 100)){
                            joiner = " and ";
                        }
                        chunks.add(longToEnglish(chunk) + ENGLISH_SUFFIXES[stage] + joiner);
                    }
                }
                lastAddedStage = stage;
            }
            stage++;
            residual /= 1000;
        }
        StringBuilder sb = new StringBuilder();
        while(!chunks.isEmpty()) sb.append(chunks.removeLast());
        return sb.toString();
    }

    public static String longToEnglishOrdinalSuffix(long number){
        int view = (int) Math.abs(number % 100);
        return switch(view){
            case 11, 12, 13 -> "th";
            default -> switch(view % 10){
                case 1 -> "st";
                case 2 -> "nd";
                case 3 -> "rd";
                default -> "th";
            };
        };
    }

    public static String longToEnglishOrdinalNumeric(long number){
        return Long.toString(number) + longToEnglishOrdinalSuffix(number);
    }

    public static String longToEnglishOrdinalNumericCommas(long number){
        return longWithCommas(number) + longToEnglishOrdinalSuffix(number);
    }

    public static String bigWithCommas(BigInteger number){
        String plain = number.toString();
        if(number.abs().compareTo(BIG_TEN_THOUSAND) < 0){
            return plain;
        }
        StringBuilder sb = new StringBuilder();
        int count = 0;
        boolean negative = number.signum() == -1;
        int stop = negative ? 1 : 0;
        for(int i=plain.length()-1; i>=stop; i--){
            if(count == 3){
                sb.append(',');
                count = 0;
            }
            sb.append(plain.charAt(i));
            count++;
        }
        if(negative) sb.append('-');
        return sb.reverse().toString();
    }

    public static String bigToEnglish(BigInteger number){
        if(number.signum() == -1){
            return "negative " + bigToEnglish(number.negate());
        }
        obscureAssert((number.compareTo(BIG_UPPER_EXCLUDE) < 0), "Currently only BigIntegers in the range (-10^66, 10^66) are supported.");
        if(number.compareTo(BIG_LONG_MAX) <= 0){
            return longToEnglish(number.longValue());
        }
        //We now know the number is at least one greater than Long.MAX_VALUE.
        ArrayList<String> chunks = new ArrayList<>();
        int stage = 0;
        int lastAddedStage = -1;
        BigInteger residual = number;
        while(residual.signum() == 1){
            BigInteger[] chop = residual.divideAndRemainder(BIG_THOUSAND);
            long chunk = chop[1].longValue();
            if(chunk > 0){
                String body = longToEnglish(chunk) + ENGLISH_SUFFIXES[stage];
                String joiner = ", ";
                if(chunks.isEmpty()){
                    joiner = "";
                } else if((lastAddedStage == 0) && (number.mod(BIG_THOUSAND).compareTo(BIG_HUNDRED) < 0)){
                    joiner = " and ";
                }
                chunks.add(body + joiner);
                lastAddedStage = stage;
            }
            stage++;
            residual = chop[0];
        }
        StringBuilder sb = new StringBuilder();
        while(!chunks.isEmpty()) sb.append(chunks.removeLast());
        return sb.toString();
    }

    public static String bigToEnglishOrdinal(BigInteger number){
        if(number.signum() == -1){
            return "negative " + bigToEnglishOrdinal(number.negate());
        }
        obscureAssert((number.compareTo(BIG_UPPER_EXCLUDE) < 0), "Currently only BigIntegers in the range (-10^66, 10^66) are supported.");
        if(number.compareTo(BIG_LONG_MAX) <= 0){
            return longToEnglishOrdinal(number.longValue());
        }
        //We now know the number is at least one greater than Long.MAX_VALUE;
        ArrayList<String> chunks = new ArrayList<>();
        int stage = 0;
        int lastAddedStage = -1;
        BigInteger residual = number;
        while(residual.signum() == 1){
            BigInteger[] chop = residual.divideAndRemainder(BIG_THOUSAND);
            long chunk = chop[1].longValue();
            if(chunk > 0){
                if(stage < 1){
                    chunks.add(longToEnglishOrdinal(chunk));
                } else {
                    if(chunks.isEmpty()){
                        chunks.add(longToEnglish(chunk) + ENGLISH_SUFFIXES[stage] + "th");
                    } else {
                        String joiner = ", ";
                        if((lastAddedStage == 0) && (number.mod(BIG_THOUSAND).compareTo(BIG_HUNDRED) < 0)){
                            joiner = " and ";
                        }
                        chunks.add(longToEnglish(chunk) + ENGLISH_SUFFIXES[stage] + joiner);
                    }
                }
                lastAddedStage = stage;
            }
            stage++;
            residual = chop[0];
        }
        StringBuilder sb = new StringBuilder();
        while(!chunks.isEmpty()) sb.append(chunks.removeLast());
        return sb.toString();
    }

    public static String bigToEnglishOrdinalSuffix(BigInteger number){
        return longToEnglishOrdinalSuffix(number.remainder(BIG_HUNDRED).longValue());
    }

    public static String bigToEnglishOrdinalNumeric(BigInteger number){
        return number.toString() + bigToEnglishOrdinalSuffix(number);
    }

    public static String bigToEnglishOrdinalNumericCommas(BigInteger number){
        return bigWithCommas(number) + bigToEnglishOrdinalSuffix(number);
    }

}