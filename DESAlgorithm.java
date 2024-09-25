import java.util.*;

public class DESAlgorithm {

    // Initial permutation table
    private static final int[] IP = { 58, 50, 42, 34, 26, 18, 10, 2,
            60, 52, 44, 36, 28, 20, 12, 4,
            62, 54, 46, 38, 30, 22, 14, 6,
            64, 56, 48, 40, 32, 24, 16, 8,
            57, 49, 41, 33, 25, 17, 9, 1,
            59, 51, 43, 35, 27, 19, 11, 3,
            61, 53, 45, 37, 29, 21, 13, 5,
            63, 55, 47, 39, 31, 23, 15, 7 };

    // Final permutation table
    private static final int[] FP = { 40, 8, 48, 16, 56, 24, 64, 32,
            39, 7, 47, 15, 55, 23, 63, 31,
            38, 6, 46, 14, 54, 22, 62, 30,
            37, 5, 45, 13, 53, 21, 61, 29,
            36, 4, 44, 12, 52, 20, 60, 28,
            35, 3, 43, 11, 51, 19, 59, 27,
            34, 2, 42, 10, 50, 18, 58, 26,
            33, 1, 41, 9, 49, 17, 57, 25 };

    // Expansion table
    private static final int[] E = { 32, 1, 2, 3, 4, 5,
            4, 5, 6, 7, 8, 9,
            8, 9, 10, 11, 12, 13,
            12, 13, 14, 15, 16, 17,
            16, 17, 18, 19, 20, 21,
            20, 21, 22, 23, 24, 25,
            24, 25, 26, 27, 28, 29,
            28, 29, 30, 31, 32, 1 };

    // Permutation table
    private static final int[] P = { 16, 7, 20, 21, 29, 12, 28, 17,
            1, 15, 23, 26, 5, 18, 31, 10,
            2, 8, 24, 14, 32, 27, 3, 9,
            19, 13, 30, 6, 22, 11, 4, 25 };

    // S-boxes (Substitution boxes)
    private static final int[][][] S_BOXES = {
            {
                    {14, 4, 13, 1, 2, 15, 11, 8, 3, 10, 6, 12, 5, 9, 0, 7},
                    {0, 15, 7, 4, 14, 2, 13, 1, 10, 6, 12, 11, 9, 5, 3, 8},
                    {4, 1, 14, 8, 13, 6, 2, 11, 15, 12, 9, 7, 3, 10, 5, 0},
                    {15, 12, 8, 2, 4, 9, 1, 7, 5, 11, 3, 14, 10, 0, 6, 13}
            },
            {
                    {15, 1, 8, 14, 6, 11, 3, 4, 9, 7, 2, 13, 12, 0, 5, 10},
                    {3, 13, 4, 7, 15, 2, 8, 14, 12, 0, 1, 10, 6, 9, 11, 5},
                    {0, 14, 7, 11, 10, 4, 13, 1, 5, 8, 12, 6, 9, 3, 2, 15},
                    {13, 8, 10, 1, 3, 15, 4, 2, 11, 6, 7, 12, 0, 5, 14, 9}
            },
            {
                    {10, 0, 9, 14, 6, 3, 15, 5, 1, 13, 12, 7, 11, 4, 2, 8},
                    {13, 7, 0, 9, 3, 4, 6, 10, 2, 8, 5, 14, 12, 11, 15, 1},
                    {13, 6, 4, 9, 8, 15, 3, 0, 11, 1, 2, 12, 5, 10, 14, 7},
                    {1, 10, 13, 0, 6, 9, 8, 7, 4, 15, 14, 3, 11, 5, 2, 12}
            },
            {
                    {7, 13, 14, 3, 0, 6, 9, 10, 1, 2, 8, 5, 11, 12, 4, 15},
                    {13, 8, 11, 5, 6, 15, 0, 3, 4, 7, 2, 12, 1, 10, 14, 9},
                    {10, 6, 9, 0, 12, 11, 7, 13, 15, 1, 3, 14, 5, 2, 8, 4},
                    {3, 15, 0, 6, 10, 1, 13, 8, 9, 4, 5, 11, 12, 7, 2, 14}
            },
            {
                    {2, 12, 4, 1, 7, 10, 11, 6, 8, 5, 3, 15, 13, 0, 14, 9},
                    {14, 11, 2, 12, 4, 7, 13, 1, 5, 0, 15, 10, 3, 9, 8, 6},
                    {4, 2, 1, 11, 10, 13, 7, 8, 15, 9, 12, 5, 6, 3, 0, 14},
                    {11, 8, 12, 7, 1, 14, 2, 13, 6, 15, 0, 9, 10, 4, 5, 3}
            },
            {
                    {12, 1, 10, 15, 9, 2, 6, 8, 0, 13, 3, 4, 14, 7, 5, 11},
                    {10, 15, 4, 2, 7, 12, 9, 5, 6, 1, 13, 14, 0, 11, 3, 8},
                    {9, 14, 15, 5, 2, 8, 12, 3, 7, 0, 4, 10, 1, 13, 11, 6},
                    {4, 3, 2, 12, 9, 5, 15, 10, 11, 14, 1, 7, 6, 0, 8, 13}
            },
            {
                    {4, 11, 2, 14, 15, 0, 8, 13, 3, 12, 9, 7, 5, 10, 6, 1},
                    {13, 0, 11, 7, 4, 9, 1, 10, 14, 3, 5, 12, 2, 15, 8, 6},
                    {1, 4, 11, 13, 12, 3, 7, 14, 10, 15, 6, 8, 0, 5, 9, 2},
                    {6, 11, 13, 8, 1, 4, 10, 7, 9, 5, 0, 15, 14, 2, 3, 12}
            },
            {
                    {13, 2, 8, 4, 6, 15, 11, 1, 10, 9, 3, 14, 5, 0, 12, 7},
                    {1, 15, 13, 8, 10, 3, 7, 4, 12, 5, 6, 11, 0, 14, 9, 2},
                    {7, 11, 4, 1, 9, 12, 14, 2, 0, 6, 10, 13, 15, 3, 5, 8},
                    {2, 1, 14, 7, 4, 10, 8, 13, 15, 12, 9, 0, 3, 5, 6, 11}
            }
    };

