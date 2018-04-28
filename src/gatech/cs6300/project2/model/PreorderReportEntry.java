package gatech.cs6300.project2.model;

import java.io.Serializable;

public class PreorderReportEntry implements Serializable, HasTotal {
	private static final long serialVersionUID = 1L;
	
	private final int cartId, vipId;
	private final double total;
	
	public PreorderReportEntry(final int cartId, final int vipId, final double total) {
		this.cartId = cartId;
		this.vipId = vipId;
		this.total = total;
	}
	
	public int getCartId() {
		return cartId;
	}

	public int getVipId() {
		return vipId;
	}

	public double getTotal() {
		return total;
	}
}