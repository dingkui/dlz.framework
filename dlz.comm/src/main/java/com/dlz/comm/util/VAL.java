package com.dlz.comm.util;

public class VAL<V1, V2> {
    public final V1 v1;
    public final V2 v2;

    private VAL(V1 v1, V2 v2) {
        this.v1 = v1;
        this.v2 = v2;
    }

    public static class VAL3<V1, V2, V3> extends VAL<V1, V2> {
        public final V3 v3;

		private VAL3(V1 v1, V2 v2, V3 v3) {
            super(v1, v2);
            this.v3 = v3;
        }

        public String toString() {
            return super.toString() + ",v3:" + v3;
        }
    }

    public static class VAL4<V1, V2, V3, V4> extends VAL3<V1, V2, V3> {
        public final V4 v4;

		private VAL4(V1 v1, V2 v2, V3 v3, V4 v4) {
            super(v1, v2, v3);
            this.v4 = v4;
        }

        public String toString() {
            return super.toString() + ",v4:" + v4;
        }
    }

    public static class VAL5<V1, V2, V3, V4, V5> extends VAL4<V1, V2, V3, V4> {
        public final V5 v5;

		private VAL5(V1 v1, V2 v2, V3 v3, V4 v4, V5 v5) {
            super(v1, v2, v3, v4);
            this.v5 = v5;
        }

        public String toString() {
            return super.toString() + ",v5:" + v5;
        }
    }


    public static <V1, V2> VAL<V1, V2> of(V1 v1, V2 v2) {
        return new VAL<>(v1, v2);
    }

    public static <V1, V2, V3> VAL3<V1, V2, V3> of(V1 v1, V2 v2, V3 v3) {
        return new VAL3<>(v1, v2, v3);
    }

    public static <V1, V2, V3, V4> VAL4<V1, V2, V3, V4> of(V1 v1, V2 v2, V3 v3, V4 v4) {
        return new VAL4<>(v1, v2, v3, v4);
    }

    public static <V1, V2, V3, V4, V5> VAL5<V1, V2, V3, V4, V5> of(V1 v1, V2 v2, V3 v3, V4 v4, V5 v5) {
        return new VAL5<>(v1, v2, v3, v4, v5);
    }

    public String toString() {
        return "v1:" + v1 + ",v2:" + v2;
    }
}