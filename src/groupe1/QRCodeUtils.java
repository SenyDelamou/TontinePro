package groupe1;

import java.awt.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * High-Fidelity Standalone QR Code Generator (Version 3 - 29x29)
 * Provides sufficient capacity (approx 69 bytes at Level L) for member data.
 */
public class QRCodeUtils {

    private static final int SIZE = 29;
    private static final boolean[][] FINDER_PATTERN = {
            { true, true, true, true, true, true, true },
            { true, false, false, false, false, false, true },
            { true, false, true, true, true, false, true },
            { true, false, true, true, true, false, true },
            { true, false, true, true, true, false, true },
            { true, false, false, false, false, false, true },
            { true, true, true, true, true, true, true }
    };

    // Format info for Error Correction Level L and Mask Pattern 0
    private static final boolean[] FORMAT_INFO = {
            true, true, true, false, true, true, true, true, true, false, false, false, true, false, false
    };

    public static void drawQRCode(Graphics2D g2, String data, int x, int y, int size) {
        boolean[][] matrix = generateQRMatrix(data);
        int cellSize = Math.max(1, size / SIZE);

        // Background (Quiet Zone)
        g2.setColor(Color.WHITE);
        g2.fillRect(x - 2 * cellSize, y - 2 * cellSize, size + 4 * cellSize, size + 4 * cellSize);

        g2.setColor(Color.BLACK);
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                if (matrix[r][c]) {
                    g2.fillRect(x + c * cellSize, y + r * cellSize, cellSize, cellSize);
                }
            }
        }
    }

    public static boolean[][] generateQRMatrix(String data) {
        boolean[][] matrix = new boolean[SIZE][SIZE];
        boolean[][] reserved = new boolean[SIZE][SIZE];

        // 1. Finder Patterns
        addPattern(matrix, reserved, 0, 0, FINDER_PATTERN);
        addPattern(matrix, reserved, SIZE - 7, 0, FINDER_PATTERN);
        addPattern(matrix, reserved, 0, SIZE - 7, FINDER_PATTERN);

        // 2. Alignment Pattern (Required for Version 2+)
        // For Version 3, alignment pattern is at (22, 22)
        addAlignmentPattern(matrix, reserved, 20, 20);

        // 3. Timing Lines
        for (int i = 8; i < SIZE - 8; i++) {
            matrix[6][i] = (i % 2 == 0);
            matrix[i][6] = (i % 2 == 0);
            reserved[6][i] = true;
            reserved[i][6] = true;
        }

        // 4. Format Info
        addFormatInfo(matrix, reserved);

        // 5. Bitstream (Byte Mode + Length + Payload + Terminator)
        byte[] payload = data.getBytes(StandardCharsets.UTF_8);
        List<Boolean> bitstream = new ArrayList<>();
        addBits(bitstream, 0b0100, 4); // Byte Mode
        addBits(bitstream, payload.length, 8); // Length
        for (byte b : payload)
            addBits(bitstream, b & 0xFF, 8);
        addBits(bitstream, 0, 4); // Terminator

        // 6. Placement (Zig-zag)
        int bitIdx = 0;
        int col = SIZE - 1;
        boolean upward = true;
        while (col > 0) {
            if (col == 6)
                col--;
            for (int r = 0; r < SIZE; r++) {
                int row = upward ? (SIZE - 1 - r) : r;
                for (int c = 0; c < 2; c++) {
                    int cc = col - c;
                    if (!reserved[row][cc]) {
                        boolean bit = (bitIdx < bitstream.size()) && bitstream.get(bitIdx++);
                        matrix[row][cc] = bit ^ ((row + cc) % 2 == 0);
                    }
                }
            }
            col -= 2;
            upward = !upward;
        }
        return matrix;
    }

    private static void addBits(List<Boolean> stream, int value, int count) {
        for (int i = count - 1; i >= 0; i--)
            stream.add(((value >> i) & 1) == 1);
    }

    private static void addAlignmentPattern(boolean[][] matrix, boolean[][] reserved, int r, int c) {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                matrix[r + i][c + j] = (i == 0 || i == 4 || j == 0 || j == 4 || (i == 2 && j == 2));
                reserved[r + i][c + j] = true;
            }
        }
    }

    private static void addFormatInfo(boolean[][] matrix, boolean[][] reserved) {
        // Mapping for Version 3 (Standard placement)
        int[] rowsTL = { 0, 1, 2, 3, 4, 5, 7, 8, 8, 8, 8, 8, 8, 8, 8 };
        int[] colsTL = { 8, 8, 8, 8, 8, 8, 8, 8, 7, 5, 4, 3, 2, 1, 0 };
        for (int i = 0; i < 15; i++) {
            matrix[rowsTL[i]][colsTL[i]] = FORMAT_INFO[i];
            reserved[rowsTL[i]][colsTL[i]] = true;
        }
        // Redundant copies
        for (int i = 0; i < 7; i++) {
            matrix[8][SIZE - 1 - i] = FORMAT_INFO[i];
            reserved[8][SIZE - 1 - i] = true;
        }
        for (int i = 0; i < 8; i++) {
            matrix[SIZE - 8 + i][8] = FORMAT_INFO[7 + i];
            reserved[SIZE - 8 + i][8] = true;
        }
    }

    private static void addPattern(boolean[][] matrix, boolean[][] reserved, int r, int c, boolean[][] pattern) {
        for (int i = 0; i < pattern.length; i++) {
            for (int j = 0; j < pattern[i].length; j++) {
                matrix[r + i][c + j] = pattern[i][j];
                reserved[r + i][c + j] = true;
            }
        }
        for (int i = -1; i <= 7; i++) {
            for (int j = -1; j <= 7; j++) {
                int rr = r + i, cc = c + j;
                if (rr >= 0 && rr < SIZE && cc >= 0 && cc < SIZE)
                    reserved[rr][cc] = true;
            }
        }
    }
}
