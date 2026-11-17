package ug.ucu.algorithmexplorer.models;

/**
 * Represents one disk movement in a Tower of Hanoi solution.
 *
 * @param disk disk number, where disk 1 is the smallest
 * @param sourcePeg peg from which the disk moves
 * @param destinationPeg peg to which the disk moves
 */
public record HanoiMove(
        int disk,
        char sourcePeg,
        char destinationPeg
) {

    public HanoiMove {
        if (disk < 1) {
            throw new IllegalArgumentException(
                    "Disk number must be at least 1."
            );
        }

        if (sourcePeg == destinationPeg) {
            throw new IllegalArgumentException(
                    "Source and destination pegs must be different."
            );
        }
    }

    /**
     * Produces text suitable for the move list in the interface.
     *
     * @return readable movement instruction
     */
    public String description() {
        return "Move disk " + disk
                + " from " + sourcePeg
                + " to " + destinationPeg;
    }
}