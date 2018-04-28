package gatech.cs6300.project2.model;

import java.io.Serializable;
import java.util.Date;

public class PurchaseReportEntry implements Serializable, HasTotal {
	private static final long serialVersionUID = 1L;
	
	private final int cartId, vipId;
	private final double total;
	private final Date time;
	
	public PurchaseReportEntry(final int cartId, final int vipId, final double total, final Date time) {
		if(time == null) {
			throw new NullPointerException();
		}
		this.cartId = cartId;
		this.vipId = vipId;
		this.total = total;
		this.time = time;
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

	public Date getTime() {
		return time;
	}
}
