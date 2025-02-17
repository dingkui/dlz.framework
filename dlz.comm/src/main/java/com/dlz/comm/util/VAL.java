package com.dlz.comm.util;

public class VAL<V1, V2> {
	public final V1 v1;
	public final V2 v2;

	public VAL(V1 v1, V2 v2) {
		this.v1 = v1;
		this.v2 = v2;
	}
	public static <V1, V2> VAL<V1, V2> of(V1 v1, V2 v2) {
		return new VAL<>(v1, v2);
	}
	public static <V1, V2, V3> VAL3<V1, V2,V3> val3(V1 v1, V2 v2, V3 v3) {
		return new VAL3<>(v1, v2,v3);
	}
	public static class VAL3<V1, V2, V3> extends VAL<V1, V2> {
		public final V3 v3;

		public VAL3(V1 v1, V2 v2,V3 v3) {
			super(v1, v2);
			this.v3 = v3;
		}
	}
}