    // Permutation choice 1
    private static final int[] PC1 = { 57, 49, 41, 33, 25, 17, 9,
            1, 58, 50, 42, 34, 26, 18,
            10, 2, 59, 51, 43, 35, 27,
            19, 11, 3, 60, 52, 44, 36,
            63, 55, 47, 39, 31, 23, 15,
            7, 62, 54, 46, 38, 30, 22,
            14, 6, 61, 53, 45, 37, 29,
            21, 13, 5, 28, 20, 12, 4 };

    // Permutation choice 2
    private static final int[] PC2 = { 14, 17, 11, 24, 1, 5,
            3, 28, 15, 6, 21, 10,
            23, 19, 12, 4, 26, 8,
            16, 7, 27, 20, 13, 2,
            41, 52, 31, 37, 47, 55,
            30, 40, 51, 45, 33, 48,
            44, 49, 39, 56, 34, 53,
            46, 42, 50, 36, 29, 32 };

    // Left shift schedule
    private static final int[] LEFT_SHIFT_SCHEDULE = { 1, 1, 2, 2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 2, 1 };

    // Convert a string of 8 characters to binary
    private static String stringToBinary(String text) {
        StringBuilder binary = new StringBuilder();
        for (char c : text.toCharArray()) {
            binary.append(String.format("%8s", Integer.toBinaryString(c)).replace(' ', '0'));
        }
        return binary.toString();
    }

