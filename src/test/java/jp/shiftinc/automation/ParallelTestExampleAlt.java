package jp.shiftinc.automation;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ParallelTestExampleAlt {

    static class P1 extends ParallelTestExample{}

    static class P2 extends ParallelTestExample{}

    static class P3 extends ParallelTestExample{}

    class P4 extends ParallelTestExample{}
}