    // Perform the initial permutation
    private static String initialPermutation(String input) {
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < IP.length; i++) {
            output.append(input.charAt(IP[i] - 1));
        }
        return output.toString();
    }

    // Perform the final permutation
    private static String finalPermutation(String input) {
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < FP.length; i++) {
            output.append(input.charAt(FP[i] - 1));
        }
        return output.toString();
    }

    // Perform a permutation
    private static String permutation(String input, int[] table) {
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < table.length; i++) {
            output.append(input.charAt(table[i] - 1));
        }
        return output.toString();
    }

    // Perform an expansion
    private static String expansion(String input) {
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < E.length; i++) {
            output.append(input.charAt(E[i] - 1));
        }
        return output.toString();
    }

    // Perform an S-box substitution
    private static String sBoxSubstitution(String input) {
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            String block = input.substring(i * 6, (i + 1) * 6);
            int row = Integer.parseInt("" + block.charAt(0) + block.charAt(5), 2);
            int column = Integer.parseInt(block.substring(1, 5), 2);
            int value = S_BOXES[i][row][column];
            output.append(String.format("%4s", Integer.toBinaryString(value & 0xF)).replace(' ', '0'));
        }
        return output.toString();
    }


    // Generate round keys
    private static List<String> generateRoundKeys(String key) {
        key = permutation(key, PC1);
        List<String> roundKeys = new ArrayList<>();
        String left = key.substring(0, 28);
        String right = key.substring(28);

        for (int i = 0; i < LEFT_SHIFT_SCHEDULE.length; i++) {
            left = leftShift(left, LEFT_SHIFT_SCHEDULE[i]);
            right = leftShift(right, LEFT_SHIFT_SCHEDULE[i]);
            roundKeys.add(permutation(left + right, PC2));
        }

        return roundKeys;
    }

    // Perform a left circular shift
    private static String leftShift(String input, int shift) {
        return input.substring(shift) + input.substring(0, shift);
    }

    // XOR operation
    private static String xor(String a, String b) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < a.length(); i++) {
            result.append(a.charAt(i) ^ b.charAt(i));
        }
        return result.toString();
    }

    // DES encryption
    public static String encrypt(String plaintext, String key) {
        if (key.length() != 8) {
            throw new IllegalArgumentException("Key must be 8 characters long");
        }

        // Perform padding if necessary
        while (plaintext.length() % 8 != 0) {
            plaintext += " ";
        }

        List<String> roundKeys = generateRoundKeys(stringToBinary(key));
        StringBuilder ciphertext = new StringBuilder();

        for (int i = 0; i < plaintext.length(); i += 8) {
            String block = initialPermutation(stringToBinary(plaintext.substring(i, i + 8)));
            String left = block.substring(0, 32);
            String right = block.substring(32);

            for (int j = 0; j < 16; j++) {
                String expandedRight = expansion(right);
                String xored = xor(expandedRight, roundKeys.get(j));
                String substituted = sBoxSubstitution(xored);
                String permuted = permutation(substituted, P);
                String newRight = xor(permuted, left);
                left = right;
                right = newRight;
            }

            String encryptedBlock = finalPermutation(right + left);
            ciphertext.append(encryptedBlock);
        }

        return binaryToString(ciphertext.toString());
    }

    // DES decryption
    // DES decryption
    public static String decrypt(String ciphertext, String key) {
        if (key.length() != 8) {
            throw new IllegalArgumentException("Key must be 8 characters long");
        }

        List<String> roundKeys = generateRoundKeys(stringToBinary(key));
        Collections.reverse(roundKeys);
        StringBuilder plaintext = new StringBuilder();

        for (int i = 0; i < ciphertext.length(); i += 8) {
            String block = ciphertext.substring(i, Math.min(i + 8, ciphertext.length()));
            if (block.length() < 8) {
                while (block.length() < 8) {
                    block += " ";
                }
            }
            String binaryBlock = stringToBinary(block);
            String initialPermutation = initialPermutation(binaryBlock);
            String left = initialPermutation.substring(0, 32);
            String right = initialPermutation.substring(32);

            for (int j = 0; j < 16; j++) {
                String expandedRight = expansion(right);
                String xored = xor(expandedRight, roundKeys.get(j));
                String substituted = sBoxSubstitution(xored);
                String permuted = permutation(substituted, P);
                String newRight = xor(permuted, left);
                left = right;
                right = newRight;
            }

            String decryptedBlock = finalPermutation(right + left);
            plaintext.append(binaryToString(decryptedBlock));
        }

        return plaintext.toString();
    }


    // Convert binary string to ASCII string
    private static String binaryToString(String binary) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < binary.length(); i += 8) {
            String byteString = binary.substring(i, i + 8);
            int charCode = Integer.parseInt(byteString, 2);
            stringBuilder.append((char) charCode);
        }
        return stringBuilder.toString();
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Input handling for plaintext
        System.out.print("Enter plaintext: ");
        String plaintext = scanner.nextLine();

        // Input handling for key
        String key;
        do {
            System.out.print("Enter key (8 characters): ");
            key = scanner.nextLine();
            if (key.length() != 8) {
                System.out.println("Key must be 8 characters long.");
            }
        } while (key.length() != 8);

        // Encryption
        String ciphertext = encrypt(plaintext, key);
        System.out.println("Encrypted: " + ciphertext);

        // Decryption
        String decrypted = decrypt(ciphertext, key);
        System.out.println("Decrypted: " + decrypted);
    }
}